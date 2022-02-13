package com.qiunan;

import com.qiunan.mapper.UserMapper;
import com.qiunan.service.UserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.spring.mybatis.MapperFactoryBean;
import org.spring.mybatis.MapperImportBeanDefinitionRegistrar;
import org.spring.mybatis.MapperScan;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;

@ComponentScan("com.qiunan")
@MapperScan("com.qiunan.mapper")
public class Application {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(Application.class);



        context.refresh();

        UserService userService = context.getBean("userService", UserService.class);

        userService.test();
    }


}
