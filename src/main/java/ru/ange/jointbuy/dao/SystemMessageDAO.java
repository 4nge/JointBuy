package ru.ange.jointbuy.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.ange.jointbuy.dao.mappers.SystemMessageMapper;
import ru.ange.jointbuy.pojo.SystemMessage;

import java.util.List;

public class SystemMessageDAO {

    private static final RowMapper<SystemMessage> MAP_MESSAGE = new SystemMessageMapper();

    private NamedParameterJdbcTemplate npjdbc;

    public NamedParameterJdbcTemplate getNpjdbc() {
        return npjdbc;
    }
    public void setNpjdbc(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }


    private static final String ADD_MESSAGE = "" +
            "insert into Messages values (" +
            "    default, " +
            "    :telegramMsgId, " +
            "    :telegramChatId, " +
            "    :typeId " +
            ")";

    public void addMessage(SystemMessage msg) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telegramMsgId", msg.getTelegramMsgId());
        params.addValue("telegramChatId", msg.getTelegramChatId());
        params.addValue("typeId", msg.getTypeId());

        npjdbc.update( ADD_MESSAGE, params);
    }


    private static final String GET_MESSAGES = "" +
            "select " +
            "    m.ID, " +
            "    m.telegramChatId, " +
            "    m.telegramMsgId, " +
            "    m.typeID, " +
            "    t.type " +
            "from " +
            "    Messages as m " +
            "    left join MessageTypes as t on m.typeID = t.ID " +
            "%s";

    public List<SystemMessage> getMessages() {
        return npjdbc.query(String.format( GET_MESSAGES, new String() ),  MAP_MESSAGE);
    }


    private static final String GET_MESSAGES_TYPE_FILTER = "" +
            "where " +
            "    m.typeID = :typeId ";

    public List<SystemMessage> getMessages(int typeId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("typeId", typeId);
        return npjdbc.query(String.format( GET_MESSAGES, GET_MESSAGES_TYPE_FILTER), params, MAP_MESSAGE);
    }
}
