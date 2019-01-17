package ru.ange.jointbuy.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.pojo.Remittance;

public class RemittanceDAO {

    private JdbcTemplate jdbc;
    private NamedParameterJdbcTemplate npjdbc;

    public NamedParameterJdbcTemplate getNpjdbc() {
        return npjdbc;
    }
    public void setNpjdbc(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }

    public JdbcTemplate getJdbc() {
        return jdbc;
    }
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final String ADD_REMITTANCE = "" +
            "insert into jointbuy.Remittances values ( " +
            "    default, " +
            "    :chatId, " +
            "    :inlineMsgID, " +
            "    :name, " +
            "    :senderId, " +
            "    :recipientID, " +
            "    :remDate,   " +
            "    :active " +
            ")";


    public Remittance addRemittance(Remittance rem) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("chatId", rem.getTelegramChatId() )
                .addValue("inlineMsgID", rem.getTelInlineMsgID())
                .addValue("name", rem.getName() )
                .addValue("senderId", rem.getSender() !=  null ? rem.getSender().getId() : null )
                .addValue("recipientID", rem.getRecipient() !=  null ? rem.getRecipient().getId() : null )
                .addValue("remDate", rem.getDate() )
                .addValue("active", rem.isActive() );

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update( ADD_REMITTANCE, params, holder );
        rem.setID( holder.getKey().intValue() );

        return rem;
    }

    private static final String GET_REMITTANCE = "" +
            "select " +
            "    r.ID, " +
            "    r.telChatID, " +
            "    r.telInlineMsgID, " +
            "    r.name, " +
            "    r.senderID, " +
            "    r.recipientID, " +
            "    r.remittanceDate, " +
            "    r.active " +
            "from " +
            "  jointbuy.Remittances as r " +
            "where " +
            "  r.ID - :remId";


    public Remittance getRemittance(int remId) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("remId", remId );

        return npjdbc.query( GET_REMITTANCE, params  );
    }
}
