package com.mydeveloperplanet.myjooqplanet.repository;

import static com.mydeveloperplanet.myjooqplanet.jooq.tables.Customer.CUSTOMER;

import java.util.List;

import com.mydeveloperplanet.myjooqplanet.repository.dto.CustomerIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.CustomerOut;

import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    private final DSLContext create;

    CustomerRepository(DSLContext create) {
        this.create = create;
    }

    public Integer addCustomer(CustomerIn customerIn) {
        return create
                .insertInto(CUSTOMER, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME)
                .values(customerIn.firstName(), customerIn.lastName())
                .returningResult(CUSTOMER.ID)
                .fetchOne(Records.mapping(value -> value));
    }

    public CustomerOut getCustomer(int customerId) {
        return create
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOne(Records.mapping(CustomerOut::new));
    }

    public List<CustomerOut> getAllCustomers() {
        return create
                .selectFrom(CUSTOMER)
                .fetch(Records.mapping(CustomerOut::new));
    }

}
