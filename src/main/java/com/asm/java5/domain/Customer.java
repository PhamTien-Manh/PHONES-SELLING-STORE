package com.asm.java5.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(columnDefinition = "nvarchar(50) not null")
    @NotBlank(message = "*Vui lòng nhập tên người dùng")
    private String name;

    @Column(columnDefinition = "nvarchar(100) not null")
    @NotEmpty(message = "*Không để trống email")
    @Email
    private String email;

    @Column(length = 20, nullable = false)
    @Size(max = 20, message = "*Mật khẩu tối đa 20 kí tự")
    @NotBlank(message = "*Vui lòng nhập mật khẩu")
    private String password;


    @Column(length = 20)
    @Size(max = 10, min = 10, message = "*Số điện thoại chỉ nhận 10 số")
    @NotBlank(message = "*Vui lòng nhập số điện thoại")
    private String phone;
    @Temporal(TemporalType.DATE)
    private Date registeredDate;
    @Column(nullable = false)
    private short status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders;
}
