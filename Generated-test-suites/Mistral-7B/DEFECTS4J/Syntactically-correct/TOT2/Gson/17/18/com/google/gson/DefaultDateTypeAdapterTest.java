import static org.junit.Assert.assertEquals;
import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.JsonWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.TimeZone;

public class DefaultDateTypeAdapterTest {

    @org.junit.Test
    public void testWriteMethods() throws Exception {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter jsonWriter = new JsonWriter(new StringWriter());
        Date date = new Date();
        dateTypeAdapter.write(jsonWriter, date);
        jsonWriter.flush();
        String expectedJson = "\"2022-12-31T12:00:00.000Z\""; // The output format is based on the default US locale
        String actualJson = jsonWriter.toString();
        assertEquals(expectedJson, actualJson);
    }
}