package ru.ange.jointbuy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.dao.mappers.MemberMapper;
import ru.ange.jointbuy.pojo.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MemberDAO {

    private static final RowMapper<Member> MAP_MEMBER = new MemberMapper();

    private NamedParameterJdbcTemplate npjdbc;

    public NamedParameterJdbcTemplate getNpjdbc() {
        return npjdbc;
    }
    public void setNpjdbc(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }

    private static final String GET_MEMBERS = "" +
            "select" +
            "  ID, telegramUserId, telegramChatId, " +
            "  firstName, lastName, alias " +
            "from " +
            "  Members " +
            "where " +
            "  telegramChatId = :telegramChatId";

    public List<Member> getMembers(long chatId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telegramChatId", chatId);
        return npjdbc.query(GET_MEMBERS, params, MAP_MEMBER);
    }

    private static final String ADD_MEMBER = "" +
            "insert into Members values (" +
            "    default," +
            "    :telegramUserId," +
            "    :telegramChatId," +
            "    :firstName," +
            "    :lastName," +
            "    :alias" +
            ")";


    public Member addMembers(Member member) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telegramUserId", member.getTelegramUserId());
        params.addValue("telegramChatId", member.getTelegramChatId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("alias", member.getAlias());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update( ADD_MEMBER, params, holder);

        return member.setId( holder.getKey().intValue() );
    }
}
