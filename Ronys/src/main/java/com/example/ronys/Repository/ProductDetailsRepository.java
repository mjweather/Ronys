package com.example.ronys.Repository;

import com.example.ronys.Model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

    @Query("SELECT pd FROM ProductDetails pd JOIN pd.product p WHERE p.id = ?1 AND (pd.size = ?2 AND pd.color=?3)")
    ProductDetails findByProductIdAndSize(Long productId, int size, String color);

    List<ProductDetails> findByProductId(Long productId);
    @Query("SELECT pd.id, pd.color, pd.size, pd.quantity, pd.product.id FROM ProductDetails pd")
    List<Object[]> findAllProductDetailsWithProductId();

    // Potentially avoid N+1 select with @EntityGraph or fetch keyword
    List<ProductDetails> findAllByProduct_Id(Long productId);
    @Query("SELECT pd FROM ProductDetails pd JOIN FETCH pd.product p JOIN p.supplier s WHERE s.name = :company")
    List<ProductDetails> findAllByProduct_Company(@Param("company") String company);


    @Query("SELECT pd FROM ProductDetails pd JOIN FETCH pd.product p WHERE p.section = :group")
    List<ProductDetails> findAllByProduct_Group(@Param("group") String group);

    @Query("SELECT pd FROM ProductDetails pd WHERE pd.product.id = :productId AND pd.size = :size AND pd.color = :color")
    List<ProductDetails> findByProductIdAndSizeAndColor(@Param("productId") Long productId, @Param("size") int size, @Param("color") String color);

}
