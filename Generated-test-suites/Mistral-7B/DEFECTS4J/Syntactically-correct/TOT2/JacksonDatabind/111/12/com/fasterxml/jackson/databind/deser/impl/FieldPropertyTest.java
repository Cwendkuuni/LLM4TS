import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {
    // ...

    @Test
    public void testFieldPropertyConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        assertNotNull(fieldProperty);
    }

    // ...
}