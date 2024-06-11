import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.AnnotationInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTest;

public class SetterlessPropertyTest {
    @Test
    public void testConstructor() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Test if the constructor sets the fields correctly
        assertEquals(propDef, setterlessProperty.getPropertyDefinition());
        assertEquals(type, setterlessProperty.getType());
        assertEquals(typeDeser, setterlessProperty.getTypeDeserializer());
        assertNotNull(setterlessProperty._annotated);
        assertNotNull(setterlessProperty._getter);
    }
}