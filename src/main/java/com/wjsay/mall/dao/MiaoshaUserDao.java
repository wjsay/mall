package com.wjsay.mall.dao;

import com.wjsay.mall.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaoshauser where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);

    @Select("select * from miaoshauser where phoneno = #{phoneno}")
    MiaoshaUser getByPhoneNo(@Param("phoneno") String phoneno);

    @Insert("insert into miaoshauser (phoneno, nickname, password, salt, registerDate, lastLoginDate) " +
            "values(#{phoneno}, #{nickname}, #{password}, #{salt}, #{registerDate}, #{lastLoginDate})")
    void addUser(MiaoshaUser user);
}
