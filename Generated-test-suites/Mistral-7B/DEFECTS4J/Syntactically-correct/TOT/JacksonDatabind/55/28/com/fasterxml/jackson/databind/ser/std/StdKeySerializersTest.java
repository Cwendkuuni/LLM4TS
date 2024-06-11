import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        SerializationConfig config = JsonMapperFactory.getJsonMapper().getSerializerProvider().getConfig();

        // Test with null rawKeyType
        assertNull(StdKeySerializers.getStdKeySerializer(config, null, true));

        // Test with String rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // Test with int rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test with Date rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, true);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Calendar rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, true);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Class rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, true);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Number rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Number.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test with Enum rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, true);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with UUID rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with custom class rawKeyType
        Class<?> customClass = new CustomClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, customClass, true);
        assertNull(serializer);

        // Test with Object rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, true);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        SerializationConfig config = JsonMapperFactory.getJsonMapper().getSerializerProvider().getConfig();

        // Test with null rawKeyType
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));

        // Test with String rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // Test with int rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // Test with Date rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Calendar rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Class rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with Enum rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with UUID rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // Test with custom class rawKeyType
        Class<?> customClass = new CustomClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, customClass);
        assertNull(serializer);

        // Test with Object rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testDynamicKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        SerializationConfig config = JsonMapperFactory.getJsonMapper().getSerializerProvider().getConfig();

        // Test with custom class
        Class<?> customClass = new CustomClass();
        PropertySerializerMap serializers = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, customClass, config);
        assertEquals(customClass.getName(), serializer.serializeValueAsString(new CustomClass(), new ObjectMapper().getSerializerProvider(), null));

        // Test with String
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, String.class, config);
        assertEquals("String", serializer.serializeValueAsString(new String("String"), new ObjectMapper().getSerializerProvider(), null));

        // Test with int
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, int.class, config);
        assertEquals("0", serializer.serializeValueAsString(0, new ObjectMapper().getSerializerProvider(), null));

        // Test with Date
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, Date.class, config);
        Date date = new Date();
        assertEquals(date.toInstant().toString(), serializer.serializeValueAsString(date, new ObjectMapper().getSerializerProvider(), null));

        // Test with Calendar
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, Calendar.class, config);
        Calendar calendar = Calendar.getInstance();
        assertEquals(calendar.getTimeInMillis(), serializer.serializeValueAsLong(calendar, new ObjectMapper().getSerializerProvider(), null));

        // Test with Enum
        enum TestEnum {A, B, C}
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, TestEnum.class, config);
        assertEquals(TestEnum.A.name(), serializer.serializeValueAsString(TestEnum.A, new ObjectMapper().getSerializerProvider(), null));
    }

    @Test
    public void testToStringKeySerializer() throws JsonMappingException, JsonGenerationException, IOException {
        SerializationConfig config = JsonMapperFactory.getJsonMapper().getSerializerProvider().getConfig();

        // Test with custom class
        Class<?> customClass = new CustomClass();
        PropertySerializerMap serializers = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, customClass, config);
        assertNull(serializer.serializeValueAsString(new CustomClass(), new ObjectMapper().getSerializerProvider(), null));

        // Test with String
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, String.class, config);
        assertEquals("String", serializer.serializeValueAsString(new String("String"), new ObjectMapper().getSerializerProvider(), null));

        // Test with int
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, int.class, config);
        assertEquals("0", serializer.serializeValueAsString(0, new ObjectMapper().getSerializerProvider(), null));

        // Test with Date
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, Date.class, config);
        Date date = new Date();
        assertEquals(date.toString(), serializer.serializeValueAsString(date, new ObjectMapper().getSerializerProvider(), null));

        // Test with Calendar
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, Calendar.class, config);
        Calendar calendar = Calendar.getInstance();
        assertEquals(calendar.toString(), serializer.serializeValueAsString(calendar, new ObjectMapper().getSerializerProvider(), null));

        // Test with Enum
        enum TestEnum {A, B, C}
        serializer = StdKeySerializers.Dynamic._findAndAddDynamic(serializers, TestEnum.class, config);
        assertEquals(TestEnum.A.name(), serializer.serializeValueAsString(TestEnum.A, new ObjectMapper().getSerializerProvider(), null));
    }

    static class CustomClass {
        // Empty class for testing
    }
}