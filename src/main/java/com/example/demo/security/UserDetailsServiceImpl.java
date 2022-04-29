package com.example.demo.security;

import com.example.demo.categories.data.user.JpaUserRepository;
import com.example.demo.categories.data.user.UserDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JpaUserRepository jpaUserRepository;


    /** Внутрений метод фильтров Security (JWTAuthenticationFilter /attemptAuthentication)
     *  для проверки существования пользователя в БД */
    @Override
    public UserDetails loadUserByUsername(String telephoneNumber) throws UsernameNotFoundException {
        UserDataMapper user = jpaUserRepository.findByTelephoneNumber(telephoneNumber);

        // Если пользователя не существует или он не подтвердил свой телефонный номер
        if(user == null || !user.getVerification()){
            throw new UsernameNotFoundException(telephoneNumber);
        }

        //Херобора для превращения нашего листа с ролями в Collection <? extends GrantedAuthority>
        //Взял от сюда: https://www.youtube.com/watch?v=m5FAo5Oa6ag&t=3818s время 30:20
        List<GrantedAuthority> authorities = user.getRoleDataMappers().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());

        return new User(user.getTelephoneNumber(), user.getPassword(), authorities);
    }



}
