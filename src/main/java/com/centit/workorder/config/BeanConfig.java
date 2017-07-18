package com.centit.workorder.config;

import com.centit.framework.hibernate.config.DataSourceConfig;
import com.centit.framework.listener.InitialWebRuntimeEnvironment;
import com.centit.framework.staticsystem.config.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@Import({SpringConfig.class, DataSourceConfig.class})
@ComponentScan(basePackages={"com.centit.workorder"},
        excludeFilters=@ComponentScan.Filter(value=org.springframework.stereotype.Controller.class))
public class BeanConfig {
    @Bean(initMethod = "initialEnvironment")
    @Lazy(value = false)
    public InitialWebRuntimeEnvironment initialEnvironment() {
        return new InitialWebRuntimeEnvironment();
    }


}
