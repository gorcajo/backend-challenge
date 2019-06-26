package com.guillermoorcajo.backendchallenge.unittests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.guillermoorcajo.backendchallenge.bll.BasketServiceV1;
import com.guillermoorcajo.backendchallenge.dal.BasketDao;
import com.guillermoorcajo.backendchallenge.dto.Basket;
import com.guillermoorcajo.backendchallenge.dto.BasketItem;
import com.guillermoorcajo.backendchallenge.dto.Product;
import com.guillermoorcajo.backendchallenge.enums.ProductCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasketServiceV1Test {

    @Mock
    BasketDao basketDao;

    @InjectMocks
    BasketServiceV1 service;
    
    private Product voucher;
    private Product tShirt;
    private Product mug;
    
    @Before
    public void init() {
        voucher = new Product();
        voucher.code = ProductCode.VOUCHER;
        voucher.name = "Cabify Voucher";
        voucher.priceInCents = 500;
        voucher.packDiscount = voucher.new PackDiscount();
        voucher.packDiscount.itemsBought = 2;
        voucher.packDiscount.freeItems = 1;
        
        tShirt = new Product();
        tShirt.code = ProductCode.TSHIRT;
        tShirt.name = "Cabify T-Shirt";
        tShirt.priceInCents = 2000;
        tShirt.bulkDiscount = tShirt.new BulkDiscount();
        tShirt.bulkDiscount.threshold = 3;
        tShirt.bulkDiscount.newPriceInCents = 1900;
        
        mug = new Product();
        mug.code = ProductCode.MUG;
        mug.name = "Cabify Coffee Mug";
        mug.priceInCents = 750;
    }

    @Test
    public void addProductToBasket() {
        // TODO
    }

    @Test
    public void getTotalAmountInBasketExample1()
            throws Exception {

        Basket basket = new Basket();
        basket.id = "1";
        basket.items = new ArrayList<>();
        
        BasketItem item1 = new BasketItem();
        item1.product = voucher;
        item1.quantity = 1;
        basket.items.add(item1);

        BasketItem item2 = new BasketItem();
        item2.product = tShirt;
        item2.quantity = 1;
        basket.items.add(item2);

        BasketItem item3 = new BasketItem();
        item3.product = mug;
        item3.quantity = 1;
        basket.items.add(item3);
        
        when(basketDao.findBasketById(any())).thenReturn(basket);
        
        assertEquals(32.50, service.getTotalAmountInBasket(UUID.randomUUID().toString()), 0.001);
    }

    @Test
    public void getTotalAmountInBasketExample2()
            throws Exception {

        Basket basket = new Basket();
        basket.id = "2";
        basket.items = new ArrayList<>();
        
        BasketItem item1 = new BasketItem();
        item1.product = voucher;
        item1.quantity = 2;
        basket.items.add(item1);

        BasketItem item2 = new BasketItem();
        item2.product = tShirt;
        item2.quantity = 1;
        basket.items.add(item2);
        
        when(basketDao.findBasketById(any())).thenReturn(basket);
        
        assertEquals(25.00, service.getTotalAmountInBasket(UUID.randomUUID().toString()), 0.001);
    }

    @Test
    public void getTotalAmountInBasketExample3()
            throws Exception {

        Basket basket = new Basket();
        basket.id = "3";
        basket.items = new ArrayList<>();
        
        BasketItem item1 = new BasketItem();
        item1.product = voucher;
        item1.quantity = 1;
        basket.items.add(item1);

        BasketItem item2 = new BasketItem();
        item2.product = tShirt;
        item2.quantity = 4;
        basket.items.add(item2);
        
        when(basketDao.findBasketById(any())).thenReturn(basket);
        
        assertEquals(81.00, service.getTotalAmountInBasket(UUID.randomUUID().toString()), 0.001);
    }

    @Test
    public void getTotalAmountInBasketExample4()
            throws Exception {

        Basket basket = new Basket();
        basket.id = "4";
        basket.items = new ArrayList<>();
        
        BasketItem item1 = new BasketItem();
        item1.product = voucher;
        item1.quantity = 3;
        basket.items.add(item1);

        BasketItem item2 = new BasketItem();
        item2.product = tShirt;
        item2.quantity = 3;
        basket.items.add(item2);

        BasketItem item3 = new BasketItem();
        item3.product = mug;
        item3.quantity = 1;
        basket.items.add(item3);
        
        when(basketDao.findBasketById(any())).thenReturn(basket);
        
        assertEquals(74.50, service.getTotalAmountInBasket(UUID.randomUUID().toString()), 0.001);
    }

    @Test
    public void removeBasket() {
        // TODO
    }
}
