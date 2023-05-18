package com.smart.MySmartContactManager.Config;

import com.smart.MySmartContactManager.Dao.UserRepository;
import com.smart.MySmartContactManager.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.getUserBuUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("could not found user");
        }
        CustomUserDetail customUserDetail=new CustomUserDetail(user);

        return customUserDetail;
    }
}
