package com.mydeveloperplanet.myjooqplanet.repository;

import static com.mydeveloperplanet.myjooqplanet.jooq.Tables.ORDER;
import static com.mydeveloperplanet.myjooqplanet.jooq.Tables.ORDER_ARTICLE;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import java.util.List;

import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderArticleIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderArticleOut;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderIn;
import com.mydeveloperplanet.myjooqplanet.repository.dto.OrderOut;

import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final DSLContext create;

    public OrderRepository(DSLContext create) {
        this.create = create;
    }

    public Integer addOrder(OrderIn order) {
        return create
                .insertInto(ORDER, ORDER.CUSTOMER_ID)
                .values(order.customerId())
                .returningResult(ORDER.ID)
                .fetchOne(Records.mapping(value -> value));
    }

    public void addArticles(Integer orderId, List<OrderArticleIn> articles) {
        for (OrderArticleIn orderArticle : articles) {
            create
                    .insertInto(ORDER_ARTICLE, ORDER_ARTICLE.ORDER_ID, ORDER_ARTICLE.ARTICLE_ID, ORDER_ARTICLE.NUMBER)
                    .values(orderId, orderArticle.articleId(), orderArticle.number())
                    .execute();
        }
    }

    public OrderOut getOrder(Integer orderId) {
        return create
                .select(
                    ORDER.CUSTOMER_ID,
                    multiset(
                            select(ORDER_ARTICLE.ARTICLE_ID, ORDER_ARTICLE.NUMBER)
                                    .from(ORDER_ARTICLE)
                                    .where(ORDER_ARTICLE.ORDER_ID.eq(ORDER.ID))
                    ).convertFrom(r -> r.map(Records.mapping(OrderArticleOut::new)))
                )
                .from(ORDER)
                .where(ORDER.ID.eq(orderId))
                .fetchOne(Records.mapping(OrderOut::new));
    }

    public List<Integer> getOrdersOfCustomer(Integer customerId) {
        return create
                .select(ORDER.ID)
                .from(ORDER)
                .where(ORDER.CUSTOMER_ID.eq(customerId))
                .fetch(Records.mapping(value -> value));
    }
}
