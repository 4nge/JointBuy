package ru.ange.jointbuy.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.ange.jointbuy.dao.mappers.PurchaseMapper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseDAO {

    private static final RowMapper<Purchase> PURCHASE_MEMBER = new PurchaseMapper();

    private NamedParameterJdbcTemplate npjdbc;

    public NamedParameterJdbcTemplate getNpjdbc() {
        return npjdbc;
    }
    public void setNpjdbc(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }

    private static final String ADD_PURCHASE = "" +
            "insert into Purchases values (" +
            "    default," +
            "    null," +
            "    :inlineMessageId," +
            "    :purchaserID," +
            "    :name," +
            "    :amount," +
            "    :date" +
            ")";

    private static final String ADD_PURCHASE_MEMBER = "" +
            "insert into PurchaseMembers VALUES (default, :purchaseID, :memberID)";

    public Purchase addPurchase(Purchase purchase) {

        // TODO in one transaction !

        // insert Purchase
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("inlineMessageId", purchase.getInlineMsgId())
                .addValue("purchaserID", purchase.getPurchaser().getId())
                .addValue("name", purchase.getName())
                .addValue("amount", purchase.getSum())
                .addValue("date", purchase.getDate());

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update( ADD_PURCHASE, params, holder);
        purchase.setID( holder.getKey().intValue() );

        // insert Purchase Members
//        MapSqlParameterSource batchArgs[] = new MapSqlParameterSource[purchase.getMembers().size()];
//        for (int i = 0; i < purchase.getMembers().size(); i++) {
//            Member member = purchase.getMembers().get( i );
//
//            MapSqlParameterSource batchArg = new MapSqlParameterSource()
//                    .addValue("purchaseID", purchase.getID())
//                    .addValue("memberID", member.getId());
//
//            batchArgs[i] = batchArg;
//        }
//        npjdbc.batchUpdate( ADD_PURCHASE_MEMBER,  batchArgs);

        return purchase;
    }

}
