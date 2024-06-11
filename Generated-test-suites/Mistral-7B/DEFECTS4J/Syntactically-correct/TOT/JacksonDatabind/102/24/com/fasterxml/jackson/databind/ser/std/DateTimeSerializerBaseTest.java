import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new JsonMapperBuilder()
                .addSerializer(DateTime.class, new DateTimeSerializerBase.StandardSerializers.Instance())
                .build();
    }

    @Test
    public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);
        ObjectNode jsonNode = (ObjectNode) node;

        // Check if the serialized value is a number
        Assert.assertEquals("number", jsonNode.getClass().getSimpleName());
        Assert.assertEquals(date.getTime(), jsonNode.numberValue().longValue());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z"));
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);
        ObjectNode jsonNode = (ObjectNode) node;

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss Z", jsonNode.get("@class").asText());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss Z", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testUseTimestampSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a number
        Assert.assertEquals("number", node.getClass().getSimpleName());
        Assert.assertEquals(date.getTime(), node.numberValue().longValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueSerialization() throws JsonGenerationException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        SerializerProvider provider = new SerializerProvider();

        Date date = null;
        serializer.createContextual(provider, null).serializeValue(date, provider, provider); // This should throw an exception
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFormatSerialization() throws JsonGenerationException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss"));
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if an exception is thrown during serialization
        Assert.fail("Expected JsonMappingException to be thrown but no exception was thrown");
    }

    @Test
    public void testSerializationWithLocale() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH));
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@class").asText());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@locale").asText());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializationWithTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH), TimeZone.getTimeZone("UTC"));
        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@class").asText());
        Assert.assertEquals("UTC", jsonNode.get("@timeZone").asText());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss UTC", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializationWithCustomInstance() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        SerializerProvider provider = new SerializerProvider();

        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        AtomicReference<DateFormat> ref = new AtomicReference<>();
        ref.set(customFormat);
        serializer._reusedCustomFormat = ref;

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@class").asText());
        DateFormat formatter = serializer._reusedCustomFormat.getAndSet(null);
        Assert.assertNotNull(formatter);
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", formatter.toPattern());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializationWithThreadLocal() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        SerializerProvider provider = new SerializerProvider();

        ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            }
        };
        serializer._reusedCustomFormat = new AtomicReference<>(threadLocal);

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@class").asText());
        DateFormat formatter = threadLocal.get();
        Assert.assertNotNull(formatter);
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", formatter.toPattern());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializationWithDateFormatBuilder() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        SerializerProvider provider = new SerializerProvider();

        DateFormatBuilder dateFormatBuilder = new SimpleDateFormatBuilder("dd-MM-yyyy HH:mm:ss");
        serializer._reusedCustomFormat = new AtomicReference<>(dateFormatBuilder);

        Date date = new Date();
        JsonNode node = serializer.createContextual(provider, null).serializeValue(date, provider, provider);

        // Check if the serialized value is a string
        Assert.assertEquals("string", jsonNode.getClass().getSimpleName());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.get("@class").asText());
        DateFormat formatter = serializer._reusedCustomFormat.getAndSet(null);
        Assert.assertNotNull(formatter);
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", formatter.toPattern());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", jsonNode.asText());

        // Deserialize and check if the deserialized value is the same
        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }
}