import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.Dynamic;
import com.fasterxml.jackson.databind.ser.std.Default;
import com.fasterxml.jackson.databind.ser.std.StringKeySerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetStdKeySerializer_String() throws JsonMappingException, JsonGenerationException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
        Assert.assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Date() throws JsonMappingException, JsonGenerationException, IOException {
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, false);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String json = mapper.writeValueAsString(new Object() {
            public Date key = date;
        }, new DefaultSerializerModifier().findValueSerializer(Object.class, mapper, date.getClass()));
        Assert.assertEquals("\"2022-03-19T14:15:24.320Z\"", json.substring(1, json.length() - 1));
        Assert.assertEquals(StdKeySerializers.DEFAULT.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(null));
    }

    @Test
    public void testGetStdKeySerializer_Calendar() throws JsonMappingException, JsonGenerationException, IOException {
        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, false);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String json = mapper.writeValueAsString(new Object() {
            public Calendar key = calendar;
        }, new DefaultSerializerModifier().findValueSerializer(Object.class, mapper, calendar.getClass()));
        Assert.assertEquals("\"2022-03-19T14:15:24.320Z\"", json.substring(1, json.length() - 1));
        Assert.assertEquals(StdKeySerializers.DEFAULT.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(null));
    }

    @Test
    public void testGetStdKeySerializer_Class() throws JsonMappingException, JsonGenerationException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Class.class, false);
        Assert.assertEquals(StdKeySerializers.DEFAULT, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Number() throws JsonMappingException, JsonGenerationException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, int.class, false);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Unknown() throws JsonMappingException, JsonGenerationException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Object.class, false);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_Enum() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        Enum[] enums = Enum.values();
        for (Enum e : enums) {
            JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, e.getClass());
            Assert.assertEquals(StdKeySerializers.Default.class, serializer.getClass());
            Assert.assertEquals(e.name(), serializer.serialize(e, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider()).toString());
        }
    }

    @Test
    public void testGetFallbackKeySerializer_Unknown() throws JsonMappingException, JsonGenerationException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Object.class);
        Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Dynamic() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        class DynamicKey {
            public Object key;
        }

        DynamicKey dynamicKey = new DynamicKey();
        dynamicKey.key = new java.util.HashMap<>();

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), dynamicKey.getClass().getDeclaredField("key").getType(), true);
        Assert.assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        String json = mapper.writeValueAsString(new Object() {
            public Object key = dynamicKey;
        });
        Assert.assertTrue(json.contains("\"class \""));
    }
}