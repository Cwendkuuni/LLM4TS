import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestBase;

public class AsPropertyTypeDeserializerTest extends TestBase {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "typePropertyName", false, null, As.PROPERTY);
        AnnotatedClass ac = mapper.getSerializerProvider().findTypeDeserializer(javaType).getClassAnnotated();
        TypeDeserializer typeDeserializer = mapper.findValueDeserializer(ac, null);
        ObjectReader objectReader = mapper.readerForType(javaType);
        JsonParser parser = objectReader.getParser();

        parser.nextValue(); // read the JSON token
        parser.nextToken(); // read the next token

        Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        Object expected = typeDeserializer.deserializeValueAs(parser, javaType, new TestContext());

        assertEquals(expected, result);
    }
}