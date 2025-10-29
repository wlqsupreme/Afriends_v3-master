package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 帖子详情页面服务类
 * 负责处理图文和文字帖子详情数据的查询和操作
 */
@Service
public class PostDetailService {

    // 帖子基础Mapper
    @Autowired
    private TextpostBaseMapper textpostBaseMapper;
    
    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;
    
    // 评论Mapper
    @Autowired
    private TextpostCommentMapper textpostCommentMapper;
    
    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;
    
    // 内容互动关系Mapper
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;
    
    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;
    
    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;

    /**
     * 获取帖子详情数据
     * @param postId 帖子ID
     * @param postType 帖子类型：text, image
     * @param userId 当前用户ID
     * @return 帖子详情数据
     */
    public Map<String, Object> getPostDetailData(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 开始获取帖子详情数据 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> postDetail = new HashMap<>();
            
            if ("text".equals(postType)) {
                // 获取文字帖子详情
                postDetail = getTextPostDetail(postId, userId);
            } else if ("image".equals(postType)) {
                // 获取图片帖子详情
                postDetail = getImagePostDetail(postId, userId);
            } else {
                throw new IllegalArgumentException("不支持的帖子类型: " + postType);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("PostDetailService: 获取帖子详情数据完成，耗时: " + duration + "ms");
            
            return postDetail;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 获取帖子详情数据失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取文字帖子详情
     */
    private Map<String, Object> getTextPostDetail(Long postId, Long userId) {
        System.out.println("PostDetailService: 获取文字帖子详情 - 帖子ID: " + postId);
        
        // 查询文字帖子基础信息
        TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
        if (textPost == null) {
            throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
        }
        
        // 构建帖子详情数据
        Map<String, Object> postDetail = new HashMap<>();
        postDetail.put("id", textPost.getPostId());
        postDetail.put("type", "text");
        postDetail.put("reviewer", "用户" + textPost.getUserId()); // 这里应该查询用户信息
        postDetail.put("avatar", "/static/avatar1.png");
        postDetail.put("rating", 5); // 文字帖子默认5星
        postDetail.put("content", textPost.getContentText());
        postDetail.put("time", formatTimeAgo(textPost.getCreatedAt()));
        postDetail.put("likes", textPost.getLikeCount() != null ? textPost.getLikeCount() : 0);
        postDetail.put("dislikes", textPost.getDislikeCount() != null ? textPost.getDislikeCount() : 0);
        postDetail.put("favorites", textPost.getCollectCount() != null ? textPost.getCollectCount() : 0);
        postDetail.put("isLiked", checkUserLikeStatus(userId, postId, (byte) 1));
        postDetail.put("isDisliked", checkUserDislikeStatus(userId, postId, (byte) 1));
        postDetail.put("isFavorited", checkUserFavoriteStatus(userId, postId, (byte) 1));
        postDetail.put("isFollowed", false); // 默认未关注
        postDetail.put("comments", getTextPostComments(postId));
        
        System.out.println("PostDetailService: 文字帖子详情构建完成");
        return postDetail;
    }
    
    /**
     * 获取图片帖子详情
     */
    private Map<String, Object> getImagePostDetail(Long postId, Long userId) {
        System.out.println("PostDetailService: 获取图片帖子详情 - 帖子ID: " + postId);
        
        // 查询图片帖子基础信息
        ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
        if (imagePost == null) {
            throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
        }
        
        // 构建帖子详情数据
        Map<String, Object> postDetail = new HashMap<>();
        postDetail.put("id", imagePost.getPostId());
        postDetail.put("type", "image");
        postDetail.put("reviewer", "用户" + imagePost.getUserId()); // 这里应该查询用户信息
        postDetail.put("avatar", "/static/avatar1.png");
        postDetail.put("rating", 5); // 图片帖子默认5星
        postDetail.put("content", imagePost.getContentText());
        postDetail.put("time", formatTimeAgo(imagePost.getCreatedAt()));
        postDetail.put("likes", imagePost.getLikeCount() != null ? imagePost.getLikeCount() : 0);
        postDetail.put("dislikes", imagePost.getDislikeCount() != null ? imagePost.getDislikeCount() : 0);
        postDetail.put("favorites", imagePost.getCollectCount() != null ? imagePost.getCollectCount() : 0);
        postDetail.put("isLiked", checkUserLikeStatus(userId, postId, (byte) 2));
        postDetail.put("isDisliked", checkUserDislikeStatus(userId, postId, (byte) 2));
        postDetail.put("isFavorited", checkUserFavoriteStatus(userId, postId, (byte) 2));
        postDetail.put("isFollowed", false); // 默认未关注
        postDetail.put("comments", getImagePostComments(postId));
        
        // 处理图片URLs
        List<String> images = new ArrayList<>();
        if (imagePost.getImageUrls() != null && !imagePost.getImageUrls().isEmpty()) {
            String[] imageArray = imagePost.getImageUrls().split(",");
            for (String imageUrl : imageArray) {
                if (imageUrl.trim().length() > 0) {
                    images.add(imageUrl.trim());
                }
            }
        }
        if (images.isEmpty()) {
            images.add("/static/default-image.png");
        }
        postDetail.put("images", images);
        
        System.out.println("PostDetailService: 图片帖子详情构建完成");
        return postDetail;
    }
    
    /**
     * 获取文字帖子评论
     */
    private List<Map<String, Object>> getTextPostComments(Long postId) {
        System.out.println("PostDetailService: 获取文字帖子评论 - 帖子ID: " + postId);
        
        // 查询评论数据
        List<TextpostComment_wlq> comments = textpostCommentMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TextpostComment_wlq>()
                .eq("text_content_id", postId)
                .eq("is_deleted", 0)
                .orderByDesc("created_at")
        );
        
        List<Map<String, Object>> commentList = new ArrayList<>();
        for (TextpostComment_wlq comment : comments) {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", "用户" + comment.getUserId());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", new ArrayList<>()); // 暂时没有回复功能
            commentList.add(commentData);
        }
        
        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条文字帖子评论");
        return commentList;
    }
    
