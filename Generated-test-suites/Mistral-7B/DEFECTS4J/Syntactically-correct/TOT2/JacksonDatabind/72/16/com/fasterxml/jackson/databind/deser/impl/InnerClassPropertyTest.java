import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        Object bean = new Object();

        JsonParser jp = jsonFactory.createParser("{}");
        innerClassProperty.deserializeAndSet(jp, null, bean);

        assertNotNull(bean);
        assertEquals(0, bean.getClass().getDeclaredFields().length);
    }
}