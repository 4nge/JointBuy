package ru.ange.jointbuy.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.HelloMsgHelper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.services.BotService;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

public class JointBuyBot extends AbilityBot {



    public static final String TOKEN = "795411227:AAEM4KjNFi9AayQzPr4eTAAQ3VcQA9QRpjw";
    public static final String NAME = "JointBuyBot";

    public static final String BUY_IMG = "https://image.ibb.co/jpLPpf/buy.png";
    public static final String REMITTANCE_IMG = "https://image.ibb.co/dyyzN0/remittance.png";

    private BotService botService;

    public JointBuyBot(DefaultBotOptions botOptions, BotService botService) {
        super( TOKEN, NAME, botOptions );
        this.botService = botService;
    }


    @Override
    public int creatorId() {
        return 0;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (int i = 0; i < updates.size(); i++) {
            Update update = updates.get( i );

            if (update.getMessage() != null && update.getMessage().getNewChatMembers() != null) {
                long chatId = update.getMessage().getChatId();
                User sender = update.getMessage().getFrom();
                List<User> members = update.getMessage().getNewChatMembers();
                this.handelNewChatMembers(chatId, sender, members);
            }

            else if (update.hasCallbackQuery()) {
                String callData = update.getCallbackQuery().getData();
                String callId = update.getCallbackQuery().getId();

                long messageId = update.getCallbackQuery().getMessage().getMessageId();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                User sender = update.getCallbackQuery().getFrom();
                this.handelCallBackQuery(callData, callId, messageId, chatId, sender);
            }
        }
    }


    private void handelNewChatMembers(long chatId, User sender, List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get( i );
            if (user.getBot() && user.getUserName().equals( NAME )) {

                Member newMember = new Member(
                        sender.getId(),
                        chatId,
                        sender.getFirstName(),
                        sender.getLastName(),
                        new String());

                List<Member> members = botService.getMembers( chatId );
                if  (!members.contains( newMember )) {
                    members.add( botService.addMembers( newMember ) );
                }

                SendMessage helloMsg = new SendMessage(chatId, HelloMsgHelper.getMsg(members))
                        .setReplyMarkup( HelloMsgHelper.getMarkup() );

                try {
                    execute(helloMsg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handelCallBackQuery(String callData, String callId , long messageId, long chatId, User sender) {
        if (callData.equals( HelloMsgHelper.CALLBACK_DATA )) {
            Member newMember = new Member(
                    sender.getId(),
                    chatId,
                    sender.getFirstName(),
                    sender.getLastName(),
                    new String());

            List<Member> members = botService.getMembers( chatId );

            String msg = new String();
            if (!members.contains( newMember )) {
                members.add( botService.addMembers( newMember ) );
                msg = "Вы присодинились к списку участников";

                EditMessageText edditedMsg = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setText(HelloMsgHelper.getMsg(members))
                        .setReplyMarkup( HelloMsgHelper.getMarkup());

                try {
                    execute(edditedMsg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                msg = "Вы уже есть в списке участников";
            }

            // Show insof msg
            AnswerCallbackQuery acq = new AnswerCallbackQuery()
                    .setCallbackQueryId( callId )
                    .setText( msg );

            try {
                execute(acq);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private List<InlineQueryResult> getInlineResult(double sum, String name) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();


        InputTextMessageContent messageContent = new InputTextMessageContent();
        //messageContent.disableWebPagePreview();
        //messageContent.enableMarkdown(true);
        messageContent.setMessageText("Text");

        String msg = String.format( "Cумма: %s \u20BD\nНаименование: %s ", sum, name );

        InlineQueryResultArticle buy = new InlineQueryResultArticle()
            .setInputMessageContent(messageContent)
            .setId(Integer.toString(1))
            .setTitle("Добавить покупку")
            .setDescription(msg)
            .setThumbUrl(BUY_IMG);

        results.add(buy);

        InlineQueryResultArticle remittance = new InlineQueryResultArticle()
            .setInputMessageContent(messageContent)
            .setId(Integer.toString(2))
            .setTitle("Добавить перевод")
            .setDescription(msg)
            .setThumbUrl(REMITTANCE_IMG);

        results.add(remittance);

        return results;
    }

    private void handelInlineUpdate(Update update) {
        InlineQuery inlineQuery = update.getInlineQuery();

        if (inlineQuery.hasQuery()) {
            String query = inlineQuery.getQuery();

            try {
                String digit = query.substring( 0, query.indexOf( " " ) );
                double sum = Double.valueOf( digit.replace( ",", "." ) );
                String name = query.substring( query.indexOf( " " ) + 1, query.length() );

                AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
                answerInlineQuery.setInlineQueryId(inlineQuery.getId());
                answerInlineQuery.setResults(getInlineResult(sum, name));
                execute( answerInlineQuery );

            } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

//    private void handelUpdate(Update update) {
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String message_text = update.getMessage().getText();
//
//            long chat_id = update.getMessage().getChatId();
//
//            SendMessage message = new SendMessage() // Create a message object object
//                    .setChatId(chat_id)
//                    .setText("You send /start");
//
//            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//            List<InlineKeyboardButton> rowInline = new ArrayList<>();
//            rowInline.add(new InlineKeyboardButton().setText("Update message text").setCallbackData("update_msg_text"));
//            // Set the keyboard to the markup
//            rowsInline.add(rowInline);
//            // Add it to the message
//            markupInline.setKeyboard(rowsInline);
//            message.setReplyMarkup(markupInline);
//            try {
//                execute(message); // Sending our message object to user
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        } else if (update.hasCallbackQuery()) {
//            System.out.println("update.hasCallbackQuery()");
//        }
//    }


    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
