import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter dateAdapter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    @Before
    public void setup() {
        dateAdapter = new DefaultDateTypeAdapter(Date.class);
        jsonReader = mock(JsonReader.class);
        jsonWriter = mock(JsonWriter.class);
    }

    @Test
    public void testWriteDate() throws IOException {
        Date date = new Date();
        when(jsonWriter.getValueType()).thenReturn(JsonToken.NULL);
        dateAdapter.write(jsonWriter, null);
        dateAdapter.write(jsonWriter, date);
        Assert.assertEquals("\"2022-12-31 12:00:00\"", jsonWriter.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
        when(jsonWriter.getValueType()).thenReturn(JsonToken.STRING);
        dateAdapter.write(jsonWriter, date);
        Assert.assertEquals(dateFormat.format(date), jsonWriter.toString());
    }

    @Test
    public void testReadDate() throws IOException, ParseException {
        Date expectedDate = new Date();
        String dateString = "2022-12-31 12:00:00";
        when(jsonReader.nextString()).thenReturn(dateString);
        Date actualDate = dateAdapter.read(jsonReader);
        Assert.assertEquals(expectedDate, actualDate);

        expectedDate = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
        dateString = "2022-12-31T12:00:00Z";
        when(jsonReader.nextString()).thenReturn(dateString);
        actualDate = dateAdapter.read(jsonReader);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Assert.assertEquals(expectedDate, new Date(dateFormat.parse(dateString).getTime()));

        jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenThrow(new IOException(""));
        Assert.assertThrows(JsonParseException.class, () -> dateAdapter.read(jsonReader));
    }

    @Test
    public void testWriteTimestamp() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(jsonWriter.getValueType()).thenReturn(JsonToken.NULL);
        dateAdapter.write(jsonWriter, null);
        dateAdapter.write(jsonWriter, timestamp);
        Assert.assertEquals("\"2022-12-31 12:00:00.123456\"", jsonWriter.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        Assert.assertEquals(dateFormat.format(timestamp), jsonWriter.toString());
    }

    @Test
    public void testReadTimestamp() throws IOException, ParseException {
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
        String timestampString = "2022-12-31 12:00:00.123456";
        when(jsonReader.nextString()).thenReturn(timestampString);
        Timestamp actualTimestamp = dateAdapter.read(jsonReader);
        Assert.assertEquals(expectedTimestamp, actualTimestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        expectedTimestamp = new Timestamp(dateFormat.parse(timestampString).getTime());
        when(jsonReader.nextString()).thenReturn(timestampString);
        actualTimestamp = dateAdapter.read(jsonReader);
        Assert.assertEquals(expectedTimestamp, actualTimestamp);

        jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenThrow(new IOException(""));
        Assert.assertThrows(JsonParseException.class, () -> dateAdapter.read(jsonReader));
    }

    @Test
    public void testWriteSqlDate() throws IOException {
        Date date = new Date(123, 11, 30);
        when(jsonWriter.getValueType()).thenReturn(JsonToken.NULL);
        dateAdapter.write(jsonWriter, null);
        dateAdapter.write(jsonWriter, date);
        Assert.assertEquals("\"2022-11-30\"", jsonWriter.toString());
    }

    @Test
    public void testReadSqlDate() throws IOException, ParseException {
        Date expectedDate = new Date(123, 11, 30);
        String dateString = "\"2022-11-30\"";
        when(jsonReader.nextString()).thenReturn(dateString);
        Date actualDate = dateAdapter.read(jsonReader);
        Assert.assertEquals(expectedDate, actualDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        expectedDate = dateFormat.parse(dateString);
        when(jsonReader.nextString()).thenReturn(dateString);
        actualDate = dateAdapter.read(jsonReader);
        Assert.assertEquals(expectedDate, actualDate);

        jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenThrow(new IOException(""));
        Assert.assertThrows(JsonParseException.class, () -> dateAdapter.read(jsonReader));
    }
}