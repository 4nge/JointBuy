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

    public Member(long telegramChatId, String firstName, String lastName) {
        this( 0, telegramChatId, firstName, lastName, "" );
    }


    public int getId() {
        return id;
    }

    public Member setId(int id) {
        this.id = id;
        return this;
    }

    public int getTelegramUserId() {
        return telegramUserId;
    }

    public Member setTelegramUserId(int telegramUserId) {
        this.telegramUserId = telegramUserId;
        return this;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public Member setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Member setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Member setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public Member setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


    public boolean equals(Object o) {
        if(o instanceof Member){
            Member tc = (Member) o;
            return this.telegramUserId == tc.getTelegramUserId() && this.telegramChatId == tc.getTelegramChatId();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", telegramUserId=" + telegramUserId +
                ", telegramChatId=" + telegramChatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
