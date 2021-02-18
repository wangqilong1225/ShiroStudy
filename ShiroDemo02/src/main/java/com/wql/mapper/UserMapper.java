package com.wql.mapper;

import com.wql.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from t_user where uname=#{uname}")
    User selUserInfoMapper(@Param("uname") String uname);

    @Select("select pinfo from t_power where pid in(select pid from t_role_power where rid in(select rid from t_user_role where uid in(select uid from t_user where uname=#{uname})))")
    List<String> selPowerInfoService(@Param("uname") String uname);

    @Select("select rname from t_role where rid in(select rid from t_user_role where uid in(select uid from t_user where uname=#{uname}))")
    List<String> selRoleInfoService(@Param("uname") String uname);
}
