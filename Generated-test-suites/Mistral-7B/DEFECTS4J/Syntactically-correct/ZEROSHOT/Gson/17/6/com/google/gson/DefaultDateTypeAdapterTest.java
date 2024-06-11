import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterInstance() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Assertions.assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterInstanceWithDatePattern() {
        String datePattern = "dd-MM-yyyy HH:mm:ss";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        Assertions.assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterInstanceWithDateStyles() {
        int dateStyle = DateFormat.FULL;
        int timeStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, dateStyle, timeStyle);
        Assertions.assertNotNull(adapter);
    }

    @Test
    public void testWriteDate() throws IOException {
        Gson gson = new Gson();
        JsonWriter jsonWriter = new JsonWriter(System.out);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();
        adapter.write(jsonWriter, date);
        jsonWriter.flush();
        String expectedOutput = "\"2023-03-23 12:34:56.000\""; // Replace with the actual output
        Assertions.assertEquals(expectedOutput, System.out.getBytes().toString());
    }

    @Test
    public void testReadDateFromValidString() throws IOException {
        String jsonString = "\"2023-03-23 12:34:56.000\""; // Replace with the actual output from writeDate test
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(jsonReader);
        Assertions.assertNotNull(date);
    }

    @Test
    public void testReadDateFromInvalidString() {
        String jsonString = "2023-03-23 invalid";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Assertions.assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testReadDateFromNullString() {
        JsonReader jsonReader = new JsonReader(new StringReader(null));
        Gson gson = new Gson();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Assertions.assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testWriteTimestamp() throws IOException {
        Gson gson = new Gson();
        JsonWriter jsonWriter = new JsonWriter(System.out);
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        adapter.write(jsonWriter, timestamp);
        jsonWriter.flush();
        String expectedOutput = "\"2023-03-23 12:34:56.789\""; // Replace with the actual output
        Assertions.assertEquals(expectedOutput, System.out.getBytes().toString());
    }

    @Test
    public void testReadTimestampFromValidString() throws IOException {
        String jsonString = "\"2023-03-23 12:34:56.789\""; // Replace with the actual output from writeTimestamp test
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
        Timestamp timestamp = adapter.read(jsonReader);
        Assertions.assertNotNull(timestamp);
    }

    @Test
    public void testReadTimestampFromInvalidString() {
        String jsonString = "2023-03-23 invalid";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
        Assertions.assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testReadTimestampFromNullString() {
        JsonReader jsonReader = new JsonReader(new StringReader(null));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
        Assertions.assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testWriteSqlDate() throws IOException {
        Gson gson = new Gson();
        JsonWriter jsonWriter = new JsonWriter(System.out);
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        adapter.write(jsonWriter, sqlDate);
        jsonWriter.flush();
        String expectedOutput = "\"2023-03-23\""; // Replace with the actual output
        Assertions.assertEquals(expectedOutput, System.out.getBytes().toString());
    }

    @Test
    public void testReadSqlDateFromValidString() throws IOException {
        String jsonString = "\"2023-03-23\""; // Replace with the actual output from writeSqlDate test
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
        java.sql.Date sqlDate = adapter.read(jsonReader);
        Assertions.assertNotNull(sqlDate);
    }

    @Test
    public void testReadSqlDateFromInvalidString() {
        String jsonString = "2023-03-23 invalid";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
        Assertions.assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testReadSqlDateFromNullString() {
        JsonReader jsonReader = new JsonReader(new StringReader(null));
        Gson gson = new Gson();
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
        Assertions.assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    }
}

class StringReader {
    StringReader(String s) {
        reader = new StringReader(s);
    }

    StringReader() {
        reader = new StringReader("");
    }

    JsonReader newStringReader(String s) {
        return new StringReader(s);
    }

    String readLine() throws IOException {
        return reader.readLine();
    }

    private StringReader reader;
}