package org.example.orderprocessing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.orderprocessing.model.dto.ProductsApiResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;
    private String name;
    private double price;
    private String description;
    private String category;
    private int stockQuantity;
    private String imageUrl;
    private double weight;
    private String dimensions;

    public static Product getProduct(ProductsApiResponse productApiResponse) {
      Product product = new Product();
      product.setId(String.valueOf(productApiResponse.getId()));
      product.setName(productApiResponse.getDescription());
      product.setPrice(productApiResponse.getPrice());
      product.setCategory(productApiResponse.getCategory());
      return product;
    }
}