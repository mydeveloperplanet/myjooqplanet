package com.mydeveloperplanet.myjooqplanet.repository;

import static com.mydeveloperplanet.myjooqplanet.jooq.tables.Article.ARTICLE;

import com.mydeveloperplanet.myjooqplanet.repository.dto.ArticleIn;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository {

    private final DSLContext create;

    public ArticleRepository(DSLContext create) {
        this.create = create;
    }

    public void addArticle(ArticleIn articleIn) {
        create
                .insertInto(ARTICLE, ARTICLE.NAME)
                .values(articleIn.name())
                .execute();
    }
}
