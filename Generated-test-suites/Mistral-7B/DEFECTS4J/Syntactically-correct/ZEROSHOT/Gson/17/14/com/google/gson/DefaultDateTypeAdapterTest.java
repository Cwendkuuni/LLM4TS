import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter dateTypeAdapter;

    @Before
    public void setUp() {
        dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testSerializeDate() {
        Date date = new Date();
        String expectedDateString = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);

        String actualDateString = "";
        try {
            dateTypeAdapter.write(new JsonWriter(System.out), date);
            actualDateString = System.out.toString().split(": ")[1];
            System.out.flush();
            System.out.reset();
        } catch (IOException e) {
            Assert.fail("Failed to serialize date object to JSON string.");
        }

        Assert.assertEquals("Expected and actual date strings do not match.", expectedDateString, actualDateString);
    }

    @Test
    public void testDeserializeDate() {
        String dateString = "2022-12-31T23:59:59.999Z";
        Date date = null;
        try {
            date = dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
        } catch (IOException e) {
            Assert.fail("Failed to deserialize date string to Date object.");
        }

        Assert.assertNotNull("Date object should not be null.", date);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeInvalidDate() {
        String dateString = "InvalidDateFormat";
        dateTypeAdapter.read(new JsonReader(new StringReader(dateString)));
    }

    @Test
    public void testSerializeTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String expectedTimestampString = dateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(timestamp.toDate());

        String actualTimestampString = "";
        try {
            dateTypeAdapter.write(new JsonWriter(System.out), timestamp);
            actualTimestampString = System.out.toString().split(": ")[1];
            System.out.flush();
            System.out.reset();
        } catch (IOException e) {
            Assert.fail("Failed to serialize Timestamp object to JSON string.");
        }

        Assert.assertEquals("Expected and actual timestamp strings do not match.", expectedTimestampString, actualTimestampString);
    }

    @Test
    public void testDeserializeTimestamp() {
        String timestampString = "2022-12-31T23:59:59.999Z";
        Timestamp timestamp = null;
        try {
            timestamp = (Timestamp) dateTypeAdapter.read(new JsonReader(new StringReader(timestampString)));
        } catch (IOException e) {
            Assert.fail("Failed to deserialize timestamp string to Timestamp object.");
        }

        Assert.assertNotNull("Timestamp object should not be null.", timestamp);
    }

    @Test
    public void testSerializeSqlDate() {
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String expectedSqlDateString = dateFormat("yyyy-MM-dd").format(sqlDate);

        String actualSqlDateString = "";
        try {
            dateTypeAdapter.write(new JsonWriter(System.out), sqlDate);
            actualSqlDateString = System.out.toString().split(": ")[1];
            System.out.flush();
            System.out.reset();
        } catch (IOException e) {
            Assert.fail("Failed to serialize sqlDate object to JSON string.");
        }

        Assert.assertEquals("Expected and actual sqlDate strings do not match.", expectedSqlDateString, actualSqlDateString);
    }

    @Test
    public void testDeserializeSqlDate() {
        String sqlDateString = "2022-12-31";
        java.sql.Date sqlDate = null;
        try {
            sqlDate = (java.sql.Date) dateTypeAdapter.read(new JsonReader(new StringReader(sqlDateString)));
        } catch (IOException e) {
            Assert.fail("Failed to deserialize sqlDate string to sqlDate object.");
        }

        Assert.assertNotNull("sqlDate object should not be null.", sqlDate);
    }

    private SimpleDateFormat dateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.US);
    }
}