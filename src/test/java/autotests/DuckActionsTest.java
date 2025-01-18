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

public class DuckActionsTest extends TestNGCitrusSpringSupport {
    private static DuckActionsUtils duckActions;
    private String host;
    @BeforeTest
    public void setup()
    {
        duckActions = new DuckActionsUtils();
        host = "http://localhost:2222";
    }
    @Test(description = "Метод создания утки")
    @CitrusTest
    public void create (@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException
    {
        String body =  "{" +
                "  \"color\": \"yellow\"," +
                "  \"height\": 5," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"ACTIVE\"" +
                "}";
        String responseBody = "{" +
                "  \"id\": \"@ignore@\"," +
                "  \"color\": \"yellow\"," +
                "  \"height\": 5.0," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"ACTIVE\"" +
                "}";
        ObjectMapper object  = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);


        duckActions.create(runner, host, bodyJson.toString());
        duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

        //Меняем поле material на wood

        ((ObjectNode) bodyJson).put("material","wood");
        ((ObjectNode) bodyResponseJson).put("material","wood");

        duckActions.create(runner, host, bodyJson.toString());
        duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
    }

    @Test(description = "Метод получения характеристик уточки")
    @CitrusTest
    public void properties ()
    {

    }
    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке")
    @CitrusTest
    public void fly()
    {

    }
    @Test(description = "Метод, позволяющий плыть уточке")
    @CitrusTest
    public void swim()
    {

    }
    @Test(description = "Метод, позволяющий крякать уточке")
    @CitrusTest
    public void quack()
    {

    }


}
