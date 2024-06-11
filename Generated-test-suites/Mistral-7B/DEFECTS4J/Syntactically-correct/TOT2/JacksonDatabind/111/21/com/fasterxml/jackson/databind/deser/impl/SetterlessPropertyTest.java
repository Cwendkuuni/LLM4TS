import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(setterlessProperty);
    }
}