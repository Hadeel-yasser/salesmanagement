package com.aiven.sales.salesmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClientService {

    @Autowired
    private ClientsRepository clientRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public List<Client> getAllClients(){
        return (List<Client>)clientRepository.findAll();
    }

    public Client getClient(Long id){
        Optional<Client> existingClient = clientRepository.findById(id);
            if (existingClient.isPresent()){
                Client existing = existingClient.get();
                return existing;
            }
            else{
                logger.warn("Couldn't find client with ID "+ id);
                return null;
            } 
    }

    public Client createClient(Client client){
        clientRepository.save(client);
        logger.info("Client with ID "+ client.getId()+" have been created successfully");
        return client;
    }
    public Client updateClient(Long id, Client client){
        try {
            client.setId(id);
            clientRepository.save(client);
            logger.info("Client with ID "+id+" have been updated successfully");
            return client;
        } catch (Exception e) {
            logger.error("An error occurred while updating the client with ID: "+ id, e);
            return null;
        }
    }
    public void deleteClient(Long id){
        Optional<Client> existingClient = clientRepository.findById(id);
            if (existingClient.isPresent()){
                clientRepository.deleteById(id);
                logger.info("Client with ID "+id+" deleted successfully!");
            }
        else{
            logger.warn("Couldn't delete non existing clinet with ID"+id);
        }
            
        
    }
}
