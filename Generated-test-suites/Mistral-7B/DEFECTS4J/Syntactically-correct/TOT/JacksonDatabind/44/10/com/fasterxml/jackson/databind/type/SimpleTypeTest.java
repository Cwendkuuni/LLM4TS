import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = new SimpleType(intType, TypeBindings.emptyBindings(), null, null);
        assertEquals(Integer.class, simpleType.getType());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringType = String.class;
        SimpleType originalSimpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getType(), copiedSimpleType.getType());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringType = String.class;
        Object handler = new StringHandler();
        SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSimpleTypeConstruction() {
        Class<List<Integer>> listType = List.class;
        new SimpleType(listType, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsafeSimpleTypeConstruction() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = SimpleType.constructUnsafe(intType);
        assertTrue(simpleType.isUnsafe());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        Class<Integer> intType = Integer.class;
        SimpleType simpleType = SimpleType.construct(intType);
    }

    @Test
    public void testNarrowingSimpleType() {
        Class<Number> numberType = Number.class;
        Class<Integer> intType = Integer.class;
        SimpleType superType = new SimpleType(numberType, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = superType.narrow(intType);
        assertEquals(intType, narrowedType.getType());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<Number> numberType = Number.class;
        Class<Number> numberWithInterface = NumberWithInterface.class;
        SimpleType simpleType = new SimpleType(numberType, TypeBindings.emptyBindings(),  null, new JavaType[] { new SimpleType(numberWithInterface, TypeBindings.emptyBindings(), null, null) });
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertEquals(1, superInterfaces.length);
        assertEquals(new SimpleType(numberWithInterface, TypeBindings.emptyBindings(), null, null), superInterfaces[0]);
    }

    private static class StringHandler {
        // Empty handler for testing purposes
    }

    private static class NumberWithInterface {
        // Interface for testing purposes
        public interface NumberInterface {
            void doSomething();
        }
    }
}