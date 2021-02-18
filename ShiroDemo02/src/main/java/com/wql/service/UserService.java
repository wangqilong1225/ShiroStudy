package com.wql.service;

import com.wql.entity.User;

import java.util.List;

public interface UserService {
    User selUserInfoService(String uname);
    List<String> selPowerInfoService(String uname);
    List<String> selRoleInfoService(String uname);
}
