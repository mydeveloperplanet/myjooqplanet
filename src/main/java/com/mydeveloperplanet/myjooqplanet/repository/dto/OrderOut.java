package com.mydeveloperplanet.myjooqplanet.repository.dto;

import java.util.List;

public record OrderOut(Integer customerId, List<OrderArticleOut> articles) {
}
