package Afriends_v3.service;

import Afriends_v3.entity.UserChatDetail_njj;
import Afriends_v3.entity.UserChatDetail_list_njj;
import Afriends_v3.mapper.UserChatDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 用户聊天详情服务类
 * 负责管理用户聊天详情数据的数据库操作和内存缓存
 */
@Service
public class UserChatDetailService {

    @Autowired
    private UserChatDetailMapper userChatDetailMapper;

    /**
     * 加载用户聊天详情数据到内存
     */
    public void loadUserChatDetailToMemory() {
        try {
            System.out.println("UserChatDetailService: 开始从数据库加载用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserChatDetailService: 使用原生SQL查询用户聊天详情数据...");
            var allUserChatDetail = userChatDetailMapper.selectAllRecords();
            System.out.println("UserChatDetailService: 原生SQL查询到 "
                    + (allUserChatDetail != null ? allUserChatDetail.size() : 0) + " 条记录");

            if (allUserChatDetail == null || allUserChatDetail.isEmpty()) {
                System.out.println("UserChatDetailService: 数据库中没有用户聊天详情数据，跳过加载");
                UserChatDetail_list_njj.loadFromDatabaseDirectly(new ArrayList<>());
                return;
            }

            UserChatDetail_list_njj.loadFromDatabaseDirectly(allUserChatDetail);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserChatDetailService: 用户聊天详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserChatDetailService: 加载用户聊天详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户聊天详情数据（从内存）
     */
    public List<UserChatDetail_njj> getAllUserChatDetailFromMemory() {
        return UserChatDetail_list_njj.getAllUserChatDetail();
    }

    /**
     * 根据ID获取用户聊天详情数据
     */
    public UserChatDetail_njj getUserChatDetailByIdFromMemory(Long id) {
        return UserChatDetail_list_njj.getUserChatDetailById(id);
    }

    /**
     * 根据会话ID获取用户聊天详情数据
     */
    public List<UserChatDetail_njj> getUserChatDetailBySessionIdFromMemory(Long sessionId) {
        return UserChatDetail_list_njj.getUserChatDetailBySessionId(sessionId);
    }

    /**
     * 根据发送者类型获取用户聊天详情数据
     */
    public List<UserChatDetail_njj> getUserChatDetailBySenderTypeFromMemory(String senderType) {
        return UserChatDetail_list_njj.getUserChatDetailBySenderType(senderType);
    }

    /**
     * 获取用户聊天详情统计信息（从内存）
     */
    public Map<String, Object> getUserChatDetailStatisticsFromMemory() {
        return UserChatDetail_list_njj.getStatistics();
    }

    public Long saveChatDetail(Map<String, Object> chatData) {
        try {
            // 生成新的聊天详情ID
            Long chatId = generateNextChatDetailId();

            // 创建聊天详情对象
            UserChatDetail_njj chatDetail = new UserChatDetail_njj();
            chatDetail.setId(chatId);

            // 设置sessionId
            Object sessionIdObj = chatData.get("sessionId");
            if (sessionIdObj != null) {
                chatDetail.setSessionId(Long.parseLong(sessionIdObj.toString()));
            }

            // 设置demandParty
            Object demandPartyObj = chatData.get("demandParty");
            if (demandPartyObj != null) {
                chatDetail.setDemandParty(demandPartyObj.toString());
            }

            // 设置message
            Object messageObj = chatData.get("message");
            if (messageObj != null) {
                chatDetail.setMessage(messageObj.toString());
            }

            // 设置responseParty
            Object responsePartyObj = chatData.get("responseParty");
            if (responsePartyObj != null) {
                chatDetail.setResponseParty(responsePartyObj.toString());
            }

            // 设置senderType
            Object senderTypeObj = chatData.get("senderType");
            if (senderTypeObj != null) {
                chatDetail.setSenderType(senderTypeObj.toString());
            }

            // 设置创建时间
            chatDetail.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int result = userChatDetailMapper.insert(chatDetail);
            if (result > 0) {
                // 添加到内存缓存
                UserChatDetail_list_njj.addToCache(chatDetail);
                System.out.println("聊天记录保存成功: chatId=" + chatId);
                return chatId;
            } else {
                System.err.println("聊天记录保存失败，数据库插入失败");
                return null;
            }
        } catch (Exception e) {
            System.err.println("保存聊天记录异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Long generateNextChatDetailId() {
        try {
            Long maxId = userChatDetailMapper.selectMaxId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成聊天详情ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 强制刷新用户聊天详情数据
     */
    public void refreshUserChatDetailData() {
        loadUserChatDetailToMemory();
    }
}
