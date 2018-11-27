package ru.ange.jointbuy.extensions.bots.handlerbot.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.ange.jointbuy.extensions.bots.handlerbot.handlers.Handler;

public abstract class BotHandler implements Handler {

    public abstract void execute(AbsSender absSender, Update update);

}
