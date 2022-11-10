package cn.wolfcode._01_hello;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/10 11:04
 */
public class HelloTest {

    @Test
    public void processEngineTest() throws Exception {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("engine = " + engine);
    }
}
