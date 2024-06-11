import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedSignature = new StringBuilder("java.lang.String");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    
}