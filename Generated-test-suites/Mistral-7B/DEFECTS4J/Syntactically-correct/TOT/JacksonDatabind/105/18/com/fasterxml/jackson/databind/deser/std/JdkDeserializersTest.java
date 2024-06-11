import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class JdkDeserializersTest {

    @Test
    public void testFindUuidDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        UUID expected = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        try {
            UUID actual = mapper.readValue("{\"uuid\":\"123e4567-e89b-12d3-a456-426614174000\"}", new TypeReference<UUID>() {});
            Assert.assertEquals(expected, actual);
            Assert.assertEquals(deserializer, mapper.deserializationConfig().findRootValueDeserializer(UUID.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize UUID");
        }
    }

    @Test
    public void testFindAtomicBooleanDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        AtomicBoolean expected = new AtomicBoolean(false);
        String json = "{\"atomicBoolean\":false}";
        try {
            AtomicBoolean actual = mapper.readValue(json, AtomicBoolean.class);
            Assert.assertEquals(expected, actual);
            Assert.assertEquals(deserializer, mapper.deserializationConfig().findRootValueDeserializer(AtomicBoolean.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize AtomicBoolean");
        }
    }

    @Test
    public void testFindStackTraceElementDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        StackTraceElement expected = new StackTraceElement("test", "testMethod", "testFile.java", 1);
        String json = "{\"className\":\"test\", \"methodName\":\"testMethod\", \"file\":\"testFile.java\", \"lineNumber\":1}";
        try {
            StackTraceElement actual = mapper.readValue(json, StackTraceElement.class);
            Assert.assertEquals(expected, actual);
            Assert.assertEquals(deserializer, mapper.deserializationConfig().findRootValueDeserializer(StackTraceElement.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize StackTraceElement");
        }
    }

    @Test
    public void testFindByteBufferDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        ByteBuffer expected = ByteBuffer.wrap(new byte[]{0x0, 0x1, 0x2, 0x3, 0x4});
        String json = "{\"byteBuffer\":\"00 01 02 03 04\"}";
        try {
            ByteBuffer actual = mapper.readValue(json, ByteBuffer.class);
            byte[] actualBytes = new byte[actual.limit()];
            actual.get(actualBytes);
            Assert.assertArrayEquals(expected.array(), actualBytes);
            Assert.assertEquals(deserializer, mapper.deserializationConfig().findRootValueDeserializer(ByteBuffer.class));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to deserialize ByteBuffer");
        }
    }

    @Test
    public void testFindUnknownDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Integer.class, "UnknownClass.class.getName()");
        Assert.assertNull(deserializer);
    }
}