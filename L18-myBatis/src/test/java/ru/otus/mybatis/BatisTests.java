package ru.otus.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.mybatis.dao.AddressDao;
import ru.otus.mybatis.dao.PersonDao;
import ru.otus.mybatis.dao.SampleMapperInterface;
import ru.otus.mybatis.model.Address;
import ru.otus.mybatis.model.Person;
import ru.otus.mybatis.model.Sample;

/*
 *
 * 0) Не забываем добавлять интерфейсы в mybatis-config.xml и подключать логирование в ehcache.xml
 * 1) Реализовать интерфейс PersonDao. Должны поддерживаться методы ''int insert(Person person)'', ''Person selectOne(int id)''
 * 2) Реализовать интерфейс AddressDao. Должны поддерживаться методы ''int insert(Address address)'', ''Address selectOne(int id)''
 * 3) Реализовать в интерфейсе PersonDao метод ''List<Person> selectByCity(String city)'' (нужен join с таблицей address)
 * 4)*Повторить предыдущие задания в xml
 *
 * */

public class BatisTests {
  private static SqlSessionFactory sqlSessionFactory;
  private static boolean isDBInited;

  @BeforeAll
  public static void beforeAll() throws IOException, SQLException {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    // Можно сконфигурировать и в коде:
    // sqlSessionFactory.getConfiguration().addMapper(Sample.class);
    // sqlSessionFactory.getConfiguration()...
  }

  @BeforeEach
  void beforEach() throws SQLException {
    if (!isDBInited) {
      isDBInited = true;
      BatisStarter demo = new BatisStarter();
      demo.createTables();
      demo.insertRecords();
    }
  }

  @Test
  void selectOne() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Sample test = session.selectOne("testMapper.selectTestOne", 1);
      System.out.println("selected: " + test);
    }
  }

  @Test
  void selectAll() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Map<String, Integer> params = new HashMap<>();
      params.put("minId", 50);
      params.put("maxId", 55);
      List<Test> testList = session.selectList("testMapper.selectTestAll", params);
      System.out.println("selected: " + testList);
    }
  }

  @Test
  void selectLike() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Map<String, String> params = new HashMap<>();
      params.put("nameParam", "%2%");
      List<Test> testList = session.selectList("testMapper.selectTestLike", params);
      System.out.println("selected like with nameParam: " + testList);

      testList = session.selectList("testMapper.selectTestLike");
      System.out.println("selected like without nameParam: " + testList);
    }
  }

  @Test
  void selectForEach() {
    // select * from Test where id in (1,2,3,4)
    try (SqlSession session = sqlSessionFactory.openSession()) {
      List<Integer> params = Arrays.asList(1, 2, 3, 4, 5);
      List<Test> testList = session.selectList("testMapper.selectTestForEach", params);
      System.out.println("selectedForEach: " + testList);
    }
  }

  @Test
  void insert() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Map<String, String> params = new HashMap<>();
      params.put("id", "500");
      params.put("name", "TestInsertovich");
      int rowCount = session.insert("testMapper.insert", params);
      System.out.println("inserted: " + rowCount);

      Sample test = session.selectOne("testMapper.selectTestOne", 500);
      System.out.println("selected: " + test);
    }
  }

  @Test
  void selectOneInterface() {
    //Optional добавили в 3.5.0 (только для "интерфейсов")

    try (SqlSession session = sqlSessionFactory.openSession()) {
      SampleMapperInterface mapper = session.getMapper(SampleMapperInterface.class);
      Optional<Sample> test = mapper.findOne(1);
      System.out.println("selected: " + test);

      Optional<Sample> testNotExists = mapper.findOne(-1);
      System.out.println("selected: " + testNotExists);
    }
  }

  @Test
  void selectCached() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      Sample test = session.selectOne("testMapper.selectTestOne", 1);
      System.out.println("1 selected: " + test);

      session.selectOne("testMapper.selectTestOne", 1);
      System.out.println("2 selected: " + test);

      session.selectOne("testMapper.selectTestOne", 1);
      System.out.println("3 selected: " + test);

      session.selectOne("testMapper.selectTestOne", 4);
      System.out.println("4 selected: " + test);
    }
  }

  @Test
  public void selectPersonByCity() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      PersonDao personDao = session.getMapper(PersonDao.class);
      AddressDao addressDao = session.getMapper(AddressDao.class);

      Person person = new Person();
      person.setId(1);
      person.setFirstName("John");
      person.setLastName("Black");

      Person person2 = new Person();
      person2.setId(2);
      person2.setFirstName("Ivan");
      person2.setLastName("Ivanov");

      Address address = new Address();
      address.setId(1);
      address.setPersonId(2);
      address.setCity("Moscow");

      personDao.insert(person);
      personDao.insert(person2);
      addressDao.insert(address);

      List<Person> loadedPerson = personDao.selectByCity("Moscow");
      System.out.println(loadedPerson);
    }
  }
}
