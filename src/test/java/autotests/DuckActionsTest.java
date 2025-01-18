package autotests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.model.testcase.core.ReceiveModel;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jayway.jsonpath.JsonPath;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;
import utils.DuckActionsUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;


public class DuckActionsTest extends TestNGCitrusSpringSupport {
    private static DuckActionsUtils duckActions;
    private String host;
    String body;
    String responseBody;
    @BeforeTest
    public void setup()
    {
        duckActions = new DuckActionsUtils();
        host = "http://localhost:2222";
        body =  "{" +
                "  \"color\": \"yellow\"," +
                "  \"height\": 5," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"ACTIVE\"" +
                "}";
        responseBody = "{" +
                "  \"id\": \"@ignore@\"," +
                "  \"color\": \"yellow\"," +
                "  \"height\": 5.0," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"ACTIVE\"" +
                "}";
    }
    @Test(description = "Метод создания утки")
    @CitrusTest
    public void create (@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException
    {
        ObjectMapper object  = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);

        duckActions.create(runner, host, bodyJson.toString());
        String duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);

        //Меняем поле material на wood и отправляем запрос на создание утки
        ((ObjectNode) bodyJson).put("material","wood");
        ((ObjectNode) bodyResponseJson).put("material","wood");

        duckActions.create(runner, host, bodyJson.toString());
        duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }

    @Test(description = "Метод удаления уточки")
    @CitrusTest
    public void delete (@Optional @CitrusResource TestCaseRunner runner)
    {
        duckActions.create(runner, host, body);
        String duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }
    @Test(description = "Метод изменения уточки")
    @CitrusTest
    public void update (@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException
    {
        ObjectMapper object  = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);
        duckActions.create(runner, host, bodyJson.toString());
        String duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

        //Меняем цвет и высоту уточки
        ((ObjectNode) bodyJson).put("color","red");
        ((ObjectNode) bodyJson).put("height",7);
        ((ObjectNode) bodyResponseJson).put("color","red");
        ((ObjectNode) bodyResponseJson).put("height",7);
        duckActions.update(runner, host,bodyJson. );
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
        //Меняем цвет и //Меняем цвет и высоту уточки уточки
        ((ObjectNode) bodyJson).put("color","red");
        ((ObjectNode) bodyResponseJson).put("height",7);
        duckActions.update();

    }
    @Test(description = "Метод получения характеристик уточки")
    @CitrusTest
    public void properties (@Optional @CitrusResource TestCaseRunner runner)
    {

    }
    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке")
    @CitrusTest
    public void fly(@Optional @CitrusResource TestCaseRunner runner)
    {

    }
    @Test(description = "Метод, позволяющий плыть уточке")
    @CitrusTest
    public void swim(@Optional @CitrusResource TestCaseRunner runner)
    {

    }
    @Test(description = "Метод, позволяющий крякать уточке")
    @CitrusTest
    public void quack(@Optional @CitrusResource TestCaseRunner runner)
    {

    }


}
