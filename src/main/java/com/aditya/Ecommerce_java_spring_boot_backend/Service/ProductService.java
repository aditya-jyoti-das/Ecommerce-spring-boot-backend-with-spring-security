package com.aditya.Ecommerce_java_spring_boot_backend.Service;

import com.aditya.Ecommerce_java_spring_boot_backend.Model.Product;
import com.aditya.Ecommerce_java_spring_boot_backend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;


    public void addOrUpdateProduct(Product p)  {
        Product save = this.productRepo.save(p);
    }

    public List<Product> getProducts() {
        List<Product> all = this.productRepo.findAll();
        return all;
    }

    public Optional<Product> getProductById(int id) {

       return this.productRepo.findById(id);
    }

    public List<Product> searchByKeyword(String keyword) {
        return this.productRepo.findByKeyword(keyword);
    }

    public void deleteProduct(int id) {
        this.productRepo.deleteById(id);
    }
}
