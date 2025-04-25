package org.t1academy.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.t1academy.tasktracker.controller.TestContainers;

@ActiveProfiles("test")
@SpringBootTest
class TaskTrackerApplicationTests extends TestContainers {

    @Test
    void contextLoads() {
    }

}
