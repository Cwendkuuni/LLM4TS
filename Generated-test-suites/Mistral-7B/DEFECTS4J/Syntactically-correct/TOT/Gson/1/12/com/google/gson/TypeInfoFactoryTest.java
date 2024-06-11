import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import com.google.gson.TypeUtils;
import com.google.gson.annotations.Exclude;
import com.google.gson.reflect.TypeToken;

/**
 * Test class for {@link com.google.gson.TypeInfoFactory}.
 */
public class TypeInfoFactoryTest {

  private static final Class<?> RAW_CLASS = Object.class;
  private static final Type ARRAY_TYPE = new Type[] { int[].class };
  private static final Type PARAMETERIZED_TYPE =
      new TypeToken<List<String>>() {}.getType();
  private static final Type FIELD_TYPE_VARIABLE = TypeVariable.intValue(0);
  private static final Type WILDCARD_TYPE = new WildcardType(null, null);

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfo info = TypeInfoFactory.getTypeInfoForArray(ARRAY_TYPE);
    assertThat(info).isInstanceOf(TypeInfoArray.class);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_IsInParameterizedType() {
    Class<?> clazz = new Foo<>();
    Field field = clazz.getField("myField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertThat(info).isInstanceOf(TypeInfo.class);
    assertThat(((TypeInfo) info).getType()).isEqualTo(Integer.TYPE);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_IsNotInParameterizedType() {
    Class<?> clazz = new Foo<>();
    Field field = clazz.getField("excludedField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    assertThrows(UnsupportedOperationException.class, () -> {
      TypeInfoFactory.getTypeInfoForField(field, parentType);
    });
  }

  @Test
  public void testGetTypeInfoForField_Class() {
    Class<?> clazz = new Foo();
    Field field = clazz.getField("myField");
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertThat(info).isInstanceOf(TypeInfo.class);
    assertThat(((TypeInfo) info).getType()).isEqualTo(Integer.TYPE);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_InWildcardType() {
    Class<?> clazz = new Foo<>();
    Field field = clazz.getField("myField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type wildcardType = new Type[] { WILDCARD_TYPE };
    Type[] expectedTypes = new Type[] { Integer.TYPE };
    when(TypeUtils.toRawClass(parentType)).thenAnswer(new Answer<Class<?>>() {
      @Override
      public Class<?> answer(InvocationOnMock invocation) throws Throwable {
        return RAW_CLASS;
      }
    });
    when(TypeUtils.toRawClass(wildcardType[0])).thenAnswer(new Answer<Class<?>>() {
      @Override
      public Class<?> answer(InvocationOnMock invocation) throws Throwable {
        return Integer.TYPE;
      }
    });
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType, RAW_CLASS);
    assertThat(info).isInstanceOf(TypeInfo.class);
    assertThat(((TypeInfo) info).getType()).isEqualTo(Integer.TYPE);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_NotInWildcardType() {
    Class<?> clazz = new Foo<>();
    Field field = clazz.getField("myField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type wildcardType = new Type[] { WILDCARD_TYPE };
    when(TypeUtils.toRawClass(parentType)).thenAnswer(new Answer<Class<?>>() {
      @Override
      public Class<?> answer(InvocationOnMock invocation) throws Throwable {
        return RAW_CLASS;
      }
    });
    when(TypeUtils.toRawClass(wildcardType[0])).thenAnswer(new Answer<Class<?>>() {
      @Override
      public Class<?> answer(InvocationOnMock invocation) throws Throwable {
        return String.class;
      }
    });
    assertThrows(UnsupportedOperationException.class, () -> {
      TypeInfoFactory.getTypeInfoForField(field, parentType, RAW_CLASS);
    });
  }

  private static class Foo {
    private int myField;

    @Exclude
    private int excludedField;

    public Foo() {}

    public static class NestedClass {}
  }
}