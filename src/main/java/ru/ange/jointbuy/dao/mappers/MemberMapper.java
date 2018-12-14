package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( "me_ID" );
        int telegramUserId  = rs.getInt( "me_telUserId" );
        long telegramChatId = rs.getLong( "me_telChatId" );
        String firstName  = rs.getString( "me_firstName" );
        String lastName = rs.getString( "me_lastName" );
        String alias = rs.getString( "me_alias" );

        return new Member(id, telegramUserId, telegramChatId, firstName, lastName, alias);
    }
}
