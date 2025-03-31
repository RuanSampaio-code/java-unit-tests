package br.com.dicasdeumdev.api.services;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDto;

import java.util.List;

public interface UserService {

    User findById(Integer id);

    List<UserDto> findAll();

    User create(UserDto userDto);

    User update(UserDto userDto);

    void delete(Integer id);
}
