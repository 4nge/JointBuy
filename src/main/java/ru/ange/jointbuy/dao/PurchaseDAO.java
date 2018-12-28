package ru.ange.jointbuy.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.ange.jointbuy.dao.mappers.PurchaseMapper;
import ru.ange.jointbuy.pojo.Member;
import ru.ange.jointbuy.pojo.Purchase;

import java.util.List;

public class PurchaseDAO {

    private static final RowMapper<Purchase> PURCHASE_MEMBER = new PurchaseMapper();

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


    private static final String ADD_PURCHASE = "" +
            "insert into jointbuy.Purchases values (" +
            "    default, " +
            "    :chatId, " +
            "    null, " +
            "    :purchaserID, " +
            "    :name, " +
            "    :amount, " +
            "    :date, " +
            "    :active " +
            ")";

    public Purchase addPurchase(Purchase purchase) {

        DataSourceTransactionManager dwhtm = new DataSourceTransactionManager( jdbc.getDataSource() );
        TransactionTemplate dwhtt = new TransactionTemplate( dwhtm );
        dwhtt.setPropagationBehavior( TransactionDefinition.PROPAGATION_REQUIRES_NEW );

        purchase.setID( dwhtt.execute( status -> {

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue( "chatId", purchase.getTelegramChatId() )
                    .addValue( "purchaserID", purchase.getPurchaser().getId() )
                    .addValue( "name", purchase.getName() )
                    .addValue( "amount", purchase.getSum() )
                    .addValue( "date", purchase.getDate() )
                    .addValue( "active", purchase.isActive() );

            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            npjdbc.update( ADD_PURCHASE, params, holder );
            int purchaseId = holder.getKey().intValue();

            addMembers(purchaseId, purchase.getMembers());
            return purchaseId;
        } ));
        return purchase;
    }

    private static final String ADD_PURCHASE_MEMBER = "" +
            "insert into jointbuy.PurchaseMembers VALUES (default, :purchaseID, :memberID)";


