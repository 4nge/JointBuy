package ru.ange.jointbuy.pojo;

public class Member {

    private int id;
    private int telegramUserId;
    private long telegramChatId;
    private String firstName;
    private String lastName;
    private String alias;

    public Member() {
        this(0, 0, 0, "", "", "");
    }

    public Member(int telegramUserId, long telegramChatId, String firstName, String lastName, String alias) {
        this.telegramUserId = telegramUserId;
        this.telegramChatId = telegramChatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
    }

    public Member(int id, int telegramUserId, long telegramChatId, String firstName, String lastName, String alias) {
        this( telegramUserId, telegramChatId, firstName, lastName, alias);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(int telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
