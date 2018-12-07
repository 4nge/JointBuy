package ru.ange.jointbuy.services;

import ru.ange.jointbuy.dao.ChatDAO;
import ru.ange.jointbuy.dao.MemberDAO;
import ru.ange.jointbuy.dao.PurchaseDAO;
import ru.ange.jointbuy.dao.SystemMessageDAO;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;
import ru.ange.jointbuy.pojo.SystemMessage;

import java.util.List;

public class DbService {

    private MemberDAO memberDAO;
    private SystemMessageDAO systemMessageDAO;
    private ChatDAO chatDAO;
    private PurchaseDAO purchaseDAO;

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }
    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }
    public SystemMessageDAO getSystemMessageDAO() {
        return systemMessageDAO;
    }
    public void setSystemMessageDAO(SystemMessageDAO systemMessageDAO) {
        this.systemMessageDAO = systemMessageDAO;
    }
    public ChatDAO getChatDAO() {
        return chatDAO;
    }
    public void setChatDAO(ChatDAO chatDAO) {
        this.chatDAO = chatDAO;
    }
    public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
    }
    public PurchaseDAO getPurchaseDAO() {
        return purchaseDAO;
    }


    public List<Member> getMembers(long chatId) {
        return memberDAO.getMembers( chatId );
    }

    public Member addMembers(Member member) {
        return memberDAO.addMembers( member );
    }

    public Member getMember(int id) {
        return memberDAO.getMember( id );
    }

    public Member getMembersByTelegramId(int telegramUserId) {
        return memberDAO.getMembersByTelegramId( telegramUserId );
    }

    public void addMessage(SystemMessage msg) {
        systemMessageDAO.addMessage( msg );
    }

    public List<SystemMessage> getMessages() {
        return systemMessageDAO.getMessages();
    }

    public List<SystemMessage> getMessages(int typeId) {
        return systemMessageDAO.getMessages(typeId);
    }

    public Purchase addPurchase(Purchase purchase) {
        return purchaseDAO.addPurchase( purchase );
    }
}
