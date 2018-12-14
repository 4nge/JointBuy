package ru.ange.jointbuy.services;


import ru.ange.jointbuy.dao.MemberDAO;
import ru.ange.jointbuy.dao.PurchaseDAO;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;


import java.util.List;

public class DbService {

    private MemberDAO memberDAO;
    private PurchaseDAO purchaseDAO;

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }
    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
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

    public Purchase addPurchase(Purchase purchase) {
        return purchaseDAO.addPurchase( purchase );
    }

    public Purchase getPurchase(String name, double summ, Member purchaser) {
        Purchase purchase = purchaseDAO.getPurchaseByParams( name, summ, purchaser );
        if (purchase != null && purchase.getID() != 0) {
            purchase.setMembers( memberDAO.getMembersByPurchaseID( purchase.getID() ) );
        }
        return purchase;
    }

    public Purchase getPurchase(String inlineMsgId) {
        Purchase purchase = purchaseDAO.getPurchaseByInlineMsgId(inlineMsgId);
        purchase.setMembers(memberDAO.getMembersByPurchaseID(purchase.getID()));
        return purchase;
    }


    public Purchase updatePurchase(Purchase purchase) {
        return purchaseDAO.updatePurchase( purchase );
    }

    public void removePurchaseMember(int purchaseID, int memberId) {
        purchaseDAO.removeMembers(purchaseID, memberId);
    }

    public void addPurchaseMember(int purchaseID, int memberId) {
        purchaseDAO.addMember(purchaseID, memberId);
    }

    public void removePurchase(Purchase purchase) {
        purchaseDAO.removePurchase(purchase);
    }

    public List<Purchase> getActivePurchases(long chatId) {
        return purchaseDAO.getActivePurchases(chatId);
    }
}
