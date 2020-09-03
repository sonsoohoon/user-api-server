package com.org.bithumb.apiserver.security;

import com.org.bithumb.apiserver.domain.repository.PctUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private PctUserRepository pctUserRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return pctUserRepository.findByUid(uid).orElseThrow(() -> new UsernameNotFoundException("not exist user: " + uid));
    }
}
