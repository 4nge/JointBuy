package ru.ange.jointbuy.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Remittance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RemittanceMapper  implements RowMapper<Remittance> {

    @Override
    public Remittance mapRow(ResultSet rs, int i) throws SQLException {

//        int ID rs.getInt( "" );
//        Long telegramChatId;
//        String telInlineMsgID;
//        String name;
//        Date date;
//        Member sender;
//        Member recipient;
//        boolean active;
//
//        Remittance remittance = new Remittance(  )
        return null;
    }
}
