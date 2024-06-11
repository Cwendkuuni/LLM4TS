import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfoType actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals("TypeInfoArray", actualTypeInfo.getClass().getSimpleName());
    assertNotNull(actualTypeInfo.getElementType());
    assertEquals(int[].class, actualTypeInfo.getElementType().getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, ReflectiveOperationException {
    Class<Integer> clazz = Integer.class;
    Field field = clazz.getField("value");
    Type type = field.getGenericType();

    Object parentTypeMock = mock(Object.class);
    when(parentTypeMock).thenReturn(clazz);

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, parentTypeMock);
    assertEquals("TypeInfo", actualTypeInfo.getClass().getSimpleName());
    assertNotNull(actualTypeInfo.getType());
    assertEquals(Integer.TYPE, actualTypeInfo.getType().getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithInvalidType() {
    Type typeToEvaluate = null;
    Type parentType = null;
    Type rawParentClass = null;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }

  @Test
  public void testGetActualTypeWithClassType() {
    Class<?> rawParentClass = String[].class;
    Type typeToEvaluate = String.class;

    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualTypeWithParameterizedType() {
    Class<?> rawParentClass = new ArrayList<String>(3).getClass();
    Type typeToEvaluate = new TypeToken<List<String>>() {}.getType();

    Type[] actualTypeArguments = TypeInfoFactory.extractRealTypes(typeToEvaluate.getTypeArguments(), null, rawParentClass);
    Type rawType = typeToEvaluate.getRawType();

    Type actualType = new ParameterizedTypeImpl(rawType, actualTypeArguments, null);
    assertEquals(List.class, actualType.getRawType());
    assertEquals(String.class, actualType.getActualTypeArguments()[0].getType());
  }

  @Test
  public void testGetActualTypeWithGenericArrayType() {
    Class<?> rawParentClass = new String[3].getClass();
    Type typeToEvaluate = new TypeToken<String[]>() {}.getType();

    Type componentType = TypeInfoFactory.getActualType(typeToEvaluate.getComponentType(), null, rawParentClass);
    Type genericArrayType = new GenericArrayTypeImpl(componentType);

    assertEquals(String[].class, genericArrayType.getType());
    assertEquals(String.class, componentType.getType());
  }

  @Test
  public void testGetActualTypeWithTypeVariable() {
    Class<String> rawParentClass = String.class;
    Type parentType = new TypeToken<Map<String, Integer>>() {}.getType();
    TypeVariable<String> fieldTypeVariable = new TypeVariable<String>("key") {};

    int indexOfActualTypeArgument = TypeInfoFactory.getIndex(rawParentClass.getTypeParameters(), fieldTypeVariable);

    Type actualTypeArgument = new TypeToken<String>() {}.getType();
    assertEquals(actualTypeArgument, TypeInfoFactory.extractRealTypes((Type[]) parentType.getActualTypeArguments(), null, rawParentClass)[indexOfActualTypeArgument]);
  }

  @Test
  public void testGetActualTypeWithWildcardType() {
    Class<?> rawParentClass = new ArrayList<Number>(3).getClass();
    Type parentType = new TypeToken<ArrayList<?>>() {}.getType();

    Type wildcardType = new WildcardTypeImpl(new Type[] { Object.class, Number.class }, null);
    assertEquals(wildcardType, TypeInfoFactory.getActualType(parentType.getGenericSuperclass(), null, rawParentClass));
  }
}