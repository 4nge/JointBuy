package ru.ange.jointbuy.utils;

import ru.ange.jointbuy.bot.JointBuyAbilityBot;

public class Constants {


    // ------ Start chat actions ------

    public static final String JOIN_USER_BTT_TEXT = "Участвовать в закупках";
    public static final String JOIN_USER_BTT_CALLBACK = "join_new_user";

    public static final String JOIN_USER_BTT_ANSWER_SUCCESS = "Вы присодинились к списку участников";
    public static final String JOIN_USER_BTT_ANSWER_EXISTS = "Вы уже есть в списке участников";


    public static final String START_CHAT_MSG_TEXT = "" +
            "Рад приветствовать!\n" +
            "Я бот для для управления совместными расходами, помогаю учесть кто что покупал и кто кому остался должен.\n\n" +
            "К сожалению у меня нет доступа к списку собеседников в чате. " +
            "Поэтому для начала необходимо добавить пользователей, которые будут участвовать в закупках.\n" +
            "Для этого нужно всем участникам нажать на кнопку \""+JOIN_USER_BTT_TEXT+"\", либо воспользоваться командой /addUser.\n\n" +
            "Текущий список пользователей:\n%s";

    public static final String START_CHAT_MSG_USER_PTT = " :bust_in_silhouette: %s %s\n";



    // ------ inline actions ------

    public static final String BUY_IMG_BTT_URL = "https://image.ibb.co/jpLPpf/buy.png";


    public static final String BUY_IMG_BTT_TEXT = "Добавить покупку";


    // ------------------------
    // ------ Remittance ------
    // ------------------------

    public static final String REMITTANCE_INLINE_BTT_IMG_URL = "https://image.ibb.co/dyyzN0/remittance.png";
    public static final String REMITTANCE_INLINE_BTT_TEXT = "Добавить перевод";


    public static final String REMITTANCE_MSG_START =
            "%s\n" +
            "Перевод\n" +
            ":euro: - %s \u20BD\n";

    public static final String REMITTANCE_MSG_ENVELOPE_LINE =
            ":envelope: - %s";

    public static final String REMITTANCE_MSG_LABEL_LINE =
            ":label: - %s\n";

    public static final String REMITTANCE_MSG_RECIP_LINE =
            "\n:mailbox_with_mail: - %s";

    public static final String REMITTANCE_MSG_LOADING_LINE =
            "\n\n:hourglass_flowing_sand: Идет загрузка списка получателей...";

    public static final String REMITTANCE_MSG_NOT_MEMBERS_LINE =
            "\n\n:exclamation: Нет доступных получателей.";


    public static final String REMITTANCE_MSG_CHOOSE_MEMBERS_LINE =
            "\n\n:mag: Выберите получателя из списка:";


    public static final String NAMED_REMITTANCE_MSG =
            REMITTANCE_MSG_START +
            REMITTANCE_MSG_LABEL_LINE +
            REMITTANCE_MSG_ENVELOPE_LINE;

    public static final String UNNAMED_REMITTANCE_MSG =
            REMITTANCE_MSG_START +
            REMITTANCE_MSG_ENVELOPE_LINE;

    public static final String REMITTANCE_RECIPIENT_BTT_PTT =
            ":bust_in_silhouette: %s";

    public static final String REMITTANCE_RECIPIENT_BTT_CALLBACK =
            "remittance_recipient_remId_%s_memId_%s";

    public static final String REMITTANCE_DELETED_MSG = "%s \nЭтот перевод был удалён";

    public static final String REMITTANCE_EDIT_BTT_CALLBACK = "edit_remittance_%s";
    public static final String REMITTANCE_DELETE_CALLBACK = "delete_remittance_%s";
    public static final String REMITTANCE_RESTORE_CALLBACK = "restore_remittance_%s";




    // ------------------------
    // ------ Buttons ------
    // ------------------------

    public static final String EDIT_BTT_LABEL = ":pencil: Радактировать";
    public static final String DELETE_BTT_LABEL = "\uD83D\uDDD1️ Удалить";
    public static final String RESTORE_BTT_LABEL = ":leftwards_arrow_with_hook: Востановить";





    public static final String ADD_USER_MSG =
            "Чтобы добавить участников закупки ответьте на это сообщение, либо сообщением с именами пользователей этого чата в формате:\n" +
            "@username @username\n\n" +
            "Либо, если вы хотите добавить пользователей, которых (почему то все еще) нет в Telegram - сообщением с их данными в формате:\n" +
            "Иван Иванов\n" +
            "Петр Петров\n" +
            "Васька";








    public static final String PURCHASE_ADD_USER_CALLBACK_ALERT = "Вы присодинились к покупке";
    public static final String PURCHASE_EXIST_USER_CALLBACK_ALERT = "Вы уже являетесь участником этой покупки";

