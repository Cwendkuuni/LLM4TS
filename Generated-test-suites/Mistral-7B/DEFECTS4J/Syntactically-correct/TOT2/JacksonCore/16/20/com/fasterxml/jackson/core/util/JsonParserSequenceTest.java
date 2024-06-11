import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class JsonParserSequenceTest {

  private JsonParser firstParser;
  private JsonParser secondParser;
  private JsonParserSequence jsonParserSequence;

  @Before
  public void setup() {
    firstParser = mock(JsonParser.class);
    secondParser = mock(JsonParser.class);
    jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
  }

  // Expert 1's test case
  @Test
  public void testJsonParserSequenceConstruction() {
    assertEquals(firstParser, jsonParserSequence.getDelegate());
    assertEquals(2, jsonParserSequence.containedParsersCount());
  }

  // Expert 2's test case
  @Test
  public void testNextToken() throws IOException {
    // Test returning the token from the first parser
    when(firstParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
    JsonToken token = jsonParserSequence.nextToken();
    verify(firstParser, times(1)).nextToken();
    assertEquals(JsonToken.START_OBJECT, token);

    // Test returning the token from the second parser
    when(secondParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
    jsonParserSequence.nextToken(); // Consume the first token
    token = jsonParserSequence.nextToken();
    verify(secondParser, times(1)).nextToken();
    assertEquals(JsonToken.VALUE_STRING, token);

    // Test cycling through all parsers
    JsonParser[] parsers = { firstParser, secondParser };
    for (int i = 0; i < parsers.length; i++) {
      JsonParser parser = parsers[i];
      when(parser.nextToken()).thenReturn(JsonToken.values()[i % parsers.length].getValue());
    }
    for (int i = 0; i < parsers.length * 2; i++) {
      jsonParserSequence.nextToken();
    }
    for (int i = 0; i < parsers.length; i++) {
      verify(parsers[i], times(parsers.length / parsers.length)).nextToken();
    }
  }

  // Expert 3's test case
  @Test
  public void testSwitchToNext() {
    // Switch to the second parser
    jsonParserSequence.switchToNext();

    // Verify that the active parser has been switched
    assertEquals(secondParser, jsonParserSequence.getDelegate());

    // Test switching back to the first parser
    jsonParserSequence.switchToNext();
    assertEquals(firstParser, jsonParserSequence.getDelegate());
  }
}