package ru.ange.jointbuy.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.utils.Constants;
import ru.ange.jointbuy.utils.StringFormater;

import java.util.function.Predicate;

public class Predicates {


    // ------ inline actions ------

    public static Predicate<Update> isInlineQuery() {
        return upd -> upd.hasInlineQuery() && upd.getInlineQuery().hasQuery();
    }


    // ------ Remittance ------

    public static Predicate<Update> isInlineRemittanceAnswer() {
        return Flag.MESSAGE.and(upd ->
                isMsgMatches(upd, Constants.REMITTANCE_MSG_TEXT_PTT)
        );
    }

    public static Predicate<Update> isRemittanceSetRecipientCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
                isCallbackMatches(upd, Constants.REMITTANCE_RECIPIENT_BTT_CALLBACK)
        );
    }






    public static Predicate<Update> isHelloReply() {
        return Flag.CALLBACK_QUERY.and( upd ->
            isCallbackMatches( upd, HelloMsg.CALLBACK_DATA )
        );
    }

    public static Predicate<Update> isAddNewMember() {
        return upd -> upd.hasMessage() && upd.getMessage().getNewChatMembers() != null;
    }

    public static Predicate<Update> isAddMyself(User me) {
        return isAddNewMember().and( upd -> upd.getMessage().getNewChatMembers().contains(me));
    }





    public static Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().trim().equalsIgnoreCase(message.trim());
        };
    }

    public static Predicate<Update> isReplyToBot(String botUserName) {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(botUserName);//
    }

    public static Predicate<Update> isInlinePutchaseAnswer() {
        return Flag.MESSAGE.and(upd ->
            isMsgMatches(upd, Constants.INLINE_BUY_MSG_TEXT_PTT)
        );
    }



    public static Predicate<Update> isPurchaseMembersCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.PURCHASE_MEMBERS_BTT_CALLBACK)
        );
    }

    public static Predicate<Update> isPurchaseJoinCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.PURCHASE_JOIN_CALLBACK)
        );
    }

    public static Predicate<Update> isPurchaseJoinOutCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.PURCHASE_JOIN_OUT_CALLBACK)
        );
    }

    public static Predicate<Update> isEditPurchaseCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.PURCHASE_EDIT_BTT_CALLBACK)
        );
    }

    public static Predicate<Update> isPurchaseRemoveCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.DELETE_PURCHASE_CALLBACK)
        );
    }

    public static Predicate<Update> isPurchaseRestoreCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.RESTORE_PURCHASE_CALLBACK)
        );
    }

    public static Predicate<Update> isBackCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.BACK_BTT_CALLBACK)
        );
    }

    public static  Predicate<Update> isMemberRemoveFromPurchaseCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.REMOVE_FROM_PURCHASE_CALLBACK_PTT)
        );
    }

    public static Predicate<Update> isMemberAddToPurchaseCallback() {
        return Flag.CALLBACK_QUERY.and(upd ->
            isCallbackMatches(upd, Constants.ADD_TO_PURCHASE_CALLBACK_PTT)
        );
    }





    private static boolean isCallbackMatches(Update upd, String callback) {
        String query = upd.getCallbackQuery().getData();
        return query.matches(StringFormater.getRegexFromFormatString(callback));
    }

    private static boolean isMsgMatches(Update upd, String msg) {
        if (upd.getMessage().hasText()) {
            String text = upd.getMessage().getText();
            String format = EmojiParser.parseToUnicode( msg );
            return StringFormater.matchesFormatString( text, StringFormater.removeMarkdownSyntax( format ));
        }
        return false;
    }

}
