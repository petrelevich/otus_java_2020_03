package ru.otus.mybatis.dao;

import org.apache.ibatis.annotations.Select;
import ru.otus.mybatis.model.Sample;

import java.util.Optional;

public interface SampleMapperInterface {

    @Select("select * from Test where id = #{id}")
    Optional<Sample> findOne(Integer id);
}
