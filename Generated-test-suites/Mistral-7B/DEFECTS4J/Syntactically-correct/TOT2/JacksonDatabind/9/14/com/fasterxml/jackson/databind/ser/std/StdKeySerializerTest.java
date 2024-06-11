import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {
    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        String expected = "\"2022-12-31T12:00:00.000Z\""; // for example, a valid ISO-8601 formatted date
        String actual = ""; // assumed to be populated by provider.defaultSerializeDateKey()

        StringWriter writer = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(date, jgen, provider);
        jgen.flush();

        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSerialize_String() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "testString";

        String expected = "\"testString\"";

        StringWriter writer = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(value, jgen, provider);
        jgen.flush();

        assertEquals(expected, writer.toString());
    }
}