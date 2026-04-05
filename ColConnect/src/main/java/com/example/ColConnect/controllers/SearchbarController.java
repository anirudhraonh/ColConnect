package com.example.ColConnect.controllers;

import com.example.ColConnect.models.User;
import com.example.ColConnect.models.UserDTO;
import com.example.ColConnect.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchbarController {

    @Autowired
    private UserRepository userRepository;

    //search profile feature
    @GetMapping("/searchProfile")
    public List<UserDTO> searchUser(@RequestParam String q){

        List<User> userList = userRepository.searchUsers(q);
        List<UserDTO> userDTOList = new ArrayList<>();

        for(User u : userList){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(u.getId());
            userDTO.setUsername(u.getName());
            userDTO.setProfilePic_url(u.getProfilePicture_url());

            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
