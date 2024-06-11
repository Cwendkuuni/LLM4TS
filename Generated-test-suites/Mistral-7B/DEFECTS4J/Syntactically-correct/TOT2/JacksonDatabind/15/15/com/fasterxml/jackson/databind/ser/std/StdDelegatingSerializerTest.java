import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        serializer = new StdDelegatingSerializer(mapper.getFactory().constructConverter(String.class, Integer.class));
    }

    @Test
    public void testSerializeStringToInteger() throws JsonGenerationException, JsonMappingException {
        Object value = "42";
        String expected = "42";

        byte[] result = serializer.serialize(value);
        String actual = mapper.writeValueAsString(result);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException {
        Object value = null;

        byte[] result = serializer.serialize(value);

        Assert.assertArrayEquals(new byte[0], result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonGenerationException, JsonMappingException {
        Object value = new Object();

        serializer.serialize(value);
    }

    @Test
    public void testGetDelegateSerializer() {
        Assert.assertNotNull(serializer.getDelegatee());
    }
}