    private void addMembers(int purchaseId,  List<Member> members) {
        MapSqlParameterSource batchArgs[] = new MapSqlParameterSource[members.size()];
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get( i );
            MapSqlParameterSource batchArg = new MapSqlParameterSource()
                    .addValue("purchaseID", purchaseId)
                    .addValue("memberID", member.getId());

            batchArgs[i] = batchArg;
        }
        npjdbc.batchUpdate( ADD_PURCHASE_MEMBER,  batchArgs);
    }

    public void addMember(int purchaseId, int memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("purchaseID", purchaseId)
                .addValue("memberID", memberId);

        npjdbc.update( ADD_PURCHASE_MEMBER,  params);
    }

    private static final String REMOVE_PURCHASE_MEMBERS = "" +
            "delete from jointbuy.PurchaseMembers where 1=1 %s ";

    private static final String REMOVE_ALL_PURCHASE_MEMBERS_PURCHASE_FILTER = "" +
            " and purchaseID = :purchaseId ";

    private static final String REMOVE_ALL_PURCHASE_MEMBERS_MEMBER_FILTER = "" +
            " and memberID = :memberId ";

    public void removeAllMembers(int purchaseId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue( "purchaseId", purchaseId );

        String query = String.format(REMOVE_PURCHASE_MEMBERS, REMOVE_ALL_PURCHASE_MEMBERS_PURCHASE_FILTER);
        npjdbc.update(query, params);
    }

    public void removeMembers(int purchaseId, int memberId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue( "purchaseId", purchaseId )
                .addValue( "memberId", memberId );

        String filter = REMOVE_ALL_PURCHASE_MEMBERS_PURCHASE_FILTER.concat(REMOVE_ALL_PURCHASE_MEMBERS_MEMBER_FILTER);
        String query = String.format(REMOVE_PURCHASE_MEMBERS, filter);
        npjdbc.update(query, params);
    }


    private static final String GET_PURCHASE = "" +
            "select " +
            "    pr.ID as pr_ID, " +
            "    pr.telChatID as pr_telChatID, " +
            "    pr.telInlineMsgID as pr_telInlineMsgID, " +
            "    pr.purchaserID as pr_purchaserID, " +
            "    pr.name as pr_name, " +
            "    pr.amount as pr_amount," +
            "    pr.purchaseDate as pr_purchaseDate, " +
            "    pr.active," +
            "    me.ID as me_ID, " +
            "    me.telUserID as me_telUserID, " +
            "    me.telChatID as me_telChatID, " +
            "    me.firstName as me_firstName, " +
            "    me.lastName as me_lastName, " +
            "    me.alias as me_alias " +
            "from " +
            "    jointbuy.Purchases pr " +
            "    inner join jointbuy.Members me on pr.purchaserID = me.ID " +
            "where " +
            "    1 = 1" +
            "    %s ";

    private static final String GET_PURCHASE_PARAMS_FILTER = "" +
            "    and pr.purchaserID = :purchaserId" +
            "    and pr.name = :name" +
            "    and pr.amount = :amount ";

    private static final String GET_ACTIVE_PURCHASE_CHATID_FILTER = "" +
            "    and pr.active = true " +
            "    and pr.telChatID = :telChatID";

    private static final String GET_PURCHASE_LIMIT = "" +
            "order by " +
            "    pr.ID desc " +
            "limit 0, 1";

    public Purchase getPurchaseByParams(String name, double amount, Member purchaser) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("amount", amount)
                .addValue("purchaserId", purchaser.getId());

        String query = String.format( GET_PURCHASE, GET_PURCHASE_PARAMS_FILTER ) + GET_PURCHASE_LIMIT;
        try {
            return npjdbc.queryForObject( query, params, PURCHASE_MEMBER );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    private static final String UPDATE_PURCHASE = "" +
            "update jointbuy.Purchases pr set " +
            "    pr.telChatID = :telChatID, " +
            "    pr.telInlineMsgID = :telInlineMsgID, " +
            "    pr.purchaserID = :purchaserID, " +
            "    pr.name = :name, " +
            "    pr.amount = :amount, " +
            "    pr.purchaseDate = :purchaseDate, " +
            "    pr.active = :active " +
            "where pr.ID = :ID ";

    public Purchase updatePurchase(Purchase purchase) {

        DataSourceTransactionManager dwhtm = new DataSourceTransactionManager( jdbc.getDataSource() );
        TransactionTemplate dwhtt = new TransactionTemplate( dwhtm );
        dwhtt.setPropagationBehavior( TransactionDefinition.PROPAGATION_REQUIRES_NEW );

        dwhtt.execute( new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("ID", purchase.getID() )
                        .addValue("telChatID", purchase.getTelegramChatId() )
                        .addValue("telInlineMsgID", purchase.getInlineMsgId() )
                        .addValue("purchaserID", purchase.getPurchaser().getId())
                        .addValue("name", purchase.getName())
                        .addValue("amount", purchase.getSum())
                        .addValue("purchaseDate", purchase.getDate())
                        .addValue("active", purchase.isActive());

                npjdbc.update( UPDATE_PURCHASE, params );

                removeAllMembers(purchase.getID());
                addMembers(purchase.getID(), purchase.getMembers());
            }
        } );
        return purchase;
    }

    private static final String GET_PURCHASE_INLINE_MSG_FILTER = "" +
            "    and pr.telInlineMsgID = :inlineMsgId";

    public Purchase getPurchaseByInlineMsgId(String inlineMsgId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("inlineMsgId", inlineMsgId);

        String query = String.format( GET_PURCHASE, GET_PURCHASE_INLINE_MSG_FILTER );
        return npjdbc.queryForObject( query, params, PURCHASE_MEMBER);
    }

    private static final String REMOVE_PURCHASE = "" +
            "delete from jointbuy.Purchase where purchaseID = :purchaseID ";

    public void removePurchase(Purchase purchase) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("purchaseID", purchase.getID());

        removeAllMembers(purchase.getID());
        npjdbc.update(REMOVE_PURCHASE, params);
    }

    public List<Purchase> getActivePurchases(long chatId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("telChatID", chatId);

        String query = String.format( GET_PURCHASE, GET_ACTIVE_PURCHASE_CHATID_FILTER );
        return npjdbc.query( query, params, PURCHASE_MEMBER );

    }
}


