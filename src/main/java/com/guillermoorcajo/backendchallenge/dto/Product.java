package com.guillermoorcajo.backendchallenge.dto;

import com.guillermoorcajo.backendchallenge.enums.ProductCode;

public class Product {

    public ProductCode code = null;
    public String name = null;
    public int priceInCents = -1;
    public PackDiscount packDiscount = null;
    public BulkDiscount bulkDiscount = null;
    
    public class PackDiscount {

        public int itemsBought = -1;
        public int freeItems = -1;
    }
    
    public class BulkDiscount {

        public int threshold = -1;
        public int newPriceInCents = -1;
    }
}
