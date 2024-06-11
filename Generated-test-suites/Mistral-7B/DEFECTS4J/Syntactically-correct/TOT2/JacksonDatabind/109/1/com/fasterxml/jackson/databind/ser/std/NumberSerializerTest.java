import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

  @Test
  public void testSerializeNumber() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    NumberSerializer numberSerializer = new NumberSerializer(Number.class);

    Number number = 123;
    String expected = "123";

    String result = objectMapper.writeValueAsString(number, numberSerializer);

    assertEquals(expected, result);
  }

  // Add test cases for other public methods and edge cases
}