package ru.ange.jointbuy.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase extends Operation {

    private int ID;
    private Long telegramChatId;
    private String inlineMsgId;
    private Date date;
    private Member purchaser;
    private List<Member> members;
    private boolean active;

    public Purchase() {
        this(0, null, "", new Member(), "", 0.0, null);
    }

    public Purchase(long chatId, Member purchaser, String name, double amount, Date date, List<Member> members) {
        this(0, chatId, "", purchaser, name, amount, date, members, true);
    }

    public Purchase(String inlineMsgId, Member purchaser, String name, double amount, Date date) {
        this(0, null, "", purchaser,
                name, amount, date, new ArrayList<Member>(), true);
    }

    public Purchase(int ID, Long telegramChatId, String inlineMsgId, Member purchaser,
                    String name, double amount, Date date) {
        this(ID, telegramChatId, inlineMsgId, purchaser, name, amount, date, new ArrayList<Member>(), true);
    }

    public Purchase(int ID, Long telegramChatId, String inlineMsgId, Member purchaser,
                    String name, double amount, Date date, List<Member> members, boolean active) {

        super( Type.PURCHASE );
        super.setName( name );
        super.setSum( amount );
        this.ID = ID;
        this.telegramChatId = telegramChatId;
        this.inlineMsgId = inlineMsgId;
        this.purchaser = purchaser;
        this.date = date;
        this.members = members;
        this.active = active;
    }

    public int getID() {
        return ID;
    }

    public Purchase setID(int ID) {
        this.ID = ID;
        return this;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public Purchase setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
        return this;
    }

    public Member getPurchaser() {
        return purchaser;
    }

    public Purchase setPurchaser(Member purchaser) {
        this.purchaser = purchaser;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Purchase setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getInlineMsgId() {
        return inlineMsgId;
    }

    public Purchase setInlineMsgId(String inlineMsgId) {
        this.inlineMsgId = inlineMsgId;
        return this;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Purchase setMembers(List<Member> members) {
        this.members = members;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Purchase setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "ID=" + ID +
                ", telegramChatId=" + telegramChatId +
                ", purchaser=" + purchaser +
                ", date=" + date +
                '}';
    }
}
