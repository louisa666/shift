package autotests;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    String body;
    int idDuck;
    String responseBody;

    @BeforeTest
    public void setup() {
        duckActions = new DuckActionsUtils();
        host = "http://localhost:2222";
        body = "{" +
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

    @Test(description = "Метод создания утки (material = rubber, material = wood")
    @CitrusTest
    public void create(@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException {
        ObjectMapper object = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);
        String duckId = "1";
        try {
            duckActions.create(runner, host, bodyJson.toString());
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }

        try {
            //Меняем поле material на wood и отправляем запрос на создание утки
            ((ObjectNode) bodyJson).put("material", "wood");
            ((ObjectNode) bodyResponseJson).put("material", "wood");

            duckActions.create(runner, host, bodyJson.toString());
            duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }

    }

    @Test(description = "Метод удаления уточки")
    @CitrusTest
    public void delete(@Optional @CitrusResource TestCaseRunner runner) {
        duckActions.create(runner, host, body);
        String duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }

    @Test(description = "Метод изменения уточки (меняем цвет и высоту, цвет и звук")
    @CitrusTest
    public void update(@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException {
        String duckId = "1";
        ObjectMapper object = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);
        try {
            // Создаем утку
            duckActions.create(runner, host, bodyJson.toString());
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

            //Меняем цвет и высоту уточки
            ((ObjectNode) bodyJson).put("color", "red");
            ((ObjectNode) bodyJson).put("height", 7);
            duckActions.update(runner, host, bodyJson.get("color").toString(), bodyJson.get("height").toString(),
                    duckId, bodyJson.get("material").toString(),
                    bodyJson.get("sound").toString(), bodyJson.get("wingsState").toString());
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck with id = " + duckId + " is updated\"" + "}", HttpStatus.OK);
        } finally {
            //Удаляем утку
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }

        try {
            // Создаем новую утку
            bodyJson = object.readTree(body);
            bodyResponseJson = object.readTree(responseBody);
            duckActions.create(runner, host, bodyJson.toString());
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

            //Меняем цвет и звук уточки уточки (недопустимо менять звук утки, поэтому должна быть ошибка 400)
            ((ObjectNode) bodyJson).put("color", "red");
            ((ObjectNode) bodyJson).put("sound", "meow");
            duckActions.update(runner, host, bodyJson.get("color").toString(), bodyJson.get("height").toString(),
                    duckId, bodyJson.get("material").toString(),
                    bodyJson.get("sound").toString(), bodyJson.get("wingsState").toString());
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck with id = " + duckId + " is not updated\"" + "}", HttpStatus.BAD_REQUEST);
        } finally {
            // Удаляем утку
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }
    }

    @Test(description = "Метод получения характеристик уточки (Нечестный id material = rubber, четный materiai = wood")
    @CitrusTest
    public void properties(@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException {
        ObjectMapper object = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);
        String duckId = "";
        duckActions.create(runner, host, bodyJson.toString());
        duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
        variable("iii", duckId);
        run(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext context) {
                String message = context.getVariable("iii");
                idDuck = Integer.parseInt(message);
            }
        });

        if(idDuck%2 !=0)
        {
            try {
                // Оставляем поле material по умолчанию rubber и отправляем запрос на просмотр  свойств
                ((ObjectNode) bodyResponseJson).remove("id");
                duckActions.getProperties(runner, host, duckId);
                duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
            }
            finally {
                // Удаляем утку
                duckActions.delete(runner, host, duckId);
                duckActions.validateResponse(runner, host, "{" +
                        "  \"message\": \"Duck is deleted\"" +
                        "}", HttpStatus.OK);
            }
            try {
                //Меняем поле material на wood и отправляем запрос на просмотр свойств
                ((ObjectNode) bodyJson).put("material", "wood");
                ((ObjectNode) bodyResponseJson).put("material", "wood");

                duckActions.create(runner, host, bodyJson.toString());
                duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
                ((ObjectNode) bodyResponseJson).remove("id");
                duckActions.getProperties(runner, host, duckId);
                duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
            }
            finally {
                // Удаляем утку
                duckActions.delete(runner, host, duckId);
                duckActions.validateResponse(runner, host, "{" +
                        "  \"message\": \"Duck is deleted\"" +
                        "}", HttpStatus.OK);
            }
        }
        else {
            try {
                // Меняем  поле material на умолчанию wood и отправляем запрос на просмотр  свойств
                ((ObjectNode) bodyJson).put("material", "wood");
                ((ObjectNode) bodyResponseJson).put("material", "wood");
                duckActions.update(runner, host, bodyJson.get("color").toString(), bodyJson.get("height").toString(),
                        duckId, bodyJson.get("material").toString(),
                        bodyJson.get("sound").toString(), bodyJson.get("wingsState").toString());;

                ((ObjectNode) bodyResponseJson).remove("id");
                duckActions.getProperties(runner, host, duckId);
                duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
            }
            finally {
                // Удаляем утку
                duckActions.delete(runner, host, duckId);
                duckActions.validateResponse(runner, host, "{" +
                        "  \"message\": \"Duck is deleted\"" +
                        "}", HttpStatus.OK);
            }
            try {
                //Создаем утку с полем material по умолчанию  и отправляем запрос на просмотр свойств
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

                ((ObjectNode) bodyResponseJson).remove("id");
                duckActions.getProperties(runner, host, duckId);
                duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);


                duckActions.getProperties(runner, host, duckId);
                duckActions.validateResponse(runner, host, bodyResponseJson.toString(), HttpStatus.OK);
            }
            finally {
                // Удаляем утку
                duckActions.delete(runner, host, duckId);
                duckActions.validateResponse(runner, host, "{" +
                        "  \"message\": \"Duck is deleted\"" +
                        "}", HttpStatus.OK);
            }
        }

    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: ACTIVE, FIXED, UNDEFINED")
    @CitrusTest
    public void fly(@Optional @CitrusResource TestCaseRunner runner) throws JsonProcessingException {
        String duckId = "1";
        ObjectMapper object = new ObjectMapper();
        JsonNode bodyJson = object.readTree(body);
        JsonNode bodyResponseJson = object.readTree(responseBody);
        try {
            // С активными крыльями
            duckActions.create(runner, host, bodyJson.toString());
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I'm flying\"}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }
        try {
            // С связанными крыльями
            bodyJson = object.readTree(body);
            bodyResponseJson = object.readTree(responseBody);
            duckActions.create(runner, host, bodyJson.toString());
            ((ObjectNode) bodyJson).put("wingsState", "FIXED");
            ((ObjectNode) bodyResponseJson).put("wingsState", "FIXED");
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I can't fly\"}", HttpStatus.OK);
        } finally {
            duckActions.fly(runner, host, duckId);
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }
        try {
            // С неопределенными крыльями
            bodyJson = object.readTree(body);
            bodyResponseJson = object.readTree(responseBody);
            duckActions.create(runner, host, bodyJson.toString());
            ((ObjectNode) bodyJson).remove("wingsState");
            ((ObjectNode) bodyResponseJson).put("wingsState", "UNDEFINED");
            duckId = duckActions.validateResponseCreate(runner, host, bodyResponseJson.toString(), HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"Wings are not detected\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);

            duckActions.fly(runner, host, duckId);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }

    }

    @Test(description = "Метод, позволяющий плыть уточке (существующий id и несуществующий)")
    @CitrusTest
    public void swim(@Optional @CitrusResource TestCaseRunner runner) {
        String duckId = "";
        try {
            // Утка с существующим id
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
            duckActions.swim(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I'm swimming\"}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
        }
        try {
            // Утка с несуществующим id
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck is deleted\"" +
                    "}", HttpStatus.OK);
            duckActions.swim(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"Paws are not found\"}", HttpStatus.NOT_FOUND);
        }

    }

    @Test(description = "Метод, позволяющий крякать уточке (четный и нечетный id)")
    @CitrusTest
    public void quack(@Optional @CitrusResource TestCaseRunner runner) {
        String duckId = "1";
        for (int i = 0; i < 2; i++) {
            try {
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreate(runner, host, responseBody, HttpStatus.OK);
                duckActions.quack(runner, host, duckId, "1", "1");
                duckActions.validateResponse(runner, host, "{" +
                        " \"sound\": \"quack\"" + "}", HttpStatus.OK);
            } finally {
                duckActions.delete(runner, host, duckId);
                duckActions.validateResponse(runner, host, "{" +
                        "  \"message\": \"Duck is deleted\"" +
                        "}", HttpStatus.OK);
            }

        }
    }

}
