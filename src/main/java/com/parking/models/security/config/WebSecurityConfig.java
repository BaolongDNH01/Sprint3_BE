package com.parking.models.security.config;


import com.parking.models.DAO.Rank;
import com.parking.models.security.constant.ERoleName;
import com.parking.models.security.user.User;
import com.parking.models.security.utils.Role;
import com.parking.repositories.RankRepository;
import com.parking.repositories.RoleRepository;
import com.parking.repositories.UserRepository;
import com.parking.services.security.JwtEntryPoint;
import com.parking.services.security.JwtFilter;
import com.parking.services.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //config in application.properties
    @Value("${app.admin.username}")
    private String adminUsername;
    //config in application.properties
    @Value("${app.admin.password}")
    private String adminPassword;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RankRepository rankRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter authenticationFilter() {
        return new JwtFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    ApplicationRunner init(RoleRepository roleRepository, UserRepository userRepository) {
        System.out.println("Created !");


        // create role and user in database
        return args -> {

            if (roleRepository.findAll().isEmpty()) {

                Role adminRole = new Role(ERoleName.ROLE_ADMIN);
                adminRole.setId(1);

                Role memberRole = new Role(ERoleName.ROLE_MEMBER);
                memberRole.setId(2);

                roleRepository.save(adminRole);
                roleRepository.save(memberRole);

                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByRoleName(ERoleName.ROLE_ADMIN).orElseThrow(
                        () -> new RuntimeException("Role doesn't exist")
                ));

                String[] ranks = {"Nhân viên giữ xe", "Bảo vệ", "Nhân viên bảo trì"};

                for (String s : ranks) {
                    Rank rank = new Rank();
                    rank.setRankName(s);
                    rankRepository.save(rank);
                }


                User admin = new User();

                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setBirthday("2020-10-30");
                admin.setFullName("ADMIN");
                admin.setEmail("admin@gmail.com");
                admin.setGender("Nam");
                admin.setAddress("Da Nang");
                admin.setPhone("0123456799");
                admin.setRoles(roles);
                admin.setRank(rankRepository.findById(1).orElse(null));
                userRepository.save(admin);

                Set<Role> rolesForMember = new HashSet<>();
                rolesForMember.add(roleRepository.findByRoleName(ERoleName.ROLE_MEMBER).orElseThrow(
                        () -> new RuntimeException("Role doesn't exist")
                ));


                User member = new User();
                member.setUsername("member@gmail.com");
                member.setPassword(passwordEncoder.encode("123123"));
                member.setFullName("MEMBER");
                member.setBirthday("2020-10-30");
                member.setEmail("member@gmail.com");
                member.setAddress("Da Nang");
                member.setPhone("0123456799");
                member.setGender("Nam");
                member.setRoles(rolesForMember);
                member.setRank(rankRepository.findById(1).orElse(null));
                userRepository.save(member);
            }
        };
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(
                        authenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );
    }
}
//package com.parking.models.security.config;
//
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//}
