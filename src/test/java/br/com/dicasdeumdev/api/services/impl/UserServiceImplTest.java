package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDto;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceImplTest {

    public static final Integer ID      = 1;
    public static final String NOME     = "nome";
    public static final String EMAIL    = "email@email.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDto userDto;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnUserIntance() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());

        assertEquals(NOME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }
    @Test
    void whenFindByIdNotFoundThenReturnException() {

        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try {
            service.findById(ID);
        }catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, e.getMessage());
        }

    }

    @Test
    void  whenfindAllThenReturnListOfUsers() {

        when(repository.findAll()).thenReturn(List.of(user));
        List<User> response = repository.findAll();
        assertNotNull(response);

        //Assegurando tamanho da lista
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(NOME, response.get(0).getName());
        assertEquals(EMAIL, response.get(0).getEmail());
        assertEquals(PASSWORD, response.get(0).getPassword());

    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private void startUser(){
        user = new User(ID, NOME, EMAIL, PASSWORD);
        userDto = new UserDto(ID, NOME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NOME, EMAIL, PASSWORD));

    }
}