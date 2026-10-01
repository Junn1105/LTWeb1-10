package vn.iotstar.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.util.JpaUtil_24110053;

@WebListener
public class ApplicationListener_24110053 implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaUtil_24110053.close();
    }
}
