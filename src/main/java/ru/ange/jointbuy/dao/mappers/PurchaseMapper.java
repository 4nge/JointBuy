package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.dao.MemberDAO;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PurchaseMapper implements RowMapper<Purchase> {

    private static final RowMapper<Member> MAP_MEMBER = new MemberMapper();

    @Override
    public Purchase mapRow(ResultSet rs, int i) throws SQLException {

        int ID = rs.getInt("pr_ID" );
        long telChatId = rs.getLong("pr_telChatId" );
        String inlineMsgId = rs.getString("pr_telInlineMsgID" );
        String name = rs.getString("pr_name" );
        double amount = rs.getDouble("pr_amount" );
        Date date = rs.getDate("pr_purchaseDate" );
        boolean active = rs.getBoolean("active" );

        Member purchaser = MAP_MEMBER.mapRow( rs, i );
        return new Purchase(ID, telChatId, inlineMsgId, purchaser, name, amount, date);
    }



}
