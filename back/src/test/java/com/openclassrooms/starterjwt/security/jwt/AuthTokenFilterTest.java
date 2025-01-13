package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    public AuthTokenFilterTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSetAuthenticationWhenJwtIsValid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-jwt-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("valid-jwt-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-jwt-token")).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(null); // Replace with actual UserDetails if
                                                                                  // needed

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("valid-jwt-token");
        verify(jwtUtils, times(1)).getUserNameFromJwtToken("valid-jwt-token");
        verify(userDetailsService, times(1)).loadUserByUsername("testUser");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenJwtIsInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-jwt-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("invalid-jwt-token")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("invalid-jwt-token");
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
