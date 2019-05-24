package ru.ange.jointbuy.bot.response;

import com.vdurmont.emoji.EmojiParser;

import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.ange.jointbuy.pojo.*;
import ru.ange.jointbuy.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


//TODO remove!
public class ResponseHandler {

    private static final SimpleDateFormat DF = new SimpleDateFormat( "yyyy_MM_dd_mm_HH_ss" );

    private final MessageSender sender;

    public ResponseHandler(MessageSender sender) {
        this.sender = sender;
    }


//    public void sendHelloMsg(long chatId, List<Member> members) {
//
//        String usersText = new String();
//        for (int i = 0; i < members.size(); i++) {
//            Member user = members.get( i );
//            String fn = user.getFirstName() != null ? user.getFirstName() : "";
//            String ln = user.getFirstName() != null ? user.getLastName() : "";
//            usersText += String.format( Constants.START_CHAT_MSG_USER_PTT, fn, ln );
//        }
//
//        List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
//                createInlineKeyboardBtt( Constants.JOIN_USER_BTT_TEXT, Constants.JOIN_USER_BTT_CALLBACK )
//        );
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
//                .setKeyboard( keyboard );
//
//        SendMessage replies = new SendMessage()
//                .setText( EmojiParser.parseToUnicode(String.format( Constants.START_CHAT_MSG_TEXT, usersText )) )
//                .setReplyMarkup( markupInline )
//                .setChatId( chatId );getRemittanceMsgData
//
//        this.send(replies);
//    }

    public void alertUserAdded(String callId) {
        alert(callId, Constants.JOIN_USER_BTT_ANSWER_SUCCESS);
    }

    public void alertUserAlreadyExist(String callId) {
        alert(callId, Constants.JOIN_USER_BTT_ANSWER_EXISTS);
    }


    public void answerInlineQuery(String query, String id, User user) {
        try {
            String str = query.trim();
            int idx = str.indexOf(" ") > 0 ? str.indexOf(" ") : str.length();
            String digit = query.substring( 0, idx ).replace( ",", "." );
            double sum = Double.valueOf( digit );
            String name = str.indexOf(" ") > 0 ? str.substring( str.indexOf(" ")+1, query.length() ) : null;

            Operation operation = new Operation()
                    .setName( name )
                    .setSum( sum );

            AnswerInlineQuery aiq = new AnswerInlineQuery()
                    .setInlineQueryId( id )
                    .setResults( getInlineResult( operation, user ) );

            sender.execute( aiq );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
            // do nothing
        }
    }

//    private String getPurchaseInlineMsgText(String name, double sum, String fName, String lName, String usersCount) {
//        String hashtag = (name != null ? name : "").replace( " ", "_" ) + DF.format( new Date() );
//        String userName = fName + " " + lName;
//        String msgText = String.format( Constants.INLINE_BUY_MSG_TEXT_PTT, hashtag, name, sum, usersCount, userName);
//        return EmojiParser.parseToUnicode( msgText );
//    }


    private String getRemittanceHashtag(String name, Date date) {
        return "#remittance_" + (name != null ? name : "").replace( " ", "_" ) + DF.format( date );
    }


    public String getRemittanceMsgData(double sum, String name, String userName, Date date) {
        String hashTag = getRemittanceHashtag(name, date);
        String remMsgTextData = ( name != null )  ?
                String.format(Constants.NAMED_REMITTANCE_MSG, hashTag, sum, name, userName) :
                String.format(Constants.UNNAMED_REMITTANCE_MSG, hashTag, sum, userName);
        return remMsgTextData;
    }

