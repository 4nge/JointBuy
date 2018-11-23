package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( "ID" );
        long telegramChatId = rs.getLong( "telegramChatId" );
        boolean addUserMode = rs.getBoolean( "addUserMode" );
        return new Chat( id, telegramChatId, addUserMode );
    }
}
