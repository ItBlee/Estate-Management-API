package com.itblee.repository.sqlbuilder.key;

import com.itblee.repository.sqlbuilder.SqlKey;
import com.itblee.repository.sqlbuilder.SqlStatement;
import com.itblee.repository.sqlbuilder.model.*;
import com.itblee.util.StringUtils;

public enum UserKey implements SqlKey {

    USERNAME ("name", String.class, SqlQuery.builder()
            .select("user.username")
            .from("user")
            .where("user.username").build()
    ),

    STATUS ("status", Integer.class, SqlQuery.builder()
            .select("user.status")
            .from("user")
            .where("user.status").build()
    ),

    //SCOPE
    USERNAME_AND_STATUS (SqlQuery.builder()
        .select("user.id",
                "user.username",
                "user.password",
                "user.fullname",
                "user.status",
                "role.id AS \"role.Id\"",
                "role.code AS \"role.Code\""
        )
        .from("user")
        .joinWith(
                SqlJoin.builder()
            .type(SqlJoin.Type.LEFT_JOIN)
            .join("user_role")
            .on("user.id = user_role.userid").done(),

                SqlJoin.builder()
            .type(SqlJoin.Type.LEFT_JOIN)
            .join("role")
            .on("role.id = user_role.roleid").done()
        ).build()
    );

    private final String param;
    private final Class<?> fieldType;
    private final SqlStatement statement;

    UserKey(SqlStatement statement) {
        if (statement == null)
            throw new IllegalArgumentException();
        this.param = "";
        this.fieldType = Object.class;
        this.statement = statement;
    }

    UserKey(String param, Class<?> fieldType, SqlStatement statement) {
        StringUtils.requireNonBlank(param);
        if (fieldType == null)
            throw new IllegalArgumentException();
        this.param = param;
        this.fieldType = fieldType;
        this.statement = statement;
    }

    @Override
    public SqlStatement getStatement() {
        return statement;
    }

    @Override
    public String getParamName() {
        return param;
    }

    @Override
    public Class<?> getType() {
        return fieldType;
    }

    @Override
    public boolean isScope() {
        return StringUtils.isBlank(getParamName())
                && getType() == Object.class
                && statement != null;
    }

}
