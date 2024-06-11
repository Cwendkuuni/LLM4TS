import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

public class MethodPropertyTest {

    private ObjectMapper mapper;
    private MethodProperty property;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        AnnotatedMethod method = Annotations.findAnnotation(MethodProperty.class, Getter.class);
        property = new MethodProperty(null, null, null, null, method);
    }

    @Test
    public void testGetter() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        parser.nextToken();
        Object result = property.deserializeAndSet(parser, null, new Object());
        assertEquals("John", property.get(new Object()));
    }

    // ...
}