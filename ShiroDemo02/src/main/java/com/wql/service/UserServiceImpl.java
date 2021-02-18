package com.wql.service;

import com.wql.entity.User;
import com.wql.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selUserInfoService(String uname) {
        return userMapper.selUserInfoMapper(uname);
    }

    @Override
    public List<String> selPowerInfoService(String uname) {
        return userMapper.selPowerInfoService(uname);
    }

    @Override
    public List<String> selRoleInfoService(String uname) {
        return userMapper.selRoleInfoService(uname);
    }
}
