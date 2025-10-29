package Afriends_v3.mapper;

import Afriends_v3.entity.UserSystemMessage_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户系统消息Mapper接口
 */
@Mapper
public interface UserSystemMessageMapper extends BaseMapper<UserSystemMessage_njj> {

    /**
     * 查询所有用户系统消息（原生SQL）
     */
    @Select("SELECT * FROM v3_user_system_message")
    List<UserSystemMessage_njj> selectAllRecords();
}
