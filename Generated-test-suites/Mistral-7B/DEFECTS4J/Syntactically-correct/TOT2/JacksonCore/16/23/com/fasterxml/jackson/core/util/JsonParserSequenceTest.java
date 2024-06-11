import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JsonParserSequenceTest {
    @Test
    public void testJsonParserSequenceConstructor() throws IOException {
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        assertNotNull(jsonParserSequence);
    }
}