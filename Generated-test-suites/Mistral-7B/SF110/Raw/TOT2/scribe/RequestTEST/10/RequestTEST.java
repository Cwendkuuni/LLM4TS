import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import static org.junit.Assert.assertEquals;

public class RequestTest {

    @Test
    public void testSend() throws Exception {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertEquals(200, response.getStatusCode());
    }
}