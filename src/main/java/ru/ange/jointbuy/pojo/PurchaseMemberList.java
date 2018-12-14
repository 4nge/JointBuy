package ru.ange.jointbuy.pojo;

import java.util.ArrayList;
import java.util.List;

public class PurchaseMemberList extends ArrayList<PurchaseMember> {

    public PurchaseMemberList() {
        super();
    }

    public PurchaseMemberList(List<PurchaseMember> members) {
        super();
        this.addAll( members );
    }

    public int getAllCount() {
        return this.size();
    }

    public int getInvolvedCount() {
        int involvedCount = 0;
        for (PurchaseMember pm : this) {
            if (pm.isInvolved()) {
                involvedCount++;
            }
        }
        return involvedCount;
    }
}
