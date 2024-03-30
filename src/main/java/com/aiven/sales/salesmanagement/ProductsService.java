package com.aiven.sales.salesmanagement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductsService {


    @Autowired
    private ProductsRepository productsRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);

    public List<Product> getAllProducts(){
        return (List<Product>)productsRepository.findAll();
    }

    public Product getProduct(Long id){
        Optional<Product> existingProduct = productsRepository.findById(id);
            if (existingProduct.isPresent()){
                Product existing = existingProduct.get();
                return existing;
            }
            else{
                logger.warn("Couldn't find Product with ID "+ id);
                return null;
            } 
    }
    public Product createProduct(Product product){
        productsRepository.save(product);
        logger.info("Product with ID "+product.getId()+" have been created successfully");
        return product;
    }

    public Product updateProduct(Long id, Product product){
        try {
            product.setId(id);
            productsRepository.save(product);
            logger.info("Product with ID "+id+" have been updated successfully");
            return product;
        } catch (Exception e) {
            logger.error("An error occurred while updating the product with ID: "+ id, e);
            return null;
        }
    }

    public Product deleteProduct(Long id){
        try {
            Optional<Product> existingProduct = productsRepository.findById(id);
            if (existingProduct.isPresent()) 
            {
                Product product = existingProduct.get();
                if (product.getAvailableQuantity() >= 1) {
                    product.setAvailableQuantity(product.getAvailableQuantity() - 1);
                    productsRepository.save(product);
                    logger.info("Product with ID "+ id+ " have been deleted successfully");
                    return product;
                    } 
                    else {
                    logger.warn("Insufficient available quantity for product: " + product.getName());
                    return null;
                    }
            } 
            else
            {
            logger.warn("Product with ID " + id + " not found");
            return null;
            }
            
        } catch (Exception e) {
            logger.error("An error occured while reducing the inventory for the product with ID "+ id, e);
            return null;
        }
    }
}
