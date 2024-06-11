import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.StdDateAccessors;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectId.class, StdDateAccessors.none());
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(obj);
    }
}