    private List<InlineQueryResult> getInlineResult(Operation op, User user) {
        String name = op.getName();
        double sum = op.getSum();
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();

        if (sum > 0) {

            String userName = user.getFirstName() + " " + user.getLastName();
            String remMsgTextData = getRemittanceMsgData( sum, name, userName, new Date() );
            String remMsgText = remMsgTextData + Constants.REMITTANCE_MSG_LOADING_LINE;

            InputTextMessageContent remitMsgCont = new InputTextMessageContent()
                    //.enableMarkdown( true ) // not work with _
                    .setMessageText( EmojiParser.parseToUnicode( remMsgText ) );

            String desc = (name != null) ? String.format( Constants.INLINE_REMIT_DESC_TEXT_PTT, sum, name)
                    : String.format( Constants.INLINE_REMIT_TEXT_PTT, sum );

            InlineQueryResultArticle remittance = new InlineQueryResultArticle()
                    .setInputMessageContent(remitMsgCont)
                    .setId( String.valueOf( Operation.Type.REMITTANCE ) )
                    .setDescription( desc )
                    .setTitle( Constants.REMITTANCE_INLINE_BTT_TEXT )
                    .setThumbUrl( Constants.REMITTANCE_INLINE_BTT_IMG_URL );

            results.add(remittance);
        }

//        if (sum > 0 && name != null && !name.isEmpty()) {
//            String msgText = getPurchaseInlineMsgText( name, sum, user.getFirstName(),
//                    user.getLastName(), Constants.INLINE_BUY_MSG_TEXT_ALL );
//
//            InputTextMessageContent purchMsgCont = new InputTextMessageContent()
//                    .enableMarkdown( true )
//                    .setMessageText( msgText );
//
//            InlineQueryResultArticle buy = new InlineQueryResultArticle()
//                    .setInputMessageContent( purchMsgCont )
//                    .setId( String.valueOf( Operation.Type.PURCHASE ) )
//                    .setDescription( String.format( Constants.INLINE_PURCH_TEXT_PTT, sum, name) ) // TODO add " " in names
//                    .setTitle( Constants.BUY_IMG_BTT_TEXT )
//                    .setThumbUrl( Constants.BUY_IMG_BTT_URL )
//                    .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );
//
//            results.add( buy );
//        }
        return results;
    }

//    private InlineKeyboardMarkup getPurchaseInlineKeyboardMarkup() {
//        List<InlineKeyboardButton> joinLine = new ArrayList<>();
//        joinLine.add(createInlineKeyboardBtt(
//                Constants.PURCHASE_JOIN_BTT_TEXT,
//                Constants.PURCHASE_JOIN_CALLBACK )
//        );
//        joinLine.add(createInlineKeyboardBtt(
//                Constants.PURCHASE_JOIN_OUT_BTT_TEXT,
//                Constants.PURCHASE_JOIN_OUT_CALLBACK )
//        );
//
//        List<InlineKeyboardButton> editLine = new ArrayList<>();
//        editLine.add(createInlineKeyboardBtt( Constants.PURCHASE_EDIT_BTT_TEXT, Constants.PURCHASE_EDIT_BTT_CALLBACK ));
//
//        List<InlineKeyboardButton> delLine = new ArrayList<>();
//        delLine.add(createInlineKeyboardBtt( Constants.DELETE_PURCHASE_BTT_TEXT, Constants.DELETE_PURCHASE_CALLBACK ));
//
//        List<List<InlineKeyboardButton>> lines = new ArrayList<>();
//        lines.add( joinLine );
//        lines.add( editLine );
//        lines.add( delLine );
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
//                .setKeyboard( lines );
//
//        return markupInline;
//    }


