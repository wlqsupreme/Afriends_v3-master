package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * A开头_wlq实体类综合服务
 * 负责管理所有A开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class AEntityService_wlq {

    @Autowired
    private ActionExpMapper actionExpMapper;

    @Autowired
    private AiChatListDetailRMapper aiChatListDetailRMapper;

    @Autowired
    private AiMatchesMapper aiMatchesMapper;

    @Autowired
    private AiTaskRequireMapper aiTaskRequireMapper;

    @Autowired
    private AiTaskRespondMapper aiTaskRespondMapper;

    // ActionExp 相关方法
    public void loadActionExpToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载行为经验数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询行为经验数据...");
            var allActionExp = actionExpMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allActionExp.size() + " 条记录");

            if (allActionExp.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有行为经验数据");
                return;
            }

            ActionExp_list_wlq.loadFromDatabaseDirectly(allActionExp);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: 行为经验数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载行为经验数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ActionExp_wlq> getAllActionExpFromMemory() {
        return ActionExp_list_wlq.getAllActionExp();
    }

    public Map<String, Object> getActionExpStatisticsFromMemory() {
        return ActionExp_list_wlq.getStatistics();
    }

    // AiChatListDetailR 相关方法
    public void loadAiChatListDetailRToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI聊天列表详情数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI聊天列表详情数据...");
            var allAiChatListDetailR = aiChatListDetailRMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiChatListDetailR.size() + " 条记录");

            if (allAiChatListDetailR.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI聊天列表详情数据");
                return;
            }

            AiChatListDetailR_list_wlq.loadFromDatabaseDirectly(allAiChatListDetailR);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI聊天列表详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI聊天列表详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiChatListDetailR_wlq> getAllAiChatListDetailRFromMemory() {
        return AiChatListDetailR_list_wlq.getAllAiChatListDetailR();
    }

    public Map<String, Object> getAiChatListDetailRStatisticsFromMemory() {
        return AiChatListDetailR_list_wlq.getStatistics();
    }

    // AiMatches 相关方法
    public void loadAiMatchesToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI匹配数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI匹配数据...");
            var allAiMatches = aiMatchesMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiMatches.size() + " 条记录");

            if (allAiMatches.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI匹配数据");
                return;
            }

            AiMatches_list_wlq.loadFromDatabaseDirectly(allAiMatches);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI匹配数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI匹配数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiMatches_wlq> getAllAiMatchesFromMemory() {
        return AiMatches_list_wlq.getAllAiMatches();
    }

    public Map<String, Object> getAiMatchesStatisticsFromMemory() {
        return AiMatches_list_wlq.getStatistics();
    }

    // AiTaskRequire 相关方法
    public void loadAiTaskRequireToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI任务需求数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI任务需求数据...");
            var allAiTaskRequire = aiTaskRequireMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiTaskRequire.size() + " 条记录");

            if (allAiTaskRequire.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI任务需求数据");
                return;
            }

            AiTaskRequire_list_wlq.loadFromDatabaseDirectly(allAiTaskRequire);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI任务需求数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务需求数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiTaskRequire_wlq> getAllAiTaskRequireFromMemory() {
        return AiTaskRequire_list_wlq.getAllAiTaskRequire();
    }

    public Map<String, Object> getAiTaskRequireStatisticsFromMemory() {
        return AiTaskRequire_list_wlq.getStatistics();
    }

    // AiTaskRespond 相关方法
    public void loadAiTaskRespondToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI任务响应数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI任务响应数据...");
            var allAiTaskRespond = aiTaskRespondMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiTaskRespond.size() + " 条记录");

            if (allAiTaskRespond.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI任务响应数据");
                return;
            }

            AiTaskRespond_list_wlq.loadFromDatabaseDirectly(allAiTaskRespond);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI任务响应数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务响应数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiTaskRespond_wlq> getAllAiTaskRespondFromMemory() {
        return AiTaskRespond_list_wlq.getAllAiTaskRespond();
    }

    public Map<String, Object> getAiTaskRespondStatisticsFromMemory() {
        return AiTaskRespond_list_wlq.getStatistics();
    }

    // 加载所有A开头_wlq实体类数据
    public void loadAllAEntityDataToMemory() {
        System.out.println("AEntityService_wlq: 开始加载所有A开头_wlq实体类数据...");

        try {
            loadActionExpToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载行为经验数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiChatListDetailRToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI聊天列表详情数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiMatchesToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI匹配数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiTaskRequireToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务需求数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiTaskRespondToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务响应数据失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("AEntityService_wlq: 所有A开头_wlq实体类数据加载完成！");
    }
}
