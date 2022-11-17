package com.data.service.custer;

import com.data.dao.custer.SongDao;
import com.data.dao.custer.UserDao;
import com.data.dbConfig.DataSourceContextHolder;
import com.data.model.easyexcel.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * The type User service.
 */
@Service("user-service")
//@Slf4j
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private SongDao songDao;


    /**
     * Gets user.
     * @return the user
     */
// @DS(DataSourceEnum.custer)
    // @Async("taskExecutor")
    @Transactional(rollbackFor = Exception.class)  //指定所有异常回滚操作，事务传播propagation默认是REQUIRED
    public List<Map<String, Object>> getUser() {
        LOGGER.info("----------------》数据源:{}", DataSourceContextHolder.getDB());
        // return userDao.getAllUser();
        return songDao.getAllSong();
    }

    /**
     * Save batch.
     * @param list the list
     */
    public void saveBatch(List<User> list) {

    }

}
