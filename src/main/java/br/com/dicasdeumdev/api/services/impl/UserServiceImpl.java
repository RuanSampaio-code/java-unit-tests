package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDto;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.UserService;
import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository repository;

    @Override
    public User findById(Integer id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
    }

    public List<UserDto> findAll() {

        List<User> obj = repository.findAll();
        List<UserDto> listDto = obj.stream()
                .map(user ->mapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return listDto;
    }

    @Override
    public User create(UserDto userDto) {
        findByEmail(userDto);
        return repository.save(mapper.map(userDto, User.class));
    }


    @Override
    public User update(UserDto userDto) {
        findByEmail(userDto);
        return repository.save(mapper.map(userDto, User.class));
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        //delete por id
        repository.deleteById(id);
        //repository.delete(userDeleted);
    }

    private void findByEmail(UserDto obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if (user.isPresent() && !user.get().getId().equals(obj.getId())  ) {
            throw new DataIntegratyViolationException("Email ja cadastrado no sistema");
        }
    }

}
