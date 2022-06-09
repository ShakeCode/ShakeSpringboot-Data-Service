package com.data.dao.custer;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {

    List<Map<String,Object>> getAllUser();

}
