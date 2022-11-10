package cn.wolfcode._03_plus;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.junit.Test;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author wby
 * @version 1.0
 * @date 2022/11/10 17:08
 */
public class ProcessDefinitionTest {

    @Test
    public void queryTest() throws Exception {
        // 查询流程定义
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        // 获取服务资源
        RepositoryService repositoryService = engine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                // 基于流程定义key查询
                .processDefinitionKey("leave")
                // 按照最后一个版本查询
                .latestVersion()
                //.orderByProcessDefinitionVersion()// 按照版本进行排序
                //.desc()
                .singleResult();

        System.out.println("流程定义 = " + processDefinition.getId());
        System.out.println("流程定义部署  id: " + processDefinition.getDeploymentId());
        System.out.println("流程定义 key = " + processDefinition.getKey());
        System.out.println("流程定义版本号 = " + processDefinition.getVersion());
    }


    @Test
    public void downloadResourceTest() throws Exception {
        // 需求： 下载最新版本的流程定义资源
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        String deploymentId;

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("leave")
                .latestVersion()
                .singleResult();
        deploymentId = definition.getDeploymentId();
        InputStream image = repositoryService.getResourceAsStream(deploymentId, "bpmn/diagram.png");
        InputStream bpmn = repositoryService.getResourceAsStream(deploymentId, "bpmn/leave.bpmn20.xml");

        IOUtils.copy(image, Files.newOutputStream(Paths.get("D:/diagram.png")));
        IOUtils.copy(bpmn, Files.newOutputStream(Paths.get("D:/leave.xml")));
    }
    @Test
    public void deleteTest() throws Exception {
        // 需求： 删除指定目录流程
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        repositoryService.deleteDeployment("10001");
    }
}
