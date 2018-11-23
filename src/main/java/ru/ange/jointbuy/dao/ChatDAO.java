package ru.ange.jointbuy.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.dao.mappers.ChatMapper;
import ru.ange.jointbuy.dao.mappers.MemberMapper;
import ru.ange.jointbuy.pojo.Chat;
import ru.ange.jointbuy.pojo.Member;

public class ChatDAO {

    private static final RowMapper<Chat> MAP_CHAT = new ChatMapper();

    private NamedParameterJdbcTemplate npjdbc;

    public NamedParameterJdbcTemplate getNpjdbc() {
        return npjdbc;
    }
    public void setNpjdbc(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }


    public static final String ADD_CHAT = "" +
            "insert into Chat values ( " +
            "    default, " +
            "    :telegramChatId," +
            "    :addUserMode " +
            ")";

    public Chat addChat(Chat chat) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telegramChatId", chat.getTelegramChatId());
        params.addValue("addUserMode", chat.isAddUserMode());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update( ADD_CHAT, params, holder);

        return chat.setId( holder.getKey().intValue() );
    }

    public static final String GET_CHAT = "" +
            "select " +
            "    ID, " +
            "    telegramChatId, " +
            "    addUserMode " +
            "from Chat " +
            "where " +
            "    telegramChatId = :chatId";

    public Chat getChat(long chatId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("chatId", chatId);

        return npjdbc.queryForObject( GET_CHAT, params, MAP_CHAT );
    }

    public static final String UPD_CHAT = "" +
            "update Chat set " +
            "    telegramChatId = :telegramChatId, " +
            "    addUserMode  = :addUserMode " +
            "where id = :id ";

    public Chat updateChat(Chat chat) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", chat.getId());
        params.addValue("telegramChatId", chat.getTelegramChatId());
        params.addValue("addUserMode", chat.isAddUserMode());

        npjdbc.update( ADD_CHAT, params);
        return chat;
    }
}
