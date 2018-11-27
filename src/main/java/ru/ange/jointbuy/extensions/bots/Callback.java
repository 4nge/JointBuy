package ru.ange.jointbuy.extensions.bots;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface Callback {

    void call(AbsSender absSender, User user, Chat chat, String[] strings);

}
