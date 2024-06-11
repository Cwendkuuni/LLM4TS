import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class InnerClassPropertyTest {

    @Test
    public void testWithName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        InnerClassProperty property = new InnerClassProperty(new AnnotatedClass(), new AnnotatedConstructor(new Class[0], new Constructor[0], new Annotation[0], new Annotation[0]));
        String originalName = property.getName().asPrefixString();
        PropertyName newName = new PropertyName("newName");

        InnerClassProperty newProperty = property.withName(newName);
        String newNameString = newProperty.getName().asPrefixString();

        assertEquals(newNameString, newName.asPrefixString());
        assertNotEquals(originalName, newNameString);

        // Test setting and getting the property with the new name
        Object bean = mapper.readValue(mapper.writeValueAsString(new Object()), Object.class);
        newProperty.set(bean, "newValue");
        String newValue = (String) newProperty.get(bean);
        assertEquals("newValue", newValue);
    }
}