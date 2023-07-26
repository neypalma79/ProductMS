package com.angular.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angular.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
