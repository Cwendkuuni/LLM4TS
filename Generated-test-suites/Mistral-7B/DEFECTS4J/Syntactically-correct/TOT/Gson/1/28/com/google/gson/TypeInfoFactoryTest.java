import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type arrayIntType = new int[0].getClass().getComponentType();
        TypeInfo expectedTypeInfo = new TypeInfoArray(new TypeToken<int[]>() {}.getType());
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(arrayIntType);

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
    }

    @Test
    public void testTypeInfoForField() throws NoSuchFieldException {
        Class<Person> clazz = Person.class;
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);

        Person person = new Person();
        PowerMockito.mockStatic(TypeUtils.class);
        when(TypeUtils.toRawClass(any(Type.class))).thenReturn(clazz);

        Type nameFieldType = nameField.getGenericType();
        TypeInfo expectedTypeInfo = new TypeInfo(String.class);
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(nameField, nameField.getGenericType());

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(String.class, expectedTypeInfo.getType());
        assertEquals(String.class, actualTypeInfo.getType());
    }

    @Test
    public void testGetActualTypeForClassType() {
        Type personListType = new TypeToken<List<Person>>() {}.getType();
        TypeInfo expectedTypeInfo = new TypeInfo(List.class, new TypeToken<Person>() {}.getType());
        TypeInfo actualTypeInfo = TypeInfoFactory.getActualType(personListType, null, null);

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(List.class, expectedTypeInfo.getRawType());
        assertEquals(List.class, actualTypeInfo.getRawType());
        assertEquals(Person.class, expectedTypeInfo.getComponentType());
        assertEquals(Person.class, actualTypeInfo.getComponentType());
    }

    @Test
    public void testGetActualTypeForParameterizedType() {
        Class<PersonList> personListClass = PersonList.class;

        Type listOfPersonType = new TypeToken<List<Person>>() {}.getType();
        Type listOfPersonRawType = new TypeToken<List<Person>>() {}.getType().getRawType();

        PowerMockito.mockStatic(TypeUtils.class);
        when(TypeUtils.toRawClass(any(Type.class))).thenReturn(personListClass);

        TypeInfo expectedTypeInfo = new TypeInfo(
            List.class,
            new TypeToken<Person>() {}.getType()
        );
        TypeInfo actualTypeInfo = TypeInfoFactory.getActualType(
            listOfPersonType,
            null,
            null
        );

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(List.class, expectedTypeInfo.getRawType());
        assertEquals(List.class, actualTypeInfo.getRawType());
        assertEquals(Person.class, expectedTypeInfo.getComponentType());
        assertEquals(Person.class, actualTypeInfo.getComponentType());

        assertEquals(expectedTypeInfo.toString(), actualTypeInfo.toString());
        assertEquals(expectedTypeInfo.hashCode(), actualTypeInfo.hashCode());
    }

    @Test
    public void testGetActualTypeForGenericArrayType() {
        Type intArrayType = new int[0].getClass().getComponentType();
        Type genericArrayType = new GenericArrayTypeImpl(int[].class);
        TypeInfo expectedTypeInfo = new TypeInfoArray(new TypeToken<int[]>() {}.getType());
        TypeInfo actualTypeInfo = TypeInfoFactory.getActualType(intArrayType, null, null);

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
        assertEquals(expectedTypeInfo.toString(), actualTypeInfo.toString());
        assertEquals(expectedTypeInfo.hashCode(), actualTypeInfo.hashCode());

        assertSame(expectedTypeInfo, actualTypeInfo);
    }

    @Test
    public void testGetActualTypeForTypeVariable() {
        Class<PersonList> personListClass = PersonList.class;

        PowerMockito.mockStatic(TypeUtils.class);
        when(TypeUtils.toRawClass(any(Type.class))).thenReturn(personListClass);

        TypeVariable<A> aTypeVariable = personListClass.getTypeParameters()[0];
        TypeVariable<B> bTypeVariable = new TypeVariable<B>("B") {};

        TypeInfo expectedTypeInfo = new TypeInfo(List.class, new TypeToken<Person>() {}.getType());
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(
            aTypeVariable,
            personListClass.getGenericSuperclass()
        );

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(List.class, expectedTypeInfo.getRawType());
        assertEquals(List.class, actualTypeInfo.getRawType());
        assertEquals(Person.class, expectedTypeInfo.getComponentType());
        assertEquals(Person.class, actualTypeInfo.getComponentType());

        assertEquals(expectedTypeInfo.toString(), actualTypeInfo.toString());
        assertEquals(expectedTypeInfo.hashCode(), actualTypeInfo.hashCode());

        assertEquals(expectedTypeInfo, actualTypeInfo);

        TypeInfo actualTypeInfoForB = TypeInfoFactory.getTypeInfoForField(
            bTypeVariable,
            personListClass.getGenericSuperclass()
        );

        assertEquals(expectedTypeInfo, actualTypeInfoForB);
    }

    @Test
    public void testGetActualTypeForWildcardType() {
        Type listType = new TypeToken<List<?>>() {}.getType();
        TypeInfo expectedTypeInfo = new TypeInfoListWithUnknownType();
        TypeInfo actualTypeInfo = TypeInfoFactory.getActualType(listType, null, null);

        assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
        assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
        assertEquals(expectedTypeInfo.getUpperBound(), actualTypeInfo.getUpperBound());

        assertEquals(expectedTypeInfo.toString(), actualTypeInfo.toString());
        assertEquals(expectedTypeInfo.hashCode(), actualTypeInfo.hashCode());

        assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    private static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class PersonList extends ArrayList<Person> {
    }

    private static class A {
        // empty
    }

    private static class B {
        // empty
    }

    private static class TypeInfoListWithUnknownType extends TypeInfo {
        private Type upperBound;

        public TypeInfoListWithUnknownType() {
            super(new Object() {}.getClass().getGenericSuperclass());
            this.upperBound = null;
        }

        @Override
        public Type getUpperBound() {
            return upperBound;
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return String.format("TypeInfoListWithUnknownType<%s>", Arrays.toString(getType().getActualTypeArguments()));
        }
    }
}