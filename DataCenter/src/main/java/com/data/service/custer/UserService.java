package com.data.service.custer;

import com.data.dao.custer.SongDao;
import com.data.dao.custer.UserDao;
import com.data.dbConfig.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("user-service")
//@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SongDao songDao;


//    @DS(DataSourceEnum.custer)
    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor=Exception.class)  //指定所有异常回滚操作，事务传播propagation默认是REQUIRED
    public List<Map<String, Object>> getUser() {
        System.out.println("----------------》数据源:" + DataSourceContextHolder.getDB());
//        return userDao.getAllUser();

        return songDao.getAllSong();
    }

}
