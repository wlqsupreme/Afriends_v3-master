package Afriends_v3.mapper;

import Afriends_v3.entity.ContentFavouriteRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 内容收藏关系Mapper接口
 */
@Mapper
public interface ContentFavouriteRelationMapper extends BaseMapper<ContentFavouriteRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_content_favourite_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_content_favourite_relation LIMIT 5")
    java.util.List<ContentFavouriteRelation_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_content_favourite_relation")
    java.util.List<ContentFavouriteRelation_wlq> selectAllRecords();
}

