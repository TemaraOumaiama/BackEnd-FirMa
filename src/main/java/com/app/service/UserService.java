package com.app.service;

import com.app.enumeration.Role;
import com.app.exception.UserNotFoundException;
import com.app.modele.Departement;
import com.app.modele.User;
import com.app.repository.DepartementRepository;
import com.app.repository.UserRepository;
import com.app.modele.*;
import     com.app.exception.*;
import org.apache.commons.lang3.RandomStringUtils;

import com.app.enumeration.Role;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.app.constant.UserImplConstant.*;
import static com.app.constant.FileConstant.*;

import static com.app.enumeration.Role.ROLE_USER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@AllArgsConstructor
@Transactional
@Qualifier("userDetailsService")
public class UserService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private   BCryptPasswordEncoder passwordEncoder;
    private  LoginAttemptService loginAttemptService;
    private  EmailService emailService;

    private final UserRepository userRepository;
    private final  DepartementRepository departementRepository;


    public User addUser(User user) {
        Departement userDepartement = user.getdepartement();
        if (userDepartement != null) {
            String departementNom = userDepartement.getNom();
            Optional<Departement> optionalDepartement = departementRepository.findByNom(departementNom);

            if (optionalDepartement.isPresent()) {
                Departement departement = optionalDepartement.get();
                user.setdepartement(departement);
            } else {
                throw new IllegalArgumentException("Departement not found with name: " + departementNom);
            }
        }

        return userRepository.save(user);
    }

    public User addNewUser(String prenom, String nom,  String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException, URISyntaxException {
        String username=nom.toLowerCase()+prenom.toLowerCase();
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        String password = generatePassword();
       // user.setUserId(generateUserId());
        user.setPrenom(prenom);
        user.setNom(nom);
        user.setDatecreation(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setImageUrl(getTemporaryProfileImageUrl());
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        LOGGER.info("Nouveau mot de passe " + password);
        return user;
    }

    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }




    public void deleteUser(String username) throws IOException {
        User user = userRepository.findByUsername(username);
        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        userRepository.deleteById(user.getUserId());
    }


    public void resetPassword(String email) throws MessagingException, EmailNotFoundException, javax.mail.MessagingException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(user.getNom(),user.getPrenom(), password, user.getEmail());
    }



    public List<User> findAllUsersSortedByUsername() {
        return userRepository.findAllByOrderByUsernameAsc();
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(updatedUser.getUserId());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // Update only the desired attribute(s)
            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getNom() != null) {
                existingUser.setNom(updatedUser.getNom());
            }
            if (updatedUser.getPrenom() != null) {
                existingUser.setPrenom(updatedUser.getPrenom());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getUsertype() != null) {
                existingUser.setUsertype(updatedUser.getUsertype());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getDatelastactivity() != null) {
                existingUser.setDatelastactivity(updatedUser.getDatelastactivity());
            }
            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getImageUrl() != null) {
                existingUser.setImageUrl(updatedUser.getImageUrl());
            }
            if (updatedUser.getDatecreation() != null) {
                existingUser.setDatecreation(updatedUser.getDatecreation());
            }
            if (updatedUser.getdepartement() != null) {
                Departement updatedDept = updatedUser.getdepartement();
//existingUser.getdepartement().getNom().equals(updatedDept.getNom()) ||
                // Check if the updated department name is different
                if (! existingUser.getdepartement().getId().equals(updatedDept.getId())){
                    // Retrieve the department with the new name from the repository
                    Departement newDept = departementRepository.findById(updatedDept.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Department not found with name: " + updatedDept.getId()));

                    existingUser.setdepartement(newDept);
                }
            }

            // Save the updated entity
            return userRepository.save(existingUser);
        } else {
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("User not found with ID: " + updatedUser.getUserId());
        }
    }



    public User updateUser(String currentUsername, String newprenom, String newnom, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        assert currentUser != null;
        currentUser.setPrenom(newprenom);
        currentUser.setNom(newnom);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }




    public User findByUserId(long id) {
        return userRepository.findByUserId(id);
    }

    public User findByNom(String nom) {
        return userRepository.findByNom(nom);
    }


    public User findUserByUsername(String nom) {
        return userRepository.findByUsername(nom);
    }


    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


public void deleteUserId(Long id) {
    userRepository.deleteUserByUserId(id);
}


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getDatelastactivity());
            user.setDatelastactivity(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }


    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    public void updateUserPassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    public User register(String prenom, String nom, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, javax.mail.MessagingException, URISyntaxException {
        String username=nom.toLowerCase()+prenom.toLowerCase();
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
      //  user.setUserId(generateUserId());
        String password = generatePassword();
        user.setPrenom(prenom);
        user.setNom(nom);
        user.setUsername(username);
        user.setEmail(email);
        user.setDatecreation(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setImageUrl(getTemporaryProfileImageUrl());
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmailNewUser(nom,prenom, password, email);
        return user;
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

  /*  private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }


    private String getTemporaryProfileImageUrl(String imageName) throws URISyntaxException {
        Path imagePath = Paths.get(Objects.requireNonNull(getClass().getResource("/")).toURI()).getParent().resolve("resources").resolve(imageName);
        return imagePath.toUri().toString();
    }
*/

    private String getTemporaryProfileImageUrl() {
        return "https://cdn.pixabay.com/photo/2018/11/13/21/43/avatar-3814049_640.png";
    }



    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }


    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }



}
