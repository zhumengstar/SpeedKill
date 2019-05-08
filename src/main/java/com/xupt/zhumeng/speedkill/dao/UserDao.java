package com.xupt.zhumeng.speedkill.dao;

import com.xupt.zhumeng.speedkill.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("select * from tb_user where id = #{id}")
    User getById(@Param("id") long id);

    @Update("update tb_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);
}
