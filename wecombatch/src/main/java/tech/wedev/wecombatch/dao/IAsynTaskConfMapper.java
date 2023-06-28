package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tech.wedev.autm.asyntask.entity.AsynTaskConf;

import java.util.List;

@Mapper
public interface IAsynTaskConfMapper {

    @Select({"<script>" +
            "       select task_type,\n" +
            "           sub_task_type,\n" +
            "           sub_task_name,\n" +
            "           sub_task_impl\n" +
            "       from sys_asyn_task_conf \n" +
            "</script>"})
    @Results({
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "sub_task_name", property = "subTaskName"),
            @Result(column = "sub_task_impl", property = "subTaskImpl")
    })
    List<AsynTaskConf> selectAll();
}
