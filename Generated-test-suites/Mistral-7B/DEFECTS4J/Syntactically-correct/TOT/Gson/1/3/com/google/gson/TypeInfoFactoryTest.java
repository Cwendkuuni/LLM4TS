import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type arrayType = new TypeToken<Integer[]>() {}.getType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertEquals(Integer[].class, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myField");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;
    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    assertEquals(String.class, actualType);
  }

  @Test
  public void testTypeInfoForFieldWithArrayComponentType() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldArray");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    Type componentType = PowerMockito.mock(Class.class);
    PowerMockito.whenNew(Class.class, Arrays.asList(String[].class)).thenReturn(componentType);

    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    PowerMockito.verifyNew(Class.class).withArguments(String[].class);
    assertEquals(String[].class, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForFieldWithUnknownTypeVariable() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldVariable");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    TypeVariable<?> unknownTypeVariable = TypeToken.getType(int.class).getType().getTypeVariables()[0];

    PowerMockito.whenNew(Class.class, Arrays.asList(unknownTypeVariable)).thenReturn(String.class);

    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    PowerMockito.verifyNew(Class.class).withArguments(unknownTypeVariable);
    PowerMockito.verifyNoMoreInteractions(Mockito.mock(Class.class));
  }

  @Test
  public void testTypeInfoForFieldWithParameterizedType() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldParameterized");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    TypeParameterized typeParameterized = PowerMockito.mock(TypeParameterized.class);
    PowerMockito.whenNew(TypeParameterized.class).withArguments(List.class).thenReturn(typeParameterized);

    Type[] actualTypeArguments = PowerMockito.mock(Type[].class);
    PowerMockito.whenNew(Type[].class, new Object[]{Integer.class}).thenReturn(actualTypeArguments);

    PowerMockito.when(typeParameterized.getActualTypeArguments()).thenReturn(actualTypeArguments);

    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    PowerMockito.verifyNew(TypeParameterized.class).withArguments(List.class);
    PowerMockito.verifyNew(Type[].class).withArguments(new Object[]{Integer.class});
    assertEquals(List.class, actualType);
  }

  @Test
  public void testTypeInfoForFieldWithGenericArrayType() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldGenericArray");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    Type componentType = PowerMockito.mock(Type.class);
    PowerMockito.when(componentType.equals(TypeUtils.getRawType(List.class))).thenReturn(true);

    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(fieldType);

    PowerMockito.verifyZeroInteractions(componentType);
    assertNotNull(typeInfo);
    assertEquals(List[].class, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForFieldWithWildcardType() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldWildcard");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    Type wildcardType = PowerMockito.mock(WildcardType.class);
    PowerMockito.when(wildcardType.getUpperBounds()).thenReturn(new Type[]{String.class});

    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    PowerMockito.verifyNew(WildcardType.class).withArguments(null, TypeVariable.class);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testTypeInfoForFieldWithTypeVariable() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldTypeVariable");
    Type fieldType = field.getGenericType();
    Type definingType = MyClass.class;

    TypeVariable<?> typeVariable = PowerMockito.mock(TypeVariable.class);
    PowerMockito.when(typeVariable.getBounds()).thenReturn(new Type[]{String.class});

    Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);

    PowerMockito.verifyNew(TypeVariable.class).withArguments("T");
    assertEquals(String.class, actualType);
  }

  @Test
  public void testTypeInfoForFieldWithUnknownTypeVariableInDefiningClass() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myFieldTypeVariable");
    Type fieldType = field.getGenericType();
    Type definingType = MyUnknownClass.class;

    TypeVariable<?> typeVariable = PowerMockito.mock(TypeVariable.class);
    PowerMockito.when(typeVariable.getBounds()).thenReturn(new Type[]{String.class});

    PowerMockito.whenNew(TypeVariable.class).withArguments("T").thenReturn(typeVariable);

    try {
      Type actualType = TypeInfoFactory.getActualType(fieldType, definingType, clazz);
      fail("Expected UnsupportedOperationException");
    } catch (IllegalArgumentException e) {
      assertEquals("Type '" + fieldType + "' is not a Class, ParameterizedType, GenericArrayType or TypeVariable. Can't extract type.", e.getMessage());
    }
  }

  static class MyClass {
    private String myField;
    private List<Integer> myFieldParameterized;
    private String[] myFieldArray;
    private List<? extends Number> myFieldWildcard;
    private Object myFieldTypeVariable;
  }

  static class MyUnknownClass<T> {
    private T myFieldTypeVariable;
  }
}