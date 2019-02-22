package ru.ange.jointbuy.services;


import ru.ange.jointbuy.dao.MemberDAO;
import ru.ange.jointbuy.dao.PurchaseDAO;
import ru.ange.jointbuy.dao.RemittanceDAO;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;
import ru.ange.jointbuy.pojo.Remittance;


import java.util.List;

public class DbService {

    private MemberDAO memberDAO;
    private PurchaseDAO purchaseDAO;
    private RemittanceDAO remittanceDAO;

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

    public RemittanceDAO getRemittanceDAO() {
        return remittanceDAO;
    }

    public void setRemittanceDAO(RemittanceDAO remittanceDAO) {
        this.remittanceDAO = remittanceDAO;
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
        List<Purchase> purchases = purchaseDAO.getActivePurchases(chatId);
        for (Purchase purchase : purchases) {
            purchase.setMembers(memberDAO.getMembersByPurchaseID(purchase.getID()));
        }
        return purchases;
    }

    public Remittance addRemittance(Remittance rem) {
        return remittanceDAO.addRemittance( rem );
    }

    public Remittance getRemittance(int remittanceId) {
        return remittanceDAO.getRemittance(remittanceId);
    }

    public Remittance updateRemittance(Remittance remittance) {
        return remittanceDAO.updateRemittance(remittance);
    }
}
