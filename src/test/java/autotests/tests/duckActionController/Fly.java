package autotests.tests.duckActionController;

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

import static com.consol.citrus.DefaultTestActionBuilder.action;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class Fly extends DuckActionsClients{
    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: ACTIVE")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomNumber(10, true)");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        // С активными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);

        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");

        Message resp = new Message().message(MessageStatus.FLYING.value());
        fly(runner);
        validateResponse(runner, resp, HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: FIXED")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomNumber(10, true)");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        // С связанными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.FIXED);
        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");

        fly(runner);
        validateResponse(runner, "{" + "\"message\": \"I can't fly\"}", HttpStatus.OK);
    }

    @Test(description = "Метод, позволяющий лететь или отказаться от полета уточке (крылья: UNDEFINED")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id","1234567");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        // С неопределенными крыльями
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.UNDEFINED);
        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");;

        fly(runner);
        validateResponse(runner, "{" + "\"message\": \"Wings are not detected\"}",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
