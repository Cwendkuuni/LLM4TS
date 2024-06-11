import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import junit.framework.TestCase;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DefaultDateTypeAdapterTest {

    private static final String TEST_DATE = "2023-01-01";
    private static final String TEST_TIMESTAMP = "2023-01-01T12:34:56.789Z";
    private static final String TEST_SQL_DATE = "2023-01-01";

    @org.junit.Test
    public void testWriteDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Date date = new Date();
        adapter.write(writer, date);
        writer.flush();

        Date parsedDate = null;
        try {
            parsedDate = gson.fromJson(writer.toString(), Date.class);
        } catch (JsonSyntaxException e) {
            TestCase.fail("Failed to parse date from string: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed date to be the same as the original date", date, parsedDate);
    }

    @org.junit.Test
    public void testWriteTimestamp() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        adapter.write(writer, timestamp);
        writer.flush();

        Timestamp parsedTimestamp = null;
        try {
            parsedTimestamp = gson.fromJson(writer.toString(), Timestamp.class);
        } catch (JsonSyntaxException e) {
            TestCase.fail("Failed to parse timestamp from string: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed timestamp to be the same as the original timestamp", timestamp, parsedTimestamp);
    }

    @org.junit.Test
    public void testWriteSqlDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        adapter.write(writer, sqlDate);
        writer.flush();

        java.sql.Date parsedSqlDate = null;
        try {
            parsedSqlDate = gson.fromJson(writer.toString(), java.sql.Date.class);
        } catch (JsonSyntaxException e) {
            TestCase.fail("Failed to parse sql-date from string: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed sql-date to be the same as the original sql-date", sqlDate, parsedSqlDate);
    }

    @org.junit.Test
    public void testReadDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(TEST_DATE);

        Date date = null;
        try {
            date = adapter.read(parser.newStreamReader(jsonElement.getAsString()));
        } catch (IOException e) {
            TestCase.fail("Failed to parse date from JsonElement: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed date to be the same as the test date", TEST_DATE, new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    @org.junit.Test
    public void testReadTimestamp() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(TEST_TIMESTAMP);

        Timestamp timestamp = null;
        try {
            timestamp = adapter.read(parser.newStreamReader(jsonElement.getAsString()));
        } catch (IOException e) {
            TestCase.fail("Failed to parse timestamp from JsonElement: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed timestamp to be the same as the test timestamp",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(TEST_TIMESTAMP),
                new Timestamp(timestamp.getTime()));
    }

    @org.junit.Test
    public void testReadSqlDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(TEST_SQL_DATE);

        java.sql.Date sqlDate = null;
        try {
            sqlDate = adapter.read(parser.newStreamReader(jsonElement.getAsString()));
        } catch (IOException e) {
            TestCase.fail("Failed to parse sql-date from JsonElement: " + e.getMessage());
        }

        TestCase.assertEquals("Expected the parsed sql-date to be the same as the test sql-date", TEST_SQL_DATE,
                new SimpleDateFormat("yyyy-MM-dd").format(sqlDate));
    }

    @org.junit.Test
    public void testReadInvalidDateFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse("invalid_date_format");

        try {
            adapter.read(parser.newStreamReader(jsonElement.getAsString()));
            TestCase.fail("Expected an exception when parsing an invalid date format");
        } catch (JsonSyntaxException e) {
            TestCase.assertEquals("Expected the exception message to be: The date should be a string value",
                    "The date should be a string value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testReadInvalidTimestampFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse("invalid_timestamp_format");

        try {
            adapter.read(parser.newStreamReader(jsonElement.getAsString()));
            TestCase.fail("Expected an exception when parsing an invalid timestamp format");
        } catch (JsonSyntaxException e) {
            TestCase.assertEquals("Expected the exception message to be: The date should be a string value",
                    "The date should be a string value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testReadInvalidSqlDateFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse("invalid_sql_date_format");

        try {
            adapter.read(parser.newStreamReader(jsonElement.getAsString()));
            TestCase.fail("Expected an exception when parsing an invalid sql-date format");
        } catch (JsonSyntaxException e) {
            TestCase.assertEquals("Expected the exception message to be: The date should be a string value",
                    "The date should be a string value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testSerializationWithUSLocale() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Locale.US);
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Date date = new Date();
        adapter.write(writer, date);
        writer.flush();

        String serializedDate = writer.toString();
        TestCase.assertEquals("Expected the serialized date to match the default US date format",
                new SimpleDateFormat("M/d/yy h:mm a z").format(date),
                serializedDate);
    }

    @org.junit.Test
    public void testSerializationWithCustomLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Locale.FRENCH, timeZone);
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Date date = new Date();
        adapter.write(writer, date);
        writer.flush();

        String serializedDate = writer.toString();
        TestCase.assertEquals("Expected the serialized date to match the French date format",
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z").format(date),
                serializedDate);
    }

    @org.junit.Test
    public void testSerializationWithCustomDateFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss z");
        Gson gson = new GsonBuilder().setLenient().create();
        JsonWriter writer = new JsonWriter(System.out);
        writer.setLenient(true);

        Date date = new Date();
        adapter.write(writer, date);
        writer.flush();

        String serializedDate = writer.toString();
        TestCase.assertEquals("Expected the serialized date to match the custom date format",
                "dd-MM-yyyy HH:mm:ss z", serializedDate.trim());
    }
}