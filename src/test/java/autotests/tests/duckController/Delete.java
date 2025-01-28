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
@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/delete")
public class Delete extends DuckActionsClients {
    @Test(description = "Метод удаления уточки")
    @CitrusTest
    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("id", "citrus:randomNumber(10, true)");
        Duck duck = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState(WingState.ACTIVE);

        updateData(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                "values (${id}, '" + duck.color() + "', " + duck.height() + ", '" + duck.material() + "', '" + duck.sound() + "'" +
                ",'" + duck.wingsState() + "');");
        delete(runner);
        validateResponse(runner, "{" +
                "  \"message\": \"Duck is deleted\"" +
                "}", HttpStatus.OK);
    }
}
