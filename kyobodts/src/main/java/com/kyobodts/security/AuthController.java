package com.kyobodts.security;

import com.kyobodts.dto.LoginRequest;
import com.kyobodts.dto.UserDto;
import com.kyobodts.entity.User;
import com.kyobodts.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

		Authentication auth = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// 세션 생성
		HttpSession session = httpRequest.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

		return ResponseEntity.ok("로그인 성공");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		request.getSession().invalidate();
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok("로그아웃 성공");
	}
	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserDto request) {
		// 비밀번호 암호화
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		User user = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(request.getPassword())
			.build();

		User saved = userRepository.save(user);

		UserDto response = UserDto.builder()
			.id(saved.getId())
			.name(saved.getName())
			.email(saved.getEmail())
			.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	@GetMapping("/me")
	public ResponseEntity<?> getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity.ok(auth.getName()); // 이메일 반환
	}

}
