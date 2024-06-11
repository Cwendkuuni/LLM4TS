import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_String() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        assertEquals(stringKeySerializer, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Date() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
        Default serializerForTypeDate = new Default(Default.TYPE_DATE, Date.class);
        assertEquals(serializerForTypeDate, serializer);

        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode rootNode = mapper2.createObjectNode();
        rootNode.put("key", date);
        mapper2.writerWithRootValue(rootNode).writeValueAsString();
    }

    @Test
    public void testGetStdKeySerializer_Calendar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
        Default serializerForTypeCalendar = new Default(Default.TYPE_CALENDAR, Calendar.class);
        assertEquals(serializerForTypeCalendar, serializer);

        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode rootNode = mapper2.createObjectNode();
        rootNode.put("key", calendar.getTimeInMillis());
        mapper2.writerWithRootValue(rootNode).writeValueAsString();
    }

    @Test
    public void testGetStdKeySerializer_Class() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Class<?> clazz = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), clazz, false);
        Default serializerForTypeClass = new Default(Default.TYPE_CLASS, clazz);
        assertEquals(serializerForTypeClass, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Enum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Enum enumValue = Enum.VALUES[0];
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), enumValue.getClass(), false);
        Default serializerForTypeEnum = new Default(Default.TYPE_ENUM, enumValue.getClass());
        assertEquals(serializerForTypeEnum, serializer);

        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode rootNode = mapper2.createObjectNode();
        rootNode.put("key", enumValue.name());
        mapper2.writerWithRootValue(rootNode).writeValueAsString();
    }

    @Test
    public void testGetStdKeySerializer_UnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object unknownType = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), unknownType.getClass(), false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_Class() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Class<?> clazz = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), clazz);
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        assertEquals(stringKeySerializer, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_UnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object unknownType = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), unknownType.getClass());
        assertNull(serializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFallbackKeySerializer_Null() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
    }
}