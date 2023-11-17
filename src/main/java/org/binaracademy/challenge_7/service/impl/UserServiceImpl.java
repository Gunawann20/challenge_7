package org.binaracademy.challenge_7.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.challenge_7.entity.Role;
import org.binaracademy.challenge_7.entity.User;
import org.binaracademy.challenge_7.entity.dto.UserDto;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.repository.RoleRepository;
import org.binaracademy.challenge_7.repository.UserRepository;
import org.binaracademy.challenge_7.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public Response<UserDto> save(UserDto user) {
        Response<UserDto> response = new Response<>();
        User newUser = new User();
        try {
            Role role = roleRepository.findRolesById(1);
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setAddress(user.getAddress());
            newUser.setRoles(roles);
            newUser.setOrders(new ArrayList<>());

            userRepository.save(newUser);
            response.setError(false);
            response.setMessage("User created successfully");
            response.setData(user);
        }catch (Exception e){
            log.error("Terjadi kesalahan : "+ e.getMessage());
            return null;
        }
        return response;
    }

    @Transactional
    @Override
    public Response<String> update(Long userId, UserDto user) {
        Response<String> response = new Response<>();
        try {
            User sessionUser = userRepository.findByUsername(getUsernameContext());
            User updateUser = userRepository.findById(userId).orElse(null);
            if (updateUser != null && Objects.equals(sessionUser.getId(), updateUser.getId())){
                if (Objects.equals(user.getUsername(), sessionUser.getUsername())){
                    updateUser.setUsername(user.getUsername());
                    if (Objects.equals(user.getEmail(), sessionUser.getEmail())){
                        updateUser.setEmail(user.getEmail());
                        updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                        updateUser.setAddress(user.getAddress());

                        userRepository.save(updateUser);
                        response.setError(false);
                        response.setMessage("success");
                        response.setData("Update user data successfully");
                    }else {
                        User userByEmail = userRepository.findByEmail(user.getEmail());
                        if (userByEmail == null){
                            updateUser.setEmail(user.getEmail());
                            updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                            updateUser.setAddress(user.getAddress());

                            userRepository.save(updateUser);
                            response.setError(false);
                            response.setMessage("Success");
                            response.setData("Update user data successfully");
                        }else {
                            response.setError(true);
                            response.setMessage("failed");
                            response.setData("Email "+ user.getEmail()+ " has already exist");
                        }
                    }
                }else {
                    User userByUsername = userRepository.findByUsername(user.getUsername());
                    if (userByUsername == null){
                        updateUser.setUsername(user.getUsername());
                        if (Objects.equals(user.getEmail(), sessionUser.getEmail())){
                            updateUser.setEmail(user.getEmail());
                            updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                            updateUser.setAddress(user.getAddress());

                            userRepository.save(updateUser);
                            response.setError(false);
                            response.setMessage("success");
                            response.setData("Update user data successfully");
                        }else {
                            User userByEmail = userRepository.findByEmail(user.getEmail());
                            if (userByEmail == null){
                                updateUser.setEmail(user.getEmail());
                                updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                                updateUser.setAddress(user.getAddress());

                                userRepository.save(updateUser);
                                response.setError(false);
                                response.setMessage("Success");
                                response.setData("Update user data successfully");
                            }else {
                                response.setError(true);
                                response.setMessage("failed");
                                response.setData("Email "+ user.getEmail()+ " has already exist");
                            }
                        }
                    }else {
                        response.setError(true);
                        response.setMessage("failed");
                        response.setData("Username "+ user.getUsername()+ " has already exist");
                    }
                }
            }else {
                response.setError(true);
                response.setMessage("failed");
                response.setData("Update user data failed, you can't update user with id: "+ userId);
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+ e.getMessage());
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Update user data failed");
        }
        return response;
    }

    @Override
    public Response<String> delete(Long userId) {
        Response<String> response = new Response<>();
        User userContext = userRepository.findByUsername(getUsernameContext());
        try{
            if(Objects.equals(userContext.getId(), userId)){
                userRepository.deleteById(userId);
                response.setError(false);
                response.setMessage("Success");
                response.setData("Delete user successfully");
            }else {
                response.setError(true);
                response.setMessage("failed");
                response.setData("delete user data failed, you can't delete user with id: "+ userId);
            }
        }catch (Exception e){
            log.error("Terjadi kesalahan: "+e.getMessage());
            response.setError(true);
            response.setMessage("failed");
            response.setData("Delete user failed");
        }
        return response;
    }

    private String getUsernameContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }else {
            return null;
        }
    }
}
