package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.SystemMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemMessageMapper implements RowMapper<SystemMessage> {
    @Override
    public SystemMessage mapRow(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt( "ID" );
        long telegramMsgId = rs.getLong( "telegramMsgId" );
        long telegramChatId = rs.getLong( "telegramChatId" );
        int typeId = rs.getInt( "typeID" );
        String typeName = rs.getString( "type" );

        return new SystemMessage(id, telegramMsgId,  telegramChatId, typeId, typeName );
    }
}
