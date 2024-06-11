import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;
import org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

 @Test
 public void testDefaultDateTypeAdapter() {
 DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 DefaultDateTypeAdapter defaultDateTypeAdapterUsFormat = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
 DefaultDateTypeAdapter defaultDateTypeAdapterCustomFormat = new DefaultDateTypeAdapter(Date.class, SimpleDateFormat.DEFAULT_DATE_FORMAT, SimpleDateFormat.DEFAULT_DATE_FORMAT);
 DefaultDateTypeAdapter defaultDateTypeAdapterCustomStyle = new DefaultDateTypeAdapter(Date.class, Date.LENGTH, Date.HOUR_OF_DAY);
 DefaultDateTypeAdapter defaultDateTypeAdapterTimestamp = new DefaultDateTypeAdapter(Timestamp.class);
 DefaultDateTypeAdapter defaultDateTypeAdapterSqlDate = new DefaultDateTypeAdapter(java.sql.Date.class);

 // Test write
 Date date = new Date();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 defaultDateTypeAdapter.write(jsonWriter, date);
 System.out.println();
 defaultDateTypeAdapterUsFormat.write(jsonWriter, date);
 System.out.println();
 defaultDateTypeAdapterCustomFormat.write(jsonWriter, date);
 System.out.println();
 defaultDateTypeAdapterCustomStyle.write(jsonWriter, date);
 System.out.println();
 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 defaultDateTypeAdapterTimestamp.write(jsonWriter, timestamp);
 System.out.println();
 java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
 defaultDateTypeAdapterSqlDate.write(jsonWriter, sqlDate);
 System.out.println();
 jsonWriter.close();

 // Test read
 Gson gson = new Gson();
 JsonElement jsonElement = JsonParser.parseString("\"2022-12-31 23:59:59\"");
 Date readDate = gson.fromJson(jsonElement, DefaultDateTypeAdapter.class).read(JsonParser.parseString("\"2022-12-31 23:59:59\""));
 assertEquals(new Date(1672751999000L), readDate);

 jsonElement = JsonParser.parseString("\"2022-12-31T23:59:59Z\"");
 Timestamp readTimestamp = gson.fromJson(jsonElement, DefaultDateTypeAdapter.class).read(JsonParser.parseString("\"2022-12-31T23:59:59Z\""));
 assertEquals(new Timestamp(1672751999000L), readTimestamp);

 jsonElement = JsonParser.parseString("\"2022-12-31\"");
 java.sql.Date readSqlDate = gson.fromJson(jsonElement, DefaultDateTypeAdapter.class).read(JsonParser.parseString("\"2022-12-31\""));
 assertEquals(new java.sql.Date(1672732800000L), readSqlDate);
 }

 @Test
 public void testSerializationWithAnnotations() {
 @SerializedName(value = "dateField")
 Date dateField = new Date();

 JsonElement jsonElement = new Gson().toJsonTree(new MyClassWithAnnotation(dateField));

 Date readDate = new Gson().fromJson(jsonElement, MyClassWithAnnotation.class).dateField;
 assertEquals(new Date(System.currentTimeMillis()), readDate);
 }

 public static class MyClassWithAnnotation {
 @SerializedName(value = "dateField")
 Date dateField;
 }
}