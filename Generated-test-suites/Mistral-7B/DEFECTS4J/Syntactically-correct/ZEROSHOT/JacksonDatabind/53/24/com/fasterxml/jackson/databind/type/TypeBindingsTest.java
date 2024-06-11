import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.hasUnbound(""));
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.isEmpty());
        assertArrayEquals(new String[0], bindings.getNameArray());
        assertArrayEquals(JavaType.ValueType.NONE, bindings.getTypeArray());
    }

    @Test
    public void testCreateTypeBindings() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);

        assertFalse(bindings.hasUnbound(""));
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.isEquals(TypeBindings.create(intListClass, intTypes)));
        assertSame(intTypes, bindings.getTypeArray());
        assertArrayEquals(new String[] { "E" }, bindings.getNameArray());
    }

    @Test
    public void testCreateTypeBindingsWithUnboundVariable() {
        Class<?> listClassWithTypeVariable = new ArrayList<Number>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(listClassWithTypeVariable, intTypes);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("T");

        assertTrue(bindingsWithUnboundVariable.hasUnbound("T"));
        assertNull(bindingsWithUnboundVariable.findBoundType("T"));
        assertFalse(bindings.isEquals(bindingsWithUnboundVariable));
        assertArrayEquals(new String[] { "E", "T" }, bindingsWithUnboundVariable.getNameArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypeBindingsInvalidSize() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class),
                                               JavaType.constructCollectionType(List.class, String.class) };
        TypeBindings.create(intListClass, intTypes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypeBindingsInvalidNamesAndTypesLength() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings.create(intListClass, new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class),
                                                           JavaType.constructCollectionType(List.class, String.class) });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypeBindingsInvalidTypeArgument() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        }};
        TypeBindings.create(intListClass, intTypes);
    }

    @Test
    public void testCreateTypeBindingsListWithTypeVariable() {
        Class<?> listClassWithTypeVariable = new ArrayList<Number>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(listClassWithTypeVariable, intTypes);

        assertFalse(bindings.hasUnbound(""));
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.findBoundType("E").isCollectionType());
        assertTrue(bindings.isEquals(TypeBindings.create(listClassWithTypeVariable, intTypes)));
        assertFalse(bindings.isEquals(TypeBindings.create(ArrayList.class, intTypes)));
    }

    @Test
    public void testCreateTypeBindingsMapWithTypeVariable() {
        Class<?> mapClassWithTypeVariable = HashMap.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType[] mapTypes = new JavaType[] { JavaType.constructMapType(HashMap.class, keyType, valueType) };
        TypeBindings bindings = TypeBindings.create(mapClassWithTypeVariable, mapTypes);

        assertFalse(bindings.hasUnbound(""));
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.findBoundType("K").isReferenceType());
        assertTrue(bindings.findBoundType("V").isReferenceType());
        assertTrue(bindings.isEquals(TypeBindings.create(mapClassWithTypeVariable, mapTypes)));
        assertFalse(bindings.isEquals(TypeBindings.create(HashMap.class, new JavaType[] { keyType, valueType })));
    }

    @Test
    public void testCreateTypeBindingsListOfListWithTypeVariable() {
        Class<?> listOfListClassWithTypeVariable = ArrayList.class;
        JavaType innerListType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType outerListType = JavaType.constructCollectionType(List.class, JavaType.constructType(List.class, innerListType.getTypeId()));
        JavaType[] intTypes = new JavaType[] { outerListType };

        TypeBindings bindings = TypeBindings.create(listOfListClassWithTypeVariable, intTypes);

        assertFalse(bindings.hasUnbound(""));
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.findBoundType("E").isCollectionType());
        assertTrue(bindings.findBoundType("M").isCollectionType());
        assertTrue(bindings.isEquals(TypeBindings.create(listOfListClassWithTypeVariable, intTypes)));
    }

    @Test
    public void testFindBoundType() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);
        JavaType boundType = bindings.findBoundType("E");

        assertSame(JavaType.constructCollectionType(List.class, Integer.class), boundType);
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testSize() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);

        assertEquals(1, bindings.size());
    }

    @Test
    public void testGetBoundType() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);

        assertEquals(0, bindings.getBoundType(0).getGenericSignature().length());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(JavaType.constructCollectionType(List.class, Integer.class), bindings.getBoundType(0));
    }

    @Test
    public void testGetStringArray() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);

        String[] names = bindings.getNameArray();
        String[] expectedNames = new String[] { "E" };
        assertArrayEquals(expectedNames, names);

        JavaType[] types = bindings.getTypeArray();
        JavaType expectedType = JavaType.constructCollectionType(List.class, Integer.class);
        assertArrayEquals(new JavaType[] { expectedType }, types);
    }

    @Test
    public void testToString() {
        Class<?> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] intTypes = new JavaType[] { JavaType.constructCollectionType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(intListClass, intTypes);

        String expectedString = "<E>";
        String actualString = bindings.toString();
        assertEquals(expectedString, actualString);
    }
}