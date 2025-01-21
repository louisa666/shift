package autotests;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;
import utils.DuckActionsUtils;


public class DuckActionsTest extends TestNGCitrusSpringSupport {
    private static DuckActionsUtils duckActions = new DuckActionsUtils();
    private String host = "http://localhost:2222";
    String duckId = "1";
    int idDuck;


    @Test(description = "Метод создания утки (material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        String body = "{" +
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
        try {
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }

    }

    @Test(description = "Метод создания утки (material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";

            duckActions.create(runner, host, body);
            duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод удаления уточки")
    @CitrusTest
    public void delete(@Optional @CitrusResource TestCaseRunner runner) {
        String body = "{" +
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
        duckActions.create(runner, host, body);
        duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
        duckActions.delete(runner, host, duckId);
        duckActions.validateResponse(runner, host, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }

    @Test(description = "Метод изменения уточки (меняем цвет и высоту")
    @CitrusTest
    public void updateHeight(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
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
            // Создаем утку и получаем id
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);

            //Меняем цвет и высоту уточки
            duckActions.update(runner, host, "red", "7",
                    duckId, "rubber",
                    "quack", "ACTIVE");
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck with id = " + duckId + " is updated\"" + "}", HttpStatus.OK);
        } finally {
            //Удаляем утку
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод изменения уточки (меняем цвет и звук")
    @CitrusTest
    public void updateSound(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
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
            // Создаем новую утку
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
            //Меняем цвет и звук уточки уточки (недопустимо менять звук утки, поэтому должна быть ошибка 400)
            duckActions.update(runner, host, "red", "5", duckId, "rubber",
                    "meow", "ACTIVE");
            duckActions.validateResponse(runner, host, "{" +
                    "  \"message\": \"Duck with id = " + duckId + " is not updated\"" + "}", HttpStatus.BAD_REQUEST);
        } finally {
            // Удаляем утку
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод получения характеристик уточки (Нечетный id material = rubber")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
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
            String responseProperty = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"rubber\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable(duckId);
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);

            // Оставляем поле material по умолчанию rubber и отправляем запрос на просмотр  свойств
            duckActions.getProperties(runner, host, duckId);
            duckActions.validateResponse(runner, host, responseProperty, HttpStatus.OK);
        } finally {
            // Удаляем утку
            duckActions.delete(runner, host, duckId);
        }

    }

    @Test(description = "Метод получения характеристик уточки (Четный materiai = wood")
    @CitrusTest
    public void propertiesOddId(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            //Меняем поле material на wood и отправляем запрос на просмотр свойств;
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseProperty = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable(duckId);
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);
            duckActions.getProperties(runner, host, duckId);
            duckActions.validateResponse(runner, host, responseProperty, HttpStatus.OK);
        } finally {
            // Удаляем утку
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: ACTIVE")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // С активными крыльями
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I'm flying\"}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }

    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: FIXED")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // С связанными крыльями
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"FIXED\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"FIXED\"" +
                    "}";
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I can't fly\"}", HttpStatus.OK);
        } finally {
            duckActions.fly(runner, host, duckId);
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: UNDEFINED")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // С неопределенными крыльями
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"UNDEFINED\"" +
                    "}";
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);

            duckActions.fly(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"Wings are not detected\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            duckActions.delete(runner, host, duckId);
        }
    }

    @Test(description = "Метод, позволяющий плыть уточке (существующий id)")
    @CitrusTest
    public void swimExists(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // Утка с существующим id
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
            duckActions.swim(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"I'm swimming\"}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }

    }

    @Test(description = "Метод, позволяющий плыть уточке (несуществующий id)")
    @CitrusTest
    public void swimNonexist(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // Утка с несуществующим id
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            duckActions.create(runner, host, body);
            duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
            duckActions.swim(runner, host, duckId);
            duckActions.validateResponse(runner, host, "{" + "\"message\": \"Paws are not found\"}", HttpStatus.NOT_FOUND);
        }
    }

    @Test(description = "Метод, позволяющий крякать уточке (четный id)")
    @CitrusTest
    public void quackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable(duckId);
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 != 0);
            duckActions.quack(runner, host, duckId, "1", "1");
            duckActions.validateResponse(runner, host, "{" +
                    " \"sound\": \"quack\"" + "}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }

    }

    @Test(description = "Метод, позволяющий крякать уточке (нечетный id)")
    @CitrusTest
    public void quackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            String body = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            String responseBody = "{" +
                    "  \"id\": \"@ignore@\"," +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                duckActions.create(runner, host, body);
                duckId = duckActions.validateResponseCreateAndGetId(runner, host, responseBody, HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable(duckId);
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);
            duckActions.quack(runner, host, duckId, "1", "1");
            duckActions.validateResponse(runner, host, "{" +
                    " \"sound\": \"quack\"" + "}", HttpStatus.OK);
        } finally {
            duckActions.delete(runner, host, duckId);
        }
    }

}
