package ru.ange.jointbuy.extensions.bots.commandbot.callbackcommands.impl;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.ange.jointbuy.extensions.bots.Callback;

public abstract class CallBackBotCommand extends BotCommand {

    private Callback callback;

    public CallBackBotCommand(String commandIdentifier, String description, Callback callback) {
        super( commandIdentifier, description );
        this.callback = callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        this.executeWithCallback( absSender, user, chat, strings );
        if (this.callback != null) {
            this.callback.call( absSender, user, chat, strings );
        }
    }

    public abstract void executeWithCallback(AbsSender absSender, User user, Chat chat, String[] strings);

}
