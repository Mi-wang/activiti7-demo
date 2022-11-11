package cn.wolfcode._03_plus;

import cn.wolfcode.entity.Leave;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/11 9:52
 */
public class BusinessKeyTest {

    private static final Map<Long, Leave> MOCK_DATA = new HashMap<>();

    static {
        MOCK_DATA.put(8001L, new Leave(8001L, "张三", 5, "相亲"));
        MOCK_DATA.put(8002L, new Leave(8002L, "王五", 10, "拆迁"));
    }

    @Test
    public void startProcessInstanceTest() throws Exception {
        // 需求： 开始流程实例

        //业务表示
        String businessKey = "8001";
        // 获取流引擎勤对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 获取 RuntimeService运行时 service
        RuntimeService runtimeService = engine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey);
        System.out.println("流程实例id = " + processInstance.getId());
        System.out.println("流程实例业务标识 = " + processInstance.getBusinessKey());
    }


    @Test
    public void queryTaskTest() throws Exception {
        // 查询张三的待处理任务，并同时查出对用的详细请假信息
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        RuntimeService runtimeService = engine.getRuntimeService();
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("张三")
                .list();

        for (Task task : tasks) {
            System.out.println("任务 id = " + task.getId());
            System.out.println("请假用户 = " + task.getProcessInstanceId());
            System.out.println("请假天数 = " + task.getAssignee());
            System.out.println("流程定义id = " + task.getProcessDefinitionId());

            // 得到流程对象
            // 思考： 如果当前流程已经结束，那么利用 RuntimeService 是否还能查到流程实例，如果不能，用什么方式一定能查到
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    // 基于任务的流程实例 id 进行铲鲟
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();

            // 获取业务表示
            String businessKey = instance.getBusinessKey();
            // 获取任务对应的详细信息
            Leave leave = MOCK_DATA.get(Long.parseLong(businessKey));
            System.out.println("请假业务 id = " + leave.getId());
            System.out.println("请假用户 = " + leave.getUserName());
            System.out.println("请假天数 = " + leave.getDays());
            System.out.println("请假理由 = " + leave.getReason());
        }
    }
}
