package com.iboxpay;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

  @Test
  public void contextLoads() throws SQLException {
    List<String> list = new ArrayList<>();
    list.add("lyl");
    list.add("lq");
    System.out.println(list);
  }

}
