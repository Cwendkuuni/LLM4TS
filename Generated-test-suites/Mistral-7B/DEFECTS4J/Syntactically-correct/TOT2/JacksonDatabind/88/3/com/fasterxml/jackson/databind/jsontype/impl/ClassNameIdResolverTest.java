import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws Exception {
        Object value = new Object();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        Object value = new Object();
        Class<?> type = String.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        DatabindContext context = new ObjectMapper().getDeserializationContext();
        String id = "java.lang.String";
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
        JavaType type = resolver.typeFromId(context, id);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testIdFromValueForEnum() throws Exception {
        Enum value = Enum.valueOf(String.class, "VALUE");
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), new TypeFactory());
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValueForInvalidType() throws Exception {
        Object value = new Object();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
        String id = resolver.idFromValue(new Integer(1));
    }

    @Test(expected = IOException.class)
    public void testTypeFromIdForUnknownType() throws Exception {
        DatabindContext context = new ObjectMapper().getDeserializationContext();
        String id = "com.unknown.UnknownType";
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
        JavaType type = resolver.typeFromId(context, id);
    }
}