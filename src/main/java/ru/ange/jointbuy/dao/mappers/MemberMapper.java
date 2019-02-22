package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberMapper implements RowMapper<Member> {


    private String id_col_label = "me_ID";
    private String tuserid_col_label = "me_telUserId";
    private String tchatid_col_label = "me_telChatId";
    private String firstname_col_label = "me_firstName";
    private String lastname_col_label = "me_lastName";
    private String alias_col_label = "me_alias";

    public MemberMapper() {}

    public MemberMapper(String id_col_label, String tuserid_col_label, String tchatid_col_label,
                        String firstname_col_label, String lastname_col_label, String alias_col_label) {

        this.id_col_label = id_col_label;
        this.tuserid_col_label = tuserid_col_label;
        this.tchatid_col_label = tchatid_col_label;
        this.firstname_col_label = firstname_col_label;
        this.lastname_col_label = lastname_col_label;
        this.alias_col_label = alias_col_label;
    }

    @Override
    public Member mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( id_col_label );
        int tUserId  = rs.getInt( tuserid_col_label );
        long tChatId = rs.getLong( tchatid_col_label );
        String firstName  = rs.getString( firstname_col_label );
        String lastName = rs.getString( lastname_col_label );
        String alias = rs.getString( alias_col_label );

        return new Member(id, tUserId, tChatId, firstName, lastName, alias);
    }

}
