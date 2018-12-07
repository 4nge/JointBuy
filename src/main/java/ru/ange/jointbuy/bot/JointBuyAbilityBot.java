package ru.ange.jointbuy.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.bot.response.ResponseHandler;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Operation;
import ru.ange.jointbuy.pojo.Purchase;
import ru.ange.jointbuy.services.BotService;
import ru.ange.jointbuy.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
                responseHandler.answerUserAlreadyExist( callId );
            else
                responseHandler.answerAddUser( callId );
        };
        return Reply.of(action, isHelloReply());
    }

    public Reply handleInlineQuery() {
        Consumer<Update> action = upd -> {
            InlineQuery inlineQuery = upd.getInlineQuery();
            User user = inlineQuery.getFrom();
            responseHandler.answerInlineQuery(inlineQuery.getQuery(), inlineQuery.getId(), user);
        };
        return Reply.of(action, isInlineQuery());
    }


    public Reply handleInlineAnswer() {
        Consumer<Update> action = upd -> {
            String inlineMsgId = upd.getChosenInlineQuery().getInlineMessageId();
            String query = upd.getChosenInlineQuery().getQuery();
            User from = upd.getChosenInlineQuery().getFrom();

            String digit = query.substring( 0, query.indexOf( " " ) );
            String name = query.substring( query.indexOf( " " ) + 1, query.length() );
            double summ = Double.valueOf( digit.replace( ",", "." ) );

            botService.addPurchase(inlineMsgId, name, summ, from, new Date());
        };
        return Reply.of(action, Flag.CHOSEN_INLINE_QUERY);
    }

    public Reply handleInlineAnswerMsg() {
        Consumer<Update> action = upd -> {
//            long chatId = getChatId( upd );
//
//            System.out.println("handleInlineAnswer : upd = " + upd);
//
//            Message msg = upd.getMessage();
//            String text = msg.getText();
//            User from = msg.getFrom();
//
//            String lines[] = text.split("[\r\n]+");
//            String type = lines[1].trim();
//            String name = lines[2].trim();
//            double summ = Double.valueOf(lines[3]
//                    .replace("\uD83D\uDCB6", "" )
//                    .replace("-", "" )
//                    .replace("\u20BD", "" )
//                    .trim());
//
//            System.out.println("handleInlineAnswer : msg = " + msg);
//
//            botService.addPurchase(chatId, name, summ, from,  new Date());
//            responseHandler.editAnswerInlineQuery(chatId, msg.getMessageId(), name, summ, from);
            // TODO update chat id for handleInlineAnswer() purshase
        };
        return Reply.of(action, isInlineAnswer());
    }

    public Reply handleShowPurshaceMembersCallback() {
        Consumer<Update> action = upd -> {

            System.out.println("handleShowPurshaceMembersCallback");
            System.out.println("getCallbackQuery = " + upd.getCallbackQuery());

            EditMessageText emt = new EditMessageText()
                    .enableMarkdown( true )
                    .setInlineMessageId( upd.getCallbackQuery().getInlineMessageId() )
                    .setText( "Test" );
        try {
            sender.execute( emt );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
            e.printStackTrace();
        }

        };
        return Reply.of(action, isPurchaseMembersCallback());
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
        // TODO try do on inline msg ID
        return Flag.MESSAGE.and(upd -> {
            if (upd.hasMessage() && upd.getMessage().hasText()) {
                String text = upd.getMessage().getText();
                String regex = EmojiParser.parseToUnicode(Constants.INLINE_BUY_MSG_TEXT_PTT)
                        .replace( "`", "" )     // replace markdown symbols
                        .replace( "*", "" )     // replace markdown symbols
                        .replace( "_", "" )     // replace markdown symbols
                        .replace("%s",".*" );   // replace String parameters

                return text.matches( EmojiParser.parseToUnicode(regex) );
            }
            return false;
        });
    }




    private Predicate<Update> isPurchaseMembersCallback() {
        return Flag.CALLBACK_QUERY.and(upd -> {
            return upd.getCallbackQuery().getData().equals( Constants.PURCHASE_MEMEBERS_BTT_CALLBACK );
        });
    }

}
