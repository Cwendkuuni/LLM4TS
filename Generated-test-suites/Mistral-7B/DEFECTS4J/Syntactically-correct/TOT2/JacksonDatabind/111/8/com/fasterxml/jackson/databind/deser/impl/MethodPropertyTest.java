import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"property\": 123 }");
        Object target = new Object();
        new MethodProperty(null, null, null, null, null)
                .deserializeAndSet(parser, null, target);
        assertEquals(123, target.getClass().getDeclaredField("property")
                .get(target));
    }
}