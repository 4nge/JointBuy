package ru.ange.jointbuy.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.response.ResponseHandler;
import ru.ange.jointbuy.exception.MemberAlreadyExistException;
import ru.ange.jointbuy.pojo.*;
import ru.ange.jointbuy.services.BotService;
import ru.ange.jointbuy.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;
import static ru.ange.jointbuy.bot.Predicates.isHelloReply;

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


    // --------------------------------
    // ------ Start chat actions ------
    // --------------------------------

    public Reply sayHello() {
        Consumer<Update> action = upd -> {
            long chatId = getChatId( upd );
            List<Member> members = botService.getMembers( new ArrayList<User>(users().values()), chatId );
            responseHandler.sendHelloMsg( chatId, members );
        };
        try {
            User me = getMe();
            return Reply.of(action, Predicates.isAddMyself(me));
        } catch (TelegramApiException e) {
            return null;
        }
    }

    public Reply replyJoinBtt() {
        Consumer<Update> action = upd -> {
            String callId = upd.getCallbackQuery().getId();
            try {
                botService.handleAddUserBtt( upd, getChatId( upd ) );
                responseHandler.alertUserAdded( callId );
            } catch (MemberAlreadyExistException e) {
                responseHandler.alertUserAlreadyExist( callId );
            }
        };
        return Reply.of(action, isHelloReply());
    }


    // ----------------------
    // ------ Commands ------
    // ----------------------

//    public Ability addUsersCommand() {
//        AddUserMsg addUserMsg = new AddUserMsg();
//        return Ability.builder()
//                .name( Constants.ADD_USER_COMMAND_NAME )
//                .info( Constants.ADD_USER_COMMAND_DESCRIPTION )
//                .locality(ALL)
//                .privacy(PUBLIC)
//                .action(ctx -> {
//                    silent.forceReply( addUserMsg.getText(), ctx.chatId() );
//                })
//                .reply(upd -> {
//                    botService.handleAddUserCommand(upd, getChatId( upd ));
//                }, Flag.MESSAGE, Flag.REPLY, Predicates.isReplyToBot(getBotUsername()),
//                        Predicates.isReplyToMessage(addUserMsg.getText()))
//                .build();
//    }
//
//    public Ability listOperationCommand() {
//        return Ability.builder()
//                .name( Constants.LIST_PURCHASES_COMMAND_NAME )
//                .info( Constants.LIST_PURCHASES_COMMAND_DESCRIPTION )
//                .locality(ALL)
//                .privacy(PUBLIC)
//                .action(ctx -> {
//                    long chatId = ctx.chatId();
//                    List<Purchase> purchases = botService.getActivePurchases(chatId);
//                    responseHandler.answerListCommand(chatId, purchases);
//                })
//                .build();
//    }
//
//
//    public Ability spitPurchasesCommand() {
//        return Ability.builder()
//                .name( Constants.SPLIT_PURCHASES_COMMAND_NAME )
//                .info( Constants.SPLIT_PURCHASES_COMMAND_DESCRIPTION )
//                .locality(ALL)
//                .privacy(PUBLIC)
//                .action(ctx -> {
//                    long chatId = ctx.chatId();
//                    List<Purchase> purchases = botService.getActivePurchases(chatId);
//                    List<Member> members = botService.getMembers( chatId );
//                    responseHandler.answerSplitCommand(chatId, members, purchases);
//                })
//                .build();
//    }


    // ----------------------------
    // ------ Inline actions ------
    // ----------------------------

    public Reply handleInlineQuery() {
        Consumer<Update> action = upd -> {
            InlineQuery inlineQuery = upd.getInlineQuery();
            User user = inlineQuery.getFrom();
            responseHandler.answerInlineQuery(inlineQuery.getQuery(), inlineQuery.getId(), user);
        };
        return Reply.of(action, Predicates.isInlineQuery());
    }


    // ------------------------
    // ------ Remittance ------
    // ------------------------

    public Reply handleRemittanceInlineAnswerMsg() {
        Consumer<Update> action = upd -> {
            long chatId = getChatId( upd );
            Message msg = upd.getMessage();

            Remittance remittance = botService.addRemittance( chatId, msg );
            List<Member> members = botService.getMembers( chatId );
            responseHandler.handleRemittanceMsg( msg, remittance, members );
        };
        return Reply.of(action, Predicates.isInlineRemittanceAnswer());
    }

    public Reply handleRemittanceSetRecipient() {
        Consumer<Update> action = upd -> {
            CallbackQuery cbQuery = upd.getCallbackQuery();
            String data = cbQuery.getData();
            String inlineMessageId = cbQuery.getInlineMessageId();
            User user = cbQuery.getFrom(); // TODO if sender != user alert error

            Remittance remittance = botService.updateRemittance(data, inlineMessageId);





            //String data = upd.getCallbackQuery().getData();
            //int remId =
            //Remittance remittance = botService.getRemittance();



            // TODO get recipient and rem id from update, update remittance in bd, change msg.
        };
        return Reply.of(action, Predicates.isRemittanceSetRecipientCallback());
    }





