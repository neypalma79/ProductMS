package com.angular.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angular.model.Product;
import com.angular.model.ProductResponse;
import com.angular.repositories.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;
   
	@GetMapping("/products")
	public ResponseEntity<?> getProducts() {
		List<Product> products=productRepository.findAll();	
		
		List<Product> finalProducts=new ArrayList<>();
		
		for(Product product:products) {
			if(product.getStatus()=='A') {
				finalProducts.add(product);
			}
		}
		
		return new ResponseEntity<>(finalProducts,HttpStatus.OK);
	}
	
	@PostMapping("/products")
	public ResponseEntity<?> saveProduct(@RequestBody Product productBody) {
		ProductResponse productResponse=new ProductResponse();
		boolean productExists=false;
		
		System.out.println("producto " + productBody);
	
		List<Product> products= productRepository.findAll();
		
		for(Product productBD:products) {
			if(productBD.getBrand().equals(productBody.getBrand()) 
					&& productBD.getName().equals(productBody.getName()) 
					&& productBD.getDescription().equals(productBody.getDescription())) {
				productExists=true;
				break;
			}
		}
		
		
		if(productExists) {
			productResponse.setCode("21");
			productResponse.setData("Producto ya existe");
		} else {		
			if(productRepository.save(productBody)!=null) {
				productResponse.setCode("00");
				productResponse.setData("Producto grabado correctamente");
			} else {
				productResponse.setCode("99");
				productResponse.setData("Error al grabar el producto");
			
			}
		}
			
		return new ResponseEntity<>(productResponse,HttpStatus.OK);	
	}
}
