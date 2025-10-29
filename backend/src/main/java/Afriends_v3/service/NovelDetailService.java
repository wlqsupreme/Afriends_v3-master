package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 小说详情页面服务类
 * 负责处理小说详情数据的查询和整合
 */
@Service
public class NovelDetailService {

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;
    
    @Autowired
    private NovelChapterInfoMapper novelChapterInfoMapper;
    
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;
    
    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;
    
    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;
    
    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;
    
    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;

    /**
     * 获取小说详情数据
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 小说详情数据
     */
    public Map<String, Object> getNovelDetail(Long novelId, Long userId) {
        System.out.println("NovelDetailService: 开始获取小说详情 - 小说ID: " + novelId + ", 用户ID: " + userId);
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. 查询小说基本信息
            System.out.println("NovelDetailService: 查询小说基本信息...");
            NovelpostBase_wlq novelBase = novelpostBaseMapper.selectById(novelId);
            
            if (novelBase == null) {
                System.out.println("NovelDetailService: 未找到小说ID为 " + novelId + " 的小说");
                throw new RuntimeException("小说不存在");
            }
            
            System.out.println("NovelDetailService: 找到小说 - 标题: " + novelBase.getNovelTitle() + 
                ", 作者: " + novelBase.getAuthorName() + ", 创建时间: " + novelBase.getCreatedAt());
            
            // 2. 查询用户对该小说的互动状态
            Map<String, Object> userInteraction = getUserInteractionStatus(userId, novelId, (byte) 3); // 3=小说类型
            
            // 3. 构建小说详情数据
            Map<String, Object> novelDetail = buildNovelDetailData(novelBase, userInteraction);
            
            // 4. 获取章节列表（只获取前几章用于预览）
            List<Map<String, Object>> chapters = getNovelChaptersPreview(novelId);
            novelDetail.put("chapters", chapters);
            
            // 5. 获取热门书评（前3条）
            List<Map<String, Object>> reviews = getNovelReviewsPreview(novelId, userId);
            novelDetail.put("reviews", reviews);
            System.out.println("NovelDetailService: 获取到书评数量: " + reviews.size());
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("NovelDetailService: 小说详情获取完成！耗时: " + duration + "ms");
            
            return novelDetail;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说详情失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取小说章节列表
     * @param novelId 小说ID
     * @return 章节列表
     */
    public List<Map<String, Object>> getNovelChapters(Long novelId) {
        System.out.println("NovelDetailService: 开始获取小说章节列表 - 小说ID: " + novelId);
        
        try {
            // 查询所有章节，按章节索引排序
            List<NovelChapterInfo_wlq> chapters = novelChapterInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelChapterInfo_wlq>()
                    .eq("novel_id", novelId)
                    .eq("is_deleted", 0)
                    .orderByAsc("chapter_index")
            );
            
            System.out.println("NovelDetailService: 查询到 " + chapters.size() + " 个章节");
            
            List<Map<String, Object>> chapterList = new ArrayList<>();
            for (NovelChapterInfo_wlq chapter : chapters) {
                Map<String, Object> chapterData = new HashMap<>();
                chapterData.put("chapterId", chapter.getChapterId());
                chapterData.put("chapterIndex", chapter.getChapterIndex());
                chapterData.put("chapterTitle", chapter.getChapterTitle());
                chapterData.put("chapterContent", chapter.getChapterContent());
                chapterData.put("createdAt", chapter.getCreatedAt());
                chapterData.put("updatedAt", chapter.getUpdatedAt());
                
                chapterList.add(chapterData);
            }
            
            return chapterList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说章节列表失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取指定章节内容
     * @param chapterId 章节ID
     * @return 章节内容
     */
    public Map<String, Object> getChapterContent(Long chapterId) {
        System.out.println("NovelDetailService: 开始获取章节内容 - 章节ID: " + chapterId);
        
        try {
            NovelChapterInfo_wlq chapter = novelChapterInfoMapper.selectById(chapterId);
            
            if (chapter == null) {
                System.out.println("NovelDetailService: 未找到章节ID为 " + chapterId + " 的章节");
                throw new RuntimeException("章节不存在");
            }
            
            Map<String, Object> chapterData = new HashMap<>();
            chapterData.put("chapterId", chapter.getChapterId());
            chapterData.put("novelId", chapter.getNovelId());
            chapterData.put("chapterIndex", chapter.getChapterIndex());
            chapterData.put("chapterTitle", chapter.getChapterTitle());
            chapterData.put("chapterContent", chapter.getChapterContent());
            chapterData.put("createdAt", chapter.getCreatedAt());
            chapterData.put("updatedAt", chapter.getUpdatedAt());
            
            System.out.println("NovelDetailService: 成功获取章节内容 - " + chapter.getChapterTitle());
            
            return chapterData;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取章节内容失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取小说章节预览（前几章）
     * @param novelId 小说ID
     * @return 章节预览列表
     */
    private List<Map<String, Object>> getNovelChaptersPreview(Long novelId) {
        try {
            // 只获取前3章用于预览
            List<NovelChapterInfo_wlq> chapters = novelChapterInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelChapterInfo_wlq>()
                    .eq("novel_id", novelId)
                    .eq("is_deleted", 0)
                    .orderByAsc("chapter_index")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> chapterList = new ArrayList<>();
            for (NovelChapterInfo_wlq chapter : chapters) {
                Map<String, Object> chapterData = new HashMap<>();
                chapterData.put("chapterId", chapter.getChapterId());
                chapterData.put("chapterIndex", chapter.getChapterIndex());
                chapterData.put("chapterTitle", chapter.getChapterTitle());
                // 预览时只显示内容的前200个字符
                String content = chapter.getChapterContent();
                if (content != null && content.length() > 200) {
                    content = content.substring(0, 200) + "...";
                }
                chapterData.put("chapterContent", content);
                chapterData.put("createdAt", chapter.getCreatedAt());
                
                chapterList.add(chapterData);
            }
            
            return chapterList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取章节预览失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取用户互动状态
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型
     * @return 互动状态
     */
    private Map<String, Object> getUserInteractionStatus(Long userId, Long contentId, Byte contentType) {
        Map<String, Object> interaction = new HashMap<>();
        
        try {
            // 查询点赞状态
            ContentLikeRelation_wlq likeRelation = contentLikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isLiked", likeRelation != null);
            
            // 查询点踩状态
            ContentDislikeRelation_wlq dislikeRelation = contentDislikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isDisliked", dislikeRelation != null);
            
            // 查询收藏状态
            ContentFavouriteRelation_wlq favoriteRelation = contentFavouriteRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isFavorited", favoriteRelation != null);
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取用户互动状态失败: " + e.getMessage());
            interaction.put("isLiked", false);
            interaction.put("isDisliked", false);
            interaction.put("isFavorited", false);
        }
        
        return interaction;
    }
    
    /**
     * 构建小说详情数据
     * @param novelBase 小说基础信息
     * @param userInteraction 用户互动状态
     * @return 小说详情数据
     */
    private Map<String, Object> buildNovelDetailData(NovelpostBase_wlq novelBase, Map<String, Object> userInteraction) {
        Map<String, Object> novelDetail = new HashMap<>();
        
        // 基本信息
        novelDetail.put("novelId", novelBase.getNovelId());
        novelDetail.put("title", novelBase.getNovelTitle());
        novelDetail.put("author", novelBase.getAuthorName() != null ? novelBase.getAuthorName() : "未知作者");
        novelDetail.put("authorId", novelBase.getAuthorId());
        novelDetail.put("description", novelBase.getNovelDescription());
        novelDetail.put("coverImage", novelBase.getNovelCoverUrl());
        novelDetail.put("rating", novelBase.getNovelScore() != null ? novelBase.getNovelScore().toString() : "4.5");
        novelDetail.put("status", novelBase.getNovelStatus());
        novelDetail.put("isVisible", novelBase.getIsVisible());
        
        // 统计数据
        novelDetail.put("readingCount", novelBase.getReadingCount() != null ? novelBase.getReadingCount() : 0);
        novelDetail.put("likeCount", novelBase.getLikeCount() != null ? novelBase.getLikeCount() : 0);
        novelDetail.put("dislikeCount", novelBase.getDislikeCount() != null ? novelBase.getDislikeCount() : 0);
        novelDetail.put("commentCount", novelBase.getCommentCount() != null ? novelBase.getCommentCount() : 0);
        novelDetail.put("favoriteCount", novelBase.getFavoriteCount() != null ? novelBase.getFavoriteCount() : 0);
        novelDetail.put("viewCount", novelBase.getViewCount() != null ? novelBase.getViewCount() : 0);
        
        // 格式化显示数据
        novelDetail.put("reviewCount", formatCount(novelBase.getCommentCount() != null ? novelBase.getCommentCount() : 0) + "人点评");
        novelDetail.put("readerCount", formatCount(novelBase.getReadingCount() != null ? novelBase.getReadingCount() : 0) + "人");
        novelDetail.put("wordCount", "12.3万字"); // 可以从章节内容计算，这里先用固定值
        novelDetail.put("updateDays", "连续更新30天"); // 可以根据最后更新时间计算
        
        // 标签处理
        List<String> tags = new ArrayList<>();
        if (novelBase.getSoftTags() != null && !novelBase.getSoftTags().isEmpty()) {
            String[] tagArray = novelBase.getSoftTags().split(",");
            for (String tag : tagArray) {
                if (tag.trim().length() > 0) {
                    tags.add("#" + tag.trim());
                }
            }
        }
        if (tags.isEmpty()) {
            tags.add("#原创小说");
            tags.add("#推荐");
        }
        novelDetail.put("tags", tags);
        
        // 简介处理
        String synopsis = novelBase.getNovelDescription();
        if (synopsis == null || synopsis.isEmpty()) {
            synopsis = "暂无简介";
        } else if (synopsis.length() > 200) {
            synopsis = synopsis.substring(0, 200) + "...";
        }
        novelDetail.put("synopsis", synopsis);
        
        // 用户互动状态
        novelDetail.put("isLiked", userInteraction.get("isLiked"));
        novelDetail.put("isDisliked", userInteraction.get("isDisliked"));
        novelDetail.put("isFavorited", userInteraction.get("isFavorited"));
        
        // 时间信息
        novelDetail.put("createdAt", novelBase.getCreatedAt());
        novelDetail.put("updatedAt", novelBase.getUpdatedAt());
        novelDetail.put("timeAgo", formatTimeAgo(novelBase.getCreatedAt()));
        
        return novelDetail;
    }
    
    /**
     * 格式化数字显示
     */
    private String formatCount(int count) {
        if (count >= 10000) {
            return (count / 10000.0) + "万";
        }
        return String.valueOf(count);
    }
    
    /**
     * 获取小说书评预览（前3条）
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评预览列表
     */
    private List<Map<String, Object>> getNovelReviewsPreview(Long novelId, Long userId) {
        try {
            // 获取前3条顶级评论
            List<NovelpostComment_wlq> comments = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("novel_id", novelId)
                    .isNull("parent_comment_id")
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (NovelpostComment_wlq comment : comments) {
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("reviewer", "书友" + comment.getUserId());
                reviewData.put("avatar", "/static/avatar-default.png");
                reviewData.put("rating", 5); // 默认5星
                reviewData.put("content", comment.getCommentText());
                reviewData.put("time", formatTimeAgo(comment.getCreatedAt()));
                
                // 获取真实的点赞数和点赞状态
                int likeCount = getCommentLikeCount(comment.getNovelpostCommentId());
                boolean isLiked = userId != null ? checkUserLikedComment(userId, comment.getNovelpostCommentId()) : false;
                
                reviewData.put("likes", likeCount);
                reviewData.put("dislikes", 0);
                reviewData.put("comments", 0);
                reviewData.put("isLiked", isLiked);
                reviewData.put("isDisliked", false);
                reviewData.put("commentId", comment.getNovelpostCommentId());
                
                // 获取回复
                List<Map<String, Object>> replies = getCommentReplies(comment.getNovelpostCommentId());
                reviewData.put("replies", replies);
                
                reviewList.add(reviewData);
            }
            
            return reviewList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取书评预览失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取评论的回复
     * @param parentCommentId 父评论ID
     * @return 回复列表
     */
    private List<Map<String, Object>> getCommentReplies(Long parentCommentId) {
        try {
            List<NovelpostComment_wlq> replies = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("parent_comment_id", parentCommentId)
                    .eq("is_deleted", 0)
                    .orderByAsc("created_at")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> replyList = new ArrayList<>();
            for (NovelpostComment_wlq reply : replies) {
                Map<String, Object> replyData = new HashMap<>();
                replyData.put("username", "书友" + reply.getUserId());
                replyData.put("userAvatar", "/static/avatar-default.png");
                replyData.put("time", formatTimeAgo(reply.getCreatedAt()));
                replyData.put("content", reply.getCommentText());
                replyData.put("likes", reply.getLikeCount() != null ? reply.getLikeCount() : 0);
                replyData.put("isLiked", false);
                replyData.put("replyTo", "书友" + reply.getUserId()); // 简化处理
                
                replyList.add(replyData);
            }
            
            return replyList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取评论回复失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取小说所有书评
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评列表
     */
    public List<Map<String, Object>> getNovelReviews(Long novelId, Long userId) {
        System.out.println("NovelDetailService: 开始获取小说书评 - 小说ID: " + novelId);
        
        try {
            // 获取所有顶级评论
            List<NovelpostComment_wlq> comments = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("novel_id", novelId)
                    .isNull("parent_comment_id")
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at")
            );
            
            System.out.println("NovelDetailService: 查询到 " + comments.size() + " 条书评");
            
            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (NovelpostComment_wlq comment : comments) {
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("reviewer", "书友" + comment.getUserId());
                reviewData.put("avatar", "/static/avatar-default.png");
                reviewData.put("rating", 5); // 默认5星
                reviewData.put("content", comment.getCommentText());
                reviewData.put("time", formatTimeAgo(comment.getCreatedAt()));
                
                // 获取真实的点赞数和点赞状态
                int likeCount = getCommentLikeCount(comment.getNovelpostCommentId());
                boolean isLiked = userId != null ? checkUserLikedComment(userId, comment.getNovelpostCommentId()) : false;
                
                reviewData.put("likes", likeCount);
                reviewData.put("dislikes", 0);
                reviewData.put("comments", 0);
                reviewData.put("isLiked", isLiked);
                reviewData.put("isDisliked", false);
                reviewData.put("commentId", comment.getNovelpostCommentId());
                
                // 获取所有回复
                List<Map<String, Object>> replies = getCommentReplies(comment.getNovelpostCommentId());
                reviewData.put("replies", replies);
                
                reviewList.add(reviewData);
            }
            
            return reviewList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说书评失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 处理评论点赞/取消点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 操作结果
     */
    public Map<String, Object> handleCommentLike(Long userId, Long commentId) {
        System.out.println("NovelDetailService: 处理评论点赞 - 用户ID: " + userId + ", 评论ID: " + commentId);
        
        try {
            // 检查是否已经点赞
            CommentLikeRelation_wlq existingLike = commentLikeRelationMapper.selectByUserIdAndCommentId(userId, commentId);
            
            if (existingLike != null) {
                // 已存在点赞记录，切换状态
                existingLike.setIsActive(existingLike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                commentLikeRelationMapper.updateById(existingLike);
                
                System.out.println("NovelDetailService: 更新点赞状态 - " + (existingLike.getIsActive() == 1 ? "点赞" : "取消点赞"));
            } else {
                // 创建新的点赞记录
                CommentLikeRelation_wlq newLike = new CommentLikeRelation_wlq();
                newLike.setUserId(userId);
                newLike.setCommentId(commentId);
                newLike.setIsActive((byte) 1);
                newLike.setType((byte) 2); // 2-小说类型
                newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                commentLikeRelationMapper.insert(newLike);
                System.out.println("NovelDetailService: 创建新点赞记录");
            }
            
            // 更新评论的点赞数
            updateCommentLikeCount(commentId);
            
            // 获取更新后的点赞状态
            boolean isLiked = checkUserLikedComment(userId, commentId);
            int likeCount = getCommentLikeCount(commentId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isLiked", isLiked);
            result.put("likeCount", likeCount);
            result.put("message", isLiked ? "点赞成功" : "取消点赞成功");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 检查用户是否点赞了评论
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    public boolean checkUserLikedComment(Long userId, Long commentId) {
        try {
            int count = commentLikeRelationMapper.checkUserLikedComment(userId, commentId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 检查用户点赞状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取评论的点赞数
     * @param commentId 评论ID
     * @return 点赞数
     */
    public int getCommentLikeCount(Long commentId) {
        try {
            return commentLikeRelationMapper.countActiveLikesByCommentId(commentId);
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取评论点赞数失败: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 更新评论的点赞数
     * @param commentId 评论ID
     */
    private void updateCommentLikeCount(Long commentId) {
        try {
            int likeCount = getCommentLikeCount(commentId);
            
            // 更新评论表中的点赞数
            NovelpostComment_wlq comment = novelpostCommentMapper.selectById(commentId);
            if (comment != null) {
                comment.setLikeCount(likeCount);
                novelpostCommentMapper.updateById(comment);
                
                System.out.println("NovelDetailService: 更新评论点赞数 - 评论ID: " + commentId + ", 点赞数: " + likeCount);
            }
        } catch (Exception e) {
            System.err.println("NovelDetailService: 更新评论点赞数失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户对书评的点赞状态
     * @param userId 用户ID
     * @param reviews 书评列表
     * @return 更新后的书评列表
     */
    public List<Map<String, Object>> updateReviewLikeStatus(Long userId, List<Map<String, Object>> reviews) {
        try {
            for (Map<String, Object> review : reviews) {
                Long commentId = (Long) review.get("commentId");
                if (commentId != null) {
                    boolean isLiked = checkUserLikedComment(userId, commentId);
                    int likeCount = getCommentLikeCount(commentId);
                    
                    review.put("isLiked", isLiked);
                    review.put("likes", likeCount);
                }
            }
            return reviews;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 更新书评点赞状态失败: " + e.getMessage());
            return reviews;
        }
    }
    
    /**
     * 格式化时间显示
     */
    private String formatTimeAgo(Date date) {
        if (date == null) return "刚刚";
        
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;
        
        long minutes = diff / (1000 * 60);
        long hours = diff / (1000 * 60 * 60);
        long days = diff / (1000 * 60 * 60 * 24);
        
        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        if (hours < 24) return hours + "小时前";
        if (days < 7) return days + "天前";
        return "一周前";
    }
}
