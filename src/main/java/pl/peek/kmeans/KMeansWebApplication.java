package pl.peek.kmeans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
public class KMeansWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KMeansWebApplication.class, args);
    }


}
