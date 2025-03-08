package com.aditya.Ecommerce_java_spring_boot_backend.Controller;

import com.aditya.Ecommerce_java_spring_boot_backend.Model.Product;
import com.aditya.Ecommerce_java_spring_boot_backend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    public ProductService prodServ;

    @PutMapping("/api/product/{id}")
    public ResponseEntity<String> UpdateProduct(@PathVariable int id,  @RequestPart Product product, @RequestPart MultipartFile imageFile)  {
        try{
            Optional<Product> productById = this.prodServ.getProductById(id);
            productById.orElseThrow();
            product.setImageType(imageFile.getContentType());
            product.setImageName(imageFile.getName());
            product.setImageData(imageFile.getBytes());
            this.prodServ.addOrUpdateProduct(product);
            return new ResponseEntity<String>("Successfully data Updated", HttpStatus.ACCEPTED);

        }
        catch (NoSuchElementException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IOException  e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/api/product")
    public ResponseEntity<String> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile)  {
        try{
            product.setImageType(imageFile.getContentType());
            product.setImageName(imageFile.getName());
            product.setImageData(imageFile.getBytes());
            this.prodServ.addOrUpdateProduct(product);
            return new ResponseEntity<String>("Successfully data accepted", HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("api/products")
    public ResponseEntity<?> GetProducts(){
        try{

            List<Product> products = this.prodServ.getProducts();
            return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("api/product/{productId}/image")
    public ResponseEntity<?> GetProductImageById(@PathVariable int productId){
        try{
            Optional<Product> productById = this.prodServ.getProductById(productId);
            Product product = productById.orElseThrow();
            return new ResponseEntity<byte[]>(product.getImageData(),HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("api/product/{id}")
    public ResponseEntity<?> GetProductById(@PathVariable int id){
        try{
            Optional<Product> productById = this.prodServ.getProductById(id);
            Product product = productById.orElseThrow();
            return new ResponseEntity<Product>(product,HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("api/products/search")
    public ResponseEntity<List<Product>> GetSearchByKeyword(@RequestParam String keyword){

       List<Product> products= this.prodServ.searchByKeyword(keyword);
       return new ResponseEntity<>(products,HttpStatus.OK);
    }


    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Optional<Product> product = this.prodServ.getProductById(id);
        try {
            this.prodServ.getProductById(id).orElseThrow();
            this.prodServ.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
