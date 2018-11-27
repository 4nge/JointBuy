package ru.ange.jointbuy.bot.msg;

public class AddUserMsg {

    public static final String MSG_TEXT = "" +
            "Чтобы добавить участников закупки пришлите мне имена пользователей этого чата в формате:\n" +
            "@username1, @username2\n\n" +
            "Либо, если вы хотите добавить пользователей, которых (почему то все еще) нет в Telegram - пришлите их данные в формате:\n" +
            "имя_1 - фамилия_1\n" +
            "имя_2 - фамилия_2\n";

    public String getText() {
        return MSG_TEXT;
    }
}
