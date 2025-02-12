package com.techacademy.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techacademy.entity.Employee;

public class UserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Employee employee;
    private final List<SimpleGrantedAuthority> authorities;

    public UserDetail(Employee employee) {
        this.employee = employee;

        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(employee.getRole().toString()));
        this.authorities = authorities;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // ロックされていなければtrueを返す
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // パスワードが期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 有効であればtrueを返す
        return true;
    }
}