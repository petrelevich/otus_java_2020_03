package ru.otus.mybatis.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import ru.otus.mybatis.model.Person;

import java.util.List;

public interface PersonDao {

    @Insert("insert into person (id, firstName, lastName) values (#{id}, #{firstName}, #{lastName})")
    int insert(Person person);

    @Select("select * from person where id = #{id}")
    Person selectOne(Integer id);

    @Select("select * from person join address where address.city = #{city}")
    List<Person> selectByCity(String city);


}
