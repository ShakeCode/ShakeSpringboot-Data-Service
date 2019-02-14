package com.data.service.custer;

import com.data.annotation.DS;
import com.data.dao.custer.SongDao;
import com.data.dao.custer.UserDao;
import com.data.dbConfig.DataSourceContextHolder;
import com.data.dbConfig.DataSourceEnum;
import com.data.dbConfig.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
//@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SongDao songDao;


//    @DS(DataSourceEnum.custer)
    public List<Map<String, Object>> getUser() {
        System.out.println("----------------》数据源:" + DataSourceContextHolder.getDB());
//        return userDao.getAllUser();

        return songDao.getAllSong();
    }

}
