import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken_SingleParser() throws IOException {
        JsonParser parser1 = createParser("[]");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1 });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parserSequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parserSequence.close();
        parser1.close();
    }

    @Test
    public void testNextToken_MultipleParsers() throws IOException {
        JsonParser parser1 = createParser("[]");
        JsonParser parser2 = createParser("{}");
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        parserSequence.nextToken();

        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        parserSequence.nextToken(); // empty string key
        parserSequence.nextToken(); // value: null

        parserSequence.nextToken(); // property name: "foo"
        parserSequence.nextToken(); // value: "bar"

        parserSequence.nextToken(); // comma

        parserSequence.nextToken(); // property name: "baz"
        parserSequence.nextToken(); // value: 1

        parserSequence.nextToken(); // comma

        parserSequence.nextToken(); // property name: "qux"
        parserSequence.nextToken(); // value: true

        parserSequence.nextToken(); // end object

        parserSequence.nextToken(); // end array

        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        JsonParser parser1 = createParser("[]");
        JsonParser parser2 = createParser("{}");
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Switch to next parser
        parserSequence.nextToken();

        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    @Test(expected = IOException.class)
    public void testNextToken_IOException() throws IOException {
        JsonParser parser = createParser("{\"foo\": \"bar\"}");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser });

        parserSequence.nextToken(); // START_OBJECT

        parserSequence.nextToken(); // key: "foo"

        parserSequence.nextToken(); // value: "bar"

        parserSequence.nextToken(); // comma

        parserSequence.nextToken(); // IOException: Unexpected character ('d' (code 100)): was expecting comma (',')
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseException() throws IOException {
        JsonParser parser = createParser("{\"foo\": [\"bar\"");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser });

        parserSequence.nextToken(); // START_OBJECT

        parserSequence.nextToken(); // key: "foo"

        parserSequence.nextToken(); // START_ARRAY

        parserSequence.nextToken(); // IOException: Cannot deserialize value of type `java.util.ArrayList` from Object value (no "type" property)
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = createParser("[]");
        JsonParser parser2 = createParser("{}");
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, parserSequence.containedParsersCount());

        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    private JsonParser createParser(String json) throws IOException {
        StringReader reader = new StringReader(json);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readerFor(JsonParser.class).readValue(reader);
    }
}