package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Remittance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RemittanceMapper  implements RowMapper<Remittance> {

    private static final String SENDER_ID_COL = "se_ID";
    private static final String SENDER_TELUSERID_COL = "se_telUserId";
    private static final String SENDER_TELCHATID_COL = "se_telChatId";
    private static final String SENDER_FIRSTNAME_COL = "se_firstName";
    private static final String SENDER_LASTNAME_COL = "se_lastName";
    private static final String SENDER_ALIAS_COL = "se_alias";

    private static final RowMapper<Member> SENDER_MP = new MemberMapper( SENDER_ID_COL, SENDER_TELUSERID_COL,
            SENDER_TELCHATID_COL, SENDER_FIRSTNAME_COL, SENDER_LASTNAME_COL, SENDER_ALIAS_COL );

    private static final String RECIPIENT_ID_COL = "re_ID";
    private static final String RECIPIENT_TELUSERID_COL = "re_telUserId";
    private static final String RECIPIENT_TELCHATID_COL = "re_telChatId";
    private static final String RECIPIENT_FIRSTNAME_COL = "re_firstName";
    private static final String RECIPIENT_LASTNAME_COL = "re_lastName";
    private static final String RECIPIENT_ALIAS_COL = "re_alias";

    private static final RowMapper<Member> RECIPIENT_MP = new MemberMapper( RECIPIENT_ID_COL, RECIPIENT_TELUSERID_COL,
            RECIPIENT_TELCHATID_COL, RECIPIENT_FIRSTNAME_COL, RECIPIENT_LASTNAME_COL, RECIPIENT_ALIAS_COL );

    @Override
    public Remittance mapRow(ResultSet rs, int i) throws SQLException {

        int id = rs.getInt( "ID" );
        Long chatId = rs.getLong( "telChatID" );
        String inlineMsgID = rs.getString( "telInlineMsgID" );
        String name = rs.getString( "name" );
        Date date = rs.getDate( "remittanceDate" );
        boolean active = rs.getBoolean( "active" );

        Member sender = SENDER_MP.mapRow( rs, i );
        Member recipient = RECIPIENT_MP.mapRow( rs, i );

        Remittance remittance = new Remittance( id, chatId, inlineMsgID, name, date, sender, recipient, active );
        return remittance;
    }
}
