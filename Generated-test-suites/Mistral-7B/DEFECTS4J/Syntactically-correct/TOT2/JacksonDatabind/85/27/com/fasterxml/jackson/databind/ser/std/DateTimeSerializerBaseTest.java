import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Class<String> stringClass = String.class;
        Boolean useTimestamp = Boolean.TRUE;
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringClass, useTimestamp, customFormat);
        assertEquals(stringClass, serializer.getType());
        assertEquals(useTimestamp, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, Boolean.FALSE, null);
        DateTimeSerializerBase<String> withFormatSerializer = serializer.withFormat(Boolean.TRUE, null);
        assertEquals(serializer.getClass(), serializer.getClass());
        assertEquals(Boolean.TRUE, withFormatSerializer._useTimestamp);
        assertEquals(null, withFormatSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, Boolean.TRUE, null);
        String value1 = null;
        boolean result1 = serializer.isEmpty(value1);
        assertTrue(result1);

        String value2 = new String();
        boolean result2 = serializer.isEmpty(value2);
        assertFalse(result2);

        long value3 = 0L;
        boolean result3 = serializer.isEmpty(value3);
        assertTrue(result3);

        String value4 = new String("2022-01-01T12:00:00.000Z");
        boolean result4 = serializer.isEmpty(value4);
        assertFalse(result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyWithInvalidValue() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, Boolean.TRUE, null);
        Object invalidValue = new Object();
        serializer.isEmpty(invalidValue);
    }

    @Test
    public void test_timestamp() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, Boolean.TRUE, null);
        Date value1 = new Date();
        long result1 = serializer._timestamp(value1);
        Date currentDate = new Date();
        long expectedResult1 = currentDate.getTime();
        assertEquals(expectedResult1, result1);

        Date value2 = null;
        long result2 = serializer._timestamp(value2);
        assertEquals(0, result2);

        String value3 = "Invalid Date";
        try {
            serializer._timestamp(value3);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSerializeWithTimestamp() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, Boolean.TRUE, null);
        Date value = new Date();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        try {
            serializer.serialize(value, gen, serializers);
            fail("Expected an IOException");
        } catch (IOException e) {
            // expected
        }

        JsonGenerator gen2 = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializers2 = new JsonSerializerFactory().getSerializerProvider();
        serializer.serialize(value, gen2, serializers2);
        String expectedResult = String.format("\"%d\"", value.getTime());
        String result = gen2.getOutputText();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSerializeWithStringFormat() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        Date value = new Date();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        try {
            serializer.serialize(value, gen, serializers);
            fail("Expected an IOException");
        } catch (IOException e) {
            // expected
        }

        JsonGenerator gen2 = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializers2 = new JsonSerializerFactory().getSerializerProvider();
        serializers2.setTimeZone(TimeZone.getTimeZone("UTC"));
        serializers2.setLocale(Locale.ENGLISH);
        serializer.serialize(value, gen2, serializers2);
        String expectedResult = "\"2022-01-01T12:00:00.000Z\"";
        String result = gen2.getOutputText();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorProvider().createJsonFormatVisitor(serializers, format);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, Boolean.FALSE, null);
        serializer._acceptJsonFormatVisitor(visitor, Date.class.getType(), Boolean.FALSE);
        assertEquals(JsonFormat.Shape.STRING, format.getShape());
        assertEquals("date-time", format.getSerializableType().getName());
    }

    @Test
    public void testGetSchema() {
        JsonFactory factory = new JsonFactory();
        SchemaFactory schemaFactory = new MinimalSchemaFactory();
        SerializerProvider serializers = new StandardSerializerFactory().build();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, Boolean.TRUE, null);
        ObjectCodec objectCodec = new ObjectMapper().getSerializerProvider();
        assertEquals("number", serializer.getSchema(serializers, Date.class.getType()).getSchemaValue().get("type").getTextValue());
    }
}