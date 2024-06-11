import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class JdkDeserializersTest {

    @Test
    public void testFindMethodWithValidArguments() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> expectedResult = new UUIDDeserializer();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStaticBlockInitialization() {
        // Assuming the _classNames hashset is not null before the static block
        // and all the expected classes are added to the hashset
        verifyNoMoreInteractions(JdkDeserializers._classNames);
    }

    @Test
    public void testFindMethodWithNullRawType() {
        String clsName = UUID.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(null, clsName);
        assertNull(result);
    }

    @Test
    public void testFindMethodWithNullClassName() {
        Class<?> rawType = UUID.class;
        String clsName = null;
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFindMethodWithInvalidClassName() {
        Class<?> rawType = UUID.class;
        String clsName = "InvalidClassName";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testUuidDeserializerWithValidJsonString() {
        String jsonString = "\"rnd-12345678-1234-1234-1234-123456789012\"";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<UUID> deserializer = new UUIDDeserializer();
        UUID result = deserializer.deserialize(jsonParser, null);
        UUID expectedResult = new UUID("rnd-12345678-1234-1234-1234-123456789012");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUuidDeserializerWithInvalidJsonString() {
        String jsonString = "\"InvalidJsonString\"";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<UUID> deserializer = new UUIDDeserializer();
        UUID result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testAtomicBooleanDeserializerWithValidJsonBoolean() {
        String jsonString = "true";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<AtomicBoolean> deserializer = new AtomicBooleanDeserializer();
        AtomicBoolean result = deserializer.deserialize(jsonParser, null);
        AtomicBoolean expectedResult = new AtomicBoolean(true);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAtomicBooleanDeserializerWithInvalidJsonBoolean() {
        String jsonString = "\"InvalidJsonBoolean\"";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<AtomicBoolean> deserializer = new AtomicBooleanDeserializer();
        AtomicBoolean result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testByteBufferDeserializerWithValidJsonByteBuffer() {
        String jsonString = "{\"bytebuffer\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],\"offset\":0,\"limit\":21}";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<ByteBuffer> deserializer = new ByteBufferDeserializer();
        ByteBuffer result = deserializer.deserialize(jsonParser, null);
        ByteBuffer expectedResult = ByteBuffer.allocate(21).put((byte) 1).put((byte) 2).put((byte) 3).put((byte) 4).put((byte) 5).put((byte) 6).put((byte) 7).put((byte) 8).put((byte) 9).put((byte) 10).put((byte) 11).put((byte) 12).put((byte) 13).put((byte) 14).put((byte) 15).put((byte) 16).put((byte) 17).put((byte) 18).put((byte) 19).put((byte) 20);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testByteBufferDeserializerWithInvalidJsonByteBuffer() {
        String jsonString = "\"InvalidJsonByteBuffer\"";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<ByteBuffer> deserializer = new ByteBufferDeserializer();
        ByteBuffer result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }

    @Test
    public void testStackTraceElementDeserializerWithValidJsonArray() {
        String jsonString = "[\"MyClass.java\",1,\"MyMethod\",\"MyLineNumber\"]";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<StackTraceElement> deserializer = new StackTraceElementDeserializer();
        StackTraceElement result = deserializer.deserialize(jsonParser, null);
        StackTraceElement expectedResult = new StackTraceElement("MyClass.java", 1, "MyMethod", "MyLineNumber");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStackTraceElementDeserializerWithInvalidJsonArray() {
        String jsonString = "\"InvalidJsonArray\"";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);
        JsonDeserializer<StackTraceElement> deserializer = new StackTraceElementDeserializer();
        StackTraceElement result = deserializer.deserialize(jsonParser, null);
        assertNull(result);
    }
}