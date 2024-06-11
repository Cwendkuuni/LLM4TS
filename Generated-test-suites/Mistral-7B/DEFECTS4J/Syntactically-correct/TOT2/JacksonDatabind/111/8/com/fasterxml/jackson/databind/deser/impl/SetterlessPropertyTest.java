import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.core.type.TypeReference;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", new TypeReference<Integer>() {});
        JavaType type = TypeFactory.defaultInstance().constructType(Integer[].class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod(SetterlessProperty.class, "testMethod");

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(setterlessProperty);

        SetterlessProperty setterlessProperty2 = new SetterlessProperty(setterlessProperty, null, null);
        assertEquals(setterlessProperty, setterlessProperty2);
    }
}