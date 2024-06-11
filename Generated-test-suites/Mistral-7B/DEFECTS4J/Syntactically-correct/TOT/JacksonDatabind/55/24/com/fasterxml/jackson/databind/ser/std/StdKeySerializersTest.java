import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.Dynamic;
import com.fasterxml.jackson.databind.ser.std.Default;
import com.fasterxml.jackson.databind.ser.std.StringKeySerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private PropertySerializerMap serializers;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        serializers = new PropertySerializerMap();
    }

    @Test
    public void testGetStdKeySerializer_String() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Date() throws IOException {
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_Calendar() throws IOException {
        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_Number() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Integer.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Class() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String[].class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_Enum() throws IOException {
        EnumSet enumSet = EnumSet.allOf(MyEnum.class);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_Enum_WriteEnumsUsingToStringEnabled() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);
    }

    @Test
    public void testGetStdKeySerializer_Unknown() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, new Object(){}.getClass(), false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_String() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_Date() throws IOException {
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);
    }

    @Test
    public void testGetFallbackKeySerializer_Unknown() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, new Object(){}.getClass());
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testStdSerializer_Dynamic() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyWriter propertyWriter = mock(PropertyWriter.class);
        when(propertyWriter.getSerializerProvider()).thenReturn(provider);
        when(provider.findValueSerializer(any(Class.class))).thenReturn(null);

        Dynamic dynamicSerializer = new Dynamic();
        dynamicSerializer.serialize("test", mock(JsonGenerator.class), provider);

        Object value = new Object() {};
        Class<?> cls = value.getClass();
        JsonSerializer<Object> serializer = dynamicSerializer.findAndAddDynamic(serializers, cls, provider);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testStdSerializer_StringKeySerializer() throws IOException {
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        String key = "test";

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_BIG_DECIMAL_AS_PLAIN)).thenReturn(false);

        stringKeySerializer.serialize(key, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(key);
    }
}