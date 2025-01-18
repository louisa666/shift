package utils;
import org.springframework.test.context.TestContext;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import org.springframework.http.MediaType;
import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
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

    public void delete(TestCaseRunner runner, String host, String id) {
        runner.$(http().client(host)
                .send()
                .delete("/api/duck/delete")
                .message()
                .queryParam("id", id));
    }
    public void update(TestCaseRunner runner, String host, String color, String height, String id, String material,
                       String sound , String wingsState) {
        runner.$(http().client(host)
                .send()
                .put("/api/duck/update")
                .message()
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));
    }
    public void getProperties(TestCaseRunner runner, String host, String id) {
        runner.$(http().client(host)
                .send()
                .get("/api/duck/action/properties")
                .message()
                .queryParam("id", id));
    }

    public void fly(TestCaseRunner runner, String host, String id) {
        runner.$(http().client(host)
                .send()
                .get("/api/duck/action/fly")
                .message()
                .queryParam("id", id));
    }

    public void swim(TestCaseRunner runner, String host, String id) {
        runner.$(http().client(host)
                .send()
                .get("/api/duck/action/swim")
                .message()
                .queryParam("id", id));
    }

    public void quack(TestCaseRunner runner, String host, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(host)
                .send()
                .get("/api/duck/action/swim")
                .message()
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public void validateResponse(TestCaseRunner runner, String host, String responseMessage, HttpStatus status) {
        runner.$(http().client(host)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }
    public String validateResponseCreate(TestCaseRunner runner, String host, String responseMessage, HttpStatus status) {
        runner.$(http().client(host)
                .receive()
                .response(status)
                .message()
                .extract(fromBody().expression("$.id", "id"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
        return "${id}";
    }
}

