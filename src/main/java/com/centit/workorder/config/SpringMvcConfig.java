package com.centit.workorder.config;

import com.centit.framework.config.WebConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zou_wy on 2017/3/29.
 */
@Configuration
@Import(WebConfig.class)
@ComponentScan(basePackages = {"com.centit.workorder.controller"},
        includeFilters = {@ComponentScan.Filter(value= org.springframework.stereotype.Controller.class)},
        useDefaultFilters = false)
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

}
