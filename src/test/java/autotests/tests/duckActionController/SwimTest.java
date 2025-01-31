package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClients;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/swim")
public class SwimTest extends DuckActionsClients {
    @Test(description = "Метод, позволяющий плыть уточке (существующий id)")
    @CitrusTest
    public void swimExists(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomNumber(10, true)");
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        // Утка с существующим id
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck.color(), duck.height() ,  duck.material() ,duck.sound(), duck.wingsState());
        swim(runner);
        validateResponse(runner, "{" + "\"message\": \"I'm swimming\"}", HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий плыть уточке (несуществующий id)")
    @CitrusTest
    public void swimNonexist(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            runner.variable("id", "citrus:randomNumber(10, true)");
            // Утка с несуществующим id
            Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
            createDuckDB(runner, duck.color(), duck.height() ,  duck.material() ,duck.sound(), duck.wingsState());
        } finally {
            deteteDuckDB(runner);
            swim(runner);
            validateResponse(runner, "{" + "\"message\": \"Paws are not found\"}", HttpStatus.NOT_FOUND);
        }
    }
}
