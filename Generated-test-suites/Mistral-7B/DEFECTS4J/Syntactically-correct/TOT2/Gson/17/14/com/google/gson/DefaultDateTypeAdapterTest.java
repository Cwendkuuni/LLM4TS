import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

  // ... Test cases from experts 1 and 2 ...

  @Test
  public void testWriteNullDateInput() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    adapter.write(jsonWriter, null);
    String jsonString = jsonWriter.toString();
    assertEquals("null", jsonString);
  }

  @Test
  public void testReadInvalidJsonInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String jsonString = "{\"date\": \"invalidformat\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testReadNullJsonInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (JsonIOException e) {
      // Expected
    }
  }

  @Test
  public void testReadEmptyJsonInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("{}"));
    try {
      adapter.read(jsonReader);
      fail();
    } catch (JsonIOException e) {
      // Expected
    }
  }

  @Test
  public void testReadJsonInputWithInvalidDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonParser parser = new JsonParser();
    String jsonString = "{\"date\": \"2023-02-30-12:30:60\"}"; // invalid format
    try {
      JsonElement jsonElement = parser.parse(jsonString);
      adapter.read(jsonElement.getAsJsonObject().entrySet().iterator().next().getValue().getAsJsonObject().get("date").getAsJsonPrimitive());
      fail();
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }

  @Test
  public void testReadJsonInputWithValidDateFormat() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonParser parser = new JsonParser();
    String jsonString = "{\"date\": \"2023-02-28 12:30:60\"}";
    JsonElement jsonElement = parser.parse(jsonString);
    Date date = adapter.read(jsonElement.getAsJsonObject().entrySet().iterator().next().getValue().getAsJsonObject().get("date").getAsJsonPrimitive());
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date expectedDate = formatter.parse("2023-02-28 12:30:60");
    assertEquals(expectedDate, date);
  }
}

//