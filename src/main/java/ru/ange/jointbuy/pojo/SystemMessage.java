package ru.ange.jointbuy.pojo;

public class SystemMessage {

    private int id;
    private long telegramMsgId;
    private long telegramChatId;
    private int typeId;
    private String typeName;

    public SystemMessage() {
        this(0, 0, 0, 0, "");
    }

    public SystemMessage(long telegramMsgId, long telegramChatId, int typeId) {
        this.telegramMsgId = telegramMsgId;
        this.telegramChatId = telegramChatId;
        this.typeId = typeId;
    }

    public SystemMessage(int id, long telegramMsgId, long telegramChatId, int typeId, String typeName) {
        this(telegramMsgId, telegramChatId, typeId);
        this.typeName = typeName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public SystemMessage setId(int id) {
        this.id = id;
        return this;
    }

    public long getTelegramMsgId() {
        return telegramMsgId;
    }

    public SystemMessage setTelegramMsgId(int telegramMsgId) {
        this.telegramMsgId = telegramMsgId;
        return this;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public SystemMessage setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
        return this;
    }

    public int getTypeId() {
        return typeId;
    }

    public SystemMessage setTypeId(int typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public SystemMessage setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "id=" + id +
                ", telegramMsgId=" + telegramMsgId +
                ", telegramChatId=" + telegramChatId +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
