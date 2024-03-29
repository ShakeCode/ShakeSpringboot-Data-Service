package com.data.config;

import com.data.model.HeaderInfo;
import com.data.util.HeaderContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


/**
 * The type Context decorator.
 */
public class ContextDecorator implements TaskDecorator {
    /**
     * Decorate the given {@code Runnable}, returning a potentially wrapped
     * {@code Runnable} for actual execution.
     * @param runnable the original {@code Runnable}
     * @return the decorated {@code Runnable}
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取主线程中的请求信息（我们的用户信息也放在里面）
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 获取主线程请求头信息
        HeaderInfo headerInfo = HeaderContext.getHeaderInfo();
        return () -> {
            try {
                // 将主线程的请求信息，设置到子线程中
                RequestContextHolder.setRequestAttributes(attributes, true);
                // 设置主线程的线程数据到子线程
                HeaderContext.setHeaderInfo(headerInfo);
                // 执行子线程，这一步不要忘了
                runnable.run();
            } finally {
                // 线程结束，清空这些信息，否则可能造成内存泄漏
                RequestContextHolder.resetRequestAttributes();
                // 避免线程复用后出现取值混乱, 退出runnable后清除线程内存数据
                HeaderContext.clear();
            }
        };
    }
}