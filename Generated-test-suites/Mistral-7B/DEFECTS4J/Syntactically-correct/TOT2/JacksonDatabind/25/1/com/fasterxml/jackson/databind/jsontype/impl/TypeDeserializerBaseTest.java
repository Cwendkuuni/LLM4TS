import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        JsonParser jsonParser = mapper.getFactory().createParser("{}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = typeDeserializerBase.deserialize(jsonParser, ctxt);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        JsonParser jsonParser = mapper.getFactory().createParser("{\"@type\":\"com.example.MyClass\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String myClassTypeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDeserializer(ctxt, myClassTypeId);

        assertEquals(MyClassDeserializer.class, deserializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerWithInvalidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        JsonParser jsonParser = mapper.getFactory().createParser("{\"@type\":\"com.nonexistent.Class\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String nonexistentClassTypeId = "com.nonexistent.Class";
        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDeserializer(ctxt, nonexistentClassTypeId);
    }
}

class MyClassDeserializer extends JsonDeserializer<MyClass> {
    // add implementation here
}

class MyClass {}