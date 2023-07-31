package com.asm.java5.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Data
@NoArgsConstructor
public class OrderAdmin implements Serializable{
    @Id
    private int id;
    private Long countOrder;
    private Date date;
    private String customer;
    private double total;

    public OrderAdmin(int id, Long countOrder, Date date, String customer, double total) {
        this.id = id;
        this.countOrder = countOrder;
        this.date = date;
        this.customer = customer;
        this.total = total;
    }

}
