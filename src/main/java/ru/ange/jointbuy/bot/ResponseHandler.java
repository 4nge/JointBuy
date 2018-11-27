package ru.ange.jointbuy.bot;

import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

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

    private List<InlineQueryResult> getInlineResult(double sum, String name) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();
        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.setMessageText("Text");

        String msg = String.format( "Cумма: %s \u20BD\nНаименование: %s ", sum, name );

        InlineQueryResultArticle buy = new InlineQueryResultArticle()
            .setInputMessageContent(messageContent)
            .setId(Integer.toString(1))
            .setTitle("Добавить покупку")
            .setDescription(msg)
            .setThumbUrl(Constants.BUY_IMG);
        results.add(buy);

        InlineQueryResultArticle remittance = new InlineQueryResultArticle()
            .setInputMessageContent(messageContent)
            .setId(Integer.toString(2))
            .setTitle("Добавить перевод")
            .setDescription(msg)
            .setThumbUrl(Constants.REMITTANCE_IMG);
        results.add(remittance);

        return results;
    }

    public void answerInlineQuery(String query, String id) {
        try {
            String digit = query.substring( 0, query.indexOf( " " ) );
            double sum = Double.valueOf( digit.replace( ",", "." ) );
            String name = query.substring( query.indexOf( " " ) + 1, query.length() );

            AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
            answerInlineQuery.setInlineQueryId(id);
            answerInlineQuery.setResults(getInlineResult(sum, name));
            sender.execute( answerInlineQuery );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public SendMessage getAddUserMsg(long chatId) {
        AddUserMsg msg = new AddUserMsg();
        SendMessage sm = new SendMessage()
                .setText( msg.getText() )
                .setChatId( chatId );
        return sm;
    }

    public void sendAddUserMsg(long chatId) {
        try {
            sender.execute( getAddUserMsg(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
