package com.example.ColConnect.controllers;

import com.example.ColConnect.models.*;
import com.example.ColConnect.repo.CommentRepository;
import com.example.ColConnect.repo.PostsRepository;
import com.example.ColConnect.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SocialsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostsRepository postsRepository;
    @Autowired
    CommentRepository commentRepository;
    //spring sec pwd encoder
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public String registerPage(Model model) {
        // This provides the "template" for the form to bind to
        model.addAttribute("userFormDTO", new UserDTO());
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    //REGISTRATION
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userFormDTO") UserDTO form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //validate form
        if (bindingResult.hasErrors()){
            return "register";
        }

        // check for duplicate users
        if (userRepository.findByName(form.getUsername()) != null) {
            bindingResult.rejectValue("username","duplicate","Username already exists. Please choose another.");
            return "register";
        }

        //save new user
        User user = new User(form.getUsername(), passwordEncoder.encode(form.getPassword()));
            user.setEmail(form.getEmail());
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "User registered.");
        return "redirect:/login";

    }

//    //LOGIN
//    @PostMapping("/login")
//    public String logUserIn(@ModelAttribute UserDTO form, Model model, HttpSession session){
//
//        User user = userRepository.findByName(form.getUsername());
//
//        // No user found
//        if (user == null) {
//            model.addAttribute("error","User not found.");
//            return "login";
//        }
//        // Pwd mismatch
//        if (!user.getPwd().equals(form.getPassword())) {
//            model.addAttribute("error","Wrong password");
//            return "login";
//        }
//
//        session.setAttribute("currentUser",user);
//
//        return "redirect:/homepage";
//    }

    // RENDER HOMEPAGE
    @GetMapping("/homepage")
    public String homepage(Model model) {
        List<Post> posts = postsRepository.findTop10ByOrderByCreatedAtDesc();
        if (posts.isEmpty()) {
            model.addAttribute("msg", "No posts to show.");
        }
        model.addAttribute("posts", posts);

        return "homepage";
    }

    //POST'S INFORMATION PAGE
    @GetMapping("/post/{title}")
    public String postPage(@PathVariable String title, Model model) {

        Post post = null;

        post = postsRepository.findByTitle(title);

        if (post == null) {
            String error = "*********** failed to fetch the post from repo**************";
            System.out.println(error);
            model.addAttribute("error", error);
        }
        model.addAttribute("post", post);
        return "PostPage";
    }

    //Myprofile - helper endpoint
    @GetMapping("/profile/me")
    public String getMyProfile(Principal principal){
        User user = userRepository.findByName(principal.getName());
        return "redirect:/profile/" + user.getId();
    }

    // RENDER PROFILE PAGE
    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            model.addAttribute("error", "User profile could not be found!");
            return "ProfilePage";
        }
        model.addAttribute("user", user.get());
        return "ProfilePage";
    }

    // RENDER UPDATE PROFILE PAGE
    @GetMapping("/updateProfile/{id}")
    public String updateProfilePage(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (!userRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("error","Profile not found");
            return "redirect:/homepage";
        }
        User currentUser = userRepository.findByName(principal.getName());
        if (currentUser != null && currentUser.getId().equals(id)) {
            return "updateProfile";
        }

        redirectAttributes.addFlashAttribute("error", "You do not have permission to edit this profile.");
        return "redirect:/profile/" + id;
    }

    // UPDATE PROFILE DATA
    @PostMapping("/updateProfile/{id}")
    public String updateProfile(@ModelAttribute UserDTO userDTO, @PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        // auth
        User currentUser = userRepository.findByName(principal.getName());
        if (!currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "You are not authorised to edit this profile.");
            return "redirect:/profile/" + id;
        }

        // String email;
        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            currentUser.setEmail(userDTO.getEmail());
        }
        //    String college;
        if (userDTO.getCollege() != null && !userDTO.getCollege().isBlank()) {
            currentUser.setCollege(userDTO.getCollege());
        }
        //    String dept;
        if (userDTO.getDept() != null && !userDTO.getDept().isBlank()) {
            currentUser.setDept(userDTO.getDept());
        }
        //    List<String> interests;
        if (userDTO.getInterests() != null && !userDTO.getInterests().isEmpty()) {
            currentUser.setInterests(userDTO.getInterests());
        }

        //    String profilePicture_url;
        MultipartFile profilePicture = userDTO.getProfilePicture();

        if (profilePicture != null && !profilePicture.isEmpty()) {
            // store in local folder
            try {
                Path uploadPath = Paths.get("uploads/profilepics/");
                Files.createDirectories(uploadPath);

                // create image name
                String image_name = profilePicture.getOriginalFilename();

                // create file
                Path filepath = uploadPath.resolve(image_name);

                //write image bytes
                Files.write(filepath, profilePicture.getBytes());

                // set String profilePicture_url
                currentUser.setProfilePicture_url("/uploads/profilepics/" + image_name);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Couldn't upload image!");
                return "redirect:/profile/" + id;
            }
        }

        userRepository.save(currentUser);
        return "redirect:/profile/" + id;

    }

    // SHOW SPECIFIC USER'S POSTS
    @GetMapping("/userposts/{user_id}")
    public String showUserPosts(@PathVariable Long user_id, Model model) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) {
            model.addAttribute("error", "User profile could not be found!");
            return "UserPostsPage";
        }

        model.addAttribute("user", user.get());
        return "UserPostsPage";
    }

    // DELETE POST
    @PostMapping("/delete/{title}")
    public String deletePost(@PathVariable String title,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        // Check if user is logged in
        User currentUser = userRepository.findByName(principal.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete your posts.");
            return "redirect:/login";
        }

        // Check if post exists
        Post post = postsRepository.findByTitle(title);
        if (post == null) {
            redirectAttributes.addFlashAttribute("error", "Post not found");
            return "redirect:/profile/" + currentUser.getId();
        }

        // Check if user owns the post
        if (!Objects.equals(currentUser.getId(), post.getUser().getId())) {
            redirectAttributes.addFlashAttribute("error",
                    "You don't have permission to delete this post. You are NOT the author of this post!");
            return "redirect:/post/" + title;
        }

        // Delete the post
        try {
            postsRepository.deleteById(title);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the post!");
        }
        redirectAttributes.addFlashAttribute("msg", "The post was successfully deleted!");

        return "redirect:/profile/" + currentUser.getId();
    }

    // RENDER UPLOAD POST PAGE
    @GetMapping("/uploadPost")
    public String uploadPostPage() {
        return "uploadPost";
    }

    // Upload post form
    @PostMapping("/uploadPost")
    public String uploadPost(@ModelAttribute PostDTO postForm, Model model, Principal principal, RedirectAttributes redirectAttributes) {

        // User check
        User currentUser = userRepository.findByName(principal.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to post!");
            return "redirect:/login";
        }
        Post post = new Post();
        post.setUser(currentUser);

        // Title check
        String title = postForm.getTitle();
        if (title.isBlank()) {
            model.addAttribute("error", "Title cannot be empty!");
            return "uploadPost";
        }
        post.setTitle(title);

        // Content check
        String content = postForm.getText();
        if (!content.isBlank()) {
            post.setText(content);
        }
        post.setText(content);

        // Image check
        MultipartFile image = postForm.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                // STORE IMAGE IN FOLDER /uploads

                // Local folder
                Path uploadPath = Paths.get("uploads/postpics/");
                Files.createDirectories(uploadPath);

                // Get image name
                String imageName = image.getOriginalFilename();
//                String extension = imageName.substring(imageName.lastIndexOf("."));
//                imageName = imageName + extension;

                //Create image file & Save image bytes
                assert imageName != null;
                Path filepath = uploadPath.resolve(imageName);
                Files.write(filepath, image.getBytes());

                post.setImage_url("/uploads/postpics/" + imageName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Image upload failed, make sure the image has a name and is less than 2 MB");
                return "redirect:/uploadPost";
            }
        }

        postsRepository.save(post);
        redirectAttributes.addFlashAttribute("success", "Post uploaded.");
        return "redirect:/post/" + title;

    }

    //Comment post feature:
    @PostMapping("/postComment/{title}")
    public String postComment(@PathVariable String title, @ModelAttribute CommentDTO commentDTO, RedirectAttributes redirectAttributes, Principal principal) {
        //get comment
        String commentString = commentDTO.getComment();
        // comment check
        if (commentString.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "You cant post empty comment");
            return "redirect:/post/" + title;
        }

        // get currentUser
        User currentUser = userRepository.findByName(principal.getName());
        // check user
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Login to comment");
            return "redirect:/post/" + title;
        }

        // get and set post
        Post post = postsRepository.findByTitle(title);

        Comment comment = new Comment();
        comment.setComment(commentString);
        comment.setUser(currentUser);
        comment.setPost(post);

        commentRepository.save(comment);

        redirectAttributes.addFlashAttribute("success", "Comment added.");
        return "redirect:/post/" + title;

    }

//    // Logout feature
//    @PostMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("currentUser");
//        return "redirect:";
//    }
}