    /**
     * 获取图片帖子评论
     */
    private List<Map<String, Object>> getImagePostComments(Long postId) {
        System.out.println("PostDetailService: 获取图片帖子评论 - 帖子ID: " + postId);
        
        // 查询评论数据
        List<ImagePostComment_wlq> comments = imagePostCommentMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImagePostComment_wlq>()
                .eq("post_id", postId)
                .eq("is_deleted", 0)
                .orderByDesc("created_at")
        );
        
        List<Map<String, Object>> commentList = new ArrayList<>();
        for (ImagePostComment_wlq comment : comments) {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", "用户" + comment.getUserId());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", new ArrayList<>()); // 暂时没有回复功能
            commentList.add(commentData);
        }
        
        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条图片帖子评论");
        return commentList;
    }
    
    /**
     * 检查用户点赞状态
     */
    private boolean checkUserLikeStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentLikeRelation_wlq likeRelation = contentLikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("content_type", contentType)
                    .eq("is_active", 1)
            );
            return likeRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户点赞状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查用户点踩状态
     */
    private boolean checkUserDislikeStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentDislikeRelation_wlq dislikeRelation = contentDislikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("content_type", contentType)
                    .eq("is_active", 1)
            );
            return dislikeRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户点踩状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查用户收藏状态
     */
    private boolean checkUserFavoriteStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentFavouriteRelation_wlq favoriteRelation = contentFavouriteRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("content_type", contentType)
                    .eq("is_active", 1)
            );
            return favoriteRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户收藏状态失败: " + e.getMessage());
            return false;
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
    
    /**
     * 处理点赞操作
     */
    public Map<String, Object> handleLike(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理点赞操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            
            // 查询是否已有点赞记录
            ContentLikeRelation_wlq existingLike = contentLikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", postId)
                    .eq("content_type", contentType)
            );
            
            boolean isLiked;
            if (existingLike != null) {
                // 已存在记录，切换状态
                existingLike.setIsActive(existingLike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentLikeRelationMapper.updateById(existingLike);
                isLiked = existingLike.getIsActive() == 1;
            } else {
                // 创建新的点赞记录
                ContentLikeRelation_wlq newLike = new ContentLikeRelation_wlq();
                Long newId = generateNewId(contentLikeRelationMapper);
                newLike.setId(newId);
                newLike.setUserId(userId);
                newLike.setContentId(postId);
                newLike.setIsActive((byte) 1);
                newLike.setContentType(contentType);
                newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                contentLikeRelationMapper.insert(newLike);
                isLiked = true;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", isLiked ? "点赞成功" : "取消点赞成功");
            response.put("isLiked", isLiked);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理点赞操作失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "点赞操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
    
    /**
     * 处理点踩操作
     */
    public Map<String, Object> handleDislike(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理点踩操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            
            // 查询是否已有点踩记录
            ContentDislikeRelation_wlq existingDislike = contentDislikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", postId)
                    .eq("content_type", contentType)
            );
            
            boolean isDisliked;
            if (existingDislike != null) {
                // 已存在记录，切换状态
                existingDislike.setIsActive(existingDislike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentDislikeRelationMapper.updateById(existingDislike);
                isDisliked = existingDislike.getIsActive() == 1;
            } else {
                // 创建新的点踩记录
                ContentDislikeRelation_wlq newDislike = new ContentDislikeRelation_wlq();
                Long newId = generateNewId(contentDislikeRelationMapper);
                newDislike.setId(newId);
                newDislike.setUserId(userId);
                newDislike.setContentId(postId);
                newDislike.setIsActive((byte) 1);
                newDislike.setContentType(contentType);
                newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                contentDislikeRelationMapper.insert(newDislike);
                isDisliked = true;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
            response.put("isDisliked", isDisliked);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理点踩操作失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "点踩操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
    
    /**
     * 处理收藏操作
     */
    public Map<String, Object> handleFavorite(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理收藏操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            
            // 查询是否已有收藏记录
            ContentFavouriteRelation_wlq existingFavorite = contentFavouriteRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", postId)
                    .eq("content_type", contentType)
            );
            
            boolean isFavorited;
            if (existingFavorite != null) {
                // 已存在记录，切换状态
                existingFavorite.setIsActive(existingFavorite.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentFavouriteRelationMapper.updateById(existingFavorite);
                isFavorited = existingFavorite.getIsActive() == 1;
            } else {
                // 创建新的收藏记录
                ContentFavouriteRelation_wlq newFavorite = new ContentFavouriteRelation_wlq();
                Long newId = generateNewId(contentFavouriteRelationMapper);
                newFavorite.setId(newId);
                newFavorite.setUserId(userId);
                newFavorite.setContentId(postId);
                newFavorite.setFolderName("默认收藏夹");
                newFavorite.setIsActive((byte) 1);
                newFavorite.setContentType(contentType);
                newFavorite.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                contentFavouriteRelationMapper.insert(newFavorite);
                isFavorited = true;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", isFavorited ? "收藏成功" : "取消收藏成功");
            response.put("isFavorited", isFavorited);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理收藏操作失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "收藏操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
    
    /**
     * 提交评论
     */
    public Map<String, Object> submitComment(Long postId, String postType, Long userId, String commentText, Long parentCommentId) {
        System.out.println("PostDetailService: 提交评论 - 帖子ID: " + postId + ", 帖子类型: " + postType + 
            ", 用户ID: " + userId + ", 评论内容: " + commentText + ", 父评论ID: " + parentCommentId);
        
        try {
            if ("text".equals(postType)) {
                // 提交文字帖子评论
                TextpostComment_wlq comment = new TextpostComment_wlq();
                Long newId = generateNewId(textpostCommentMapper);
                comment.setTextpostCommentId(newId);
                comment.setUserId(userId);
                comment.setParentCommentId(parentCommentId);
                comment.setCommentText(commentText);
                comment.setLikeCount(0);
                comment.setIsVisible((byte) 1);
                comment.setStatus((byte) 1);
                comment.setIsDeleted((byte) 0);
                comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setTextContentId(postId);
                
                textpostCommentMapper.insert(comment);
            } else if ("image".equals(postType)) {
                // 提交图片帖子评论
                ImagePostComment_wlq comment = new ImagePostComment_wlq();
                Long newId = generateNewId(imagePostCommentMapper);
                comment.setImagepostCommentId(newId);
                comment.setPostId(postId);
                comment.setUserId(userId);
                comment.setParentCommentId(parentCommentId);
                comment.setCommentText(commentText);
                comment.setLikeCount(0);
                comment.setIsVisible((byte) 1);
                comment.setStatus((byte) 1);
                comment.setIsDeleted((byte) 0);
                comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                imagePostCommentMapper.insert(comment);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "评论成功");
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 提交评论失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
    
    /**
     * 生成从10000010开始的自增ID
     */
    private Long generateNewId(Object mapper) {
        try {
            Long maxId = 10000009L; // 起始值减1
            
            if (mapper instanceof ContentLikeRelationMapper) {
                List<ContentLikeRelation_wlq> allLikes = contentLikeRelationMapper.selectList(null);
                for (ContentLikeRelation_wlq like : allLikes) {
                    if (like.getId() != null && like.getId() >= 10000010L && like.getId() > maxId) {
                        maxId = like.getId();
                    }
                }
            } else if (mapper instanceof ContentDislikeRelationMapper) {
                List<ContentDislikeRelation_wlq> allDislikes = contentDislikeRelationMapper.selectList(null);
                for (ContentDislikeRelation_wlq dislike : allDislikes) {
                    if (dislike.getId() != null && dislike.getId() >= 10000010L && dislike.getId() > maxId) {
                        maxId = dislike.getId();
                    }
                }
            } else if (mapper instanceof ContentFavouriteRelationMapper) {
                List<ContentFavouriteRelation_wlq> allFavorites = contentFavouriteRelationMapper.selectList(null);
                for (ContentFavouriteRelation_wlq favorite : allFavorites) {
                    if (favorite.getId() != null && favorite.getId() >= 10000010L && favorite.getId() > maxId) {
                        maxId = favorite.getId();
                    }
                }
            } else if (mapper instanceof TextpostCommentMapper) {
                List<TextpostComment_wlq> allComments = textpostCommentMapper.selectList(null);
                for (TextpostComment_wlq comment : allComments) {
                    if (comment.getTextpostCommentId() != null && comment.getTextpostCommentId() >= 10000010L && comment.getTextpostCommentId() > maxId) {
                        maxId = comment.getTextpostCommentId();
                    }
                }
            } else if (mapper instanceof ImagePostCommentMapper) {
                List<ImagePostComment_wlq> allComments = imagePostCommentMapper.selectList(null);
                for (ImagePostComment_wlq comment : allComments) {
                    if (comment.getImagepostCommentId() != null && comment.getImagepostCommentId() >= 10000010L && comment.getImagepostCommentId() > maxId) {
                        maxId = comment.getImagepostCommentId();
                    }
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("PostDetailService: 生成新ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("PostDetailService: 生成ID失败，使用默认值: " + e.getMessage());
            return 10000010L;
        }
    }
}