//    public Reply handleInlineAnswerPurchaseMsg() {
//        Consumer<Update> action = upd -> {
//            long chatId = getChatId( upd );
//            Message msg = upd.getMessage();
//            String text = msg.getText();
//            User from = msg.getFrom();
//            botService.handleInlineAnswerPurchaseMsg( chatId, text, from );
//        };
//        return Reply.of(action, Predicates.isInlinePutchaseAnswer());
//    }



//    public Reply handleInlineAnswer() {
//        Consumer<Update> action = upd -> {
//            String resultId = upd.getChosenInlineQuery().getResultId();
//            String inlineMsgId = upd.getChosenInlineQuery().getInlineMessageId();
//            String query = upd.getChosenInlineQuery().getQuery();
//            User from = upd.getChosenInlineQuery().getFrom();
//
//            botService.handleInlineAnswerPurchase( inlineMsgId, query, from );
//        };
//        return Reply.of(action, Flag.CHOSEN_INLINE_QUERY);
//    }
//
//    public Reply handlePurchaseJoinUser() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String callId = callbackQuery.getId();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//            User user = callbackQuery.getFrom();
//
//            try {
//                botService.purchaseAddUser( inlineMsgId, user );
//                responseHandler.alertPurchaseJoinUser(callId);
//            } catch (MemberAlreadyExistException e) {
//                responseHandler.alertPurchaseExistUser(callId);
//            }
//
//        };
//        return Reply.of(action, Predicates.isPurchaseJoinCallback());
//    }
//
//
//    public Reply handlePurchaseJoinOutUser() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String callId = callbackQuery.getId();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//            User user = callbackQuery.getFrom();
//
//            Purchase pr = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList currentMembers = botService.getPurchaseInvokeMember(pr);
//
//            if (currentMembers.getInvolvedCount() == 1) {
//                responseHandler.alertLastPurchaseMember(callId);
//            } else {
//                // TODO if user not contains  remove
//            }
//        };
//        return Reply.of(action, Predicates.isPurchaseJoinOutCallback());
//    }
//
//
//    public Reply handleEditPurchase() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase purchase = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList prMembers = botService.getPurchaseInvokeMember(purchase);
//
//            responseHandler.handleEditPurchase(inlineMsgId, purchase, prMembers);
//        };
//        return Reply.of(action, Predicates.isEditPurchaseCallback());
//    }
//
//
//    public Reply handleShowPurchaseMembers() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase purchase = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList prMembers = botService.getPurchaseInvokeMember(purchase);
//            responseHandler.showPurchaseMembers(inlineMsgId, purchase, prMembers);
//        };
//        return Reply.of(action, Predicates.isPurchaseMembersCallback());
//    }
//
//    public Reply handleBackCallback() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase purchase = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList prMembers = botService.getPurchaseInvokeMember(purchase);
//
//            responseHandler.showPurchase(inlineMsgId, purchase, prMembers);
//        };
//        return Reply.of(action, Predicates.isBackCallback());
//    }
//
//
//    public Reply handleRemoveMemberFromPurchase() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String callId = callbackQuery.getId();
//            String query = upd.getCallbackQuery().getData();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase pr = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList currentMembers = botService.getPurchaseInvokeMember(pr);
//
//            if (currentMembers.getInvolvedCount() == 1) {
//                responseHandler.alertLastPurchaseMember(callId);
//            } else {
//                botService.removeFromPurchase(query);
//                responseHandler.showPurchaseMembers(inlineMsgId, pr, botService.getPurchaseInvokeMember(pr));
//            }
//        };
//        return Reply.of(action, Predicates.isMemberRemoveFromPurchaseCallback());
//    }
//
//    public Reply handleAddMemberToPurchase() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String query = upd.getCallbackQuery().getData();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            botService.addToPurchase(query);
//
//            Purchase purchase = botService.getPurchase( inlineMsgId );
//            PurchaseMemberList prMembers = botService.getPurchaseInvokeMember(purchase);
//
//            responseHandler.showPurchaseMembers(inlineMsgId, purchase, prMembers);
//        };
//        return Reply.of(action, Predicates.isMemberAddToPurchaseCallback());
//    }
//
//    public Reply handleRemovePurchase() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase purchase = botService.deactivePurchase(inlineMsgId);
//            responseHandler.handleRemovePurchase(inlineMsgId, purchase);
//        };
//        return Reply.of(action, Predicates.isPurchaseRemoveCallback());
//    }
//
//    public Reply handleRestorePurchase() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Purchase purchase = botService.activePurchase(inlineMsgId);
//            PurchaseMemberList prMembers = botService.getPurchaseInvokeMember(purchase);
//
//            responseHandler.showPurchase(inlineMsgId, purchase, prMembers);
//        };
//        return Reply.of(action, Predicates.isPurchaseRestoreCallback());
//    }
//
//    public Reply handleRemittanceSetExecutor() {
//        Consumer<Update> action = upd -> {
//            System.out.println(" -- handleRemittanceSetExecutor -- ");
//            System.out.println("upd = " + upd);
//
//
//        };
//        return Reply.of(action, Predicates.isRemittanceSetExecutorCallback());
//    }



}
