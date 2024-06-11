import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetStdKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);

        String key = "testKey";
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(key, generator, new SerializerProvider());
        generator.flush();

        assertEquals("\"testKey\"", generator.toString());
    }

    @Test
    public void testGetStdKeySerializer_Date() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);

        Date date = new Date();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(date, generator, new SerializerProvider());
        generator.flush();

        assertEquals("\"Thu Oct 21 13:35:21 CEST 2021\"", generator.toString());
    }

    @Test
    public void testGetStdKeySerializer_Calendar() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);

        Calendar calendar = Calendar.getInstance();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(calendar, generator, new SerializerProvider());
        generator.flush();

        assertEquals("\"Thu Oct 21 13:35:21 CEST 2021\"", generator.toString());
    }

    @Test
    public void testGetStdKeySerializer_Class() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String[].class, false);

        String[] array = new String[]{"test", "key"};
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(array, generator, new SerializerProvider());
        generator.flush();

        assertEquals("[\"test\", \"key\"]", generator.toString());
    }

    @Test
    public void testGetStdKeySerializer_Enum() throws Exception {
        enum TestEnum {
            TEST_ONE, TEST_TWO
        }
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, TestEnum.class, false);

        TestEnum testEnum = TestEnum.TEST_ONE;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(testEnum, generator, new SerializerProvider());
        generator.flush();

        assertEquals("\"TEST_ONE\"", generator.toString());
    }

    @Test
    public void testGetStdKeySerializer_UnknownClass() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyUnknownClass.class, true);

        MyUnknownClass myUnknownClass = new MyUnknownClass();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(myUnknownClass, generator, new SerializerProvider());
        generator.flush();

        // This test relies on the fallback key serializer, which is not covered here
        // In a real implementation, make sure that the fallback key serializer is working
        // and that the generated JSON matches the expected format
        // For example, it might look like: {"@class":"com.mypackage.MyUnknownClass", "@":<unique_id>}
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, MyUnknownClass.class);

        MyUnknownClass myUnknownClass = new MyUnknownClass();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(myUnknownClass, generator, new SerializerProvider());
        generator.flush();

        // This test relies on the fallback key serializer, which is not covered here
        // In a real implementation, make sure that the fallback key serializer is working
        // and that the generated JSON matches the expected format
        // For example, it might look like: {"@class":"com.mypackage.MyUnknownClass", "@":<unique_id>}
    }
}