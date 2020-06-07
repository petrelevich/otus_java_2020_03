package ru.otus.mybatis.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import ru.otus.mybatis.model.Address;

import java.util.List;

public interface AddressDao {

  @Insert("insert into address(id, personId, city) values (#{id}, #{personId}, #{city})")
  int insert(Address person);

  @Select("select * from address where id = #{id}")
  Address selectOne(int id);

  @Select("select * from address")
  List<Address> selectAll();
}
