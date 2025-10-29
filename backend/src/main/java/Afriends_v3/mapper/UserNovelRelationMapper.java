package Afriends_v3.mapper;

import Afriends_v3.entity.UserNovelRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户小说关系Mapper接口
 */
@Mapper
public interface UserNovelRelationMapper extends BaseMapper<UserNovelRelation_njj> {

    /**
     * 查询所有用户小说关系（原生SQL）
     */
    @Select("SELECT * FROM v2_user_novel_relation")
    List<UserNovelRelation_njj> selectAllRecords();
}
