package timelineapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TimelineApp {

    public static void main(String[] args) {
        SpringApplication.run(TimelineApp.class, args);
    }

}
