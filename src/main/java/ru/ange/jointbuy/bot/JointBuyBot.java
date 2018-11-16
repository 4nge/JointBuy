package ru.ange.jointbuy.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
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
                String call_data = update.getCallbackQuery().getData();
                long message_id = update.getCallbackQuery().getMessage().getMessageId();
                long chat_id = update.getCallbackQuery().getMessage().getChatId();
                this.handelCallBackQuery(call_data, message_id, chat_id);
            }

//            if (update.hasInlineQuery()) {
//                handelInlineUpdate(update);
//            }
        }
    }


    private void handelNewChatMembers(long chatId, User sender, List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get( i );
            if (user.getBot() && user.getUserName().equals( NAME )) {
                // TODO check existing
                Member member = new Member(
                        sender.getId(),
                        chatId,
                        sender.getFirstName(),
                        sender.getLastName(),
                        "");
                botService.addMembers( member );
                List<Member> members = botService.getMembers( chatId );

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


    private void handelCallBackQuery(String call_data, long message_id, long chat_id) {
        if (call_data.equals( HelloMsgHelper.CALLBACK_DATA )) {

            List<Member> members = botService.getMembers( chat_id );
            EditMessageText edditedMsg = new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(toIntExact(message_id))
                    .setText(HelloMsgHelper.getMsg(members))
                    .setReplyMarkup( HelloMsgHelper.getMarkup());
            try {
                execute(edditedMsg);
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
