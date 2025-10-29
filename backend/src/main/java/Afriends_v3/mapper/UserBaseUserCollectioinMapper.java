package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseUserCollectioin_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户基础用户收藏Mapper接口
 */
@Mapper
public interface UserBaseUserCollectioinMapper extends BaseMapper<UserBaseUserCollectioin_njj> {

    /**
     * 查询所有用户基础用户收藏记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_base_user_collection @heng")
    List<UserBaseUserCollectioin_njj> selectAllRecords();
}
