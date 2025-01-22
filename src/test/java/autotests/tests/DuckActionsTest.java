package autotests.tests;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;
import autotests.clients.DuckActionsClients;


public class DuckActionsTest extends DuckActionsClients {
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
            create(runner,  body);
            validateResponseCreateAndGetId(runner,  responseBody, HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
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

            create(runner,  body);
            validateResponseCreateAndGetId(runner,  responseBody, HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
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
        create(runner,  body);
        getIdDuck(runner,   HttpStatus.OK);
        delete(runner,  "${id}");
        validateResponse(runner,  "{" +
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
            // Создаем утку и получаем id
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);

            //Меняем цвет и высоту уточки
            update(runner,  "red", "7",
                    "${id}", "rubber",
                    "quack", "ACTIVE");
            validateResponse(runner,  "{" +
                    "  \"message\": \"Duck with id = ${id} is updated\"" + "}", HttpStatus.OK);
        } finally {
            //Удаляем утку
            delete(runner,  "${id}");
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
            // Создаем новую утку
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);
            //Меняем цвет и звук уточки уточки (недопустимо менять звук утки, поэтому должна быть ошибка 400)
            update(runner,  "red", "5", "${id}", "rubber",
                    "meow", "ACTIVE");
            validateResponse(runner,  "{" +
                    "  \"message\": \"Duck with id = \"${id}\" is not updated\"" + "}", HttpStatus.BAD_REQUEST);
        } finally {
            // Удаляем утку
            delete(runner,  "${id}");
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
            String responseProperty = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"rubber\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                create(runner,  body);
                getIdDuck(runner,   HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable("${id}");
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);

            // Оставляем поле material по умолчанию rubber и отправляем запрос на просмотр  свойств
            getProperties(runner,  "${id}");
            validateResponse(runner,  responseProperty, HttpStatus.OK);
        } finally {
            // Удаляем утку
            delete(runner,  "${id}");
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
            String responseProperty = "{" +
                    "  \"color\": \"yellow\"," +
                    "  \"height\": 5.0," +
                    "  \"material\": \"wood\"," +
                    "  \"sound\": \"quack\"," +
                    "  \"wingsState\": \"ACTIVE\"" +
                    "}";
            do {
                create(runner,  body);
                getIdDuck(runner,   HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable("${id}");
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);
            getProperties(runner,  "${id}");
            validateResponse(runner,  responseProperty, HttpStatus.OK);
        } finally {
            // Удаляем утку
            delete(runner,  "${id}");
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
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);

            fly(runner,  "${id}");
            validateResponse(runner,  "{" + "\"message\": \"I'm flying\"}", HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
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
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);

            fly(runner,  "${id}");
            validateResponse(runner,  "{" + "\"message\": \"I can't fly\"}", HttpStatus.OK);
        } finally {
            //fly(runner,  "${id}");
            delete(runner,  "${id}");
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
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);

            fly(runner,  "${id}");
            validateResponse(runner,  "{" + "\"message\": \"Wings are not detected\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            delete(runner,  "${id}");
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
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);
            swim(runner,  "${id}");
            validateResponse(runner,  "{" + "\"message\": \"I'm swimming\"}", HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
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
            create(runner,  body);
            getIdDuck(runner,   HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
            swim(runner,  "${id}");
            validateResponse(runner,  "{" + "\"message\": \"Paws are not found\"}", HttpStatus.NOT_FOUND);
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
            do {
                create(runner,  body);
                getIdDuck(runner,   HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable("${id}");
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 != 0);
            quack(runner,  "${id}", "1", "1");
            validateResponse(runner,  "{" +
                    " \"sound\": \"quack\"" + "}", HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
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
            do {
                create(runner,  body);
                getIdDuck(runner,  HttpStatus.OK);
                run(new AbstractTestAction() {
                    @Override
                    public void doExecute(TestContext context) {
                        String message = context.getVariable("${id}");
                        idDuck = Integer.parseInt(message);
                    }
                });
            }
            while (idDuck % 2 == 0);
            quack(runner, "${id}", "1", "1");
            validateResponse(runner,  "{" +
                    " \"sound\": \"quack\"" + "}", HttpStatus.OK);
        } finally {
            delete(runner,  "${id}");
        }
    }

}
