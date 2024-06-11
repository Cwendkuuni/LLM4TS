import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(null, null, true);
        String json = mapper.writeValueAsString(new Object());
        assertEquals("{}", json);
    }

    @Test
    public void testGetStdKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, true);
        String json = mapper.writeValueAsString(new String("test"));
        assertEquals("{\"test\":{\"@type\":\"StringKeySerializer\"}}", json);
    }

    @Test
    public void testGetFallbackKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
        String json = mapper.writeValueAsString(new Object());
        assertEquals("{}", json);
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, int[].class, true);
        Properties properties = new Properties();
        properties.put("0", "value1");
        properties.put("1", "value2");
        String json = mapper.writeValueAsString(properties);
        assertEquals("{\"0\":{\"@type\":\"Dynamic\"},\"1\":{\"@type\":\"Dynamic\"}}", json);
    }

    @Test
    public void testDateKeySerializer() throws IOException {
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
        String json = mapper.writeValueAsString(date);
        assertEquals("{\"@type\":\"Default\",\"@typeId\":1,\"value\":\"Fri Aug 27 10:43:15 CDT 2021\"}", json);
    }
}