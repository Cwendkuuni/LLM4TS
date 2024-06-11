import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstructor() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(methodProperty);
    }
}