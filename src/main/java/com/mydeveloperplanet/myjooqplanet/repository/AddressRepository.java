package com.mydeveloperplanet.myjooqplanet.repository;

import static com.mydeveloperplanet.myjooqplanet.jooq.Tables.ADDRESS;

import com.mydeveloperplanet.myjooqplanet.repository.dto.AddressIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.AddressOut;

import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepository {

    private final DSLContext create;

    AddressRepository(DSLContext create) {
        this.create = create;
    }

    public void addAddress(AddressIn addressIn, Integer customerId) {
        create
                .insertInto(ADDRESS, ADDRESS.STREET, ADDRESS.CITY, ADDRESS.CUSTOMER_ID)
                .values(addressIn.street(), addressIn.city(), customerId)
                .execute();
    }

    public AddressOut getAddressByCustomer(int customerId) {
        return create
                .select(ADDRESS.STREET, ADDRESS.CITY)
                .from(ADDRESS)
                .where(ADDRESS.CUSTOMER_ID.eq(customerId))
                .fetchOne(Records.mapping(AddressOut::new));
    }
}