    private InlineKeyboardMarkup getRemittanceKeyboardMarkup(Remittance remittance) {

        String editCallBack = String.format( Constants.REMITTANCE_EDIT_BTT_CALLBACK, remittance.getID() );
        List<InlineKeyboardButton> editLine = new ArrayList<>();
        editLine.add( createInlineKeyboardBtt( Constants.EDIT_BTT_LABEL, editCallBack ) );

        String deleteCallBack = String.format( Constants.REMITTANCE_DELETE_CALLBACK, remittance.getID() );
        List<InlineKeyboardButton> delLine = new ArrayList<>();
        delLine.add( createInlineKeyboardBtt( Constants.DELETE_BTT_LABEL, deleteCallBack ) );

        List<List<InlineKeyboardButton>> lines = new ArrayList<>();
        lines.add( editLine );
        lines.add( delLine );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup().setKeyboard( lines );

        return markupInline;
    }


//    public void handleAddRemittanceMsg(Message replies, Remittance rem, List<Member> members) {
//        members.remove( rem.getSender() );
//        List<InlineKeyboardButton> btts = new ArrayList<InlineKeyboardButton>();
//
//        for (Member member : members) {
//            int remID = rem.getID();
//            int memID = member.getId();
//            String memName = member.getFullName();
//            String bttText = String.format( Constants.REMITTANCE_RECIPIENT_BTT_PTT, memName );
//            String bttCallback = String.format( Constants.REMITTANCE_RECIPIENT_BTT_CALLBACK, remID, memID );
//            btts.add( createInlineKeyboardBtt( bttText, bttCallback ) );
//        }
//
//        List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard( btts );
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
//                .setKeyboard( keyboard );
//
//        String remMsgTextData = getRemittanceMsgData( rem.getAmount(), rem.getName(), rem.getSender().getFullName(),
//                rem.getDate() );
//        String remMsgText = members.size() > 0 ?
//                remMsgTextData + Constants.REMITTANCE_MSG_CHOOSE_MEMBERS_LINE :
//                remMsgTextData + Constants.REMITTANCE_MSG_NOT_MEMBERS_LINE;
//
//        EditMessageText emt = new EditMessageText()
//                .setChatId( rem.getTelegramChatId() )
//                .setMessageId( replies.getMessageId() )
//                //.enableMarkdown( true )
//                .setText( EmojiParser.parseToUnicode( remMsgText ) )
//                .setReplyMarkup( markupInline );
//
//        this.send( emt );
//    }
//
//    public void handleUpdateRemittanceMsg(Remittance rem) {
//
//        String remMsgTextData = getRemittanceMsgData( rem.getAmount(), rem.getName(), rem.getSender().getFullName(),
//                rem.getDate() );
//        String remMsgPtt = remMsgTextData + Constants.REMITTANCE_MSG_RECIP_LINE;
//        String remMsgText = String.format( remMsgPtt, rem.getRecipient().getFullName() );
//
//        EditMessageText emt = new EditMessageText()
//                .setInlineMessageId( rem.getTelInlineMsgID() )
//                //.enableMarkdown( true )
//                .setReplyMarkup( getRemittanceKeyboardMarkup( rem ) )
//                .setText( EmojiParser.parseToUnicode( remMsgText ) );
//
//        this.send( emt );
//    }
//







