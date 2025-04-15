package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDto;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserServiceImplTest {

    public static final Integer ID      = 1;
    public static final String NOME     = "nome";
    public static final String EMAIL    = "email@email.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    public static final int INDEX = 0;

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
        assertEquals(User.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NOME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());

    }

    @Test
    void createUserWichsucess() {

        when(repository.save(any())).thenReturn(user);

        User response = service.create(userDto);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());

    }


    @Test
    void whenCreateUseThenReturnAnDataIntragrateViolation() {

        // Arrange
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        user.setId(30);
        // Act & Assert
        DataIntegratyViolationException exception = assertThrows(
                DataIntegratyViolationException.class,
                () -> service.create(userDto)
        );

        assertEquals("Email ja cadastrado no sistema", exception.getMessage());
        verify(repository, never()).save(any());

    }

    @Test
    void whenUpdateUserwithsucess() {

        when(repository.save(any())).thenReturn(user);

        User response = service.update(userDto);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenUpdateUserwithNotsucess() {

        when(repository.findByEmail(any())).thenReturn(Optional.of(user));

        user.setId(30);
        userDto.setEmail("joao@email.com");
        // Act & Assert
        DataIntegratyViolationException exception = assertThrows(
                DataIntegratyViolationException.class,
                () -> service.create(userDto)
        );

        assertEquals("Email ja cadastrado no sistema", exception.getMessage());
        verify(repository, never()).save(any());


    }


    @Test
    void deleteWithSucess() {

        when(repository.findById(anyInt())).thenReturn(optionalUser);
        doNothing().when(repository).deleteById(optionalUser.get().getId());
        service.delete(ID);
        verify(repository, times(1)).deleteById(optionalUser.get().getId());

    }

    @Test
    void deleteWithFailed() {

        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException("Usuário nao encontrado"));

        try {
            service.delete(ID);
        }catch (ObjectNotFoundException e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Usuário nao encontrado", e.getMessage());
        }

    }

    private void startUser(){
        user = new User(ID, NOME, EMAIL, PASSWORD);
        userDto = new UserDto(ID, NOME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NOME, EMAIL, PASSWORD));

    }
}