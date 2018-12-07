package ru.ange.jointbuy.bot.response;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Operation;
import ru.ange.jointbuy.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {

    private final MessageSender sender;

    public ResponseHandler(MessageSender sender) {
        this.sender = sender;
    }

    private SendMessage getHelloSendMessage(long chatId, List<Member> members) {
        HelloMsg helloMsg = new HelloMsg( members );
        SendMessage msg = new SendMessage()
                .setText( helloMsg.getText() )
                .setReplyMarkup( helloMsg.getMarkup() )
                .setChatId( chatId );
        return msg;
    }

    public void sendHelloMsg(long chatId, List<Member> members) {
        try {
            sender.execute( getHelloSendMessage(chatId, members));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private AnswerCallbackQuery getAnswerCallbackQuery(String callId, String msg) {
        AnswerCallbackQuery acq = new AnswerCallbackQuery()
                .setCallbackQueryId( callId )
                .setText( msg );
        return acq;
    }

    public void answerAddUser(String callId) {
        try {
            sender.execute(getAnswerCallbackQuery(callId, Constants.ADD_USER_CALLBACK_ANSWER));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void answerUserAlreadyExist(String callId) {
        try {
            sender.execute(getAnswerCallbackQuery(callId, Constants.USER_EXIST_CALLBACK_ANSWER));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private InlineKeyboardMarkup getPurchaseInlineKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton()
                .setText(Constants.PURCHASE_MEMEBERS_BTT_TEXT)

                .setCallbackData( Constants.PURCHASE_MEMEBERS_BTT_CALLBACK));

        rowsInline.add(rowInline1);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton()
                .setText(Constants.PURCHASE_EDIT_BTT_TEXT)
                .setCallbackData( Constants.PURCHASE_EDIT_BTT_CALLBACK));
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(new InlineKeyboardButton()
                .setText(Constants.PURCHASE_DELETE_BTT_TEXT)
                .setCallbackData( Constants.PURCHASE_DELETE_BTT_CALLBACK));
        rowsInline.add(rowInline3);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private String getPurchaseInlineMsgText(String name,  double sum, User user) {
        String hashtag = name.replace( " ", "_" );
        String userName = user.getFirstName() + " " + user.getLastName();
        String msgText = String.format(Constants.INLINE_BUY_MSG_TEXT_PTT, hashtag, name, sum,
                userName, Constants.INLINE_BUY_MSG_TEXT_ALL);

        return EmojiParser.parseToUnicode( msgText );
    }


    private List<InlineQueryResult> getInlineResult(Operation op, User user) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();
        String name = op.getName();
        double sum = op.getSum();

        InputTextMessageContent msgCont = new InputTextMessageContent()
                .enableMarkdown( true )
                .setMessageText( getPurchaseInlineMsgText(name, sum, user) );

        InlineQueryResultArticle buy = new InlineQueryResultArticle()
                .setInputMessageContent(msgCont)
                .setId( String.valueOf( Operation.Type.PURCHASE ) )
                .setDescription(String.format( Constants.INLINE_TEXT_PTT, sum, name ))
                .setTitle(Constants.BUY_IMG_BTT_TEXT)
                .setThumbUrl(Constants.BUY_IMG_BTT_URL)
                .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );

        results.add(buy);

//        InlineQueryResultArticle transfer = new InlineQueryResultArticle()
//                .setInputMessageContent(getTextMessageCont(Constants.INLINE_MSG_TYPE_TRANSFER, name, sum, user, mc))
//                .setId(Integer.toString(2))
//                .setDescription(String.format( Constants.INLINE_TEXT_PTT, sum, name ))
//                .setTitle(Constants.REMITTANCE_IMG_BTT_TEXT)
//                .setThumbUrl(Constants.REMITTANCE_IMG_BTT_URL);
//        results.add(transfer);

        return results;
    }

    private Operation readOperation(String query) {
        String digit = query.substring( 0, query.indexOf( " " ) );
        Operation operation = new Operation()
                .setName( query.substring( query.indexOf( " " ) + 1, query.length() ) )
                .setSum( Double.valueOf( digit.replace( ",", "." ) ));
        return operation;
    }


    public void answerInlineQuery(String query, String id, User user) {
        try {
            AnswerInlineQuery aiq = new AnswerInlineQuery()
                    .setInlineQueryId(id)
                    .setResults(getInlineResult(readOperation( query ), user));

            sender.execute( aiq );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {}
    }

    public void editAnswerInlineQuery(long chatId, int msgId, String name, double sum, User user) {
        try {
            String text = getPurchaseInlineMsgText(name, sum, user);
            EditMessageText emt = new EditMessageText()
                    .enableMarkdown( true )
                    .setChatId( chatId )
                    .setMessageId( msgId )
                    .setText( EmojiParser.parseToUnicode(text) )
                    .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );

            sender.execute( emt );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {}
    }





    public SendMessage getAddUserMsg(long chatId) {
        AddUserMsg msg = new AddUserMsg();
        SendMessage sm = new SendMessage()
                .setText( msg.getText() )
                .setChatId( chatId );
        return sm;
    }

    public void sendMsg(long chatId, String msg, boolean markdown) {
        try {
            SendMessage sm = new SendMessage()
                    .enableMarkdown( markdown )
                    .setText(msg )
                    .setChatId( chatId );
            sender.execute( getAddUserMsg(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
