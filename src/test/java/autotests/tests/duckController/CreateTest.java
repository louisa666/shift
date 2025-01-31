package autotests.tests.duckController;

import autotests.clients.DuckActionsClients;
import autotests.payloads.*;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class CreateTest extends DuckActionsClients {
    @Test(description = "Метод создания утки (material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        String responseBody = "duckActionsTest/successfulCreateRubber.json";
        create(runner, duck);
        validateResponseCreateAndGetId(runner, responseBody, HttpStatus.OK);
        validateDuck(runner, "${id}", duck.color(), duck.height().toString(), duck.material(), duck.sound(),
                duck.wingsState().toString());

    }

    @Test(description = "Метод создания утки (material = wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        String responseBody = "duckActionsTest/successfulCreateWood.json";
        create(runner, duck);
        validateResponseCreateAndGetId(runner, responseBody, HttpStatus.OK);
        validateDuck(runner, "${id}", duck.color(), duck.height().toString(), duck.material(), duck.sound(),
                duck.wingsState().toString());
    }
    Duck d1 = new Duck().color("black").height(2.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
    Duck d2 = new Duck().color("orange").height(3.0).material("hhhh").sound("quack").wingsState(WingState.ACTIVE);
    Duck d3 = new Duck().color("wight").height(8.0).material("woqqqqod").sound("quack").wingsState(WingState.ACTIVE);
    Duck d4 = new Duck().color("blue").height(1.0).material("qqqqd").sound("quack").wingsState(WingState.ACTIVE);
    Duck d5 = new Duck().color("pink").height(99.0).material("waaa").sound("quack").wingsState(WingState.ACTIVE);
    @DataProvider
    public Object[][] duckList() {
        return new Object[][]{
                {d1,"duckActionsTest/getProperties/duckOne.json",null},
                {d2 ,"duckActionsTest/getProperties/duckTwo.json",null},
                {d3,"duckActionsTest/getProperties/duckThree.json",null},
                {d4,"duckActionsTest/getProperties/duckFour.json",null},
                {d5,"duckActionsTest/getProperties/duckFive.json",null}}; }
    @Test(description = "Параметризованный тест на создание 5-ти разных уток", dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"payload", "response", "runner"})
    public void createDucks(Object payload, String response, @Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        create(runner, payload);
        validateResponseCreateAndGetId(runner, response, HttpStatus.OK);
    }
}
