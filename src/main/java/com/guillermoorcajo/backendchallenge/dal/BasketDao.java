package com.guillermoorcajo.backendchallenge.dal;

import com.guillermoorcajo.backendchallenge.dto.Basket;
import com.guillermoorcajo.backendchallenge.enums.ProductCode;

public interface BasketDao {

    public void insertNewBasket(String id) throws Exception;
    public void insertProductIntoBasket(String id, ProductCode productCode) throws Exception;
    public Basket findBasketById(String id) throws Exception;
    public void deleteBasket(String id) throws Exception;
}
