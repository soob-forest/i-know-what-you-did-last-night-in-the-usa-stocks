package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

// ngrok http 3000
@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages = "api")
public class ServerApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ServerApplication.class, args);
  }
}
