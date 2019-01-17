package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MemberMapper implements RowMapper<Member> {

    private String idCol = "me_ID";
    private String telUserIdCol = "me_telUserId";
    private String telChatIdCol = "me_telChatId";
    private String firstNameCol = "me_firstName";
    private String lastNameCol = "me_lastName";
    private String aliasCol = "me_alias";

    public MemberMapper() {}

    public MemberMapper(String idCol, String telUserIdCol, String telChatIdCol, String firstNameCol, String lastNameCol, String aliasCol) {
        this.idCol = idCol;
        this.telUserIdCol = telUserIdCol;
        this.telChatIdCol = telChatIdCol;
        this.firstNameCol = firstNameCol;
        this.lastNameCol = lastNameCol;
        this.aliasCol = aliasCol;
    }

    @Override
    public Member mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( idCol );
        int telegramUserId  = rs.getInt( telUserIdCol );
        long telegramChatId = rs.getLong( telChatIdCol );
        String firstName  = rs.getString( firstNameCol );
        String lastName = rs.getString( lastNameCol );
        String alias = rs.getString( aliasCol );

        return new Member(id, telegramUserId, telegramChatId, firstName, lastName, alias);
    }
}
