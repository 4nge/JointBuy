package ru.ange.jointbuy.extensions.bots.handlerbot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface Handler {

    boolean supports(Update update);
    void execute(AbsSender sender, Update update);

}
