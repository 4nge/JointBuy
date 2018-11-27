//package ru.ange.jointbuy;
//
//import org.telegram.telegrambots.bots.DefaultBotOptions;
//import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
//import org.telegram.telegrambots.meta.api.objects.Chat;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.User;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import ru.ange.jointbuy.extensions.bots.bot.commands.AddUserCommand;
//import ru.ange.jointbuy.services.BotService;
//
//
//
//public class JointBuyCommandBot extends TelegramLongPollingCommandBot {
//
//    public static final String TOKEN = "795411227:AAEM4KjNFi9AayQzPr4eTAAQ3VcQA9QRpjw";
//    public static final String NAME = "JointBuyBot";
//    public static final boolean ALLOW_COM_USER_NAME = true;
//
//    private BotService botService;
//
//    public JointBuyCommandBot(DefaultBotOptions options, BotService botService) {
//        super( options, ALLOW_COM_USER_NAME, NAME );
//        this.botService = botService;
//
//        register( new AddUserCommand((AbsSender absSender, User user, Chat chat, String[] strings)-> {
//            botService.enableAddUserMode( chat );
//        }) );
//
//    }
//
//    @Override
//    public String getBotToken() {
//        return TOKEN;
//    }
//
//    @Override
//    public void processNonCommandUpdate(Update update) {
//        botService.handleNonCommandUpdate( update );
//    }
//
//    }
//        //        try {
////            List<BotApiMethod> bams = new ArrayList<BotApiMethod>();
////            if (update.getMessage() != null) {
////                bams.addAll( handleMessage(update.getMessage()) );
////            }
////            if (update.hasCallbackQuery()) {
////                bams.addAll( handleCallBackQuery(update.getCallbackQuery()) );
////            }
////
////            for (int j = 0; j < bams.size(); j++) {
////                BotApiMethod bam = bams.get( j );
////                execute( bam );
////            }
////        } catch (TelegramApiException e) {
////            e.printStackTrace();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    private List<BotApiMethod> handleCallBackQuery(CallbackQuery callbackQuery) {
////        return botService.handleCallbackQuery( callbackQuery );
////    }
////
////
////    private List<BotApiMethod> handleMessage(org.telegram.telegrambots.meta.api.objects.Message msg) {
////
////        List<BotApiMethod> result = new ArrayList<BotApiMethod>();
////
////        long chatId = msg.getChatId();
////        long messageId = msg.getMessageId();
////
////        if (msg.getNewChatMembers() != null) {
////            List<User> users = msg.getNewChatMembers();
////            for (int i = 0; i < users.size(); i++) {
////                User user = users.get( i );
////                if (user.getBot() && user.getUserName().equals( NAME )) {
////                    User sender = msg.getFrom();
////                    result.addAll( botService.handleStartChat(chatId, messageId, sender) );
////                } else {
////                    result.addAll( botService.handleAddUser(chatId, messageId, user) );
////                }
////            }
////        }
////        return result;
////    }
////
////    public void enableAddUserMode() {}
////
////    public void disableAddUserMode() {}
////}
