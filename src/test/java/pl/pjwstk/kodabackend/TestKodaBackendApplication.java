package pl.pjwstk.kodabackend;

import org.springframework.boot.SpringApplication;

public class TestKodaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(KodaBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
