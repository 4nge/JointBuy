package ru.ange.jointbuy.pojo;

import java.util.Date;
import java.util.List;

public class Remittance {

    private int ID;
    private Long telegramChatId;
    private String telInlineMsgID;
    private String name;
    private Date date;
    private Member sender;
    private Member recipient;
    private boolean active;

    public Remittance() {}

    public Remittance(int ID, Long telegramChatId, String inlineMsgId, String name, Date date, Member sender,
                      Member recipient, boolean active) {

        this.ID = ID;
        this.telegramChatId = telegramChatId;
        this.name = name;
        this.date = date;
        this.sender = sender;
        this.recipient = recipient;
        this.active = active;
    }

    public int getID() {
        return ID;
    }

    public Remittance setID(int ID) {
        this.ID = ID;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Remittance setDate(Date date) {
        this.date = date;
        return this;
    }

    public Member getSender() {
        return sender;
    }

    public Remittance setSender(Member sender) {
        this.sender = sender;
        return this;
    }

    public Member getRecipient() {
        return recipient;
    }

    public Remittance setRecipient(Member recipient) {
        this.recipient = recipient;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Remittance setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public Remittance setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
        return this;
    }


    public String getName() {
        return name;
    }

    public Remittance setName(String name) {
        this.name = name;
        return this;
    }

    public String getTelInlineMsgID() {
        return telInlineMsgID;
    }

    public Remittance setTelInlineMsgID(String telInlineMsgID) {
        this.telInlineMsgID = telInlineMsgID;
        return this;
    }

    @Override
    public String toString() {
        return "Remittance{" +
                "ID=" + ID +
                ", date=" + date +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", active=" + active +
                '}';
    }
}
