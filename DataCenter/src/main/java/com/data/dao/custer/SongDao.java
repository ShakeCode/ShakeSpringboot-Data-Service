package com.data.dao.custer;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongDao {

    List<Map<String,Object>> getAllSong();
}
