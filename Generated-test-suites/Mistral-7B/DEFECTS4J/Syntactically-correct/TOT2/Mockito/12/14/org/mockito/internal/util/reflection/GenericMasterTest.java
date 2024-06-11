
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType_NullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;

        try {
            Class actualType = genericMaster.getGenericType(field);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGenericType_NonPublicField() throws IllegalAccessException, NoSuchFieldException {
        Class clazz = MyTestClass.class;
        Field field = clazz.getDeclaredField("myNonPublicField");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Integer.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_NonExistentField() throws NoSuchFieldException {
        Class clazz = MyTestClass.class;
        GenericMaster genericMaster = new GenericMaster();

        genericMaster.getGenericType(clazz.getDeclaredField("nonExistentField"));
    }

    @Test
    public void testGetGenericType_NonGenericField() throws NoSuchFieldException {
        Class clazz = MyTestClass.class;
        Field field = clazz.getDeclaredField("myNonGenericField");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType_MultipleTypeArguments() throws NoSuchFieldException {
        Class clazz = MyNestedTestClass.class;
        Field field = clazz.getDeclaredField("myNestedField");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class<List<Integer>> expectedType = List.class;
        Type genericType = field.getGenericType();
        ParameterizedType paramType = (ParameterizedType) genericType;

        Class<?>[] actualTypeArguments = paramType.getActualTypeArguments();
        Class<?> firstArgument = actualTypeArguments[0];

        assertEquals(expectedType, firstArgument.getClass());
        assertEquals(Integer.class, actualTypeArguments[1].getClass());

        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Class clazz = MyTestClass.class;
        Field field = clazz.getDeclaredField("myField");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    private static class MyTestClass {
        private String myField;
    }

    private static class MyNestedTestClass<T, I> {
        private List<I> myNestedField;
    }
}