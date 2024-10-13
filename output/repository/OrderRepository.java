package com.lazy.devhub;

import org.springframework.data.jpa.repository.JpaRepository;
import java.io.Serializable;
import com.lazy.devhub.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Serializable> {
}
