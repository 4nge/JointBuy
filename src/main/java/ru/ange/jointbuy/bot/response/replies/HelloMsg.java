package ru.ange.jointbuy.bot.response.replies;


import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.jointbuy.bot.response.ResponseHandler;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.utils.Constants;

import java.util.List;

public class HelloMsg extends SendMessage {


    public HelloMsg(long chatId, List<Member> members) {
        super();
        setText( getText( members ) );
        setReplyMarkup( getMarkupInline() );
        setChatId( chatId );
    }


    public InlineKeyboardMarkup getMarkupInline() {
        List<List<InlineKeyboardButton>> keyboard = ResponseHandler.createInlineRowsKeyboard(
                ResponseHandler.createInlineKeyboardBtt( getJoinUserBttText(), getJoinUserBttCallback() ) );
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );
        return markupInline;
    }

    private String getText(List<Member> members) {
        String usersText = new String();
        for (int i = 0; i < members.size(); i++) {
            Member user = members.get( i );
            String fn = user.getFirstName() != null ? user.getFirstName() : "";
            String ln = user.getFirstName() != null ? user.getLastName() : "";
            usersText += String.format( Constants.START_CHAT_MSG_USER_PTT, fn, ln );
        }
        return EmojiParser.parseToUnicode(String.format( Constants.START_CHAT_MSG_TEXT, usersText) ) ;
    }

    public static String getJoinUserBttText() {
        return Constants.JOIN_USER_BTT_TEXT;
    }

    public static String getJoinUserBttCallback() {
        return Constants.JOIN_USER_BTT_CALLBACK;
    }

}




