package com.iec;

import com.iec.service.DeviceTCPService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IecApplication
 *
 * @author wxp
 * @version 1.0
 * @date 2020-09-09
 */
@SpringBootApplication
public class IecApplication {
    public static void main(String[] args) {
        SpringApplication.run(IecApplication.class, args);
        DeviceTCPService.INSTANCE.initTCPService();
    }
}
