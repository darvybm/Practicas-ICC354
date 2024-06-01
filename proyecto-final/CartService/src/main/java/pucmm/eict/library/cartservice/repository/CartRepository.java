package pucmm.eict.library.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.eict.library.cartservice.model.CartItem;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
}