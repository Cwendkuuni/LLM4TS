import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        SerializationConfig config = mapper.getSerializationConfig();

        // Test with null rawKeyType
        Class<?> rawKeyType1 = null;
        JsonSerializer<Object> serializer1 = StdKeySerializers.getStdKeySerializer(config, rawKeyType1, true);
        assertNotNull(serializer1);
        assertInstanceOf(StdKeySerializers.Dynamic.class, serializer1);

        // Test with String rawKeyType
        Class<?> rawKeyType2 = String.class;
        JsonSerializer<Object> serializer2 = StdKeySerializers.getStdKeySerializer(config, rawKeyType2, false);
        assertNotNull(serializer2);
        assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer2);

        // Test with Number rawKeyType
        Class<?> rawKeyType3 = Integer.class;
        JsonSerializer<Object> serializer3 = StdKeySerializers.getStdKeySerializer(config, rawKeyType3, false);
        assertNotNull(serializer3);
        assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer3);

        // Test with Class rawKeyType
        Class<?> rawKeyType4 = Date.class;
        JsonSerializer<Object> serializer4 = StdKeySerializers.getStdKeySerializer(config, rawKeyType4, false);
        assertNotNull(serializer4);
        assertInstanceOf(StdKeySerializers.Default.class, serializer4);

        // Test with Calendar rawKeyType
        Class<?> rawKeyType5 = Calendar.class;
        JsonSerializer<Object> serializer5 = StdKeySerializers.getStdKeySerializer(config, rawKeyType5, false);
        assertNotNull(serializer5);
        assertInstanceOf(StdKeySerializers.Default.class, serializer5);

        // Test with UUID rawKeyType
        Class<?> rawKeyType6 = UUID.class;
        JsonSerializer<Object> serializer6 = StdKeySerializers.getStdKeySerializer(config, rawKeyType6, false);
        assertNotNull(serializer6);
        assertInstanceOf(StdKeySerializers.Default.class, serializer6);

        // Test with unknown rawKeyType
        Class<?> rawKeyType7 = new Object() {}.getClass();
        JsonSerializer<Object> serializer7 = StdKeySerializers.getStdKeySerializer(config, rawKeyType7, false);
        assertNull(serializer7);
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        SerializationConfig config = mapper.getSerializationConfig();

        // Test with null rawKeyType
        Class<?> rawKeyType1 = null;
        JsonSerializer<Object> serializer1 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType1);
        assertNull(serializer1);

        // Test with Enum rawKeyType
        Class<?> rawKeyType2 = Enum.class;
        JsonSerializer<Object> serializer2 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType2);
        assertNotNull(serializer2);
        assertInstanceOf(StdKeySerializers.Dynamic.class, serializer2);

        // Test with String rawKeyType
        Class<?> rawKeyType3 = String.class;
        JsonSerializer<Object> serializer3 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType3);
        assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer3);

        // Test with Number rawKeyType
        Class<?> rawKeyType4 = Integer.class;
        JsonSerializer<Object> serializer4 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType4);
        assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer4);

        // Test with Class rawKeyType
        Class<?> rawKeyType5 = Date.class;
        JsonSerializer<Object> serializer5 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType5);
        assertInstanceOf(StdKeySerializers.Default.class, serializer5);

        // Test with unknown rawKeyType
        Class<?> rawKeyType6 = new Object() {}.getClass();
        JsonSerializer<Object> serializer6 = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType6);
        assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer6);
    }

    @Test
    public void testDynamicKeySerialization() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        SerializationConfig config = mapper.getSerializationConfig();
        config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        Object obj = new Object() {};
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, obj.getClass());

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Object[] { obj, obj });

        assertEquals("{\"0\":\"com.fasterxml.jackson.databind.ser.std.StdKeySerializers$Object\"" +
                ",\"1\":\"com.fasterxml.jackson.databind.ser.std.StdKeySerializers$Object\"}", json);
    }

    @Test
    public void testToStringSerialization() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        SerializationConfig config = mapper.getSerializationConfig();
        config.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        Enum enumValue = Enum.valueOf(String.class, "TEST");
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, enumValue.getClass());

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Object[] { enumValue, enumValue });

        assertEquals("{\"0\":\"TEST\",\"1\":\"TEST\"}", json);
    }

    @Test
    public void testDefaultKeySerialization() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        SerializationConfig config = mapper.getSerializationConfig();

        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Object[] { date, date });

        assertEquals("{\"0\":\"2023-03-18T14:19:01.234Z\",\"1\":\"2023-03-18T14:19:01.234Z\"}", json);

        Calendar calendar = Calendar.getInstance();
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);

        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Object[] { calendar, calendar });

        assertEquals("{\"0\":\"Sat Mar 18 14:19:01 CET 2023\",\"1\":\"Sat Mar 18 14:19:01 CET 2023\"}", json);
    }
}