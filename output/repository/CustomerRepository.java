package com.lazy.devhub;

import org.springframework.data.jpa.repository.JpaRepository;
import java.io.Serializable;
import com.lazy.devhub.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Serializable> {
}
