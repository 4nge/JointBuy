package ru.ange.jointbuy.bot;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import ru.ange.jointbuy.bot.response.Predicates;
import ru.ange.jointbuy.bot.response.replies.*;
import ru.ange.jointbuy.bot.response.replies.remittance.RemittanceInlineAnswer;
import ru.ange.jointbuy.pojo.*;
import ru.ange.jointbuy.services.DbService;
import ru.ange.jointbuy.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JointBuyBotService {

    public DbService dbService;

    public DbService getDbService() {
        return dbService;
    }

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    private static JointBuyBotService instance;

    private JointBuyBotService() {
//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml" );
//        instance = ctx.getBean("botService", JointBuyBotService.class );
    }

    public static JointBuyBotService getInstance() {
        if ( instance == null ) {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml" );
            instance = ctx.getBean("botService", JointBuyBotService.class );
        }
        return instance;
    }



    public HelloMsg getHelloMsg(Update upd, long chatId, Collection<User> users) {

        List<User> newUsers = new ArrayList<User>(users);
        List<Member> members = dbService.getMembers( chatId );
        for (User newUser : newUsers) {
            Member newMember = new Member(
                    newUser.getId(),
                    chatId,
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    new String());

            if  (!members.contains( newMember )) {
                members.add( dbService.addMembers( newMember ) );
            }
        }
        return new HelloMsg( chatId, members );
    }

    public AnswerHelloMsg getAnswerHelloMsg(Update upd, long chatId) {

        String callId = upd.getCallbackQuery().getId();
        User sender = upd.getCallbackQuery().getFrom();

        List<Member> members = dbService.getMembers( chatId );
        Member newMember = new Member(
                sender.getId(),
                chatId,
                sender.getFirstName(),
                sender.getLastName(),
                new String());

        if ( members.contains( newMember ) ) {
            return new MemberAlreadyExistAnswer( callId );
        } else {
            dbService.addMembers( newMember );
            return new MemberAddedAnswer( callId );
        }
    }




    public InlineQueryAnswer getInlineQueryAnswer(Update upd) {

        InlineQuery inlineQuery = upd.getInlineQuery();
        User user = inlineQuery.getFrom();
        String query = inlineQuery.getQuery();

        String str = query.trim();
        int idx = str.indexOf(" ") > 0 ? str.indexOf(" ") : str.length();
        String digit = query.substring( 0, idx ).replace( ",", "." );
        double sum = Double.valueOf( digit );
        String name = str.indexOf(" ") > 0 ? str.substring( str.indexOf(" ")+1, query.length() ) : null;

        Operation operation = new Operation()
                .setName( name )
                .setSum( sum );

        return new InlineQueryAnswer(inlineQuery.getId(), operation, user);
    }

    public RemittanceInlineAnswer getRemittanceInlineAnswer(Update upd, long chatId) {
        Message replies = upd.getMessage();

        boolean isNamedRemittance = StringUtil.isMatches(replies.getText(),
                InlineQueryAnswer.getNamdePtt() + InlineQueryAnswer.getRemittanceMsgEnd() );

        Remittance remittance = addRemittance( chatId, replies, isNamedRemittance );
        List<Member> members = dbService.getMembers( chatId );

        return new RemittanceInlineAnswer(remittance, members);
    }


//        try {
//            botService.handleAddUserBtt( upd, chatId );
//            responseHandler.alertUserAdded( callId );
//        } catch (MemberAlreadyExistException e) {
//            responseHandler.alertUserAlreadyExist( callId );
//        }
//    }
//    public Member addMember(Member newMember, long chatId) {
//        List<Member> members = dbService.getMembers( chatId );
//        if  (!members.contains( newMember )) {
//            return dbService.addMembers( newMember );
//        } else {
//            return newMember;
//        }
//    }

//    /**
//     Get all members from database
//     @param chatId telegram chatId (stored ib db)
//     @return list of members
//    */
//    public List<Member> getMembers(long chatId) {
//        return dbService.getMembers( chatId );
//    }
//
//    /**
//     Insert new users in database and return all of chat members
//     @param newUsers list of new users who insert in database
//     @param chatId telegram chatId (stored ib db)
//     @return list of members
//     */
//    public List<Member> getMembers(List<User> newUsers, long chatId) {
//        List<Member> members = dbService.getMembers( chatId );
//        for (User newUser : newUsers) {
//            Member newMember = new Member(
//                    newUser.getId(),
//                    chatId,
//                    newUser.getFirstName(),
//                    newUser.getLastName(),
//                    new String());
//
//            if  (!members.contains( newMember )) {
//                members.add( dbService.addMembers( newMember ) );
//            }
//        }
//        return members;
//    }



//    public void handleAddUserCommand(Update upd, long chatId) {
//        User sender = upd.getMessage().getFrom();
//        this.addMember( sender, chatId );
//
//        for (MessageEntity me : upd.getMessage().getEntities()) {
//            if (me.getUser() != null) {
//                this.addMember( me.getUser(), chatId );
//            }
//        }
//
//        String query = upd.getMessage().getText();
//        String lines[] = query.split("[\r\n]+");
//        for (String line : lines) {
//            int sepIdx = line.indexOf(" ");
//            String firstName = line.substring( 0, sepIdx > 0 ? sepIdx : line.length() );
//            String lastName = sepIdx > 0 ? line.substring( sepIdx, line.length()) : null;
//            this.addMember( new Member( chatId, firstName, lastName ), chatId );
//        }
//    }
//
//
//    public void handleAddUserBtt(Update upd, long chatId) throws MemberAlreadyExistException {
//        User sender = upd.getCallbackQuery().getFrom();
//        List<Member> members = this.getMembers( chatId );
//        if (members.contains( sender )) {
//            throw new MemberAlreadyExistException();
//        } else {
//            this.addMember( sender, chatId );
//        }
//    }
//
//
//

//
//    public Member addMember(User newUser, long chatId) {
//        Member newMember = new Member(
//                newUser.getId(),
//                chatId,
//                newUser.getFirstName(),
//                newUser.getLastName(),
//                new String());
//        return this.addMember(newMember, chatId);
//    }
//
//    public List<Member> addMember(List<User> newUsers, long chatId) {
//        List<Member> members = new ArrayList<Member>();
//        for (User newUser : newUsers) {
//            members.add( this.addMember( newUser, chatId ) );
//        }
//        return members;
//    }
//
//
//    public List<Member> addMemberFromEntities(List<MessageEntity> entities, long chatId) {
//        List<User> addingUsers = new ArrayList<User>();
//        for (MessageEntity me : entities) {
//            if (me.getUser() != null) {
//                addingUsers.add( me.getUser() );
//            }
//        }
//        return this.addMember( addingUsers, chatId );
//    }
//
//
//    public Purchase handleInlineAnswerPurchaseMsg(long chatId, String text, User user) {
//        String format = StringUtil.removeMarkdownSyntax(
//                EmojiParser.parseToUnicode( Constants.INLINE_BUY_MSG_TEXT_PTT )
//        );
//
//        if (StringUtil.matchesFormatString( text, format )) {
//            List<String> params = StringUtil.extractParametersFromFormatString(text, format);
//            String name = params.get(1);
//            double amount = Double.valueOf(params.get(2));
//            Member purchaser = dbService.getMembersByTelegramId( user.getId() );
//            List<Member> members = dbService.getMembers( chatId );
//            Purchase existPurchase = dbService.getPurchase( name, amount, purchaser );
//
//            if (existPurchase != null) {
//                existPurchase.setTelegramChatId( chatId );
//                existPurchase.setMembers( members );
//                return dbService.updatePurchase( existPurchase );
//            } else {
//                Purchase newPurchase = new Purchase(chatId, purchaser, name, amount, new Date(), members);
//                return dbService.addPurchase( newPurchase );
//            }
//        }
//        return null;
//    }
//
    public Remittance addRemittance(long chatId, Message replies, boolean isNamedRemittance) {

        String startMsgPtt = isNamedRemittance ? Constants.NAMED_REMITTANCE_MSG : Constants.UNNAMED_REMITTANCE_MSG;
        String msgPtt = EmojiParser.parseToUnicode( startMsgPtt + Constants.REMITTANCE_MSG_LOADING_LINE );

        List<String> params = StringUtil.extractParametersFromFormatString( replies.getText(), msgPtt );
        double amount = Double.valueOf( params.get(1) );
        String name = isNamedRemittance ? params.get(2) : null;

        Member sender = dbService.getMembersByTelegramId( replies.getFrom().getId() );

        Remittance rem = new Remittance()
                .setTelegramChatId( chatId )
                .setAmount( amount )
                .setDate( new Date() )
                .setName( name )
                .setActive( false )
                .setSender( sender );

        return dbService.addRemittance( rem );
    }
//
//
//
//    public void handleInlineAnswerPurchase( String inlineMsgId, String query, User user) {
//        int idx =  query.indexOf( " " ) > 0 ? query.indexOf( " " ) : query.length();
//        String digit = query.substring( 0, idx );
//        String name = query.substring( query.indexOf( " " ) + 1, query.length() );
//        double summ = Double.valueOf( digit.replace( ",", "." ) );
//
//        Member purchaser = dbService.getMembersByTelegramId( user.getId() );
//        Purchase existPurchase = dbService.getPurchase( name, summ, purchaser );
//
//        if (existPurchase != null) {
//            existPurchase.setInlineMsgId( inlineMsgId );
//            dbService.updatePurchase( existPurchase );
//        } else {
//            Purchase newPurchase = new Purchase( inlineMsgId, purchaser, name, summ, new Date() );
//            dbService.addPurchase( newPurchase );
//        }
//    }
//
//    public Purchase getPurchase(String inlineMsgId) {
//        return dbService.getPurchase(inlineMsgId);
//    }
//
//    public List<Purchase> getActivePurchases(long chatId) {
//        return dbService.getActivePurchases(chatId);
//    }
//
//    public PurchaseMemberList getPurchaseInvokeMember(Purchase purchase) {
//        List<Member> chatMembers = dbService.getMembers(purchase.getTelegramChatId());
//        PurchaseMemberList prMembers = new PurchaseMemberList();
//        for (Member chatMember : chatMembers) {
//            prMembers.add( new PurchaseMember( chatMember, purchase.getMembers().contains(chatMember)) );
//        }
//        return prMembers;
//    }
//
//    public void removeFromPurchase(String query) {
//        Pattern p = Pattern.compile("\\d+");
//        Matcher m = p.matcher(query);
//        int memberId = m.find() ? Integer.parseInt( m.group() ) : 0;
//        int purchaseID = m.find() ? Integer.parseInt( m.group() ) : 0;
//
//        dbService.removePurchaseMember(purchaseID, memberId);
//
//    }
//
//    public void addToPurchase(String query) {
//        Pattern p = Pattern.compile("\\d+");
//        Matcher m = p.matcher(query);
//        int memberId = m.find() ? Integer.parseInt( m.group() ) : 0;
//        int purchaseID = m.find() ? Integer.parseInt( m.group() ) : 0;
//
//        dbService.addPurchaseMember(purchaseID, memberId);
//    }
//
//
//    public Purchase deactivePurchase(String inlineMsgId) {
//        Purchase purchase = dbService.getPurchase( inlineMsgId );
//        purchase.setActive( false );
//        return dbService.updatePurchase( purchase );
//    }
//
//    public Purchase activePurchase(String inlineMsgId) {
//        Purchase purchase = dbService.getPurchase( inlineMsgId );
//        purchase.setActive( true );
//        return dbService.updatePurchase( purchase );
//    }
//
//    public Purchase purchaseAddUser(String inlineMsgId, User user) throws MemberAlreadyExistException {
//        Purchase purchase = dbService.getPurchase( inlineMsgId );
//        Member member = dbService.getMembersByTelegramId( user.getId() );
//
//        if (purchase.getMembers().contains( member )) {
//            throw new MemberAlreadyExistException();
//        } else {
//            dbService.addPurchaseMember( purchase.getID(), member.getId() );
//            purchase.getMembers().add( member );
//            return purchase;
//        }
//    }
//
//    public Remittance updateRemittance(String data, String inlineMessageId) {
//        Pattern p = Pattern.compile( "\\d+" );
//        Matcher m = p.matcher( data );
//        int remittanceId = m.find() ? Integer.parseInt( m.group() ) : 0;
//        int memberId = m.find() ? Integer.parseInt( m.group() ) : 0;
//
//        Remittance editedRem = dbService.getRemittance( remittanceId )
//                .setActive( true )
//                .setRecipient(dbService.getMember( memberId ) )
//                .setTelInlineMsgID(inlineMessageId);
//
//        return dbService.updateRemittance( editedRem );
//    }
//
//    public Remittance deactiveRemittance(String query) {
//        Pattern p = Pattern.compile( "\\d+" );
//        Matcher m = p.matcher( query );
//        int remittanceId = m.find() ? Integer.parseInt( m.group() ) : 0;
//
//        Remittance editedRem = dbService.getRemittance( remittanceId )
//                .setActive( false );
//
//        return dbService.updateRemittance( editedRem );
//    }
//
//    public Remittance restoreRemittance(String query) {
//        Pattern p = Pattern.compile( "\\d+" );
//        Matcher m = p.matcher( query );
//        int remittanceId = m.find() ? Integer.parseInt( m.group() ) : 0;
//
//        Remittance editedRem = dbService.getRemittance( remittanceId )
//                .setActive( true );
//
//        return dbService.updateRemittance( editedRem );
//    }
}
