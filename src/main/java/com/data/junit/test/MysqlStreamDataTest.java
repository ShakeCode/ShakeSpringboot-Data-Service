//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.data.junit.test;

import com.data.annotation.DS;
import com.data.dao.master.AdminDao;
import com.data.dbConfig.DataSourceEnum;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlStreamDataTest {
    @Autowired
    private AdminDao adminDao;

    public MysqlStreamDataTest() {
    }

    @DS(DataSourceEnum.master)
    @Test
    public void streamTest() {
        final List<Map<String, Object>> list = Lists.newArrayList();
        this.adminDao.getAllAdminByStream(new ResultHandler<Map<String, Object>>() {
            @Override
            public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
                System.out.println("第" + resultContext.getResultCount() + "条数据:" + list.add(resultContext.getResultObject()));
            }
        });
        System.out.println("获取数据:" + list);
    }

    public static void main(String[] args) {
        System.out.println(1.0D);
        System.out.println(-2147483648);
    }

    public class ddd {
        public ddd() {
        }
    }

    protected class tt111t {
        protected tt111t() {
        }
    }

    private class ttt {
        private ttt() {
        }
    }

    static final class tt {
        tt() {
        }
    }
}
