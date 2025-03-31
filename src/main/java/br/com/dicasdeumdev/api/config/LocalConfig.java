package br.com.dicasdeumdev.api.config;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;


@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository userRepository;

    // Use @PostConstruct para executar a configuração na inicialização
    @PostConstruct
    public void startDb(){
        User u1 = new User(null, "nome", "email1@email.com", "123");
        User u2 = new User(null, "nome", "email2@email.com", "1232123");
        User u3 = new User(null, "nome", "email3@email.com", "456");



        userRepository.saveAll(List.of(u1, u2, u3));

    }
}
