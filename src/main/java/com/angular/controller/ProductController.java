package com.angular.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angular.model.Product;
import com.angular.model.ProductResponse;
import com.angular.repositories.ProductRepository;
import com.google.gson.Gson;

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
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
		ProductResponse productResponse=new ProductResponse();
		
		System.out.println("Id a eliminar: " + id);
		
		Optional<Product> productById=productRepository.findById(id);
		
		if(productById.isPresent()) {
			if(productById.get().getStatus()=='I') {
				productResponse.setCode("21");
				productResponse.setData("Producto con id " + id + " ya ha sido eliminado");
			} else {
			System.out.println("Existe el producto con id " + id);
			productById.get().setStatus('I');
			productRepository.save(productById.get());
			productResponse.setCode("00");
			productResponse.setData("Producto eliminado correctamente");
			}
		} else {
			System.out.println("No existe el producto con id " + id);
			productResponse.setCode("99");
			productResponse.setData("Error al eliminar el producto");
		}
		
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<?> listProductById(@PathVariable Integer id) {
		ProductResponse productResponse=new ProductResponse();
	
		System.out.println("Id a buscar: " + id);
		
		Optional<Product> productById=productRepository.findById(id);
		
		if(productById.isPresent()) {
			productResponse.setCode("00");
			productResponse.setData(new Gson().toJson(productById.get()));		
		} else {
			System.out.println("No existe el producto con id " + id);
			productResponse.setCode("99");
			productResponse.setData("Error al encontrar producto: id " +  id + " no existe");
		}
		
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	@PutMapping("/products")
	public ResponseEntity<?> updateProduct(@RequestBody Product productBody) {
		ProductResponse productResponse=new ProductResponse();
		
		if(productBody.getId()==0) {
			System.out.println("Error al actualizar producto con id " + productBody.getId());
			productResponse.setCode("99");
			productResponse.setData("Error al actualizar producto con id " + productBody.getId());
		} else {
			if(productRepository.save(productBody)!=null) {
				productResponse.setCode("00");
				productResponse.setData("Actualización de producto con id " + productBody.getId() + " exitosa");		
			} else {
				System.out.println("Error al actualizar producto con id " + productBody.getId());
				productResponse.setCode("99");
				productResponse.setData("Error al actualizar producto con id " + productBody.getId());
			}
		}
		
		return new ResponseEntity<>(productResponse,HttpStatus.OK);	
	}
}
