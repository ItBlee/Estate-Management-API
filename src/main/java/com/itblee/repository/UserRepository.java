package com.itblee.repository;

import com.itblee.repository.entity.User;

import java.util.List;
import java.util.Map;

public interface UserRepository extends GenericRepository<User, Long> {

	List<User> findByCondition(Map<?, ?> conditions);
	List<User> findByCondition(Map<?, ?> conditions, int limit, int offset);

}
