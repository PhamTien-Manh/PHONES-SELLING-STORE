package com.asm.java5.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(columnDefinition = "nvarchar(100) not null")
    @NotBlank(message = "*Vui lòng nhập tên")
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Positive(message = "*Đơn giá phải là số nguyên dương")
            private double unitPrice;

    @Transient
    public String getUnitPriceSt(){
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
        return nf.format(unitPrice);
    }
    @Transient
    public void setUnitPriceSt(String value) {
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
        try {
            unitPrice = nf.parse(value).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Column(length = 200)
    private String image;

    @Column(columnDefinition = "nvarchar(500) not null")
    @NotBlank(message = "*Không bỏ trống mô tả")
    private String description;

    @Column(nullable = false)
    @Digits(fraction = 0,message = "*Giảm giá phải là số dương", integer = 3)
    @Min(value = 0, message = "*Giảm giá phải lớn hơn 0%")
    @Max(value = 100, message = "*Giảm giá lớn nhất chỉ 100%")
    private double discount;

    @Temporal(TemporalType.DATE)
    private Date enteredDate;

    @Column(nullable = false)
    private short status;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;
}
