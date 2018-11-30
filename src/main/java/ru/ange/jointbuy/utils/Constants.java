package ru.ange.jointbuy.utils;

public class Constants {


    public static final String BUY_IMG_BTT_URL = "https://image.ibb.co/jpLPpf/buy.png";
    public static final String REMITTANCE_IMG_BTT_URL = "https://image.ibb.co/dyyzN0/remittance.png";

    public static final String BUY_IMG_BTT_TEXT = "Добавить покупку";
    public static final String REMITTANCE_IMG_BTT_TEXT = "Добавить перевод";


    public static final String ADD_USER_MSG =
            "Чтобы добавить участников закупки ответьте на это сообщение, либо сообщением с именами пользователей этого чата в формате:\n" +
            "@username @username\n\n" +
            "Либо, если вы хотите добавить пользователей, которых (почему то все еще) нет в Telegram - сообщением с их данными в формате:\n" +
            "Иван Иванов\n" +
            "Петр Петров\n" +
            "Васька";

    private static final String JOIN_BTT_TEXT = "Участвовать в закупках";

    public static final String ADD_USER_CALLBACK_DATA = "join_new_user";

    private static final String HELLO_CHAT_MSG_TEXT =
            "Рад приветствовать!\n" +
            "Я бот для для управления совместными расходами, помогаю учесть кто что покупал и кто кому остался должен.\n\n" +
            "К сожалению у меня нет доступа к списку собеседников в чате. " +
            "Поэтому для начала необходимо добавить пользователей, которые будут участвовать в закупках.\n" +
            "Для этого нужно всем участникам нажать на кнопку \""+JOIN_BTT_TEXT+"\", либо воспользоваться командой /addUser.\n\n";

    private static final String USER_LIST_MSG_TEXT =
            "Текущий список пользователей:\n%s";

    private static final String USER_LIST_MSG_PTT = " :bust_in_silhouette: %s %s\n";

    public static final String ADD_USER_CALLBACK_ANSWER = "Вы присодинились к списку участников";
    public static final String USER_EXIST_CALLBACK_ANSWER = "Вы уже есть в списке участников";

    public static final String INLINE_TEXT_PTT = "Cумма: %s \u20BD\nНаименование: %s ";


    public static final String INLINE_BUY_MSG_TEXT_PTT = "#%s\nПокупка:\n\n%s\n:euro: - %s \u20BD\n:bust_in_silhouette: - %s\n:busts_in_silhouette: - %s";

}
