package com.ordenconmimo.orden_con_mimo_frontend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(SpringExtension.class)
@SuppressWarnings("all")
class OrdenConMimoFrontendApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
        
        assertTrue(applicationContext.containsBean("tareaController"));
        assertTrue(applicationContext.containsBean("espacioController"));
        assertTrue(applicationContext.containsBean("homeController"));
        assertTrue(applicationContext.containsBean("metodoMimoController"));
    }
    
    @Test
    void mainMethodStartsApplication(CapturedOutput output) {
        OrdenConMimoFrontendApplication.main(new String[]{"--server.port=0"});
        
        
        assertNotNull(OrdenConMimoFrontendApplication.class);
    }
    
        
    @Test
    void applicationCanBeInstantiated() {
        OrdenConMimoFrontendApplication application = new OrdenConMimoFrontendApplication();
        assertNotNull(application);
    }
    
    @Test
    void applicationClassPresentsExpectedStructure() {
        assertEquals(1, OrdenConMimoFrontendApplication.class.getDeclaredMethods().length);
        assertEquals("main", OrdenConMimoFrontendApplication.class.getDeclaredMethods()[0].getName());
        
        assertEquals(1, OrdenConMimoFrontendApplication.class.getDeclaredConstructors().length);
        assertEquals(0, OrdenConMimoFrontendApplication.class.getDeclaredConstructors()[0].getParameterCount());
    }
}