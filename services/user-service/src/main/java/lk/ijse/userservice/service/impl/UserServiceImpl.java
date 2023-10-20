package lk.ijse.userservice.service.impl;

import lk.ijse.userservice.dto.RequestDto;
import lk.ijse.userservice.dto.ResponseDto;
import lk.ijse.userservice.entity.Vehicle;
import lk.ijse.userservice.exception.AlreadyExistsException;
import lk.ijse.userservice.exception.NotFoundException;
import lk.ijse.userservice.repository.UserRepository;
import lk.ijse.userservice.service.UserService;
import lk.ijse.userservice.util.constants.Role;
import lk.ijse.userservice.util.mappers.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    private final RequestMapper requestMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RequestMapper requestMapper) {
        this.userRepository = userRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public void registerUser(RequestDto requestDto) {
        if (userRepository.findUserByUsername(requestDto.getUsername()) != null) {
            throw new AlreadyExistsException("Username already exists. Username : " + requestDto.getUsername());
        } else {
            userRepository.save(requestMapper.requestDtoToUser(requestDto));
        }
    }

    @Override
    public Long updateUser(RequestDto user) {
        Vehicle vehicleByUsername = userRepository.findUserByUsername(user.getUsername());
        if (vehicleByUsername == null) {
            throw new NotFoundException("User not found. Username : " + user.getUsername());
        } else {
            if (user.getPassword() != null) {
            } else {
                user.setPassword(vehicleByUsername.getPassword());
            }
            userRepository.save(requestMapper.requestDtoToUser(user));
            return user.getUserId();
        }
    }

    @Override
    public ResponseDto deleteUser(String username) {
        Vehicle vehicle = userRepository.findUserByUsername(username);
        if (vehicle == null) {
            throw new NotFoundException("User not found. Username : " + username);
        } else {
            userRepository.delete(vehicle);
            return requestMapper.userToResponseDto(vehicle);
        }
    }

    @Override
    public ResponseDto findUserByUsername(String username) {
        Vehicle vehicle = userRepository.findUserByUsername(username);
        if (vehicle == null) {
            throw new NotFoundException("User not found. Username : " + username);
        } else {
            return requestMapper.userToResponseDto(userRepository.findUserByUsername(username));
        }
    }

    @Override
    public ResponseDto findUserByEmail(String email) {
        Vehicle vehicle = userRepository.findUserByEmail(email);
        if (vehicle == null) {
            throw new NotFoundException("User not found. Email : " + email);
        } else {
            return requestMapper.userToResponseDto(vehicle);
        }
    }

    @Override
    public List<ResponseDto> findAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(requestMapper::userToResponseDto)
                .collect(toList());
    }

    @Override
    public List<ResponseDto> findUserByRole(Role role) {
        return userRepository
                .findUsersByRole(role.toString())
                .stream()
                .map(requestMapper::userToResponseDto)
                .collect(toList());
    }
}
