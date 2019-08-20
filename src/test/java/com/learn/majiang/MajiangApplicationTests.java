package com.learn.majiang;

import com.learn.majiang.service.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MajiangApplicationTests {

    @Autowired
    QuestionService questionService;
    @Test
    public void contextLoads() {
        System.out.println(System.currentTimeMillis());
    }

}
