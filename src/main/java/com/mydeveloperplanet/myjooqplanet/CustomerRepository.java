package com.mydeveloperplanet.myjooqplanet;

import static com.mydeveloperplanet.myjooqplanet.jooq.tables.Customer.*;

import java.util.List;

import com.mydeveloperplanet.myjooqplanet.jooq.tables.records.CustomerRecord;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    private final DSLContext create;

    CustomerRepository(DSLContext create) {
        this.create = create;
    }

    public void addCustomer(final Customer customer) {
        create.insertInto(CUSTOMER, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.COUNTRY)
                .values(customer.getFirstName(), customer.getLastName(), customer.getCountry())
                .execute();
    }

    public CustomerRecord getCustomer(int customerId) {
        Result<?> result = create.select().from(CUSTOMER).where(CUSTOMER.ID.eq(customerId)).fetch();
        return (CustomerRecord) result.getFirst();
    }

    public List<CustomerRecord> getAllCustomers() {
        Result<CustomerRecord> records = create.selectFrom(CUSTOMER)
                .fetch();
        return records.stream().toList();
    }

}
