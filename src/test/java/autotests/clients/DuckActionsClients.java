package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.TestCaseRunner;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;


@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClients extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient yellowDuckService;

    // Создание утки
    public void create(TestCaseRunner runner, String body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    // Удаление утки
    public void delete(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .message()
                .queryParam("id", id));
    }

    // Обновление свойств утки
    public void update(TestCaseRunner runner, String color, String height, String id, String material,
                       String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .message()
                .queryParam("color", color.replace("\"", ""))
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material.replace("\"", ""))
                .queryParam("sound", sound.replace("\"", ""))
                .queryParam("wingsState", wingsState.replace("\"", "")));
    }

    // Получение свойств утки
    public void getProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .message()
                .queryParam("id", id));
    }

    // Заставить утку летать
    public void fly(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .message()
                .queryParam("id", id));
    }

    // Заставить утку плавать
    public void swim(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .message()
                .queryParam("id", id));
    }

    // Заставить утку крякать
    public void quack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .message()
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    // Получение и проверка ответа для всех методов
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    // Создание утки и извлечение id
    public void getIdDuck(TestCaseRunner runner, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .extract(fromBody().expression("$.id", "id")));
    }
    // Создание утки, проверка ответа и извлечение id
    public void validateResponseCreateAndGetId(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .extract(fromBody().expression("$.id", "id"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

}

