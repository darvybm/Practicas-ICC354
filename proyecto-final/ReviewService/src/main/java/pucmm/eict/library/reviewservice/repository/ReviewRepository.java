package pucmm.eict.library.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.eict.library.reviewservice.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(String bookId);
    List<Review> findByUserId(String userId);
}