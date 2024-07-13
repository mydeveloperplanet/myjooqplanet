package com.mydeveloperplanet.myjooqplanet;

import java.util.ArrayList;
import java.util.List;

import com.mydeveloperplanet.myjooqplanet.api.CustomersApi;
import com.mydeveloperplanet.myjooqplanet.jooq.tables.records.CustomerRecord;
import com.mydeveloperplanet.myjooqplanet.model.Customer;
import com.mydeveloperplanet.myjooqplanet.model.CustomerFullData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomersApi {

    public final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<Void> createCustomer(Customer apiCustomer) {
        com.mydeveloperplanet.myjooqplanet.Customer customer = new com.mydeveloperplanet.myjooqplanet.Customer();
        customer.setFirstName(apiCustomer.getFirstName());
        customer.setLastName(apiCustomer.getLastName());
        customer.setCountry(apiCustomer.getCountry());

        customerRepository.addCustomer(customer);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CustomerFullData>> getCustomers() {
        List<CustomerRecord> customers = customerRepository.getAllCustomers();

        List<CustomerFullData> convertedCustomers = convertToCustomerFullData(customers);
        return ResponseEntity.ok(convertedCustomers);
    }

    @Override
    public ResponseEntity<CustomerFullData> getCustomer(Long customerId) {
        CustomerRecord customer = customerRepository.getCustomer(customerId.intValue());
        return ResponseEntity.ok(repoToApi(customer));
    }

    private List<CustomerFullData> convertToCustomerFullData(List<CustomerRecord> customers) {
        List<CustomerFullData> fullData = new ArrayList<>();
        for (CustomerRecord customer : customers) {
            fullData.add(repoToApi(customer));
        }
        return fullData;
    }

    private CustomerFullData repoToApi(CustomerRecord customer) {
        CustomerFullData cfd = new CustomerFullData();
        cfd.setCustomerId(customer.getId().longValue());
        cfd.setFirstName(customer.getFirstName());
        cfd.setLastName(customer.getLastName());
        cfd.setCountry(customer.getCountry());
        return cfd;
    }

}
