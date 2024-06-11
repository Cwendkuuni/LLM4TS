import com.google.gson.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteDate() throws IOException {
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.write(writer, date);
    String jsonOutput = writer.toString();
    assertEquals("Expected date to be serialized correctly", date.toString(), jsonOutput);
  }
  
  @Test(expected = JsonIOException.class)
  public void testReadNullInput() {
    JsonReader reader = new JsonReader(new StringReader(""));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(reader);
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadEmptyInput() {
    JsonReader reader = new JsonReader(new StringReader(""));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(reader);
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadInvalidInput() {
    JsonReader reader = new JsonReader(new StringReader("invalidDateString"));
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    adapter.read(reader);
  }
  
  @Test
  public void testReadValidDates() throws IOException {
    JsonReader reader;
    Date date;
    
    reader = new JsonReader(new StringReader("2022-01-01"));
    date = new DefaultDateTypeAdapter(Date.class).read(reader);
    assertEquals("Expected parsed date to be equal to input", "2022-01-01", new SimpleDateFormat("yyyy-MM-dd").format(date));

    reader = new JsonReader(new StringReader("2022-01-01T12:34:56.789Z"));
    date = new DefaultDateTypeAdapter(Date.class).read(reader);
    assertEquals("Expected parsed date to be equal to input", "2022-01-01T12:34:56.789Z", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date));
  }
  
  @Test
  public void testSerializationAndDeserialization() throws IOException {
    JsonReader reader = new JsonReader(new FileReader("test.json"));
    JsonParser parser = new JsonParser(reader);
    JsonElement jsonElement = parser.parse();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    Date date = new DefaultDateTypeAdapter().read(jsonObject.get("date").getAsJsonPrimitive().getAsString()).getTime();
    JsonWriter writer = new JsonWriter(new StringWriter());
    new DefaultDateTypeAdapter().write(writer, date);
    String jsonOutput = writer.toString();
    JsonObject expectedJsonObject = new JsonParser().parse(new FileReader("test.json")).getAsJsonObject();
    JsonObject actualJsonObject = new JsonParser().parse(new StringReader(jsonOutput)).getAsJsonObject();
    assertEquals("Expected serialized and deserialized JSON objects to be equal", expectedJsonObject, actualJsonObject);
  }
}