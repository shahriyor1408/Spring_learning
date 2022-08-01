package uz.jl.init;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import uz.jl.configs.security.PasswordEncoder;
import uz.jl.domains.AuthUser;
import uz.jl.repository.UserRepository;

//@Component
public class Initializer implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        UserRepository userRepository = context.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        AuthUser authUser = AuthUser.builder()
                .username("shahriyor")
                .password(passwordEncoder.encode("123"))
                .role("ADMIN")
                .build();
        userRepository.save(authUser);
    }
}
