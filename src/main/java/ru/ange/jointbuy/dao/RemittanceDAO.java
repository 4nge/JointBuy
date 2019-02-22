package ru.ange.jointbuy.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.dao.mappers.RemittanceMapper;
import ru.ange.jointbuy.pojo.Remittance;

public class RemittanceDAO {

    private static final RowMapper<Remittance> MAPPER = new RemittanceMapper();

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
            "    r.remittanceDate, " +
            "    r.active, " +
            "    r.senderID, " +
            "    snd.telUserID as snd_telUserID, " +
            "    snd.telChatID as snd_telChatID, " +
            "    snd.firstName as snd_firstName, " +
            "    snd.lastName as snd_lastName, " +
            "    snd.alias as snd_alias, " +
            "    r.recipientID, " +
            "    rcp.telUserID as rcp_telUserID, " +
            "    rcp.telChatID as rcp_telChatID, " +
            "    rcp.firstName as rcp_firstName, " +
            "    rcp.lastName as rcp_lastName, " +
            "    rcp.alias as rcp_alias " +
            "from " +
            "    jointbuy.Remittances as r " +
            "    left join jointbuy.Members as snd on snd.ID = r.senderID " +
            "    left join jointbuy.Members as rcp on rcp.ID = r.recipientID " +
            "where " +
            "    r.ID = :remId";

    public Remittance getRemittance(int remId) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("remId", remId );

        return npjdbc.queryForObject( GET_REMITTANCE, params , MAPPER );
    }

    public Remittance updateRemittance(Remittance remittance) {
        // TODO
        return null;
    }
}
