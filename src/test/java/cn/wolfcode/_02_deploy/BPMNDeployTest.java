package cn.wolfcode._02_deploy;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/10 11:33
 */
public class BPMNDeployTest {

    @Test
    public void deployTest() throws Exception {
        // 得到流程引擎对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 获取 RepositoryService 接口实例对象
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程定义文件
        Deployment deployment = repositoryService.createDeployment()
                // 流程定义图片位置
                .addClasspathResource("bpmn/diagram.png")
                //流程定义文件
                .addClasspathResource("bpmn/leave.bpmn20.xml")
                //流程定义命名
                .name("请假流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getKey());
    }

    @Test
    public void createProcessInstanceTest() throws Exception {
        // 需求： 启动流程实例
        // 1. 得到流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 RuntimeService 实例对象。
        RuntimeService runtimeService = engine.getRuntimeService();
        // 3. 使用 api 接口基于流程定义 key 启动流程实例， 得到流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave");
        // 打印流程实例对象中的相关信息
        System.out.println(" + processInstance.getId() = " + processInstance.getId());
        System.out.println("processInstance.getProcessInstanceId() = " + processInstance.getProcessInstanceId());
        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getDeploymentId() = " + processInstance.getDeploymentId());
    }

    @Test
    public void queryTaskTest() throws Exception {
        // 需求：查询张三的代办任务
        //1. 得到流程引擎对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取 Task Service
        TaskService taskService = engine.getTaskService();
        // 3.基于流程实例/定义id禅心指定用户的待办任务
        List<Task> list = taskService.createTaskQuery()
//                .processDefinitionKey("leave")
                .processInstanceId("2501")
                .taskAssignee("李四")
                .active()
                .list();

        if (list == null || list.size() == 0) {
            System.out.println("没有待办任务");
            return;
        }
        // 4. 打印任务信息
        for (Task task : list) {
            System.out.println("----------------------准备处理任务----------------------");
            taskService.addComment(task.getId(), task.getProcessInstanceId(), task.getAssignee() + "金额不合理，不予审批通过");
            System.out.println("添加批注信息");
            // 准备处理任务
            // 先将下一个节点的数据加入到历史任务以及运行时任务
            // 更新旧任务在历史表中的结束时间
            // 删除运行时的旧任务
            taskService.complete(task.getId());
            System.out.println("----------------------处理任务完成----------------------");
        }
    }

    @Test
    public void queryHistoryTest() throws Exception {
        // 需求: 获取李四的历史审批信息，包括批注信息
        //1. 获取流程引擎对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //2. 获取 HistoryService 历史服务接口对象
        HistoryService historyService = engine.getHistoryService();
        TaskService taskService = engine.getTaskService();
        //3. 基于任务负责人/流程定义key/是否已完成
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                // 基于流程定义key查询
                .processDefinitionKey("leave")
                // 基于任务负责人查询
//                .processInstanceId("2501")
                .taskAssignee("张三")
                // 只查询任务已完成的
                .finished()
                .list();
        //4.基于历史任务查询历史批注消息
        for (HistoricTaskInstance instance : list) {
            System.out.println("任务 id = " + instance.getId());
            System.out.println("负责人 = " + instance.getAssignee());
            System.out.println("开始时间 = " + instance.getStartTime());
            System.out.println("结束时间 = " + instance.getEndTime());
            System.out.println("使用时常 = " + (instance.getDurationInMillis()/ 1000/ 60) + "分钟");
            List<Comment> comments = taskService.getTaskComments(instance.getId());
            for (Comment comment : comments) {
                System.out.println("批注信息 = " + comment.getFullMessage());
            }
        }
    }
}
