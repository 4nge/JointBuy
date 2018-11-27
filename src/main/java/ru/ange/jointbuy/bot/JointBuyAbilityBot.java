package ru.ange.jointbuy.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.exception.MemberAlreadyExistException;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.services.BotService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.telegram.abilitybots.api.util.AbilityUtils;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

import java.util.function.Predicate;

public class JointBuyAbilityBot extends AbilityBot {

    public static final String TOKEN = "795411227:AAEM4KjNFi9AayQzPr4eTAAQ3VcQA9QRpjw";
    public static final String NAME = "JointBuyBot";

    private BotService botService;
    private ResponseHandler responseHandler;

    public JointBuyAbilityBot(DefaultBotOptions botOptions, BotService botService) {
        super( TOKEN, NAME, botOptions );

        this.botService = botService;
        this.responseHandler = new ResponseHandler(sender);
    }

    @Override
    public int creatorId() {
        return 0;
    }

    public Ability addUser() {

        AddUserMsg addUserMsg = new AddUserMsg();
        return Ability.builder()
                .name("add")
                .info("add new users")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    System.out.println("users = " + users().values());
                    silent.forceReply(addUserMsg.getText(), ctx.chatId());
                })
                .reply(upd -> {

                    User sender = upd.getMessage().getFrom();
                    List<User> mentions = new ArrayList<User>();
//
//                    for (MessageEntity me : upd.getMessage().getEntities()) {
//                        System.out.println("me = " + me.getUser());
//                        System.out.println("me = " + me.getText());
//                        //mentions.add(  );
//                    }

                    System.out.println("sender = " + sender);
                    System.out.println("mentions = " + mentions);
                    // TODO createMembersfromString

                    String query = upd.getMessage().getText();

                    while (query.indexOf( "-" ) > 0) {

                        System.out.println("query = " + query);

                        int idx = query.indexOf( "-" );
                        int start = query.substring(0, idx).lastIndexOf( "\n" );
                        int end = idx + query.substring( idx, query.length()).indexOf( "\n" );

                        System.out.println("idx = " + idx);
                        System.out.println("start = " + start);
                        System.out.println("end = " + end + "; end query = " + query.substring( idx, query.length()));

                        String firstName = query.substring(start > 0 ? start : 0, idx).trim();
                        String lastName = query.substring(idx+1, end > 0 ? end : query.length()).trim();

                        query = query.substring( end, query.length() );
                        System.out.println("firstName = " + firstName);
                        System.out.println("lastName = " + lastName);
                    }



                }, Flag.MESSAGE, Flag.REPLY, isReplyToBot(), isReplyToMessage(addUserMsg.getText()))
                .build();
    }

    public Reply sayHello() {
        Consumer<Update> action = upd -> {
            long chatId = getChatId( upd );
            List<User> chatUsers = new ArrayList<User>(users().values());
            List<Member> currentMembers =  botService.getMembers( chatUsers, chatId );
            responseHandler.sendHelloMsg( chatId, currentMembers );
        };
        return Reply.of(action, isAddMyself());
    }


    public Reply replyAddUserBtt() {
        Consumer<Update> action = upd -> {
            long chatId = getChatId( upd );
            User sender = upd.getCallbackQuery().getFrom();
            String callId = upd.getCallbackQuery().getId();
            try {
                botService.addMember( sender, chatId );
                responseHandler.answerAddUser( callId );
            } catch (MemberAlreadyExistException e) {
                responseHandler.answerUserAlreadyExist( callId );
            }

        };
        return Reply.of(action, isHelloReply());
    }

    public Reply handleInline() {
        Consumer<Update> action = upd -> {
            InlineQuery query = upd.getInlineQuery();
            responseHandler.answerInlineQuery( query.getQuery(), query.getId() );
        };
        return Reply.of(action, isInline());
    }



    private Predicate<Update> isHelloReply() {
        return Flag.CALLBACK_QUERY.and( upd -> upd.getCallbackQuery().getData().equals( HelloMsg.CALLBACK_DATA ) );
    }

    private Predicate<Update> isAddNewMember() {
        return upd -> upd.hasMessage() && upd.getMessage().getNewChatMembers() != null;
    }

    private Predicate<Update> isAddMyself() {
        try {
            User me = getMe();
            return isAddNewMember().and( upd -> upd.getMessage().getNewChatMembers().contains( me ) );
        } catch (TelegramApiException e) {
            return upd -> false;
        }
    }

    private Predicate<Update> isInline() {
        return upd -> upd.hasInlineQuery() && upd.getInlineQuery().hasQuery();
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().trim().equalsIgnoreCase(message.trim());
        };
    }

    private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
    }
}
