package Afriends_v3.mapper;

import Afriends_v3.entity.ContentDislikeRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 内容点踩关系Mapper接口
 */
@Mapper
public interface ContentDislikeRelationMapper extends BaseMapper<ContentDislikeRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_content_dislike_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_content_dislike_relation LIMIT 5")
    java.util.List<ContentDislikeRelation_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_content_dislike_relation")
    java.util.List<ContentDislikeRelation_wlq> selectAllRecords();
}

