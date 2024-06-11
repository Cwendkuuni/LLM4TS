
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeBasic() throws JsonProcessingException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        String input = "Hello World!";
        String expectedOutput = "\"Hello World!\"";

        ObjectNode result = (ObjectNode) stringSerializer.serializeValues(objectMapper.createObjectCodec(), null, input, null);
        String actualOutput = result.toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeNullInput() throws JsonProcessingException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        String input = null;
        String expectedOutput = "null";

        ObjectNode result = (ObjectNode) stringSerializer.serializeValues(objectMapper.createObjectCodec(), null, input, null);
        String actualOutput = result.toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // Add the remaining test cases here

}