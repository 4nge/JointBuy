package ru.ange.jointbuy.pojo;

public class Chat {

    private int id;
    private long telegramChatId;
    private boolean addUserMode;

    public Chat() {
        this(0, 0, false);
    }

    public Chat(long telegramChatId, boolean addUserMode) {
        this.telegramChatId = telegramChatId;
        this.addUserMode = addUserMode;
    }

    public Chat(int id, long telegramChatId, boolean addUserMode) {
        this(telegramChatId, addUserMode);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Chat setId(int id) {
        this.id = id;
        return this;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public Chat setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
        return this;
    }

    public boolean isAddUserMode() {
        return addUserMode;
    }

    public Chat setAddUserMode(boolean addUserMode) {
        this.addUserMode = addUserMode;
        return this;
    }
}
