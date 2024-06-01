package pucmm.eict.library.carritoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pucmm.eict.library.carritoservice.model.CartItem;
import pucmm.eict.library.carritoservice.repository.CartRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<CartItem> getCartItems(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}