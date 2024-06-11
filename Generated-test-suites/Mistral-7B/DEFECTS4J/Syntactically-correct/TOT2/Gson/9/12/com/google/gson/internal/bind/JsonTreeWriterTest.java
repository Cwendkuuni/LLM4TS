import static org.junit.Assert.assertEquals;
import com.google.gson.JsonElement;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Hello World");
    JsonElement jsonElement = writer.get();
    JsonPrimitive expectedJsonPrimitive = new JsonPrimitive("Hello World");
    assertEquals(expectedJsonPrimitive, jsonElement);
  }
}