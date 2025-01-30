package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.WingState;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;


public class DuckActionsClients extends BaseTest {
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
        sendRequestPost(runner,yellowDuckService, "/api/duck/create", body);
    }

    @Step("Эндпоинт для удаления уточки")
    public void delete(TestCaseRunner runner) {
        sendRequestDelete(runner, yellowDuckService, "/api/duck/delete?id=${id}");
    }

    @Step("Эндпоинт для обновления свойств уточки")
    public void update(TestCaseRunner runner, String color, double height, String material, String sound,
                       WingState wingsState) {
        sendRequestPut(runner, yellowDuckService, "/api/duck/update?color="+ color +
                "&height="+ height + "&material=" + material + "&sound=" + sound +
                "&wingsState=" + wingsState.toString() + "&id=${id}");
    }

    @Step("Эндпоинт для получения свойств уточки")
    public void getProperties(TestCaseRunner runner) {
        sendRequestGet(runner, yellowDuckService, "/api/duck/action/properties?id=${id}");
    }

    @Step("Эндпоинт заставить утку летать")
    public void fly(TestCaseRunner runner) {
        sendRequestGet(runner, yellowDuckService, "/api/duck/action/fly?id=${id}");
    }

    @Step("Эндпоинт заставить утку плавать")
    public void swim(TestCaseRunner runner) {
        sendRequestGet(runner, yellowDuckService, "/api/duck/action/swim?id=${id}");
    }

    @Step("Эндпоинт заставить утку крякать")
    public void quack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendRequestGet(runner, yellowDuckService, "/api/duck/action/quack?id="+ id +"&repetitionCount=" +
                repetitionCount +"&soundCount=" + soundCount);
    }

    // Получение и проверка ответа для всех методов (С использованием String в качестве параметра)
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        validateResponseTemplate(runner, yellowDuckService, responseMessage, status);
    }
    // Получение и проверка ответа для всех методов (С использованием payload)
    public void validateResponse(TestCaseRunner runner, Object responseMessage, HttpStatus status) {
        validateResponseTemplate(runner, yellowDuckService, responseMessage, status);
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
        validateResponseTemplatePath(runner, yellowDuckService, responseMessage, status);
    }

}

