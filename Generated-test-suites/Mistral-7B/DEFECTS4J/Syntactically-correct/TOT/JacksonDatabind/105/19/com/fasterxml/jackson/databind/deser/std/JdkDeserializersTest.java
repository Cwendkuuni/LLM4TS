import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindUuidDeserializer() throws IOException {
        Class<UUID> uuidClass = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(UUIDDeserializer.class, deserializer.getClass());

        String jsonString = "\"123e4567-e89b-12d3-a456-426614174000\"";
        UUID expected = new UUID("123e4567-e89b-12d3-a456-426614174000");
        UUID actual = objectMapper.readValue(jsonString, UUID.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindAtomicBooleanDeserializer() throws IOException {
        Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());

        String jsonString = "true";
        AtomicBoolean expected = new AtomicBoolean(true);
        AtomicBoolean actual = objectMapper.readValue(jsonString, AtomicBoolean.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindStackTraceElementDeserializer() throws IOException {
        Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());

        String jsonString = "{\"fileName\":\"test.java\",\"lineNumber\":1,\"columnNumber\":1,\"methodName\":\"testMethod\",\"className\":\"com.example.TestClass\",\"nativeMethod\":false}";
        StackTraceElement expected = new StackTraceElement("test.java", "testMethod", "com.example.TestClass", 1);
        StackTraceElement actual = objectMapper.readValue(jsonString, StackTraceElement.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindByteBufferDeserializer() throws IOException {
        Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(ByteBufferDeserializer.class, deserializer.getClass());

        String jsonString = "\"0102030405060708090a0b0c0d0e0f0g\"";
        byte[] expectedBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        ByteBuffer expected = ByteBuffer.wrap(expectedBytes);
        ByteBuffer actual = objectMapper.readValue(jsonString, new TypeReference<ByteBuffer>() {});
        byte[] actualBytes = actual.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testFindNullDeserializer() throws IOException {
        Class<String> stringClass = String.class;
        String clsName = String.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, clsName);
        Assert.assertNull(deserializer);
    }
}