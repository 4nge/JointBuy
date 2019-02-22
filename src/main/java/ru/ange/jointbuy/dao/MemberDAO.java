package ru.ange.jointbuy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.dao.mappers.MemberMapper;
import ru.ange.jointbuy.pojo.Member;

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
            "    me.ID as me_ID, " +
            "    me.telUserID as me_telUserID, " +
            "    me.telChatID as me_telChatID, " +
            "    me.firstName as me_firstName, " +
            "    me.lastName as me_lastName, " +
            "    me.alias as me_alias " +
            "from " +
            "    jointbuy.Members me " +
            "    %s " +
            "where " +
            "    1=1 ";

    private static final String GET_MEMBERS_CHAT_ID_FILTER = "" +
            "  and me.telChatId = :telChatID ";

    public List<Member> getMembers(long chatId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telChatID", chatId);

        String query = String.format(GET_MEMBERS, "") + GET_MEMBERS_CHAT_ID_FILTER;
        return npjdbc.query(query, params, MAP_MEMBER);
    }

    private static final String ADD_MEMBER = "" +
            "insert into Members values (" +
            "    default," +
            "    :telUserID," +
            "    :telChatID," +
            "    :firstName," +
            "    :lastName," +
            "    :alias" +
            ")";

    public Member addMembers(Member member) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telUserID", member.getTelegramUserId());
        params.addValue("telChatID", member.getTelegramChatId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("alias", member.getAlias());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update( ADD_MEMBER, params, holder);

        return member.setId( holder.getKey().intValue() );
    }

    private static final String GET_MEMBERS_ID_FILTER = "" +
            "  and me.ID = :id ";

    public Member getMember(int ID) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", ID);

        String query = String.format(GET_MEMBERS, "") + GET_MEMBERS_ID_FILTER;

        return npjdbc.queryForObject(query, params, MAP_MEMBER);
    }


    private static final String GET_MEMBERS_TELEGRAM_USER_ID_FILTER = "" +
            "  and telUserID = :telUserID ";

    public Member getMembersByTelegramId(int telegramUserId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("telUserID", telegramUserId);

        String query = String.format(GET_MEMBERS, "") + GET_MEMBERS_TELEGRAM_USER_ID_FILTER;

        return npjdbc.queryForObject(query, params, MAP_MEMBER);
    }

    private static final String GET_MEMBERS_BY_PURCHASE_JOIN = "" +
            "    inner join jointbuy.PurchaseMembers pm on me.ID = pm.memberID ";

    private static final String GET_MEMBERS_BY_PURCHASE_FILTER = "" +
            "    and pm.purchaseID = :purchaseID ";

    public List<Member> getMembersByPurchaseID(int purchaseID) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("purchaseID", purchaseID);

        String query = String.format(GET_MEMBERS, GET_MEMBERS_BY_PURCHASE_JOIN) + GET_MEMBERS_BY_PURCHASE_FILTER;

        return npjdbc.query(query, params, MAP_MEMBER);
    }
}
