import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test construction with simple class
        JavaType simpleType1 = new SimpleType(String.class);
        assertEquals(String.class, simpleType1._class);
        assertEquals(TypeBindings.emptyBindings(), simpleType1._bindings);
        assertEquals(null, simpleType1._superClass);
        assertEquals(null, simpleType1._superInterfaces);

        // Test construction with class, bindings, superclass and superinterfaces
        JavaType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleType2._class);
        assertEquals(TypeBindings.emptyBindings(), simpleType2._bindings);
        assertEquals(null, simpleType2._superClass);
        assertEquals(null, simpleType2._superInterfaces);

        // Test copy constructor
        JavaType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test construction with class, bindings, superclass, superinterfaces, valueHandler, typeHandler and asStatic
        JavaType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(String.class, simpleType4._class);
        assertEquals(TypeBindings.emptyBindings(), simpleType4._bindings);
        assertEquals(null, simpleType4._superClass);
        assertEquals(null, simpleType4._superInterfaces);
        assertEquals(null, simpleType4._valueHandler);
        assertEquals(null, simpleType4._typeHandler);
        assertEquals(false, simpleType4._asStatic);
    }

    @Test
    public void testSimpleTypeMethods() {
        JavaType simpleType = new SimpleType(String.class);

        // Test isContainerType()
        assertFalse(simpleType.isContainerType());

        // Test getErasedSignature()
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        // Test getGenericSignature()
        sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("<java.lang.String>", sb.toString());

        // Test withContentType()
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(JavaType.constructCollectionType(List.class, Integer.class)));

        // Test withTypeHandler()
        JavaType simpleTypeWithHandler = simpleType.withTypeHandler(new MyTypeHandler());
        assertNotEquals(simpleType, simpleTypeWithHandler);
        assertNull(simpleType._typeHandler);
        assertEquals(simpleTypeWithHandler, simpleTypeWithHandler.withTypeHandler(new MyTypeHandler()));

        // Test withContentTypeHandler()
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(new MyContentTypeHandler()));

        // Test withValueHandler()
        JavaType simpleTypeWithValueHandler = simpleType.withValueHandler(new MyValueHandler());
        assertNotEquals(simpleType, simpleTypeWithValueHandler);
        assertNull(simpleType._valueHandler);
        assertEquals(simpleTypeWithValueHandler, simpleTypeWithValueHandler.withValueHandler(new MyValueHandler()));

        // Test withStaticTyping()
        JavaType simpleTypeStatic = simpleType.withStaticTyping();
        assertFalse(simpleTypeStatic._asStatic);
        JavaType simpleTypeStatic2 = simpleTypeStatic.withStaticTyping();
        assertEquals(simpleTypeStatic, simpleTypeStatic2);
        assertTrue(simpleTypeStatic._asStatic);
    }

    @Test
    public void testConstructStaticMethod() {
        // Test constructUnsafe()
        JavaType simpleTypeUnsafe = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleTypeUnsafe._class);
        assertEquals(TypeBindings.emptyBindings(), simpleTypeUnsafe._bindings);
        assertEquals(null, simpleTypeUnsafe._superClass);
        assertEquals(null, simpleTypeUnsafe._superInterfaces);

        // Test construct()
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(String[].class));
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(ArrayList.class));
        JavaType simpleTypeConstruct = SimpleType.construct(String.class);
        assertEquals(String.class, simpleTypeConstruct._class);
        assertEquals(TypeBindings.emptyBindings(), simpleTypeConstruct._bindings);
        assertEquals(null, simpleTypeConstruct._superClass);
        assertEquals(null, simpleTypeConstruct._superInterfaces);
    }

    @Test
    public void testNarrowMethod() {
        // Test narrow with same class
        JavaType simpleType = new SimpleType(String.class);
        JavaType narrowType = simpleType.narrow(String.class);
        assertEquals(simpleType, narrowType);

        // Test narrow with subclass
        JavaType subclass = new SimpleType(StringSubclass.class);
        JavaType narrowSubclass = simpleType.narrow(StringSubclass.class);
        assertNotEquals(subclass, narrowSubclass);
        assertEquals(simpleType, narrowSubclass.getSuperClass());
        assertEquals(String.class, narrowSubclass._class);
    }
}

class MyTypeHandler {
}

class MyContentTypeHandler {
}

class MyValueHandler {
}

class StringSubclass extends String {
}