    /*
    private InlineKeyboardMarkup getRemittanceInlineKeyboardMarkup() {

        List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
                createInlineKeyboardBtt( "️:mailbox_with_mail: Назначить получателя", Constants.BACK_BTT_CALLBACK )
        );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard( keyboard );

        return markupInline;
    }


    public void answerInlineQuery(String query, String id, User user) {
        try {
            String str = query.trim();
            int idx = str.indexOf(" ") > 0 ? str.indexOf(" ") : str.length();
            String digit = query.substring( 0, idx ).replace( ",", "." );
            double sum = Double.valueOf( digit );
            String name = str.indexOf(" ") > 0 ? str.substring( str.indexOf(" ")+1, query.length() ) : null;

            Operation operation = new Operation()
                    .setName( name )
                    .setSum( sum );

            AnswerInlineQuery aiq = new AnswerInlineQuery()
                    .setInlineQueryId( id )
                    .setResults( getInlineResult( operation, user ) );

            sender.execute( aiq );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {}
    }

    public void editAnswerInlineQuery(long chatId, int msgId, String name, double sum, User user) {
        try {
            String text = getPurchaseInlineMsgText( name, sum, user.getFirstName(), user.getLastName(), "" );
            EditMessageText emt = new EditMessageText()
                    .enableMarkdown( true )
                    .setChatId( chatId )
                    .setMessageId( msgId )
                    .setText( EmojiParser.parseToUnicode( text ) )
                    .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );

            sender.execute( emt );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
        }
    }


    public SendMessage getAddUserMsg(long chatId) {
        AddUserMsg replies = new AddUserMsg();
        SendMessage sm = new SendMessage()
                .setText( replies.getText() )
                .setChatId( chatId );
        return sm;
    }

    public void sendMsg(long chatId, String replies, boolean markdown) {
        try {
            SendMessage sm = new SendMessage()
                    .enableMarkdown( markdown )
                    .setText( replies )
                    .setChatId( chatId );
            sender.execute( getAddUserMsg( chatId ) );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void showPurchaseMembers(String inlineMsgId, Purchase purchase, PurchaseMemberList members) {
        try {
            List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
                createPurchaseMemberKeyboardButtons( purchase, members )
            );

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard( keyboard );

            Member purchaser = purchase.getPurchaser();
            String name = purchase.getName();
            double sum = purchase.getSum();
            int all = members.getAllCount();
            int involved = members.getInvolvedCount();

            String text = getPurchaseInlineMsgText( name, sum, purchaser.getFirstName(), purchaser.getLastName(),
                    getMembersCountText( all, involved ) );

            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .enableMarkdown( true )
                    .setText( text )
                    .setReplyMarkup( markupInline );

            sender.execute( emt );

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void showPurchase(String inlineMsgId, Purchase purchase, PurchaseMemberList members) {
        try {
            String name = purchase.getName();
            double summ = purchase.getSum();
            String fName = purchase.getPurchaser().getFirstName();
            String lName = purchase.getPurchaser().getLastName();
            int all = members.getAllCount();
            int involved = members.getInvolvedCount();

            String text = getPurchaseInlineMsgText( name, summ, fName, lName, getMembersCountText(all, involved));

            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .enableMarkdown( true )
                    .setText( text )
                    .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );

            sender.execute( emt );

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private InlineKeyboardButton createInlineKeyboardBtt(String text, String callback) {
        return new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode( text ) )
                .setCallbackData( callback );
    }


    private List<List<InlineKeyboardButton>> createInlineRowsKeyboard(InlineKeyboardButton... buttons) {
        return createInlineRowsKeyboard( Arrays.asList( buttons ) );
    }

    private List<List<InlineKeyboardButton>> createInlineRowsKeyboard(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        for (InlineKeyboardButton btt : buttons) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add( btt );
            result.add( rowInline );
        }
        return result;
    }

    private List<InlineKeyboardButton> createPurchaseMemberKeyboardButtons(Purchase pr, List<PurchaseMember> mems) {
        List<InlineKeyboardButton> result = new ArrayList<InlineKeyboardButton>();
        for (PurchaseMember prMember : mems) {
            boolean involved = prMember.isInvolved();
            String textPtt = involved ? Constants.IMPROVED_MEMBER_PTT : Constants.NOT_IMPROVED_MEMBER_PTT;
            String callBackPtt = involved ? Constants.REMOVE_FROM_PURCHASE_CALLBACK_PTT
                    : Constants.ADD_TO_PURCHASE_CALLBACK_PTT;

            String bttText = String.format( textPtt, prMember.getFullName() );
            String bttCallback = String.format( callBackPtt, prMember.getId(), pr.getID() );
            result.add( createInlineKeyboardBtt( bttText, bttCallback ) );
        }
        result.add( createInlineKeyboardBtt( Constants.BACK_BTT_TEXT, Constants.PURCHASE_EDIT_BTT_CALLBACK ) );
        return result;
    }


    private List<InlineKeyboardButton> createExecutorKeyboardButtons(List<Member> members) {
        List<InlineKeyboardButton> result = new ArrayList<InlineKeyboardButton>();
        for (Member member : members) {
            String bttText = String.format( Constants.REMITTANCE_RECIPIENT_BTT_PTT, member.getFullName() );
            String bttCallback = String.format( Constants.REMITTANCE_RECIPIENT_BTT_CALLBACK, member.getId());
            result.add( createInlineKeyboardBtt( bttText, bttCallback ) );
        }
        return result;
    }


    private String getMembersCountText(int allCount, int invokeCount) {
        if (invokeCount == allCount) {
            return Constants.INLINE_BUY_MSG_TEXT_ALL;
        } else {
            return String.format( Constants.INLINE_BUY_MSG_TEXT_COUNT, invokeCount, allCount );
        }
    }

    public void alert(String callId, String replies) {
        try {
            AnswerCallbackQuery acq = new AnswerCallbackQuery()
                    .setCallbackQueryId( callId )
                    .setText( replies );

            sender.execute(acq);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void alertLastPurchaseMember(String callId) {
        alert(callId, Constants.AT_LEAST_ONE_MEMBER_ALERT);
    }


    public void handleRemovePurchase(String inlineMsgId, Purchase purchase) {
        try {
            List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
                    createInlineKeyboardBtt( Constants.RESTORE_PURCHASE_BTT_TEXT, Constants.RESTORE_PURCHASE_CALLBACK )
            );

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard( keyboard );

            String hashtag = purchase.getName().replace( " ", "_" );
            String text = String.format( Constants.DELETED_PURCHASE_MSG, hashtag );

            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .enableMarkdown( true )
                    .setText( text )
                    .setReplyMarkup( markupInline );

            sender.execute( emt );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void alertPurchaseJoinUser(String callId) {
        alert( callId, Constants.PURCHASE_ADD_USER_CALLBACK_ALERT );
    }

    public void alertPurchaseExistUser(String callId) {
        alert( callId, Constants.PURCHASE_EXIST_USER_CALLBACK_ALERT );
    }

    public void handleEditPurchase(String inlineMsgId, Purchase purchase, PurchaseMemberList members) {
        try {
            List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
                    createInlineKeyboardBtt( "\uD83C\uDFF7️ Наименование", Constants.BACK_BTT_CALLBACK ),
                    createInlineKeyboardBtt( ":euro: Сумма", Constants.BACK_BTT_CALLBACK ),
                    createInlineKeyboardBtt( ":bust_in_silhouette: Исполнитель", Constants.BACK_BTT_CALLBACK ),
                    createInlineKeyboardBtt( Constants.PURCHASE_MEMBERS_BTT_TEXT, Constants.PURCHASE_MEMBERS_BTT_CALLBACK ),
                    createInlineKeyboardBtt( Constants.BACK_BTT_TEXT, Constants.BACK_BTT_CALLBACK )
            );

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard( keyboard );

            String name = purchase.getName();
            double summ = purchase.getSum();
            String fName = purchase.getPurchaser().getFirstName();
            String lName = purchase.getPurchaser().getLastName();
            int all = members.getAllCount();
            int involved = members.getInvolvedCount();

            String text = getPurchaseInlineMsgText( name, summ, fName, lName, getMembersCountText(all, involved));


            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .enableMarkdown( true )
                    .setText( text )
                    .setReplyMarkup( markupInline );

            sender.execute( emt );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerListCommand(long chatId, List<Purchase> purchases) {
        try {

            String text = new String();
            if (purchases.size() == 0) {
                text = Constants.LIST_PURCHASES_MSG_EMPTY;
            } else {
                String purchasesList = new String();
                double total = 0;

//                int maxDigitCount = 0;
//                for (Purchase purchase : purchases) {
//                    int length = String.valueOf(purchase.getSum()).length();
//                    maxDigitCount = maxDigitCount < length ? length : maxDigitCount;
//                }

                for (Purchase purchase : purchases) {
                    String name = purchase.getName();
                    double amount = purchase.getSum();
                    total += amount;
                    // TODO добавлять пробелво перед цифрами для ровного оображения
//                    int length = String.valueOf(amount).length();
//                    String amountStr = new String();
//
//                    while (maxDigitCount - length >= 0) {
//                        System.out.println("amount = " + amount+ "; name = " + name + "; length = " + length);
//                        amountStr += "  ";
//                        length ++;
//                    }
//                    amountStr += String.valueOf(amount);
//
                    String line = String.format( Constants.LIST_PURCHASE_LINE_PTT, name, amount, name);
                    purchasesList += line;
                }
                text = String.format( Constants.LIST_PURCHASES_MSG_PTT, purchasesList, purchases.size(), total);
            }

            SendMessage replies = new SendMessage()
                    .setText( EmojiParser.parseToUnicode(text) )
                    .enableMarkdown( true )
                    .setChatId( chatId );

            sender.execute( replies );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerSplitCommand(long chatId, List<Member> members, List<Purchase> purchases) {
        try {

            String text = new String();
            for (Member member : members) {
                String debtorsStr = new String();
                double spelt = 0;

                for (Purchase purchase : purchases) {
                    if (purchase.getPurchaser().equals( member ))
                        spelt += purchase.getSum();
                }

                for (Member debtor : members) {
                    double debt = 0.0;
                    if (!member.equals( debtor )) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getPurchaser().equals( member ) && purchase.getMembers().size() != 0)
                                debt += purchase.getSum() / purchase.getMembers().size();
                        }
                        if (debt > 0)
                            debtorsStr += String.format( Constants.SPLIT_DEBTOR_PTT, debtor.getFullName(), debt );
                    }
                }

                if (spelt > 0) {
                    String name = member.getFirstName();
                    if (debtorsStr.isEmpty()) {
                        String should = Constants.SPLIT_MEMBER_SHOULD_NONE;
                        text += String.format( Constants.SPLIT_MEMBER_PTT, name, spelt, should );
                    } else {
                        String should = String.format( Constants.SPLIT_MEMBER_SHOULD, debtorsStr );
                        text += String.format( Constants.SPLIT_MEMBER_PTT, name, spelt, should);
                    }
                }
            }
            String res = text.isEmpty() ? Constants.SPLIT_DEBTORS_NONE : text.replaceFirst("\n", "" );

            SendMessage replies = new SendMessage()
                    .setText( EmojiParser.parseToUnicode(res) )
                    .enableMarkdown( true )
                    .setChatId( chatId );

            sender.execute( replies );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




    */

