package com.guillermoorcajo.backendchallenge.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.guillermoorcajo.backendchallenge.dto.Basket;
import com.guillermoorcajo.backendchallenge.dto.BasketItem;
import com.guillermoorcajo.backendchallenge.dto.Product;
import com.guillermoorcajo.backendchallenge.enums.ProductCode;

@Repository
public class BasketH2JdbcTemplateDao implements BasketDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertNewBasket(String id)
            throws Exception {

        jdbcTemplate.update("INSERT INTO baskets(uuid, last_accessed) VALUES(?, CURRENT_TIMESTAMP())", id);
    }

    @Override
    public void insertProductIntoBasket(String uuid, ProductCode productCode)
            throws Exception {

        int basketId;

        try {
            String query = "SELECT id FROM baskets WHERE uuid = ?";
            basketId = jdbcTemplate.queryForObject(query, new Object[] { uuid }, Integer.class);
        }
        catch (EmptyResultDataAccessException erdae) {
            // Try-catch just for code clarity
            // (if no basket is into the DB, we must throw the exception to return a 404)
            throw erdae;
        }

        int newQuantity;

        try {
            String query = String.join("\n",
                                       "SELECT",
                                       "    products_in_baskets.quantity",
                                       "FROM",
                                       "    products_in_baskets",
                                       "LEFT JOIN baskets",
                                       "    ON products_in_baskets.basket_id = baskets.id",
                                       "LEFT JOIN products",
                                       "    ON products_in_baskets.product_id = products.id",
                                       "WHERE",
                                       "    baskets.uuid = ?",
                                       "    AND products.code = ?");

            newQuantity = jdbcTemplate.queryForObject(query,
                                                      new Object[] { uuid, productCode.toString() },
                                                      Integer.class);
            newQuantity += 1;
        }
        catch (EmptyResultDataAccessException erdae) {
            newQuantity = 1;
        }

        if (newQuantity == 1) {
            String query = String.join("\n",
                                       "INSERT INTO",
                                       "    products_in_baskets(basket_id, product_id, quantity)",
                                       "VALUES",
                                       "    (?, (SELECT id FROM products WHERE code = ?), ?)");

            jdbcTemplate.update(query, new Object[] { basketId, productCode.toString(), newQuantity });
        }
        else {
            String query = String.join("\n",
                                       "UPDATE",
                                       "    products_in_baskets",
                                       "SET",
                                       "    quantity = ?",
                                       "WHERE",
                                       "    basket_id = ?",
                                       "    AND product_id = (SELECT id FROM products WHERE code = ?)");

            jdbcTemplate.update(query, new Object[] { newQuantity, basketId, productCode.toString() });
        }
        
        String query = "UPDATE baskets SET last_accessed = CURRENT_TIMESTAMP() WHERE uuid = ?";
        basketId = jdbcTemplate.update(query, new Object[] { uuid });
    }

    @Override
    public Basket findBasketById(String uuid)
            throws Exception {

        String query = String.join("\n",
                                   "SELECT",
                                   "    baskets.id,",
                                   "    products.code,",
                                   "    products.name,",
                                   "    products.price_in_cents,",
                                   "    products_in_baskets.quantity,",
                                   "    pack_discounts.items_bought,",
                                   "    pack_discounts.free_items,",
                                   "    bulk_discounts.threshold,",
                                   "    bulk_discounts.new_price_in_cents",
                                   "FROM",
                                   "    products_in_baskets",
                                   "LEFT JOIN baskets",
                                   "    ON products_in_baskets.basket_id = baskets.id",
                                   "LEFT JOIN products",
                                   "    ON products_in_baskets.product_id = products.id",
                                   "LEFT JOIN pack_discounts",
                                   "    ON products_in_baskets.product_id = pack_discounts.product_id",
                                   "LEFT JOIN bulk_discounts",
                                   "    ON products_in_baskets.product_id = bulk_discounts.product_id",
                                   "WHERE",
                                   "    baskets.uuid = ?");

        List<BasketItem> items = jdbcTemplate.query(query, new Object[] { uuid }, new RowMapper<BasketItem>() {

            @Override
            public BasketItem mapRow(ResultSet rs, int rowNum)
                    throws SQLException {

                Product product = new Product();
                product.code = ProductCode.valueOf(rs.getString("code"));
                product.name = rs.getString("name");
                product.priceInCents = rs.getInt("price_in_cents");

                if ((rs.getInt("items_bought") != 0) && (rs.getInt("free_items") != 0)) {
                    product.packDiscount = product.new PackDiscount();
                    product.packDiscount.itemsBought = rs.getInt("items_bought");
                    product.packDiscount.freeItems = rs.getInt("free_items");
                }
                else {
                    product.packDiscount = null;
                }

                if ((rs.getInt("threshold") != 0) && (rs.getInt("new_price_in_cents") != 0)) {
                    product.bulkDiscount = product.new BulkDiscount();
                    product.bulkDiscount.threshold = rs.getInt("threshold");
                    product.bulkDiscount.newPriceInCents = rs.getInt("new_price_in_cents");
                }
                else {
                    product.bulkDiscount = null;
                }

                BasketItem item = new BasketItem();
                item.product = product;
                item.quantity = rs.getInt("quantity");

                return item;
            }
        });

        Basket basket = new Basket();
        basket.items = items;
        basket.id = uuid;

        return basket;
    }

    @Override
    public void deleteBasket(String uuid)
            throws Exception {
        
        jdbcTemplate.update("DELETE FROM products_in_baskets WHERE basket_id = (SELECT id FROM baskets WHERE uuid = ?)",
                            uuid);

        jdbcTemplate.update("DELETE FROM baskets WHERE uuid = ?", uuid);
    }

}
