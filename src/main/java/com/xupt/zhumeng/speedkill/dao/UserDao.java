package com.xupt.zhumeng.speedkill.dao;

import com.xupt.zhumeng.speedkill.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    /**
     * 通过id获取用户信息
     *
     * @param id
     * @return
     */
    @Select("select * from tb_user where id = #{id}")
    User getById(@Param("id") long id);


    /**
     * 设置用户密码
     *
     * @param toBeUpdate
     */
    @Update("update tb_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);
}
