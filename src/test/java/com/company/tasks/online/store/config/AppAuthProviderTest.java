package com.company.tasks.online.store.config;

import com.company.tasks.online.store.entities.user.User;
import com.company.tasks.online.store.helpers.TestDataProvider;
import com.company.tasks.online.store.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppAuthProviderTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepo;

    private AppAuthProvider authProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authProvider = new AppAuthProvider(passwordEncoder, userRepo);
    }

    @Test
    void authenticate_when_userNotFound() {
        //given
        Authentication auth = mock(Authentication.class, RETURNS_MOCKS);

        //user not found in repo
        when(userRepo.getUserByUserName(anyString())).thenReturn(null);

        //then
        assertThrows(BadCredentialsException.class, () -> this.authProvider.authenticate(auth));
    }

    @Test
    void authenticate_when_passwordDoesNotMatch() {
        //given
        Authentication auth = mock(Authentication.class, RETURNS_MOCKS);
        User user = mock(User.class, RETURNS_MOCKS);

        //when
        when(userRepo.getUserByUserName(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        //then
        assertThrows(BadCredentialsException.class, () -> this.authProvider.authenticate(auth));
    }

    @Test
    void authenticate_when_correctCredentialsProvided() {
        //given
        Authentication providedAuth = mock(Authentication.class, RETURNS_MOCKS);
        User user = mock(User.class, RETURNS_MOCKS);
        List<String> expectedRoleNames = List.of("USER", "ADMIN");

        //when
        when(userRepo.getUserByUserName(anyString())).thenReturn(user);
        when(user.getRoles()).thenReturn(TestDataProvider.getRolesByNames(expectedRoleNames));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        //authenticate
        Authentication successfulAuth = this.authProvider.authenticate(providedAuth);

        //then
        List<String> actualRoleNames = successfulAuth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertTrue(expectedRoleNames.containsAll(actualRoleNames));
        assertEquals(expectedRoleNames.size(), actualRoleNames.size());
    }

    @Test
    void supports_when_providedSupportingToken() {
        //given
        Class<?> clazz = UsernamePasswordAuthenticationToken.class;

        //when
        boolean supports = authProvider.supports(clazz);

        //then
        assertTrue(supports);
    }

    @Test
    void supports_when_providedNotSupportingToken() {
        //given
        Class<?> clazz = TestingAuthenticationToken.class;

        //when
        boolean supports = authProvider.supports(clazz);

        //then
        assertFalse(supports);
    }

}
