package com.guillermoorcajo.backendchallenge.bll;

import com.guillermoorcajo.backendchallenge.enums.ProductCode;

public interface BasketService {

    public String createBasket() throws Exception;
    public void addProductToBasket(String id, ProductCode productCode) throws Exception;
    public double getTotalAmountInBasket(String id) throws Exception;
    public void removeBasket(String id) throws Exception;
}
