package autotests.tests.duckController;

import autotests.clients.DuckActionsClients;
import autotests.payloads.*;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import com.consol.citrus.TestCaseRunner;
import org.testng.annotations.Optional;
import com.consol.citrus.annotations.CitrusResource;
import org.springframework.http.HttpStatus;
import autotests.clients.DuckActionsClients;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class Create  extends DuckActionsClients {
    @Test(description = "Метод создания утки (material = rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
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
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        String responseBody = "duckActionsTest/successfulCreateWood.json";
        create(runner, duck);
        validateResponseCreateAndGetId(runner, responseBody, HttpStatus.OK);
        validateDuck(runner, "${id}", duck.color(), duck.height().toString(), duck.material(), duck.sound(),
                duck.wingsState().toString());
    }
}
