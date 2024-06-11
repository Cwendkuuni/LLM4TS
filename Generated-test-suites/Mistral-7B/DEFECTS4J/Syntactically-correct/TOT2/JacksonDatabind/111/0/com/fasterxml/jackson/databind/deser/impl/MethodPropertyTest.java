import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstructor() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty._annotated);
        assertEquals(method, methodProperty._setter);
        assertEquals(false, methodProperty._skipNulls);
    }
}