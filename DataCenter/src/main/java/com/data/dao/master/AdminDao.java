package com.data.dao.master;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminDao {

    String name = "hello";

    default String getName() {
        return name;
    }

    List<Map<String, Object>> getAllAdmin();
}
