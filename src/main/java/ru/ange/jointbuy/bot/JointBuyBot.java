package ru.ange.jointbuy.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.response.replies.AnswerHelloMsg;
import ru.ange.jointbuy.bot.response.replies.HelloMsg;
import ru.ange.jointbuy.bot.response.replies.InlineQueryAnswer;
import ru.ange.jointbuy.bot.response.Predicates;
import ru.ange.jointbuy.bot.response.replies.remittance.RemittanceInlineAnswer;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;


public class JointBuyBot extends AbilityBot {

    private JointBuyBotService botService;

    public JointBuyBot(String botToken, String botName, DefaultBotOptions botOptions ) {
        super( botToken, botName, botOptions );
        this.initComponent();
    }

    public JointBuyBot(String botToken, String botName ) {
        super( botToken, botName );
        this.initComponent();
    }

    private void initComponent() {
        this.botService = JointBuyBotService.getInstance();
    }


    @Override
    public int creatorId() {
        return 0;
    }


    private void send(BotApiMethod bam) {
        try {
            sender.execute( bam );
        } catch (TelegramApiException e) {
            // TODO add logger
            e.printStackTrace();
        }
    }



    // --------------------------------
    // ------ Start chat actions ------
    // --------------------------------

    public Reply sayHello() {
        Consumer<Update> action = upd -> {
            HelloMsg msg = botService.getHelloMsg( upd, getChatId( upd ), users().values() );
            send( msg );
        };
        try {
            return Reply.of( action, Predicates.isAddMyself( getMe() ) );
        } catch (TelegramApiException e) {
            return null;
        }
    }

    public Reply replyJoinBtt() {
        Consumer<Update> action = upd -> {
            AnswerHelloMsg answer = botService.getAnswerHelloMsg( upd, getChatId( upd ) );
            send( answer );
        };
        return Reply.of( action, Flag.CALLBACK_QUERY.and( Predicates.isCallbackMatches(
                HelloMsg.getJoinUserBttCallback() ) ) );
    }



    // ----------------------------
    // ------ Inline actions ------
    // ----------------------------

    public Reply handleInlineQuery() {
        Consumer<Update> action = upd -> {
            InlineQueryAnswer answer = botService.getInlineQueryAnswer( upd );
            send( answer );
        };
        return Reply.of( action, Predicates.isInlineQuery() );
    }



    public Reply handleRemittanceInlineAnswerMsg() {
        Consumer<Update> action = upd -> {
            RemittanceInlineAnswer answer = botService.getRemittanceInlineAnswer(upd, getChatId( upd ));
            send( answer );

//            RemittanceInlineAnswer answer
//            long chatId = getChatId( upd );
//            Message replies = upd.getMessage();
//            boolean isNamedRemittance = Predicates.isNamedRemittanceMsg( replies.getText() );
//            Remittance remittance = botService.addRemittance( chatId, replies, isNamedRemittance );
//            List<Member> members = botService.getMembers( chatId );
//            responseHandler.handleAddRemittanceMsg( replies, remittance, members );
        };
        return Reply.of( action, Predicates.isInlineRemittanceAnswer() );
    }





//    // ---------------------
//    // ------ Actions ------
//    // ---------------------
//
//public Reply sayHello() {
//        Consumer<Update> action = upd -> {
//
//            HelloMsg replies = botService.getHelloMsg(Update upd);
//            sender.execute( replies );
//
//            long chatId = getChatId( upd );
//            List<Member> members = botService.getMembers( new ArrayList<User>(users().values()), chatId );
//            responseHandler.sendHelloMsg( chatId, members );
//        };
//        try {
//            User me = getMe();
//            return Reply.of(action, Predicates.isAddMyself(me));
//        } catch (TelegramApiException e) {
//            return null;
//        }

//    public Reply sayHello() {
//        try {
//
//            SayHello sh = new SayHello( users().values() );
//
//            Predicate ianm = Predicates.isAddNewMember();
//            Predicate iam = Predicates.isAddMyself( getMe() );
//
//            return Reply.of( sh, ianm.and( iam ) );
//
//        } catch (TelegramApiException e) {
//            return null;
//        }
//    }




