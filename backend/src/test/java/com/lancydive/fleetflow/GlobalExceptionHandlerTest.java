package com.lancydive.fleetflow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancydive.fleetflow.entity.Shipment;
import com.lancydive.fleetflow.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void optimisticLockingFailureShouldReturn409() throws Exception {

        mockMvc.perform(
                put("/test/optimistic-lock")
        )
        .andExpect(status().isConflict())
        .andExpect(content().string(
                "Update conflict: The resource was modified by another request. Please refresh and try again."
        ));
    }

    @RestController
    static class TestController {

        @PutMapping("/test/optimistic-lock")
        public void triggerOptimisticLockingFailure() {

            throw new ObjectOptimisticLockingFailureException(
                    Shipment.class,
                    1L
            );
        }
    }
}