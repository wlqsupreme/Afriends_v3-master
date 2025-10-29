package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 互动消息页面服务类
 * 负责处理各种互动消息数据的查询和整合
 */
@Service
public class InteractionMessagesService {

    // 内容互动关系Mapper
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;
    
    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;
    
    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;
    
    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;
    
    // 各种评论Mapper
    @Autowired
    private TextpostCommentMapper textpostCommentMapper;
    
    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;
    
    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;
    
    @Autowired
    private ILikeCommentMapper iLikeCommentMapper;
    
    @Autowired
    private IDislikeCommentMapper iDislikeCommentMapper;
    
    @Autowired
    private IHaveCommentMapper iHaveCommentMapper;
    
    @Autowired
    private INeedCommentMapper iNeedCommentMapper;

    /**
     * 获取用户的互动消息数据
     * @param userId 用户ID
     * @param filterType 筛选类型：all, likes, received-comments, sent-comments
     * @return 整合后的互动消息数据列表
     */
    public List<Map<String, Object>> getInteractionMessagesData(Long userId, String filterType) {
        System.out.println("InteractionMessagesService: 开始获取用户 " + userId + " 的互动消息数据，筛选类型: " + filterType);
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> messagesData = new ArrayList<>();
        
        try {
            // 检查Mapper是否注入成功
            System.out.println("=== InteractionMessagesService: 检查Mapper注入状态 ===");
            System.out.println("contentLikeRelationMapper: " + (contentLikeRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("contentDislikeRelationMapper: " + (contentDislikeRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("contentFavouriteRelationMapper: " + (contentFavouriteRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("commentLikeRelationMapper: " + (commentLikeRelationMapper != null ? "已注入" : "未注入"));
            
            // 根据筛选类型获取不同的数据
            switch (filterType) {
                case "all":
                    // 获取所有类型的互动消息
                    messagesData.addAll(getLikesAndFavorites(userId));
                    messagesData.addAll(getReceivedComments(userId));
                    messagesData.addAll(getSentComments(userId));
                    break;
                case "likes":
                    // 只获取赞与收藏
                    messagesData.addAll(getLikesAndFavorites(userId));
                    break;
                case "received-comments":
                    // 只获取收到的评论
                    messagesData.addAll(getReceivedComments(userId));
                    break;
                case "sent-comments":
                    // 只获取发出的评论
                    messagesData.addAll(getSentComments(userId));
                    break;
                default:
                    // 默认获取所有数据
                    messagesData.addAll(getLikesAndFavorites(userId));
                    messagesData.addAll(getReceivedComments(userId));
                    messagesData.addAll(getSentComments(userId));
                    break;
            }
            
            // 按时间排序（最新的在前）
            messagesData.sort((a, b) -> {
                Date timeA = (Date) a.get("createdAt");
                Date timeB = (Date) b.get("createdAt");
                if (timeA == null || timeB == null) return 0;
                return timeB.compareTo(timeA);
            });
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("InteractionMessagesService: 用户互动消息数据获取完成！总共 " + messagesData.size() + " 条数据，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取互动消息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messagesData;
    }
    
    /**
     * 获取点赞和收藏消息
     */
    private List<Map<String, Object>> getLikesAndFavorites(Long userId) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            // 获取用户收到的点赞（查询用户发布的内容被点赞的记录）
            // 这里需要先查询用户发布的内容，然后查询这些内容被点赞的记录
            // 由于涉及多个表，这里先创建一些模拟数据
            
            // 模拟点赞消息
            Map<String, Object> likeMessage1 = new HashMap<>();
            likeMessage1.put("id", 1L);
            likeMessage1.put("type", "like");
            likeMessage1.put("actionType", "like");
            likeMessage1.put("fromUserId", 1000001L);
            likeMessage1.put("fromUsername", "鸢尾");
            likeMessage1.put("fromUserAvatar", "");
            likeMessage1.put("contentType", "novel");
            likeMessage1.put("contentId", 1001L);
            likeMessage1.put("contentTitle", "《修仙传》");
            likeMessage1.put("contentThumbnail", "");
            likeMessage1.put("messageText", "赞了你分享的小说");
            likeMessage1.put("createdAt", new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L)); // 2天前
            likeMessage1.put("timeAgo", "2天前");
            likeMessage1.put("isRead", false);
            messages.add(likeMessage1);
            
            // 模拟收藏消息
            Map<String, Object> favoriteMessage1 = new HashMap<>();
            favoriteMessage1.put("id", 2L);
            favoriteMessage1.put("type", "favorite");
            favoriteMessage1.put("actionType", "favorite");
            favoriteMessage1.put("fromUserId", 1000002L);
            favoriteMessage1.put("fromUsername", "傻不拉几");
            favoriteMessage1.put("fromUserAvatar", "");
            favoriteMessage1.put("contentType", "image");
            favoriteMessage1.put("contentId", 1002L);
            favoriteMessage1.put("contentTitle", "美丽的风景");
            favoriteMessage1.put("contentThumbnail", "");
            favoriteMessage1.put("messageText", "收藏了你的图文");
            favoriteMessage1.put("createdAt", new Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000L)); // 4天前
            favoriteMessage1.put("timeAgo", "4天前");
            favoriteMessage1.put("isRead", false);
            messages.add(favoriteMessage1);
            
            // 模拟点赞消息
            Map<String, Object> likeMessage2 = new HashMap<>();
            likeMessage2.put("id", 3L);
            likeMessage2.put("type", "like");
            likeMessage2.put("actionType", "like");
            likeMessage2.put("fromUserId", 1000003L);
            likeMessage2.put("fromUsername", "何瑜鑫");
            likeMessage2.put("fromUserAvatar", "");
            likeMessage2.put("contentType", "image");
            likeMessage2.put("contentId", 1003L);
            likeMessage2.put("contentTitle", "美食分享");
            likeMessage2.put("contentThumbnail", "");
            likeMessage2.put("messageText", "赞了你的图文");
            likeMessage2.put("createdAt", new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L)); // 6天前
            likeMessage2.put("timeAgo", "6天前");
            likeMessage2.put("isRead", true);
            messages.add(likeMessage2);
            
            // 模拟点赞消息
            Map<String, Object> likeMessage3 = new HashMap<>();
            likeMessage3.put("id", 4L);
            likeMessage3.put("type", "like");
            likeMessage3.put("actionType", "like");
            likeMessage3.put("fromUserId", 1000004L);
            likeMessage3.put("fromUsername", "尹星雅");
            likeMessage3.put("fromUserAvatar", "");
            likeMessage3.put("contentType", "image");
            likeMessage3.put("contentId", 1004L);
            likeMessage3.put("contentTitle", "旅行日记");
            likeMessage3.put("contentThumbnail", "");
            likeMessage3.put("messageText", "赞了你的图文");
            likeMessage3.put("createdAt", new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L)); // 6天前
            likeMessage3.put("timeAgo", "6天前");
            likeMessage3.put("isRead", true);
            messages.add(likeMessage3);
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条点赞和收藏消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取点赞和收藏消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 获取收到的评论消息
     */
    private List<Map<String, Object>> getReceivedComments(Long userId) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            // 模拟收到的评论消息
            Map<String, Object> commentMessage1 = new HashMap<>();
            commentMessage1.put("id", 5L);
            commentMessage1.put("type", "comment");
            commentMessage1.put("actionType", "received-comment");
            commentMessage1.put("fromUserId", 1000005L);
            commentMessage1.put("fromUsername", "康乃馨");
            commentMessage1.put("fromUserAvatar", "");
            commentMessage1.put("contentType", "video");
            commentMessage1.put("contentId", 1005L);
            commentMessage1.put("contentTitle", "我的视频");
            commentMessage1.put("contentThumbnail", "");
            commentMessage1.put("messageText", "评论了你的视频");
            commentMessage1.put("commentText", "这个视频太棒了！");
            commentMessage1.put("createdAt", new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L)); // 7天前
            commentMessage1.put("timeAgo", "7天前");
            commentMessage1.put("isRead", true);
            messages.add(commentMessage1);
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条收到的评论消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取收到的评论消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 获取发出的评论消息
     */
    private List<Map<String, Object>> getSentComments(Long userId) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            // 模拟发出的评论消息
            Map<String, Object> sentCommentMessage1 = new HashMap<>();
            sentCommentMessage1.put("id", 6L);
            sentCommentMessage1.put("type", "comment");
            sentCommentMessage1.put("actionType", "sent-comment");
            sentCommentMessage1.put("toUserId", 1000006L);
            sentCommentMessage1.put("toUsername", "张三");
            sentCommentMessage1.put("toUserAvatar", "");
            sentCommentMessage1.put("contentType", "text");
            sentCommentMessage1.put("contentId", 1006L);
            sentCommentMessage1.put("contentTitle", "今天天气真好");
            sentCommentMessage1.put("contentThumbnail", "");
            sentCommentMessage1.put("messageText", "你评论了张三的文字");
            sentCommentMessage1.put("commentText", "确实，阳光明媚！");
            sentCommentMessage1.put("createdAt", new Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L)); // 1天前
            sentCommentMessage1.put("timeAgo", "1天前");
            sentCommentMessage1.put("isRead", false);
            messages.add(sentCommentMessage1);
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条发出的评论消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取发出的评论消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 标记所有消息为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> markAllAsRead(Long userId) {
        System.out.println("InteractionMessagesService: 标记用户 " + userId + " 的所有消息为已读");
        
        try {
            // 这里应该更新数据库中所有未读消息的状态
            // 由于当前是模拟数据，这里只返回成功结果
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "已全部标记为已读");
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("InteractionMessagesService: 标记所有消息为已读成功");
            
            return response;
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "标记所有消息为已读失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
}
