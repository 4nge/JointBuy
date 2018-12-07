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

        int ID = rs.getInt( "ID" );
        long tcID = rs.getLong( "telegramChatId" );
        String name = rs.getString( "name" );
        double amount = rs.getDouble( "amount" );
        Date date = rs.getDate( "purchaseDate" );

        Member purchaser = MAP_MEMBER.mapRow( rs, i );

        //return new Purchase( ID, tcID, purchaser, name, amount, date);
        return null;
    }
}
