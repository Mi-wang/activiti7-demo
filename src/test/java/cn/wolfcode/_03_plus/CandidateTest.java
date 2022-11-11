package cn.wolfcode._03_plus;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/11 15:59
 */
public class CandidateTest {

    @Test
    public void activitiesTest() throws Exception {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 获取 RepositoryService 接口实例对象
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程定义文件
        repositoryService.createDeployment()
                //流程定义文件
                .addClasspathResource("bpmn/leave_candidate.bpmn20.xml")
                .name("请求流程")
                .deploy();
    }

    @Test
    public void startProcessInstanceVariablesTest() throws Exception {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("deptMgr","张三丰");
        variables.put("hrMgr","李二");
        variables.put("candidate","");
        variables.put("day",5);

        runtimeService.startProcessInstanceByKey("leave_var",variables);
    }

    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("leave_var")
                .taskAssignee("张三丰")
                .list();
        for (Task task : tasks) {
            System.out.println("开始审批任务--------------");
            taskService.complete(task.getId());
            System.out.println("完成审批任务--------------");
        }
    }
}
