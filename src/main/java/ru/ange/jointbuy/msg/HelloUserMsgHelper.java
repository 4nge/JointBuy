package ru.ange.jointbuy.msg;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class HelloUserMsgHelper {

    private static final String MSG_TEXT = "" +
            "Добро пожаловать %s\n" +
            "Я добавил Вас в список участников закупок. Все траты созданные в этой группе будут делится и на Вас.";

    public static final String CALLBACK_DATA = "edit_users";

    private static final String BTT_TEXT = "Редактировать список пользователей";

    public static String getMsg(String userName) {
        return String.format( MSG_TEXT, userName );
    }

    public static InlineKeyboardMarkup getMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(BTT_TEXT).setCallbackData(CALLBACK_DATA));
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}
