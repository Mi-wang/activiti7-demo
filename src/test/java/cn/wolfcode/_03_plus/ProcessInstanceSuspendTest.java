package cn.wolfcode._03_plus;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/11 11:16
 * 流程实例挂起
 */
public class ProcessInstanceSuspendTest {


    @Test
    public void suspendAllTest() throws Exception {
        // 挂起流程定义下的所有流程实例
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();

        // 查询流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("leave")
                .singleResult();

        // 流程定义是否挂起
        boolean suspended = definition.isSuspended();
        System.out.println("流程定义：" + definition.getId() + " " + (suspended ? "挂起" : "未挂起"));


        if (suspended) {
            // 激活
            repositoryService.suspendProcessDefinitionByKey("leave",true,null);
        } else {
            //挂起
            // 仅挂起流程定义, 流程定义下的流程实例不会被挂起, 这个时候只是不能启动新的流程实例，之前的实例还是可以运行的
            //repositoryService.suspendProcessDefinitionByKey("leave");
            repositoryService.suspendProcessDefinitionByKey("leave",true,null);
        }
        //重新查询流程定义的挂起状态
        definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("leave")
                .singleResult();

        // 判断流程定义是否已经挂起
        suspended = definition.isSuspended();
        System.out.println("流程定义：" + definition.getId() + " " + (suspended ? "挂起" : "未挂起"));
    }
}
