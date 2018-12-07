package ru.ange.jointbuy.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase extends Operation {

    private int ID;
    private long telegramChatId;
    private String inlineMsgId;
    private Date date;
    private Member purchaser;

    public Purchase() {
        this(0, 0, "", new Member(), "", 0.0, null);
    }

    public Purchase(String inlineMessageId, Member purchaser, String name, double amount, Date date) {

        this(0, 0, inlineMessageId, purchaser, name, amount, date);
    }

    public Purchase(int ID, long telegramChatId, String inlineMsgId, Member purchaser,
                    String name, double amount, Date date) {

        super( Type.PURCHASE );
        super.setName( name );
        super.setSum( amount );
        this.ID = ID;
        this.telegramChatId = telegramChatId;
        this.inlineMsgId = inlineMsgId;
        this.purchaser = purchaser;
        this.date = date;
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
