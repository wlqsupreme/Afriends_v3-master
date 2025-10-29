package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseImagepostBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础图片帖子Mapper接口
 */
@Mapper
public interface UserBaseImagepostBaseMapper extends BaseMapper<UserBaseImagepostBase_njj> {

    /**
     * 查询所有用户基础图片帖子记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_imagepost_base @heng")
    List<UserBaseImagepostBase_njj> selectAllRecords();
}
