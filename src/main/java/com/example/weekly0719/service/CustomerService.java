package com.example.weekly0719.service;

import com.example.weekly0719.repository.CustomerRepository;
import com.example.weekly0719.model.Customer;
import com.example.weekly0719.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = CustomerDTO.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDTO.fromEntity(savedCustomer);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id).map(CustomerDTO::fromEntity);
    }

    public Optional<CustomerDTO> updateCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(o -> {
            o.setName(customerDTO.getName());
            o.setPhoneNumber(customerDTO.getPhoneNumber());
            o.setAddress(customerDTO.getAddress());
            return CustomerDTO.fromEntity(customerRepository.save(o));
        });
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
