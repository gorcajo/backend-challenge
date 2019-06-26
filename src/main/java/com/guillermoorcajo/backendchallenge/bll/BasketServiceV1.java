package com.guillermoorcajo.backendchallenge.bll;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guillermoorcajo.backendchallenge.dal.BasketDao;
import com.guillermoorcajo.backendchallenge.dto.Basket;
import com.guillermoorcajo.backendchallenge.dto.BasketItem;
import com.guillermoorcajo.backendchallenge.enums.ProductCode;

@Service
public class BasketServiceV1 implements BasketService {

    private static final Logger LOG = LoggerFactory.getLogger(BasketServiceV1.class);

    @Autowired
    private BasketDao basketDao;

    @Override
    public String createBasket()
            throws Exception {

        String id = UUID.randomUUID().toString();
        
        LOG.debug("Creating basket with ID '" + id + "'...");
        basketDao.insertNewBasket(id);
        LOG.debug("... done");
        
        return id;
    }

    @Override
    public void addProductToBasket(String id, ProductCode productCode)
            throws Exception {

        LOG.debug("Inserting '" + productCode + "' into basket with ID '" + id + "'...");
        basketDao.insertProductIntoBasket(id, productCode);
        LOG.debug("... done");
    }

    @Override
    public double getTotalAmountInBasket(String id)
            throws Exception {

        LOG.debug("Getting basket with ID '" + id + "' from DB...");
        Basket basket = basketDao.findBasketById(id);
        
        LOG.debug("Calculating total amount...");

        int totalAmountInCents = 0;

        for (BasketItem item : basket.items) {
            if ((item.product.packDiscount == null) && (item.product.bulkDiscount == null)) {
                totalAmountInCents += item.quantity * item.product.priceInCents;
            }
            else if ((item.product.packDiscount != null) && (item.product.bulkDiscount == null)) {
                int quantityToCharge = 0;
                quantityToCharge += item.quantity / item.product.packDiscount.itemsBought;
                quantityToCharge += item.quantity % item.product.packDiscount.itemsBought;
                
                totalAmountInCents += quantityToCharge * item.product.priceInCents;
            }
            else if ((item.product.packDiscount == null) && (item.product.bulkDiscount != null)) {
                if (item.quantity >= item.product.bulkDiscount.threshold)
                    totalAmountInCents += item.quantity * item.product.bulkDiscount.newPriceInCents;
                else
                    totalAmountInCents += item.quantity * item.product.priceInCents;
            }
            else {
                throw new IllegalStateException("the product '" + item.product.code + "' has more than 1 discount");
            }
        }

        LOG.debug("... done, total amount in cents = " + totalAmountInCents);

        return ((double) totalAmountInCents) / 100;
    }

    @Override
    public void removeBasket(String id)
            throws Exception {

        LOG.debug("Deleting basket with ID '" + id + "'...");
        basketDao.deleteBasket(id);
        LOG.debug("... done");
    }
}
