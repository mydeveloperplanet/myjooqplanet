package com.mydeveloperplanet.myjooqplanet.controller;

import java.util.ArrayList;
import java.util.List;

import com.mydeveloperplanet.myjooqplanet.api.CustomersApi;
import com.mydeveloperplanet.myjooqplanet.model.CustomerFullData;
import com.mydeveloperplanet.myjooqplanet.repository.AddressRepository;
import com.mydeveloperplanet.myjooqplanet.repository.CustomerRepository;
import com.mydeveloperplanet.myjooqplanet.repository.dto.AddressIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.AddressOut;
import com.mydeveloperplanet.myjooqplanet.repository.dto.CustomerIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.CustomerOut;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomersApi {

    public final CustomerRepository customerRepository;
    public final AddressRepository addressRepository;

    public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public ResponseEntity<Void> createCustomer(com.mydeveloperplanet.myjooqplanet.model.Customer apiCustomer) {
        CustomerIn customerIn = new CustomerIn(apiCustomer.getFirstName(), apiCustomer.getLastName(), apiCustomer.getCountry());
        Integer customerId = customerRepository.addCustomer(customerIn);

        AddressIn addressIn = new AddressIn(apiCustomer.getStreet(), apiCustomer.getCity());
        addressRepository.addAddress(addressIn, customerId);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CustomerFullData>> getCustomers() {
        List<CustomerOut> customers = customerRepository.getAllCustomers();

        List<CustomerFullData> convertedCustomers = convertToCustomerFullData(customers);
        return ResponseEntity.ok(convertedCustomers);
    }

    @Override
    public ResponseEntity<CustomerFullData> getCustomer(Long customerId) {
        CustomerOut customer = customerRepository.getCustomer(customerId.intValue());
        AddressOut address = addressRepository.getAddressByCustomer(customerId.intValue());
        return ResponseEntity.ok(repoToApi(customer, address));
    }

    private List<CustomerFullData> convertToCustomerFullData(List<CustomerOut> customers) {
        List<CustomerFullData> fullData = new ArrayList<>();
        for (CustomerOut customer : customers) {
            AddressOut address = addressRepository.getAddressByCustomer(customer.id());
            fullData.add(repoToApi(customer, address));
        }
        return fullData;
    }

    private CustomerFullData repoToApi(CustomerOut customer, AddressOut address) {
        CustomerFullData cfd = new CustomerFullData();
        cfd.setCustomerId(customer.id().longValue());
        cfd.setFirstName(customer.firstName());
        cfd.setLastName(customer.lastName());
        cfd.setCountry(customer.country());
        cfd.setStreet(address.street());
        cfd.setCity(address.city());
        return cfd;
    }

}
