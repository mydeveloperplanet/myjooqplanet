package com.mydeveloperplanet.myjooqplanet.controller;

import com.mydeveloperplanet.myjooqplanet.repository.dto.ArticleIn;
import com.mydeveloperplanet.myjooqplanet.repository.ArticleRepository;
import com.mydeveloperplanet.myjooqplanet.api.ArticlesApi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController implements ArticlesApi {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ResponseEntity<Void> createArticle(com.mydeveloperplanet.myjooqplanet.model.Article apiArticle) {
        ArticleIn articleIn = new ArticleIn(apiArticle.getName());
        articleRepository.addArticle(articleIn);
        return ResponseEntity.ok().build();
    }
}
