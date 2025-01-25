package autotests.tests;

import autotests.payloads.*;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;
import autotests.clients.DuckActionsClients;

import static com.consol.citrus.DefaultTestActionBuilder.action;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;


public class DuckActionsTest extends DuckActionsClients {

    @Test(description = "Метод создания утки (material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        String responseBody = "duckActionsTest/successfulCreateRubber.json";
        create(runner, duck);
        validateResponseCreateAndGetId(runner, responseBody, HttpStatus.OK);

    }

    @Test(description = "Метод создания утки (material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        String responseBody = "duckActionsTest/successfulCreateWood.json";
        create(runner, duck);
        validateResponseCreateAndGetId(runner, responseBody, HttpStatus.OK);
    }

    @Test(description = "Метод удаления уточки")
    @CitrusTest
    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        create(runner, duck);
        getIdDuck(runner);
        delete(runner);
        validateResponse(runner, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }

    @Test(description = "Метод изменения уточки (меняем цвет и высоту")
    @CitrusTest
    public void updateHeight(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        // Создаем утку и получаем id
        create(runner, duck);
        getIdDuck(runner);

        //Меняем цвет и высоту уточки
        duck.color("red");
        duck.height(7.0);
        update(runner, duck.color(), duck.height(), duck.material(), duck.sound(), duck.wingsState());
        validateResponse(runner, "{" +
                "  \"message\": \"Duck with id = ${id} is updated\"" + "}", HttpStatus.OK);

    }

    @Test(description = "Метод изменения уточки (меняем цвет и звук")
    @CitrusTest
    public void updateSound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        // Создаем новую утку
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        // Создаем утку и получаем id
        create(runner, duck);
        getIdDuck(runner);

        //Меняем цвет и звук уточки уточки (недопустимо менять звук утки, поэтому должна быть ошибка 400)
        duck.color("red");
        duck.sound("mew");
        update(runner, duck.color(), duck.height(), duck.material(), duck.sound(), duck.wingsState());
        validateResponse(runner, "{" +
                "  \"message\": \"Duck with id = \"${id}\" is not updated\"" + "}", HttpStatus.BAD_REQUEST);

    }

    @Test(description = "Метод получения характеристик уточки (Нечетный id material = rubber")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        runner.$(action(context -> {
            do {
                create(runner, duck);
                getIdDuck(runner);
            }
            while (Integer.parseInt(context.getVariable("id")) % 2 == 0);
        }));
        // Оставляем поле material по умолчанию rubber и отправляем запрос на просмотр  свойств
        getProperties(runner);
        validateResponse(runner, duck, HttpStatus.OK);

    }

    @Test(description = "Метод получения характеристик уточки (Четный materiai = wood")
    @CitrusTest
    public void propertiesOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        //Меняем поле material на wood и отправляем запрос на просмотр свойств;
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        runner.$(action(context -> {
            do {
                create(runner, duck);
                getIdDuck(runner);
            }
            while (Integer.parseInt(context.getVariable("id")) % 2 != 0);
        }));
        getProperties(runner);
        validateResponse(runner, duck, HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: ACTIVE")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        // С активными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        create(runner, duck);
        getIdDuck(runner);
        Message resp = new Message().message(MessageStatus.FLYING.getValue());
        fly(runner);
        validateResponse(runner, resp, HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: FIXED")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        // С связанными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.FIXED);
        create(runner, duck);
        getIdDuck(runner);

        fly(runner);
        validateResponse(runner, "{" + "\"message\": \"I can't fly\"}", HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: UNDEFINED")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        // С неопределенными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack");
        create(runner, duck);
        getIdDuck(runner);

        fly(runner);
        validateResponse(runner, "{" + "\"message\": \"Wings are not detected\"}",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Метод, позволяющий плыть уточке (существующий id)")
    @CitrusTest
    public void swimExists(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        // Утка с существующим id
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        create(runner, duck);
        getIdDuck(runner);
        swim(runner);
        validateResponse(runner, "{" + "\"message\": \"I'm swimming\"}", HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий плыть уточке (несуществующий id)")
    @CitrusTest
    public void swimNonexist(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            // Утка с несуществующим id
            Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
            create(runner, duck);
            getIdDuck(runner);
        } finally {
            delete(runner);
            swim(runner);
            validateResponse(runner, "{" + "\"message\": \"Paws are not found\"}", HttpStatus.NOT_FOUND);
        }
    }

    @Test(description = "Метод, позволяющий крякать уточке (четный id)")
    @CitrusTest
    public void quackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        runner.$(action(context -> {
            do {
                create(runner, duck);
                getIdDuck(runner);
            }
            while (Integer.parseInt(context.getVariable("id")) % 2 != 0);
        }));
        quack(runner, "${id}", "1", "1");
        Sound soundDuck = new Sound().sound("quack");
        validateResponse(runner, soundDuck, HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий крякать уточке (нечетный id)")
    @CitrusTest
    public void quackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> delete(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        runner.$(action(context -> {
            do {
                create(runner, duck);
                getIdDuck(runner);
            }
            while (Integer.parseInt(context.getVariable("id")) % 2 == 0);
        }));

        quack(runner, "${id}", "1", "1");
        Sound soundDuck = new Sound().sound("quack");
        validateResponse(runner, soundDuck, HttpStatus.OK);
    }
}
