package com.aiven.sales.salesmanagement;


import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;
    
    
    private Date creationDate;

    @ManyToOne
    private Client clientId;
    private String seller;
    private double total;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<SalesItem> items;


    public List<SalesItem> getItems() {
        return items;
    }
    public void setItems(List<SalesItem> items) {
        this.items = items;
    }
    public Long getOperationId() {
        return operationId;
    }
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Client getClient() {
        return clientId;
    }
    public void setClient(Client client) {
        this.clientId = client;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    
}
