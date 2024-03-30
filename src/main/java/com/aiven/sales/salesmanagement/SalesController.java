package com.aiven.sales.salesmanagement;


import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/sales")
public class SalesController {

    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);

    @Autowired
    private SalesService salesService;

    @GetMapping("/view")
    public List<Sales> getAllSales() {
        
        return salesService.getAllSales();
        
    }

    @PostMapping("/add")
    public Sales createSale(@RequestBody Sales sale){
    
    logger.info("Request body"+sale.getClient().getId());
    //Extract client ID and product IDs
    Long clientId = sale.getClient().getId();
    List<Long> productIds = sale.getItems().stream()
                                      .map(item -> item.getProduct().getId())
                                      .collect(Collectors.toList());

        sale = salesService.createSale(sale,clientId,productIds);
        return sale;
    }

    @PutMapping("/{saleId}")
    public Sales updateSale(@PathVariable Long saleId, @RequestBody Sales updateData) {
        Sales updatedSale = salesService.updateSale(saleId, updateData);
        return updatedSale;
    }

    // @DeleteMapping("/{saleId}")
    // public Void deleteSale(@PathVariable Long saleId) {
    //     salesService.deleteSale(saleId);
    // }
    // @DeleteMapping
    // public void deleteAllSale() {
    //     salesService.deleteAll();;
    // }
}


