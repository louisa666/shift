package autotests.tests.duckController;

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
@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/update")
public class Update extends DuckActionsClients {
    @Test(description = "Метод изменения уточки (меняем цвет и высоту")
    @CitrusTest
    public void updateHeight(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id","1234567");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        // Создаем утку
        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");

        //Меняем цвет и высоту уточки
        duck.color("red");
        duck.height(7.0);
        update(runner, duck.color(), duck.height(), duck.material(), duck.sound(), duck.wingsState());
        validateResponse(runner, "{" +
                "  \"message\": \"Duck with id = ${id} is updated\"" + "}", HttpStatus.OK);

        validateDuck(runner, "${id}", duck.color(), duck.height().toString(), duck.material(), duck.sound(),
                duck.wingsState().toString());

    }

    @Test(description = "Метод изменения уточки (меняем цвет и звук")
    @CitrusTest
    public void updateSound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomNumber(10, true)");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
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

        validateDuck(runner, "${id}", duck.color(), duck.height().toString(), duck.material(), duck.sound(),
                duck.wingsState().toString());

    }
}
