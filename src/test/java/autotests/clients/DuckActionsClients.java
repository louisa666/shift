package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.WingState;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.TestCaseRunner;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;


@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClients extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient yellowDuckService;
    @Autowired
    protected SingleConnectionDataSource testDb;


    public void validateDuck (TestCaseRunner runner, String id, String color, String height,
                              String material, String sound, String wingsState) {
        runner.$(query(testDb).statement("SELECT * FROM DUCK WHERE ID=" + id)
                .validate("COLOR",color)
                .validate("HEIGHT",height)
                .validate("MATERIAL",material)
                .validate("SOUND",sound)
                .validate("WINGS_STATE",wingsState));
    }

    public void updateData (TestCaseRunner runner, String sql)
    {
        runner.$(sql(testDb).statement(sql));
    }
    @Step("Эндпоинт для создания уточки")
    public void create(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Эндпоинт для удаления уточки")
    public void delete(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .message()
                .queryParam( "id", "${id}"));
    }

    @Step("Эндпоинт для обновления свойств уточки")
    public void update(TestCaseRunner runner, String color, double height, String material, String sound,
                       WingState wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .message()
                .queryParam("color", color)
                .queryParam("height",String.valueOf(height))
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", String.valueOf(wingsState))
                .queryParam("id", "${id}")
         );
    }

    @Step("Эндпоинт для получения свойств уточки")
    public void getProperties(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .message()
                .queryParam("id","${id}"));
    }

    @Step("Эндпоинт заставить утку летать")
    public void fly(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .message()
                .queryParam("id", "${id}"));
    }

    @Step("Эндпоинт заставить утку плавать")
    public void swim(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .message()
                .queryParam("id", "${id}"));
    }

    @Step("Эндпоинт заставить утку крякать")
    public void quack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .message()
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    // Получение и проверка ответа для всех методов (С использованием String в качестве параметра)
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }
    // Получение и проверка ответа для всех методов (С использованием payload)
    public void validateResponse(TestCaseRunner runner, Object responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(responseMessage, new ObjectMapper())));
    }

     // Получение id утки
    public void getIdDuck(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "id")));
    }
    // Проверка ответа и извлечение id утки (С использованием ClassPathResource)
    public void validateResponseCreateAndGetId(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .extract(fromBody().expression("$.id", "id"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(responseMessage)));
    }

}