    public static final String ADD_USER_COMMAND_NAME = "add";
    public static final String ADD_USER_COMMAND_DESCRIPTION = "Добавить новых пользователей";

    public static final String LIST_PURCHASES_COMMAND_NAME = "list";
    public static final String LIST_PURCHASES_COMMAND_DESCRIPTION = "Показать список операций";

    public static final String SPLIT_PURCHASES_COMMAND_NAME = "split";
    public static final String SPLIT_PURCHASES_COMMAND_DESCRIPTION = "Разбить счет";

    public static final String INLINE_PURCH_TEXT_PTT = "Cумма: %s \u20BD\nНаименование: %s ";
    public static final String INLINE_REMIT_TEXT_PTT = "Cумма: %s \u20BD";
    public static final String INLINE_REMIT_DESC_TEXT_PTT = "Cумма: %s \u20BD\nОписание: %s ";

    //public static final String INLINE_REMITTANCE_MSG_TEXT_PTT = "#remittance%_s\nПеревод\n:euro: - %s \u20BD\n:incoming_envelope: - %s\n:mailbox_with_mail: - %s";


    public static final String INLINE_BUY_MSG_TEXT_PTT = "#%s\n`Покупка`\n\uD83C\uDFF7️ - *%s*\n:euro: - %s \u20BD\n:busts_in_silhouette: - %s\n:bust_in_silhouette: - %s";
    public static final String INLINE_BUY_MSG_TEXT_ALL = "Все";
    public static final String INLINE_BUY_MSG_TEXT_COUNT = "%s / %s";


    public static final String PURCHASE_JOIN_BTT_TEXT = "\uD83D\uDE4B\u200D♂️";
    public static final String PURCHASE_JOIN_CALLBACK = "purchase_join";

    public static final String PURCHASE_JOIN_OUT_BTT_TEXT = "\uD83D\uDE45\u200D♂️️";
    public static final String PURCHASE_JOIN_OUT_CALLBACK = "purchase_join_out";

    public static final String PURCHASE_MEMBERS_BTT_TEXT = ":busts_in_silhouette: Участники";
    public static final String PURCHASE_MEMBERS_BTT_CALLBACK = "show_purchase_member";

    public static final String PURCHASE_EDIT_BTT_TEXT = ":pencil: Радактировать";
    public static final String PURCHASE_EDIT_BTT_CALLBACK = "edit_purchase";

    public static final String DELETE_PURCHASE_BTT_TEXT = "\uD83D\uDDD1️ Удалить";
    public static final String DELETE_PURCHASE_CALLBACK = "delete_purchase";

    public static final String BACK_BTT_TEXT = ":arrow_left: Назад";
    public static final String BACK_BTT_CALLBACK  = "members_back";

    public static final String IMPROVED_MEMBER_PTT = ":ballot_box_with_check: %s";
    public static final String NOT_IMPROVED_MEMBER_PTT = ":white_medium_square: %s";

    public static final String REMOVE_FROM_PURCHASE_CALLBACK_PTT  = "remove_from_purchase_mID_%s_prId_%s";
    public static final String ADD_TO_PURCHASE_CALLBACK_PTT = "add_to_purchase_mID_%s_prId_%s";

    public static final String AT_LEAST_ONE_MEMBER_ALERT = "У покупки должен быть хотя бы один участник";

    public static final String DELETED_PURCHASE_MSG = "#%s\n`Покупка удалена`";

    public static final String RESTORE_PURCHASE_BTT_TEXT = ":leftwards_arrow_with_hook: Востановить";
    public static final String RESTORE_PURCHASE_CALLBACK = "restore_purchase";

    public static final String LIST_PURCHASES_MSG_EMPTY = "Список покупок пока пуст. Для добавления новых записей наберите '@" + JointBuyAbilityBot.NAME + " сумма Наименование покупки'";

    public static final String LIST_PURCHASES_MSG_PTT = "Список покупок:%s\n\n `ИТОГО (%s) : ` *%s* \u20BD";
    public static final String LIST_PURCHASE_LINE_PTT = "\n\n#%s\n:euro: %s \u20BD : \uD83C\uDFF7️ *%s*";

    public static final String SPLIT_MEMBER_PTT = "\n*%s* `потратил` *%s* \u20BD%s";
    public static final String SPLIT_MEMBER_SHOULD = "`; тебе должны:`\n%s";
    public static final String SPLIT_MEMBER_SHOULD_NONE = "`; тебе никто не должен.`";


    //public static final String SPLIT_DEBTOR_PTT = ":bust_in_silhouette: %s - :euro: %s \u20BD\n";
    public static final String SPLIT_DEBTOR_PTT = "- %s - %s \u20BD\n";
    public static final String SPLIT_DEBTORS_NONE = "Никто никому ничего не должен";
}
