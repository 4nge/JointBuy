package ru.ange.jointbuy.bot.response.replies;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import ru.ange.jointbuy.bot.response.replies.remittance.Remittance;
import ru.ange.jointbuy.pojo.Operation;
import ru.ange.jointbuy.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InlineQueryAnswer extends AnswerInlineQuery {

    public InlineQueryAnswer(String id, Operation operation, User user) {
        super();
        setInlineQueryId( id );
        setResults( getInlineResult( operation, user ) );
    }

    private List<InlineQueryResult> getInlineResult(Operation op, User user) {
        String name = op.getName();
        double sum = op.getSum();
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();

        if (sum > 0) {
            String userName = user.getFirstName() + " " + user.getLastName();
            String remMsgTextData = getRemittanceMsgData( sum, name, userName, new Date() );
            String remMsgText = remMsgTextData + getRemittanceMsgEnd();

            InputTextMessageContent remitMsgCont = new InputTextMessageContent()
                    //.enableMarkdown( true ) // not work with _
                    .setMessageText( EmojiParser.parseToUnicode( remMsgText ) );

            String desc = (name != null) ? String.format( Constants.INLINE_REMIT_DESC_TEXT_PTT, sum, name)
                    : String.format( Constants.INLINE_REMIT_TEXT_PTT, sum );

            InlineQueryResultArticle remittance = new InlineQueryResultArticle()
                    .setInputMessageContent(remitMsgCont)
                    .setId( String.valueOf( Operation.Type.REMITTANCE ) )
                    .setDescription( desc )
                    .setTitle( Constants.REMITTANCE_INLINE_BTT_TEXT )
                    .setThumbUrl( Constants.REMITTANCE_INLINE_BTT_IMG_URL );

            results.add(remittance);
        }

//        if (sum > 0 && name != null && !name.isEmpty()) {
//            String msgText = getPurchaseInlineMsgText( name, sum, user.getFirstName(),
//                    user.getLastName(), Constants.INLINE_BUY_MSG_TEXT_ALL );
//
//            InputTextMessageContent purchMsgCont = new InputTextMessageContent()
//                    .enableMarkdown( true )
//                    .setMessageText( msgText );
//
//            InlineQueryResultArticle buy = new InlineQueryResultArticle()
//                    .setInputMessageContent( purchMsgCont )
//                    .setId( String.valueOf( Operation.Type.PURCHASE ) )
//                    .setDescription( String.format( Constants.INLINE_PURCH_TEXT_PTT, sum, name) ) // TODO add " " in names
//                    .setTitle( Constants.BUY_IMG_BTT_TEXT )
//                    .setThumbUrl( Constants.BUY_IMG_BTT_URL )
//                    .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );
//
//            results.add( buy );
//        }
        return results;
    }


    // TODO перенести в общий для куmittance класс
    private static final SimpleDateFormat DF = new SimpleDateFormat( "yyyy_MM_dd_mm_HH_ss" );
    private static final String HASH_TAG_PREFIX = "#remittance_";

    private static String getRemittanceHashtag(String name, Date date) {
        return HASH_TAG_PREFIX + (name != null ? name : "").replace( " ", "_" ) + DF.format( date );
    }

    public static String getRemittanceMsgData(double sum, String name, String userName, Date date) {
        String hashTag = getRemittanceHashtag(name, date);
        String remMsgTextData = ( name != null )  ?
                String.format(getNamdePtt(), hashTag, sum, name, userName) :
                String.format(getUnNamdePtt(), hashTag, sum, userName);
        return remMsgTextData;
    }

    public static String getNamdePtt() {
        return Constants.NAMED_REMITTANCE_MSG;
    }

    public static String getUnNamdePtt() {
        return Constants.UNNAMED_REMITTANCE_MSG;
    }

    public static String getRemittanceMsgEnd() {
        return Constants.REMITTANCE_MSG_LOADING_LINE;
    }

}
