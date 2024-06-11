import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type arrayType = new TypeToken<Integer[]>() {}.getType();
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(arrayType);

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);

    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Field field = Foo.class.getField("field");
    Type fieldType = new TypeToken<Integer>() {}.getType();

    Type definingClassType = new TypeToken<Foo>() {}.getType();
    Type parentType = definingClassType;

    TypeInfo expectedTypeInfo = new TypeInfo(fieldType);

    Type definingFieldType = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(definingFieldType, parentType, rawClass);

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);

    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getRawType(), actualTypeInfo.getRawType());
  }

  @Test
  public void testGetActualTypeForClassType() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Foo>() {}.getType();

    Type expectedType = rawClass;
    Type actualType = TypeInfoFactory.getActualType(expectedType, parentType, rawClass);

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Bar<Integer>>() {}.getType();

    Class<Bar> innerClass = Mockito.mock(Bar.class);
    when(rawClass.getGenericSuperclass()).thenReturn(new TypeToken<Bar<Integer>>() {}.getType());
    when(rawClass.getDeclaredClasses()).thenReturn(new Class<?>[] { innerClass });

    Type expectedType = innerClass;
    Type actualType = TypeInfoFactory.getActualType(parentType, parentType, rawClass);

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Foo[]>() {}.getType();

    Class<Integer> rawComponentType = Integer.TYPE;
    Type componentType = new TypeToken<Integer>() {}.getType();

    Type expectedType = new GenericArrayTypeImpl(componentType);
    Type actualType = TypeInfoFactory.getActualType(componentType, parentType, rawClass);

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Foo>() {}.getType();

    TypeVariable<String> typeVariable = new TypeVariable<String>("T") {} ;
    TypeVariable<String> classTypeVariable = new TypeVariable<String>("T") {} ;

    when(rawClass.getTypeParameters()).thenReturn(new TypeVariable<?>[] { classTypeVariable });

    int index = 0;
    Type expectedType = new TypeToken<Integer>() {}.getType();

    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, rawClass);

    assertEquals(expectedType, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForInvalidType() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Foo>() {}.getType();

    Type invalidType = new Object() {};
    TypeInfoFactory.getActualType(invalidType, parentType, rawClass);
  }

  @Test
  public void testExtractRealTypes() {
    Class<Foo> rawClass = Mockito.mock(Foo.class);
    Type parentType = new TypeToken<Foo>() {}.getType();

    Type[] expectedTypes = new Type[] { new TypeToken<Integer>() {}.getType(),
        new TypeToken<Double>() {}.getType() };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(new Type[] { null, new TypeToken<Integer>() {}.getType() },
        parentType, rawClass);

    assertNotNull(actualTypes);
    assertEquals(expectedTypes.length, actualTypes.length);

    for (int i = 0; i < expectedTypes.length; ++i) {
      assertEquals(expectedTypes[i], actualTypes[i]);
    }
  }

  @Test
  public void testGetIndex() {
    TypeVariable<String>[] classTypeVariables = new TypeVariable<?>[] { new TypeVariable<String>("T") {} };
    TypeVariable<String> typeVariable = new TypeVariable<String>("T") {};

    int expectedIndex = 0;
    int actualIndex = TypeInfoFactory.getIndex(classTypeVariables, typeVariable);

    assertEquals(expectedIndex, actualIndex);
  }

  private static class Foo {
    int field;
  }

  private static class Bar<T> {
  }
}