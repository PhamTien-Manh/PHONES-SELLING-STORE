package com.asm.java5.service;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.enums.Role;
import com.asm.java5.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class CustomerService implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    CookieService cookieService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByEmail(username);

        if(customer == null) throw new UsernameNotFoundException("Not manh");

        cookieService.add(SessionAttr.CUSTOMER,customer.getEmail(),10);

        return customer;
    }

    public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) throws UsernameNotFoundException {
        String email = oauth2.getPrincipal().getAttribute("email");
        String password = Long.toHexString(System.currentTimeMillis());
        String fullname = oauth2.getPrincipal().getAttribute("name");
        Customer customer = checkAccount(email, fullname, password);
//        cookieService.add(SessionAttr.CUSTOMER,customer.getEmail(),10);
        loadUserByUsername(customer.getEmail());
    }

    public Customer checkAccount(String email, String fullname, String password){
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null){
            return customer;
        }
        customer = new Customer();
        customer.setName(fullname);
        customer.setEmail(email);
        customer.setPassword(encoder.encode(password));
        customer.setRole(Role.CUSTOMER);
        return customerRepository.save(customer);
    }
}
