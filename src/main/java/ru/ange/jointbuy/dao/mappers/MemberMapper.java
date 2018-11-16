package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( "ID" );
        int telegramUserId  = rs.getInt( "telegramUserId" );
        long telegramChatId = rs.getLong( "telegramChatId" );
        String firstName  = rs.getString( "firstName" );
        String lastName = rs.getString( "lastName" );
        String alias = rs.getString( "alias" );

        return new Member(id, telegramUserId, telegramChatId, firstName, lastName, alias);
    }
}
