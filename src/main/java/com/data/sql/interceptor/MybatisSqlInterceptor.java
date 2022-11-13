package com.data.sql.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
@Component
public class MybatisSqlInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisSqlInterceptor.class);

    public MybatisSqlInterceptor() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] queryArgs = invocation.getArgs();
        String name = invocation.getMethod().getName();
        LOGGER.info("====>> 执行sql方法:{}", name);
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        LOGGER.info("====>> 执行sql方法路径:{}", mappedStatement.getId());
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql();
        LOGGER.info("====>> 执行sql:{}", sql);
        long startTimeMillis = System.currentTimeMillis();
        Object proceedReslut = invocation.proceed();
        long endTimeMillis = System.currentTimeMillis();
        LOGGER.info("====>> sql 执行耗时:{}", (endTimeMillis - startTimeMillis) / 1000L + "s");
        return proceedReslut;
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}