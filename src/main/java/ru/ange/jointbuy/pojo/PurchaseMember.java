package ru.ange.jointbuy.pojo;

public class PurchaseMember extends Member {

    private boolean involved;

    public PurchaseMember(Member member, boolean involved) {
        super(member.getId(), member.getTelegramUserId(), member.getTelegramChatId(),
                member.getFirstName(), member.getLastName(), member.getAlias());
        this.involved = involved;
    }



    public boolean isInvolved() {
        return involved;
    }

    public PurchaseMember setInvolved(boolean involved) {
        this.involved = involved;
        return this;
    }
}
