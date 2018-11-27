package ru.ange.jointbuy.extensions.bots.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.extensions.bots.Callback;
import ru.ange.jointbuy.extensions.bots.commandbot.callbackcommands.impl.CallBackBotCommand;

public class AddUserCommand extends CallBackBotCommand {

    public static final String COMMAND = "add";
    public static final String DESCRIPTION = "Добавить пользователя к закупкам";

    public static final String MSG_TEXT = "" +
            "Чтобы добавить участников закупки пришлите мне имена пользователей этого чата в формате:\n" +
            "@username1, @username2\n\n" +
            "Либо, если вы хотите добавить пользователей, которых (почему то все еще) нет в Telegram - пришлите их данные в формате:\n" +
            "Иван - Иванов\n" +
            "Петр - Петров\n";

    public AddUserCommand(Callback callback) {
        super( COMMAND, DESCRIPTION, callback);
    }

    @Override
    public void executeWithCallback(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            SendMessage msg = new SendMessage( chat.getId(), MSG_TEXT);
            absSender.execute( msg );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
