package cn.wolfcode._02_deploy;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
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
                .taskAssignee("张三")
                .active()
                .list();
        // 4. 打印任务信息
        for (Task task : list) {
            System.out.println("任务 di = " + task.getId());
            System.out.println("负责人 = " + task.getAssignee());
            System.out.println("流程实例 id = " + task.getProcessInstanceId());
            System.out.println("流程定义 id = " + task.getProcessDefinitionId());

            System.out.println("----------------------准备处理任务----------------------");

            taskService.complete(task.getId());
            System.out.println("----------------------处理任务完成----------------------");
        }
    }
}
