import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestConstruction() {
        long expectedStatusId = 123L;
        String expectedUrl = "https://twitter.com/statuses/123";
        OEmbedRequest actual = new OEmbedRequest(expectedStatusId, expectedUrl);
        OEmbedRequest expected = new OEmbedRequest(expectedStatusId, expectedUrl);
        assertEquals(expected, actual);
    }
}