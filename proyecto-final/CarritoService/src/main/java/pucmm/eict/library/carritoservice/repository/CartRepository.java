package pucmm.eict.library.carritoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.eict.library.carritoservice.model.CartItem;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
}