    public void alert(String callId, String msg) {
        try {
            AnswerCallbackQuery acq = new AnswerCallbackQuery()
                    .setCallbackQueryId( callId )
                    .setText( msg );

            sender.execute(acq);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    public send(BotApiMethod replies) {
//        try {
//            sender.execute( replies );
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }


    public static List<List<InlineKeyboardButton>> createInlineRowsKeyboard(InlineKeyboardButton... buttons) {
        return createInlineRowsKeyboard( Arrays.asList( buttons ) );
    }

    public static List<List<InlineKeyboardButton>> createInlineRowsKeyboard(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        for (InlineKeyboardButton btt : buttons) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add( btt );
            result.add( rowInline );
        }
        return result;
    }

    public static InlineKeyboardButton createInlineKeyboardBtt(String text, String callback) {
        return new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode( text ) )
                .setCallbackData( callback );
    }

    public void handleRemoveRemittance(String inlineMsgId, Remittance remittance) {
        try {
            String restoreCallback = String.format(Constants.REMITTANCE_RESTORE_CALLBACK, remittance.getID());
            List<List<InlineKeyboardButton>> keyboard = createInlineRowsKeyboard(
                    createInlineKeyboardBtt( Constants.RESTORE_BTT_LABEL, restoreCallback )
            );

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard( keyboard );

            String hashtag = getRemittanceHashtag( remittance.getName(), remittance.getDate() );
            String text = String.format( Constants.REMITTANCE_DELETED_MSG, hashtag );

            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .setText( text )
                    .setReplyMarkup( markupInline );
                    //.enableMarkdown( true );

            sender.execute( emt );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleRestoreRemittance(String inlineMsgId, Remittance rem) {
        try {
            String remMsgTextData = getRemittanceMsgData( rem.getAmount(), rem.getName(), rem.getSender().getFullName(),
                    rem.getDate() );
            String remMsgPtt = remMsgTextData + Constants.REMITTANCE_MSG_RECIP_LINE;
            String remMsgText = String.format( remMsgPtt, rem.getRecipient().getFullName() );

            EditMessageText emt = new EditMessageText()
                    .setInlineMessageId( inlineMsgId )
                    .setText( EmojiParser.parseToUnicode( remMsgText ) )
                    .setReplyMarkup( getRemittanceKeyboardMarkup( rem ) );

            sender.execute( emt );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
