package ru.ange.jointbuy.bot.msg;


import com.vdurmont.emoji.EmojiParser;
import org.jetbrains.annotations.NotNull;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.jointbuy.pojo.Member;

import java.util.ArrayList;
import java.util.List;

public class HelloMsg {

    private static final String JOIN_BTT_TEXT = "Участвовать в закупках";

    private static final String MSG_TEXT = "" +
            "Рад приветствовать!\n" +
            "Я бот для для управления совместными расходами, помогаю учесть кто что покупал и кто кому остался должен.\n\n" +
            "К сожалению у меня нет доступа к списку собеседников в чате. " +
            "Поэтому для начала необходимо добавить пользователей, которые будут участвовать в закупках.\n" +
            "Для этого нужно всем участникам нажать на кнопку \""+JOIN_BTT_TEXT+"\", либо воспользоваться командой /addUser.\n\n" +
            "Текущий список пользователей:\n%s";

    private static final String USER_PTT = " :bust_in_silhouette: %s %s\n";
    public static final String CALLBACK_DATA = "join_new_user";

    public static final String ADD_USER = "Вы присодинились к списку участников";
    public static final String USER_ALREADY_EXISTS = "Вы уже есть в списке участников";

    private List<Member> members;


    public HelloMsg(List<Member> members) {
        this.members = members;
    }

    public String getText() {
        String usersText = new String();
        for (int i = 0; i < members.size(); i++) {
            Member user = members.get( i );
            String fn = user.getFirstName() != null ? user.getFirstName() : "";
            String ln = user.getFirstName() != null ? user.getLastName() : "";
            usersText += String.format( USER_PTT, fn, ln );
        }
        return EmojiParser.parseToUnicode(String.format( MSG_TEXT, usersText ));
    }

    public ReplyKeyboard getMarkup() {
        return getReplyKeyboard( JOIN_BTT_TEXT, CALLBACK_DATA );
    }

    @NotNull
    static ReplyKeyboard getReplyKeyboard(String joinBttText, String callbackData) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText( joinBttText ).setCallbackData( callbackData ));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}




