import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {
    SetterlessProperty setterlessProperty;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        TypeDeserializer typeDeser = mapper.getTypeDeserializers().findDeserializer(String[].class);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(MyClass.class);
        AnnotatedMethod method = new AnnotationIntrospector().findAnnotationOnMethod(MyClass.class, "getMyList");
        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(setterlessProperty);
    }

    @Test
    public void testGetMethod() throws IOException {
        Object instance = new Object();
        List<String> myList = new ArrayList<>();
        when(setterlessProperty._getter.invoke(instance)).thenReturn(myList);
        List<String> deserializedList = setterlessProperty.deserializeAndSet(mockJsonParser.get(), mockDeserializationContext, instance);
        verify(mockJsonParser).nextToken();
        assertEquals(myList, deserializedList);
    }
}