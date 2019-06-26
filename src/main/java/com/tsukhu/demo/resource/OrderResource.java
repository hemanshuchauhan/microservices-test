package com.tsukhu.demo.resource;

import com.github.javafaker.Faker;
import com.tsukhu.demo.com.service.OrderService;
import com.tsukhu.demo.domain.Address;
import com.tsukhu.demo.domain.Error;
import com.tsukhu.demo.domain.Order;
import com.tsukhu.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource {

    @Autowired
    private OrderService orderService;


    @RequestMapping(method = {RequestMethod.GET}, value = {"/order/{sku}"})
    public ResponseEntity<Object> getOrderBySKU(@PathVariable("sku")  String orderSKU) {
        if (!orderSKU.matches("^[A-Z]*[-]\\d+")) {
            return new ResponseEntity<>(new Error(100,"error", "Invalid SKU"), HttpStatus.BAD_REQUEST);
        }
        User user = orderService.fetchUserById(1);
        String billToState = Faker.instance().address().stateAbbr();
        String shipToState = Faker.instance().address().stateAbbr();

        Address billTo = user.getAddress();
        Address shipTo = user.getAddress();
        return new ResponseEntity<>(Order.builder()
                .billTo(billTo)
                .shipTo(shipTo)
                .name(user.getName())
                .sku(orderSKU).build(),HttpStatus.OK);
    }
}
