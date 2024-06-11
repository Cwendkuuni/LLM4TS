import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class FieldPropertyTest {

    @Test
    public void testFieldPropertyNormalCase() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        String jsonInput = "{\"fieldName\": \"expectedValue\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonInput);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("fieldName", String.class, null, null);
        JavaType javaType = mapper.getTypeFactory().construct();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardDeserializationContext().emptyContextAnnotations();
        AnnotatedField annotatedField = new AnnotatedField(new AnnotatedMember(new PropertyName("fieldName"), "fieldName"), null, null, null, null);
        FieldProperty prop = new FieldProperty(propDef, javaType, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        prop.deserializeAndSet(parser, new DeserializationContext(mapper.getConfig()), instance);

        assertEquals("expectedValue", prop.getField().get(instance));
    }

    // Add other test cases for each method following the same pattern

    // Test edge cases and exceptions

    @Test(expected = IOException.class)
    public void testFieldPropertyNullValue() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        String jsonInput = "{\"fieldName\": null}";
        JsonParser parser = mapper.getFactory().createParser(jsonInput);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("fieldName", String.class, null, null);
        JavaType javaType = mapper.getTypeFactory().construct();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardDeserializationContext().emptyContextAnnotations();
        AnnotatedField annotatedField = new AnnotatedField(new AnnotatedMember(new PropertyName("fieldName"), "fieldName"), null, null, null, null);
        FieldProperty prop = new FieldProperty(propDef, javaType, typeDeser, contextAnnotations, annotatedField);

        Object instance = new Object();
        prop.deserializeAndSet(parser, new DeserializationContext(mapper.getConfig()), instance);

        assertNull(prop.getField().get(instance));
    }

    // Add other test cases for edge cases and exceptions following the same pattern

    // Merge all test cases into a complete JUnit 4 test file

    

    // Add all test cases here

    
}