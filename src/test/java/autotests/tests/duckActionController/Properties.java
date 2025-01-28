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

import static com.consol.citrus.DefaultTestActionBuilder.action;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/properties")
public class Properties extends DuckActionsClients {
    @Test(description = "Метод получения характеристик уточки (Нечетный id material = rubber")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomEnumValue('999999979', '77777797', '55555655')");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        Duck duck = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(WingState.ACTIVE);

        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");

        // Оставляем поле material по умолчанию rubber и отправляем запрос на просмотр  свойств
        getProperties(runner);
        validateResponse(runner, duck, HttpStatus.OK);

    }

    @Test(description = "Метод получения характеристик уточки (Четный materiai = wood")
    @CitrusTest
    public void propertiesOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomEnumValue('999992998', '7775777788', '5555155588')");
        runner.$(doFinally().actions(action -> updateData(runner, "DELETE FROM DUCK WHERE ID = ${id}")));
        //Меняем поле material на wood и отправляем запрос на просмотр свойств;
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);

        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");

        getProperties(runner);
        validateResponse(runner, duck, HttpStatus.OK);
    }
}
