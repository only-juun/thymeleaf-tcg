package com.splab.splabassignment.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDSLConfig {

    @Bean
    fun jpaQueryFactory(entityManager: EntityManager) =
        JPAQueryFactory(entityManager)
}