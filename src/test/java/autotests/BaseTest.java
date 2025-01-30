package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient yellowDuckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    protected void sendRequestGet(TestCaseRunner runner, HttpClient URL, String path)
    {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get(path));
    }
    protected void sendRequestDelete(TestCaseRunner runner, HttpClient URL, String path)
    {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .delete(path));
    }
    protected void sendRequestPut(TestCaseRunner runner, HttpClient URL, String path)
    {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .put(path));
    }
    protected void sendRequestPost(TestCaseRunner runner, HttpClient URL, String path, Object body)
    {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }
    public void validateResponseTemplate(TestCaseRunner runner,HttpClient URL, String responseMessage, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }
    public void validateResponseTemplate(TestCaseRunner runner, HttpClient URL, Object responseMessage, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(responseMessage, new ObjectMapper())));
    }
    public void validateResponseTemplatePath(TestCaseRunner runner, HttpClient URL, String responseMessage, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .extract(fromBody().expression("$.id", "id"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(responseMessage)));
    }

}
