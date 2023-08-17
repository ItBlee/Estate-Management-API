package com.itblee.repository.impl;

import com.itblee.exception.ErrorRepositoryException;
import com.itblee.repository.BuildingRepository;
import com.itblee.repository.entity.Building;
import com.itblee.repository.sqlbuilder.SqlBuilder;
import com.itblee.repository.sqlbuilder.SqlExecutor;
import com.itblee.repository.sqlbuilder.SqlMap;
import com.itblee.repository.sqlbuilder.impl.SqlBuilderFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    @Override
    public List<Building> findByCondition(Map<?, ?> conditions) {
        return findByCondition(conditions, -1, -1);
    }

    @Override
    public List<Building> findByCondition(Map<?, ?> conditions, int limit, int offset) {
        SqlMap<?> statements = (SqlMap<?>) conditions;
        SqlBuilderFactory factory = SqlBuilderFactory.newInstance("query", statements);
        SqlBuilder builder = factory.getBuilder();
        SqlExecutor executor = factory.getExecutor();
        //builder.limit(limit);
        //builder.offset(offset);
        try {
            String sql = builder.build();
            return executor.executeQuery(sql, Building.class);
        } catch (SQLSyntaxErrorException e) {
            throw new ErrorRepositoryException(e);
        }
    }

}
