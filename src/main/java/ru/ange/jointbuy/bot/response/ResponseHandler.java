package ru.ange.jointbuy.bot.response;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.jointbuy.bot.msg.AddUserMsg;
import ru.ange.jointbuy.bot.msg.HelloMsg;
import ru.ange.jointbuy.pojo.*;
import ru.ange.jointbuy.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseHandler {

    private final MessageSender sender;

    public ResponseHandler(MessageSender sender) {
        this.sender = sender;
    }

    private SendMessage getHelloSendMessage(long chatId, List<Member> members) {
        HelloMsg helloMsg = new HelloMsg( members );
        SendMessage msg = new SendMessage()
                .setText( helloMsg.getText() )
                .setReplyMarkup( helloMsg.getMarkup() )
                .setChatId( chatId );
        return msg;
    }

    public void sendHelloMsg(long chatId, List<Member> members) {
        try {
            sender.execute( getHelloSendMessage( chatId, members ) );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void alertUserAdded(String callId) {
        alert(callId, Constants.ADD_USER_CALLBACK_ALERT);
    }



    public void alertUserAlreadyExist(String callId) {
        alert(callId, Constants.ADD_USER_CALLBACK_ALERT);
    }


    private InlineKeyboardMarkup getPurchaseInlineKeyboardMarkup() {

        List<InlineKeyboardButton> joinLine = new ArrayList<>();
        joinLine.add(createInlineKeyboardBtt(
                Constants.PURCHASE_JOIN_BTT_TEXT,
                Constants.PURCHASE_JOIN_CALLBACK )
        );
        joinLine.add(createInlineKeyboardBtt(
                Constants.PURCHASE_JOIN_OUT_BTT_TEXT,
                Constants.PURCHASE_JOIN_OUT_CALLBACK )
        );

        List<InlineKeyboardButton> editLine = new ArrayList<>();
        editLine.add(createInlineKeyboardBtt( Constants.PURCHASE_EDIT_BTT_TEXT, Constants.PURCHASE_EDIT_BTT_CALLBACK ));

        List<InlineKeyboardButton> delLine = new ArrayList<>();
        delLine.add(createInlineKeyboardBtt( Constants.DELETE_PURCHASE_BTT_TEXT, Constants.DELETE_PURCHASE_CALLBACK ));

        List<List<InlineKeyboardButton>> lines = new ArrayList<>();
        lines.add( joinLine );
        lines.add( editLine );
        lines.add( delLine );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard( lines );

        return markupInline;
    }

    private String getPurchaseInlineMsgText(String name, double sum, String fName, String lName, String usersCount) {
        String hashtag = name.replace( " ", "_" );
        String userName = fName + " " + lName;
        String msgText = String.format( Constants.INLINE_BUY_MSG_TEXT_PTT, hashtag, name, sum,
                usersCount, userName);

        return EmojiParser.parseToUnicode( msgText );
    }


    private List<InlineQueryResult> getInlineResult(Operation op, User user) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();
        String name = op.getName();
        double sum = op.getSum();
        String msgText = getPurchaseInlineMsgText( name, sum, user.getFirstName(),
                user.getLastName(), Constants.INLINE_BUY_MSG_TEXT_ALL );

        InputTextMessageContent msgCont = new InputTextMessageContent()
                .enableMarkdown( true )
                .setMessageText( msgText );

        InlineQueryResultArticle buy = new InlineQueryResultArticle()
                .setInputMessageContent( msgCont )
                .setId( String.valueOf( Operation.Type.PURCHASE ) )
                .setDescription( String.format( Constants.INLINE_TEXT_PTT, sum, name ) )
                .setTitle( Constants.BUY_IMG_BTT_TEXT )
                .setThumbUrl( Constants.BUY_IMG_BTT_URL )
                .setReplyMarkup( getPurchaseInlineKeyboardMarkup() );

        results.add( buy );

//        InlineQueryResultArticle transfer = new InlineQueryResultArticle()
//                .setInputMessageContent(getTextMessageCont(Constants.INLINE_MSG_TYPE_TRANSFER, name, sum, user, mc))
//                .setId(Integer.toString(2))
//                .setDescription(String.format( Constants.INLINE_TEXT_PTT, sum, name ))
//                .setTitle(Constants.REMITTANCE_IMG_BTT_TEXT)
//                .setThumbUrl(Constants.REMITTANCE_IMG_BTT_URL);
//        results.add(transfer);

        return results;
    }

    private Operation readOperation(String query) {
        String digit = query.substring( 0, query.indexOf( " " ) );
        Operation operation = new Operation()
                .setName( query.substring( query.indexOf( " " ) + 1, query.length() ) )
                .setSum( Double.valueOf( digit.replace( ",", "." ) ) );
        return operation;
    }


    public void answerInlineQuery(String query, String id, User user) {
        try {
            AnswerInlineQuery aiq = new AnswerInlineQuery()
                    .setInlineQueryId( id )
                    .setResults( getInlineResult( readOperation( query ), user ) );

            sender.execute( aiq );
        } catch (StringIndexOutOfBoundsException | NumberFormatException | TelegramApiException e) {
        }
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
        AddUserMsg msg = new AddUserMsg();
        SendMessage sm = new SendMessage()
                .setText( msg.getText() )
                .setChatId( chatId );
        return sm;
    }

    public void sendMsg(long chatId, String msg, boolean markdown) {
        try {
            SendMessage sm = new SendMessage()
                    .enableMarkdown( markdown )
                    .setText( msg )
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

    private String getMembersCountText(int allCount, int invokeCount) {
        if (invokeCount == allCount) {
            return Constants.INLINE_BUY_MSG_TEXT_ALL;
        } else {
            return String.format( Constants.INLINE_BUY_MSG_TEXT_COUNT, invokeCount, allCount );
        }
    }

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

            SendMessage msg = new SendMessage()
                    .setText( EmojiParser.parseToUnicode(text) )
                    .enableMarkdown( true )
                    .setChatId( chatId );

            sender.execute( msg );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