    // ----------------------
    // ------ Commands ------
    // ----------------------

//    public Ability addUsersCommand() {
//
//        //AddUserMsg addUserMsg = new AddUserMsg();
//
//        Ability addUserCommand = Ability.builder()
//                .name( Constants.ADD_USER_COMMAND_NAME )
//                .info( Constants.ADD_USER_COMMAND_DESCRIPTION )
//                .locality(ALL)
//                .privacy(PUBLIC)
//                .action(ctx -> {
//                    silent.forceReply( addUserMsg.getText(), ctx.chatId() );
//                })
//                .reply(upd -> {
//                            botService.handleAddUserCommand(upd, getChatId( upd ));
//                        }, Flag.MESSAGE, Flag.REPLY, Predicates.isReplyToBot(getBotUsername()),
//                        Predicates.isReplyToMessage(addUserMsg.getText()))
//                .build();
//
//        return addUserCommand;
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


//    public Reply handleQuery() {
//        Consumer<Update> action = upd -> {
//            InlineQuery inlineQuery = upd.getInlineQuery();
//            User user = inlineQuery.getFrom();
//            responseHandler.answerInlineQuery( inlineQuery.getQuery(), inlineQuery.getId(), user );
//        };
//        return Reply.of( action, Predicates.isInlineQuery() );
//    }
//
//
//    public Reply handleInlineQuery() {
//        Consumer<Update> action = upd -> {
//            InlineQuery inlineQuery = upd.getInlineQuery();
//            User user = inlineQuery.getFrom();
//            responseHandler.answerInlineQuery( inlineQuery.getQuery(), inlineQuery.getId(), user );
//        };
//        return Reply.of( action, Predicates.isInlineQuery() );
//    }


    // ------------------------
    // ------ Remittance ------
    // ------------------------

//    public Reply handleRemittanceInlineAnswerMsg() {
//        Consumer<Update> action = upd -> {
//            long chatId = getChatId( upd );
//            Message replies = upd.getMessage();
//            boolean isNamedRemittance = Predicates.isNamedRemittanceMsg( replies.getText() );
//            Remittance remittance = botService.addRemittance( chatId, replies, isNamedRemittance );
//            List<Member> members = botService.getMembers( chatId );
//            responseHandler.handleAddRemittanceMsg( replies, remittance, members );
//        };
//        return Reply.of( action, Predicates.isInlineRemittanceAnswer() );
//    }
//
//    public Reply handleRemittanceSetRecipient() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery cbQuery = upd.getCallbackQuery();
//            String data = cbQuery.getData();
//            String inlineMessageId = cbQuery.getInlineMessageId();
//            User user = cbQuery.getFrom(); // TODO if sender != user alert error
//
//            Remittance remittance = botService.updateRemittance( data, inlineMessageId );
//            responseHandler.handleUpdateRemittanceMsg( remittance );
//        };
//        return Reply.of( action, Predicates.isRemittanceSetRecipientCallback() );
//    }
//
//    public Reply handleEditRemittance() {
//        Consumer<Update> action = upd -> {
//            String query = upd.getCallbackQuery().getData();
//            //Remittance remittance = botService.getRemittance( query );
//            //responseHandler.handleEditRemittanceBtt( remittance );
//        };
//        return Reply.of( action, Predicates.isEditRemittanceCallback() );
//    }
//
//    public Reply handleDeleteRemittance() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String query = callbackQuery.getData();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Remittance deactiveRemittance = botService.deactiveRemittance( query );
//            responseHandler.handleRemoveRemittance(inlineMsgId, deactiveRemittance);
//        };
//        return Reply.of( action, Predicates.isDeleteRemittanceCallback() );
//    }
//
//    public Reply handleRestoreRemittance() {
//        Consumer<Update> action = upd -> {
//            CallbackQuery callbackQuery = upd.getCallbackQuery();
//            String query = callbackQuery.getData();
//            String inlineMsgId = callbackQuery.getInlineMessageId();
//
//            Remittance restoredRemittance = botService.restoreRemittance( query );
//            responseHandler.handleRestoreRemittance(inlineMsgId, restoredRemittance);
//        };
//        return Reply.of( action, Predicates.isDeleteRemittanceCallback() );
//    }


//    public Reply handleInlineAnswerPurchaseMsg() {
//        Consumer<Update> action = upd -> {
//            long chatId = getChatId( upd );
//            Message replies = upd.getMessage();
//            String text = replies.getText();
//            User from = replies.getFrom();
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
