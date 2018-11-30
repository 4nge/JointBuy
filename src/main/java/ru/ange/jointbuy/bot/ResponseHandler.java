package ru.ange.jointbuy.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.utils.Constants;

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
            sender.execute(getAnswerCallbackQuery(callId, HelloMsg.ADD_USER));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void answerUserAlreadyExist(String callId) {
        try {
            sender.execute(getAnswerCallbackQuery(callId, HelloMsg.USER_ALREADY_EXISTS));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private List<InlineQueryResult> getInlineResult(double sum, String name, User user, int mc) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();

        String hashtag = name.replace( " ", "_" );
        String userName = user.getFirstName() + " " + user.getLastName();
        String msgText = String.format(Constants.INLINE_BUY_MSG_TEXT_PTT, hashtag, name, sum, userName, mc);

        InputTextMessageContent msgCont = new InputTextMessageContent()
                .setMessageText( EmojiParser.parseToUnicode(msgText) );

        InlineQueryResultArticle buy = new InlineQueryResultArticle()
                .setInputMessageContent(msgCont)
                .setId(Integer.toString(1))
                .setDescription(String.format( Constants.INLINE_TEXT_PTT, sum, name ))
                .setTitle(Constants.BUY_IMG_BTT_TEXT)
                .setThumbUrl(Constants.BUY_IMG_BTT_URL);
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

    public void answerInlineQuery(String query, String id, User user, int memCounts) {
        try {
            String digit = query.substring( 0, query.indexOf( " " ) );
            double sum = Double.valueOf( digit.replace( ",", "." ) );
            String name = query.substring( query.indexOf( " " ) + 1, query.length() );

            AnswerInlineQuery aiq = new AnswerInlineQuery()
                    .setInlineQueryId(id)
                    .setResults(getInlineResult(sum, name, user, memCounts));

            sender.execute( aiq );
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
