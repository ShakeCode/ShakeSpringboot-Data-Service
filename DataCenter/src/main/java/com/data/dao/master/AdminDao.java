package com.data.dao.master;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminDao {

    List<Map<String,Object>> getAllAdmin();
}
