package ru.ange.jointbuy.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.services.BotService;
import ru.ange.jointbuy.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

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




    // ----- Actions -----

    public Ability addUser() {
        AddUserMsg addUserMsg = new AddUserMsg();
        return Ability.builder()
                .name("addUser")
                .info("add new users")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    silent.forceReply( addUserMsg.getText(), ctx.chatId() );
                })
                .reply(upd -> {
                    botService.handleAddUserCommand(upd, getChatId( upd ));
                    //responseHandler.sendHelloMsg( chatId, currentMembers );
                }, Flag.MESSAGE, Flag.REPLY, isReplyToBot(), isReplyToMessage(addUserMsg.getText()))
                .build();
    }

    public Reply sayHello() {
        Consumer<Update> action = upd -> {
            long chatId = getChatId( upd );
            List<Member> members = botService.getMembers( new ArrayList<User>(users().values()), chatId );
            responseHandler.sendHelloMsg( chatId, members );
        };
        return Reply.of(action, isAddMyself());
    }


    public Reply replyAddUserBtt() {
        Consumer<Update> action = upd -> {
            String callId = upd.getCallbackQuery().getId();
            boolean alreadyExist = botService.handleAddUserBtt( upd, getChatId( upd ) );
            if (alreadyExist)
                responseHandler.answerAddUser( callId );
            else
                responseHandler.answerUserAlreadyExist( callId );
        };
        return Reply.of(action, isHelloReply());
    }

    public Reply handleInlineQuery() {
        Consumer<Update> action = upd -> {
            InlineQuery query = upd.getInlineQuery();
            User user = query.getFrom();
            int memCounts = botService.getMembers(getChatId(upd)).size();
            responseHandler.answerInlineQuery(query.getQuery(), query.getId(), user, memCounts);
        };
        return Reply.of(action, isInlineQuery());
    }


    public Reply handleInlineAnswer() {
        Consumer<Update> action = upd -> {
            String text = upd.getMessage().getText();
            String lines[] = text.split("[\r\n]+");
            String name = lines[2].trim();
            String sumStr = lines[3].replace("\u1F4B6 - ","").replace("u20BD","");
            double sum = Double.valueOf( sumStr );

            System.out.println("name = " + name);
            System.out.println("sum = " + sum);
        };
        return Reply.of(action, Flag.MESSAGE, isInlineAnswer());
    }



    // ----- Predicates -----

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

    private Predicate<Update> isInlineQuery() {
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

    private Predicate<Update> isInlineAnswer() {
        return upd -> {
            if (upd.hasMessage() && upd.getMessage().hasText()) {
                String text = upd.getMessage().getText();
                String regex = String.format(Constants.INLINE_BUY_MSG_TEXT_PTT.replace("%s",".*" ));
                return text.matches( EmojiParser.parseToUnicode(regex) );
            }
            return false;
        };
    }
}
