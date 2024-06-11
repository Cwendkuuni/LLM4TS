import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type type = new int[5].getClass().getComponentType();
        TypeInfo expectedTypeInfo = new TypeInfoArray(type);
        TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);

        assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
    }

    @Test
    public void testTypeInfoForField() throws NoSuchFieldException {
        Class<TestClass> testClass = TestClass.class;
        Field field = testClass.getDeclaredField("field");
        Type type = testClass.getGenericSuperclass();

        TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type);

        assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTypeInfoForUnsupportedType() throws NoSuchFieldException {
        Class<TestClass> testClass = TestClass.class;
        Field field = testClass.getDeclaredField("field");
        Type type = testClass.getSuperclass();

        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type);
    }

    @Test
    public void testGetActualType() {
        Class<TestClass> testClass = TestClass.class;
        Type type = testClass.getDeclaredField("field").getGenericType();

        Class<?> expectedRawClass = Integer.TYPE.getClass();
        TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
        Type actualType = TypeInfoFactory.getActualType(type, testClass.getGenericSuperclass(), testClass);

        assertThat(TypeUtils.toRawType(actualType)).isEqualTo(expectedRawClass);
        assertThat(TypeInfoFactory.getTypeInfoForField(null, null)).isNull();
        assertThat(TypeInfoFactory.getTypeInfoForField(null, testClass.getGenericSuperclass())).isNull();

        Type fieldTypeVariable = TypeToken.of(Integer.class).getType();
        TypeVariable<?> typeVariable = (TypeVariable<?>) fieldTypeVariable;
        Type expectedActualType = Integer.TYPE;

        Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, testClass.getGenericSuperclass(), testClass);

        assertThat(actualType).isEqualTo(expectedActualType);
    }

    @Test
    public void testGetActualTypeForWildcardType() {
        Class<TestClass> testClass = TestClass.class;
        Type type = testClass.getGenericSuperclass();

        Type expectedUpperBound = String.class;
        TypeInfo expectedTypeInfo = new TypeInfo(String.class);
        Type actualType = TypeInfoFactory.getActualType(new WildcardType(null, new Class[]{String.class}), type, testClass);

        assertThat(actualType).isInstanceOf(TypeInfo.class);
        ((TypeInfo) actualType).getType().equals(expectedUpperBound);
    }

    @Test
    public void testExtractRealTypes() {
        Type type = new TestClass<Integer, String>() {}.getClass().getGenericSuperclass();
        Type[] expectedTypes = new Type[]{Integer.TYPE, String.class};

        Type[] actualTypes = TypeInfoFactory.extractRealTypes(type.getActualTypeArguments(), type, TestClass.class);

        for (int i = 0; i < actualTypes.length; i++) {
            assertThat(actualTypes[i]).isEqualTo(expectedTypes[i]);
        }
    }

    @Test
    public void testTypeInfoForArrayWithGenericComponentType() {
        Type type = new int[5].getClass().getComponentType();
        Type componentType = new TestClass<Integer, String>() {}.getClass().getGenericSuperclass();

        TypeInfo expectedTypeInfo = new TypeInfoArray(new GenericArrayTypeImpl(componentType));
        TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);

        assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
    }

    private static class TestClass<A, B> {
        int field;
    }
}

class TestClass {
    int field;
}