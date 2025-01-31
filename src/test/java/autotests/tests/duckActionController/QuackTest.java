package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClients;
import autotests.payloads.Duck;
import autotests.payloads.Sound;
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
@Feature("Эндпоинт /api/duck/action/quack")
public class QuackTest extends DuckActionsClients {
    @Test(description = "Метод, позволяющий крякать уточке (четный id)")
    @CitrusTest
    public void quackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomEnumValue('99999998', '777777788', '555555588')");
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck.color(), duck.height() ,  duck.material() ,duck.sound(), duck.wingsState());
        quack(runner, "${id}", "1", "1");
        Sound soundDuck = new Sound().sound("quack");
        validateResponse(runner, soundDuck, HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий крякать уточке (нечетный id)")
    @CitrusTest
    public void quackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomEnumValue('99999999', '7777777', '5555555')");
        runner.$(doFinally().actions(action -> deteteDuckDB(runner)));
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck.color(), duck.height() ,  duck.material() ,duck.sound(), duck.wingsState());
        quack(runner, "${id}", "1", "1");
        Sound soundDuck = new Sound().sound("quack");
        validateResponse(runner, soundDuck, HttpStatus.OK);
    }
}
