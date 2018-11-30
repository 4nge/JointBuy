package ru.ange.jointbuy.services;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
//import ru.ange.jointbuy.bot.msg.HelloMsgHelper;
//import ru.ange.jointbuy.bot.msg.HelloUserMsgHelper;
import ru.ange.jointbuy.exception.MemberAlreadyExistException;
import ru.ange.jointbuy.pojo.Chat;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.SystemMessage;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class BotService {

    public DbService dbService;

    public DbService getDbService() {
        return dbService;
    }

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    /**
     Get all members from database
     @param chatId telegram chatId (stored ib db)
     @return list of members
    */
    public List<Member> getMembers(long chatId) {
        return dbService.getMembers( chatId );
    }

    /**
     Insert new users in database and return all of chat members
     @param newUsers list of new users who insert in database
     @param chatId telegram chatId (stored ib db)
     @return list of members
     */
    public List<Member> getMembers(List<User> newUsers, long chatId) {
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
        return members;
    }


    public void handleAddUserCommand(Update upd, long chatId) {
        User sender = upd.getMessage().getFrom();
        this.addMember( sender, chatId );

        for (MessageEntity me : upd.getMessage().getEntities()) {
            if (me.getUser() != null) {
                this.addMember( me.getUser(), chatId );
            }
        }

        String query = upd.getMessage().getText();
        String lines[] = query.split("[\r\n]+");
        for (String line : lines) {
            int sepIdx = line.indexOf(" ");
            String firstName = line.substring( 0, sepIdx > 0 ? sepIdx : line.length() );
            String lastName = sepIdx > 0 ? line.substring( sepIdx, line.length()) : null;
            this.addMember( new Member( chatId, firstName, lastName ), chatId );
        }
    }


    public boolean handleAddUserBtt(Update upd, long chatId) {
        User sender = upd.getCallbackQuery().getFrom();
        List<Member> members = this.getMembers( chatId );
        if (!members.contains( sender )) {
            this.addMember( sender, chatId );
            return true;
        } else {
            return false;
        }
    }



    public Member addMember(Member newMember, long chatId) {
        List<Member> members = dbService.getMembers( chatId );
        if  (!members.contains( newMember )) {
            return dbService.addMembers( newMember );
        } else {
            return newMember;
        }
    }

    public Member addMember(User newUser, long chatId) {
        Member newMember = new Member(
                newUser.getId(),
                chatId,
                newUser.getFirstName(),
                newUser.getLastName(),
                new String());
        return this.addMember(newMember, chatId);
    }

    public List<Member> addMember(List<User> newUsers, long chatId) {
        List<Member> members = new ArrayList<Member>();
        for (User newUser : newUsers) {
            members.add( this.addMember( newUser, chatId ) );
        }
        return members;
    }


    public List<Member> addMemberFromEntities(List<MessageEntity> entities, long chatId) {
        List<User> addingUsers = new ArrayList<User>();
        for (MessageEntity me : entities) {
            if (me.getUser() != null) {
                addingUsers.add( me.getUser() );
            }
        }
        return this.addMember( addingUsers, chatId );
    }


//    public List<BotApiMethod> handleStartChat(long chatId, long messageId, User user) {
//
//        Member newMember = new Member(
//                user.getId(),
//                chatId,
//                user.getFirstName(),
//                user.getLastName(),
//                new String());
//
//        List<Member> members = dbService.getMembers( chatId );
//        if  (!members.contains( newMember )) {
//            members.add( dbService.addMembers( newMember ) );
//        }
//
//        List<BotApiMethod> result = new ArrayList<BotApiMethod>();
////        result.add(new SendMessage( chatId, HelloMsgHelper.getMsg( members ) )
////                .setReplyMarkup( HelloMsgHelper.getMarkup() ));
//
//        dbService.addMessage( new SystemMessage( chatId, messageId, 1 ) );
//
//        return result;
//    }
//
//    public List<BotApiMethod> handleCallbackQuery(CallbackQuery cbq) {
//        List<BotApiMethod> result = new ArrayList<BotApiMethod>();
////        if (cbq.getData().equals( HelloMsgHelper.CALLBACK_DATA )) {
////            long chatId = cbq.getMessage().getChatId();
////            long messageId = cbq.getMessage().getMessageId();
////            User sender = cbq.getFrom();
////            String callId = cbq.getId();
////            result.addAll( addUser(chatId, messageId, sender, callId) );
////        }
//        return result;
//    }
//
//
//
//
//
//    public List<BotApiMethod> addUser(long chatId, long messageId, User user, String callId) {
//        List<BotApiMethod> result = new ArrayList<BotApiMethod>();
//
//        Member newMember = new Member(
//                user.getId(),
//                chatId,
//                user.getFirstName(),
//                user.getLastName(),
//                new String());
//
//        List<Member> members = dbService.getMembers(chatId);
//        if  (!members.contains( newMember )) {
//            members.add(dbService.addMembers( newMember ));
//
////            result.add(new EditMessageText()
////                        .setChatId(chatId)
////                        .setMessageId(toIntExact(messageId))
////                        .setText( HelloMsgHelper.getMsg(members))
////                        .setReplyMarkup( HelloMsgHelper.getMarkup()));
//
//            result.add(new AnswerCallbackQuery()
//                    .setCallbackQueryId( callId )
//                    .setText("Вы присодинились к списку участников"));
//        } else {
//            result.add(new AnswerCallbackQuery()
//                    .setCallbackQueryId( callId )
//                    .setText("Вы уже есть в списке участников"));
//        }
//        return result;
//    }
//
//    public List<BotApiMethod> handleAddUser(long chatId, long messageId, User user) {
//        List<BotApiMethod> result = new ArrayList<BotApiMethod>();
//
//        Member newMember = new Member(
//                user.getId(),
//                chatId,
//                user.getFirstName(),
//                user.getLastName(),
//                new String());
//
//        List<Member> members = dbService.getMembers(chatId);
//        if  (!members.contains( newMember )) {
//            members.add( dbService.addMembers( newMember ) );
//
////            result.add(new SendMessage( chatId, HelloUserMsgHelper.getMsg( newMember.getFullName() ))
////                .setReplyMarkup( HelloUserMsgHelper.getMarkup()));
//
////            // TODO добавить дли изминения уже существующих значений если даст API
////            List<SystemMessage> helloMesages = dbService.getMessages(1);
////            for (int j = 0; j < helloMesages.size() ; j++) {
////                SystemMessage hmsg = helloMesages.get( j );
////                result.add(new EditMessageText()
////                    .setChatId(hmsg.getTelegramChatId())
////                    .setMessageId(toIntExact(hmsg.getTelegramMsgId()))
////                    .setText( HelloMsg.getMsg(members))
////                    .setReplyMarkup( HelloMsg.getMarkup()));
////            }
//        }
//
//        return result;
//    }
//
//
//    public void enableAddUserMode(org.telegram.telegrambots.meta.api.objects.Chat chat) {
//        Chat nc = new Chat( chat.getId(), true );
//    }
//
//    public void handleNonCommandUpdate(Update update) {
//
//    }

}
