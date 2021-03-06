package com.parking.models.security.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parking.models.security.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails, Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private String fullName;

    private String email;

    private String address;

    private String phoneNumber;

    private String gender;

    private String birthday;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal() {
    }

    public UserPrincipal(Integer id, String username, String password, String fullName, String email, String address,
                         String phoneNumber,String gender,String birthday, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender=gender;
        this.birthday=birthday;
        this.authorities = authorities;
    }

    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority (role.getRoleName().name())).collect(Collectors.toList()
        );
        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getEmail(),
                user.getBirthday(),
                user.getGender(),
                user.getAddress(),
                user.getPhone(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}

//package com.parking.models.security.utils;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.parking.models.security.user.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class UserPrincipal implements UserDetails, Serializable {
//
//    private static final Long serialVersionUID = 1L;
//
//    private Integer id;
//
//    private String username;
//
//    @JsonIgnore
//    private String password;
//
//    private String fullName;
//
//    private String email;
//
//    private String address;
//
//    private String phoneNumber;
//
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserPrincipal() {
//    }
//
//    public UserPrincipal(Integer id, String username, String password, String fullName, String email, String address,
//                         String phoneNumber, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.fullName = fullName;
//        this.email = email;
//        this.address = address;
//        this.phoneNumber = phoneNumber;
//        this.authorities = authorities;
//    }
//
//    public static UserPrincipal build(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
//                new SimpleGrantedAuthority (role.getRoleName().name())).collect(Collectors.toList()
//        );
//        return new UserPrincipal(
//                user.getUserId(),
//                user.getUsername(),
//                user.getPassword(),
//                user.getFullName(),
//                user.getEmail(),
//                user.getAddress(),
//                user.getPhone(),
//                authorities
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public static Long getSerialVersionUID() {
//        return serialVersionUID;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//
//    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
//}
//
