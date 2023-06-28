package tech.wedev.wecombatch.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tech.wedev.autm.asyntask.entity.AsynTaskDef;

import java.util.List;

@Mapper
public interface IAsynTaskDefMapper {

    @Select({"<script>" +
            "       select task_type,\n" +
            "           task_name,\n" +
            "           task_impl,\n" +
            "           fetch_task_interval,\n" +
            "           fetch_task_count,\n" +
            "           execute_task_delay,\n" +
            "           min_pool_size,\n" +
            "           max_pool_size,\n" +
            "           queue_limit,\n" +
            "           redo_count,\n" +
            "           redo_interval,\n" +
            "           timeout_limit,\n" +
            "           timeout_check_interval\n" +
            "       from sys_asyn_task_def \n" +
            "</script>"})
    @Results({
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "task_impl", property = "taskImpl"),
            @Result(column = "fetch_task_interval", property = "fetchTaskInterval"),
            @Result(column = "fetch_task_count", property = "fetchTaskCount"),
            @Result(column = "execute_task_delay", property = "executeTaskDelay"),
            @Result(column = "min_pool_size", property = "minPoolSize"),
            @Result(column = "max_pool_size", property = "maxPoolSize"),
            @Result(column = "queue_limit", property = "queueLimit"),
            @Result(column = "redo_count", property = "redoCount"),
            @Result(column = "redo_interval", property = "redoInterval"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "timeout_check_interval", property = "timeoutCheckInterval")
    })
    List<AsynTaskDef> selectAll();

    @Select({"<script>" +
            "       select task_type,\n" +
            "           task_name,\n" +
            "           task_impl,\n" +
            "           fetch_task_interval,\n" +
            "           fetch_task_count,\n" +
            "           execute_task_delay,\n" +
            "           min_pool_size,\n" +
            "           max_pool_size,\n" +
            "           queue_limit,\n" +
            "           redo_count,\n" +
            "           redo_interval,\n" +
            "           timeout_limit,\n" +
            "           timeout_check_interval\n" +
            "       from sys_asyn_task_def \n" +
            "       where task_type=#{taskType}" +
            "</script>"})
    @Results({
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "task_impl", property = "taskImpl"),
            @Result(column = "fetch_task_interval", property = "fetchTaskInterval"),
            @Result(column = "fetch_task_count", property = "fetchTaskCount"),
            @Result(column = "execute_task_delay", property = "executeTaskDelay"),
            @Result(column = "min_pool_size", property = "minPoolSize"),
            @Result(column = "max_pool_size", property = "maxPoolSize"),
            @Result(column = "queue_limit", property = "queueLimit"),
            @Result(column = "redo_count", property = "redoCount"),
            @Result(column = "redo_interval", property = "redoInterval"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "timeout_check_interval", property = "timeoutCheckInterval")
    })
    List<AsynTaskDef> selectByTaskType(String taskType);
}
