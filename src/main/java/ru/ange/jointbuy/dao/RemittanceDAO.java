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
            "    :amount, " +
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
                .addValue("amount", rem.getAmount() )
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
            "    r.amount, " +
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




    private static final String UPD_REMITTANCE = "" +
            "update jointbuy.Remittances as r set "+
            "    r.telChatID = :telChatID, "+
            "    r.telInlineMsgID = :telInlineMsgID, "+
            "    r.name = :name, "+
            "    r.amount = :amount, "+
            "    r.senderID = :senderID, "+
            "    r.recipientID = :recipientID, "+
            "    r.remittanceDate = :remittanceDate, "+
            "    r.active = :active "+
            "where "+
            "    r.ID = :remID ";

    public Remittance updateRemittance(Remittance remittance) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("remID", remittance.getID() )
                .addValue("telChatID", remittance.getTelegramChatId() )
                .addValue("telInlineMsgID", remittance.getTelInlineMsgID() )
                .addValue("name", remittance.getName() )
                .addValue("amount", remittance.getAmount() )
                .addValue("senderID", remittance.getSender().getId() )
                .addValue("recipientID", remittance.getRecipient().getId() )
                .addValue("remittanceDate", remittance.getDate() )
                .addValue("active", remittance.isActive() );

        npjdbc.update( UPD_REMITTANCE, params );
        return remittance;
    }
}
