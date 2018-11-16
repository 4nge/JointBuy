package ru.ange.jointbuy.services;

import ru.ange.jointbuy.dao.MemberDAO;
import ru.ange.jointbuy.pojo.Member;

import java.util.List;

public class BotService {

    private MemberDAO memberDAO;

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }

    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public List<Member> getMembers(long chatId) {
        return memberDAO.getMembers( chatId );
    }

    public void addMembers(Member member) {
        memberDAO.addMembers( member );
    }

}
