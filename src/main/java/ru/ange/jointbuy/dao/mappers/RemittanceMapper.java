package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Remittance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RemittanceMapper  implements RowMapper<Remittance> {

    private static final String SND_ID_COL_LABEL = "senderID";
    private static final String SND_TUSERID_COL_LABEL = "snd_telUserId";
    private static final String SND_TCHATID_COL_LABEL = "snd_telChatId";
    private static final String SND_FIRSTNAME_COL_LABEL = "snd_firstName";
    private static final String SND_LASTNAME_COL_LABEL = "snd_lastName";
    private static final String SND_ALIAS_COL_LABEL = "snd_alias";

    private static final String RCP_ID_COL_LABEL = "recipientID";
    private static final String RCP_TUSERID_COL_LABEL= "rcp_telUserId";
    private static final String RCP_TCHATID_COL_LABEL = "rcp_telChatId";
    private static final String RCP_FIRSTNAME_COL_LABEL = "rcp_firstName";
    private static final String RCP_LASTNAME_COL_LABEL = "rcp_lastName";
    private static final String RCP_ALIAS_COL_LABEL = "rcp_alias";

    private static final String ID_COL_LABEL = "ID";
    private static final String TCHATID_COL_LABEL = "telChatID";
    private static final String TINLINEMSGID_COL_LABEL = "telInlineMsgID";
    private static final String NAME_COL_LABEL = "name";
    private static final String DATE_COL_LABEL = "remittanceDate";
    private static final String ACTIVE_COL_LABEL = "active";


    private static final RowMapper<Member> SND_MAPPER = new MemberMapper(SND_ID_COL_LABEL, SND_TUSERID_COL_LABEL,
            SND_TCHATID_COL_LABEL, SND_FIRSTNAME_COL_LABEL, SND_LASTNAME_COL_LABEL, SND_ALIAS_COL_LABEL);
    
    private static final RowMapper<Member> RCP_MAPPER = new MemberMapper(RCP_ID_COL_LABEL, RCP_TUSERID_COL_LABEL,
            RCP_TCHATID_COL_LABEL, RCP_FIRSTNAME_COL_LABEL, RCP_LASTNAME_COL_LABEL, RCP_ALIAS_COL_LABEL);

    @Override
    public Remittance mapRow(ResultSet rs, int i) throws SQLException {

        int id = rs.getInt(ID_COL_LABEL);
        Long telegramChatId = rs.getLong(TCHATID_COL_LABEL);
        String inlineMsgId = rs.getString(TINLINEMSGID_COL_LABEL);
        String name = rs.getString(NAME_COL_LABEL);
        Date date = rs.getDate(DATE_COL_LABEL);
        boolean active = rs.getBoolean(ACTIVE_COL_LABEL);

        Member sender = SND_MAPPER.mapRow(rs, i);
        Member recipient = RCP_MAPPER.mapRow(rs, i);

        return new Remittance(id, telegramChatId, inlineMsgId, name, date, sender, recipient, active);
    }
}
