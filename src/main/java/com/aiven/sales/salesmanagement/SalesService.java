package com.aiven.sales.salesmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesService {
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    @Autowired SalesItemRepository salesItemRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ClientService clientService;

    // @Autowired
    // private ProductsService productService;

    public List<Sales> getAllSales() {
        return (List<Sales>)salesRepository.findAll();
    }

    public Sales createSale(Sales sale, Long clientId, List<Long> productIds) {

        // Fetch client and product objects for validation and price calculation
        Optional<Client> existingClient = clientsRepository.findById(clientId);
        if (existingClient.isPresent()){
            sale.setClient(clientService.getClient(clientId));
        }
        else{
            logger.warn("Client with ID "+clientId+ " Doesn't exist");
        }
        
        // check if products exists
        List<SalesItem> newItems = new ArrayList<>();
        for(Long productID: productIds){
            Optional<Product> existingProduct = productsRepository.findById(productID);
            if (existingProduct.isPresent()){
                Product product = existingProduct.get();
                // Find the corresponding SalesItem and set the product on it
                for (SalesItem item : sale.getItems()) {
                    if (item.getProduct().getId().equals(productID)) {
                        item.setProduct(product);
                        item.setTotalPrice(item.getQuantity() * product.getPrice());
                        salesItemRepository.save(item);
                        newItems.add(item);
                        break; // Move to the next product ID
                    }
                }
                sale.setItems(newItems);
            } else {
                logger.warn("Product with ID " + productID + " not found");
            }
                
            
        }
        // Calculate total sale amount 
        double totalSaleAmount = 0.0;
        for (SalesItem item : sale.getItems()) {
            totalSaleAmount += item.getTotalPrice();
        }
         sale.setTotal(totalSaleAmount); 

        return salesRepository.save(sale);
    }


    public Sales updateSale(Long saleId, Sales updateData) {
        // Fetch existing sale
        Optional<Sales> existingSale = salesRepository.findById(saleId);
            if (existingSale.isPresent()){
                Sales existing = existingSale.get();
               // existing.setSeller(updateData.getSeller());
    
                for (SalesItem existingItem : existing.getItems()) {
                    Long existingItemId = existingItem.getId();
                    SalesItem updateItem = updateData.getItems().stream()
                            .filter(item -> item.getId().equals(existingItemId))
                            .findFirst().orElse(null);
        
                    if (updateItem != null) {
                        int newQuantity = existingItem.getQuantity() + updateItem.getQuantity();
                        existingItem.setQuantity(newQuantity);
                        existingItem.setTotalPrice(newQuantity * existingItem.getProduct().getPrice()); // Recalculate total price
                    }
                }
        
                // Recalculate total sale amount (optional)
                double totalSaleAmount = 0.0;
                for (SalesItem item : existing.getItems()) {
                    totalSaleAmount += item.getTotalPrice();
                }
                existing.setTotal(totalSaleAmount);
        
                return salesRepository.save(existing);
            }

        else{
            logger.warn("Couldn't update sale operation with ID "+ saleId);
            return null;
        }
        
    }
    // public void deleteAll(){
    //     salesRepository.deleteAll();
    // }
}


