package ru.ange.jointbuy.extensions.bots.handlerbot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.ange.jointbuy.extensions.bots.handlerbot.handlers.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public abstract  class TelegramLongPollingHandlerBot extends TelegramLongPollingCommandBot {

    private List<Handler> handlers;

    public TelegramLongPollingHandlerBot(String botName) {
        super( botName );
        this.handlers = new ArrayList<Handler>();
    }

    public TelegramLongPollingHandlerBot(DefaultBotOptions options, String botName) {
        super( options, botName );
        this.handlers = new ArrayList<Handler>();
    }

    public TelegramLongPollingHandlerBot(DefaultBotOptions options, boolean allowCommandsWithUsername, String botName) {
        super( options, allowCommandsWithUsername, botName );
        this.handlers = new ArrayList<Handler>();
    }

    public void register(Handler botHandler) {
        this.handlers.add( botHandler );
    }

    public void registerAll(Handler... botHandlers) {
        this.handlers.addAll( Arrays.asList( botHandlers ) );
    }

    public boolean deregister(Handler botHandler) {
        if (this.handlers.contains( botHandler )) {
            return this.handlers.remove( botHandler );
        }
        return false;
    }

    public final Collection<Handler> getRegisteredHandlers() {
        return this.handlers;
    }

//    @Override
//    public void processNonCommandUpdate(Update update) {
//        for (Handler handlers : this.handlers) {
//            if (handlers.supports( update )) {
//                handlers.execute( update );
//            }
//        }
//    }

}
