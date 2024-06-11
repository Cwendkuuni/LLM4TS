import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFindWithStringClassName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"rawType\":\"java.util.UUID\",\"className\":\"java.util.UUID\"}";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        Assert.assertEquals(deserializer.getClass(), UUIDDeserializer.class);
        // Add more test cases for other classes
    }
}