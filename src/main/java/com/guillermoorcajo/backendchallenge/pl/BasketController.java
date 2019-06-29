package com.guillermoorcajo.backendchallenge.pl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guillermoorcajo.backendchallenge.bll.BasketService;
import com.guillermoorcajo.backendchallenge.enums.ProductCode;

@RestController
@RequestMapping("/api/v1/basket")
public class BasketController {

    private static final Logger LOG = LoggerFactory.getLogger(BasketController.class);

    @Autowired
    private BasketService service;

    @PostMapping("")
    public Object createBasket() {
        try {
            String id = service.createBasket();
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        }
        catch (Exception e) {
            LOG.error("Exception happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("{id}")
    public Object addProductsToBasket(@PathVariable("id") String id, @RequestBody String requestBody) {
        try {
            ProductCode productCode;
            
            try {
                UUID.fromString(id);
                productCode = ProductCode.valueOf(requestBody);
            }
            catch (IllegalArgumentException iae) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            
            service.addProductToBasket(id, productCode);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        catch (EmptyResultDataAccessException erdae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            LOG.error("Exception happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("{id}/totalamount")
    public Object getBasketTotalAmount(@PathVariable("id") String id) {
        try {
            try {
                UUID.fromString(id);
            }
            catch (IllegalArgumentException iae) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            
            double totalAmount = service.getTotalAmountInBasket(id);
            return ResponseEntity.status(HttpStatus.OK).body(totalAmount);
        }
        catch (EmptyResultDataAccessException erdae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            LOG.error("Exception happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("{id}")
    public Object deleteBasket(@PathVariable("id") String id) {
        try {
            try {
                UUID.fromString(id);
            }
            catch (IllegalArgumentException iae) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            
            service.removeBasket(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        catch (EmptyResultDataAccessException erdae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            LOG.error("Exception happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
