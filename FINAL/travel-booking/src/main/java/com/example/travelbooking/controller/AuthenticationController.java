package com.example.travelbooking.controller;

import com.example.travelbooking.entity.RefreshToken;
import com.example.travelbooking.exception.OTPNotFoundException;
import com.example.travelbooking.exception.RefreshTokenNotFoundException;
import com.example.travelbooking.model.request.registration.*;
import com.example.travelbooking.model.response.JwtResponse;
import com.example.travelbooking.repository.RefreshTokenRepository;
import com.example.travelbooking.repository.UserRepository;
import com.example.travelbooking.security.CustomUserDetails;
import com.example.travelbooking.security.JwtUtils;
import com.example.travelbooking.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authentication")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    JwtUtils jwtUtils;

    UserService userService;

    UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(userRepository.findById(userDetails.getId()).get())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return JwtResponse.builder()
                .jwt(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> new ResponseEntity<>("Email is existed", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    userService.registerUser(request);
                    return new ResponseEntity<>(null, HttpStatus.CREATED);
                });
    }
    @PostMapping("/signup-partner")
    public ResponseEntity<?> registerPartner(@Valid @RequestBody RegistrationPartnerRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> new ResponseEntity<>("Email is existed", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    userService.registerPartner(request);
                    return new ResponseEntity<>(null, HttpStatus.CREATED);
                });
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("id") Long id, @RequestParam("code") String code) {
        boolean isVerified = userService.verifyUser(id, code);
        if (isVerified) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/success_page")
                    .build();
        } else {
            throw new OTPNotFoundException("Invalid OTP");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(userService.refreshToken(request));
        } catch (RefreshTokenNotFoundException | UsernameNotFoundException ex) {
            return new ResponseEntity<>("Thông tin refreshToken không chính xác", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<?> sendPasswordResetCode(@RequestBody @Valid EmailForgotPassRequest request) {
        userService.sendResetPasswordCode(request);

        return ResponseEntity.ok("Mã xác nhận đã được gửi đến email");
    }

    @GetMapping("/reset-password/verify")
    public ModelAndView verifyResetPass(@RequestParam("id") Long id, @RequestParam("code") String code) {
        try {
            userService.verifyUser(id, code);
            ModelAndView modelAndView = new ModelAndView("/user/reset_new_password.html");
            modelAndView.addObject("userId", id);
            return modelAndView;
        }
        catch (OTPNotFoundException e){
            return new ModelAndView("/home");
        }

    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@Valid @RequestParam Long userId, @RequestParam String newPassword) {

        userService.resetPassword(userId, newPassword);

        return new  ModelAndView("/user/success-page.html");
    }

//    @PostMapping("/reset-password")
//    public ModelAndView resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
//
//        userService.resetPassword(resetPasswordRequest);
//
//        return new  ModelAndView("/user/success-page.html");
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{email}/otp-sending")
    public void sendOtp(@PathVariable String email) {
        userService.sendOtp(email);
    }

    @PostMapping("/{email}/attach-file")
    public void sendAttachedFileMail(@PathVariable String email) throws MessagingException {
        userService.sendAttachedMail(email);
    }
}
