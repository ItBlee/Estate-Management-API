package com.itblee.service.impl;

import com.itblee.model.MyUser;
import com.itblee.repository.UserRepository;
import com.itblee.repository.entity.User;
import com.itblee.repository.sqlbuilder.SqlMap;
import com.itblee.repository.sqlbuilder.impl.LinkedSqlMap;
import com.itblee.repository.sqlbuilder.key.UserKey;
import com.itblee.util.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itblee.constant.SystemConstants.ACTIVE_STATUS;
import static com.itblee.constant.SystemConstants.SPRING_ROLE;

@Service(value = "userCustomService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Optional<User> findOneByUserNameAndStatus(String username, int status) {
        ValidateUtils.requireNonNull(username);
        SqlMap<UserKey> statements = new LinkedSqlMap<>();
        statements.addScope(UserKey.USERNAME_AND_STATUS);
        statements.put(UserKey.USERNAME, username);
        statements.put(UserKey.STATUS, status);
        List<User> results = userRepository.findByCondition(statements);
        return results.stream().findFirst();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = findOneByUserNameAndStatus(username, ACTIVE_STATUS)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<GrantedAuthority> authorities = userEntity.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(SPRING_ROLE + role.getCode()))
                .collect(Collectors.toList());

        UserDetails userDetails = MyUser.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(authorities)
                .build();

        MyUser myUserDetail = MyUser.of(userDetails);
        myUserDetail.setFullName(userEntity.getFullName());
        return myUserDetail;
    }
}
