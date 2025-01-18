package utils;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import org.springframework.http.MediaType;
import com.consol.citrus.TestCaseRunner;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsUtils extends TestNGCitrusSpringSupport {
    public void create(TestCaseRunner runner, String host, String body) {
        runner.$(http().client(host)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    public void getProperties() {

    }

    public void fly() {

    }

    public void swim() {

    }

    public void quack() {

    }

    public void validateResponse(TestCaseRunner runner, String host, String responseMessage, HttpStatus status) {
        runner.$(http().client(host)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }
}

