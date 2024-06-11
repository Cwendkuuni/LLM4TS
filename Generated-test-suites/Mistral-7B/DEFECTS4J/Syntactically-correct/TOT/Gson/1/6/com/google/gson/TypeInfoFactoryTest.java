import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.ReflectionUtil.FieldAccessException;
import com.google.gson.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoArray.getTypeInfoForArray(type);
    assertThat(typeInfo.getType()).isInstanceOf(int[].class);
  }

  @Test
  public void testTypeInfoForField_simple() throws NoSuchFieldException {
    int[] intArray = new int[5];
    int element = 1;
    TopLevelClass topLevelClass = new TopLevelClass(intArray, element);
    Field field = TopLevelClass.class.getField("intArray");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TypeUtils.unmodifiableWrapperType(type), TopLevelClass.class);
    assertThat(typeInfo.getType()).isInstanceOf(int[].class);
  }

  @Test
  public void testTypeInfoForField_parameterized() throws NoSuchFieldException {
    int[] intArray = new int[5];
    TopLevelClassWithGenericField topLevelClass = new TopLevelClassWithGenericField(intArray);
    Field field = TopLevelClassWithGenericField.class.getField("arrayOfStrings");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TypeUtils.unmodifiableWrapperType(type), TopLevelClassWithGenericField.class);
    assertThat(typeInfo.getType()).isInstanceOf(String[].class);
  }

  @Test
  public void testTypeInfoForField_wildcard() throws NoSuchFieldException {
    TopLevelClassWithWildcardField topLevelClass = new TopLevelClassWithWildcardField();
    Field field = TopLevelClassWithWildcardField.class.getField("field");
    Type type = field.getGenericType();
    assertThrows(FieldAccessException.class, () -> TypeInfoFactory.getTypeInfoForField(field, type, TopLevelClassWithWildcardField.class));
  }

  @Test
  public void testTypeInfoForField_unsupported() throws NoSuchFieldException {
    TopLevelClassWithUnsupportedField topLevelClass = new TopLevelClassWithUnsupportedField();
    Field field = TopLevelClassWithUnsupportedField.class.getField("unsupportedField");
    Type type = field.getGenericType();
    assertThrows(UnsupportedOperationException.class, () -> TypeInfoFactory.getTypeInfoForField(field, type, TopLevelClassWithUnsupportedField.class));
  }

  @Test
  public void testGetActualType_simpleClass() {
    Type actualType = TypeInfoFactory.getActualType(Integer.TYPE, null, Object.class);
    assertThat(actualType).isEqualTo(Integer.TYPE);
  }

  @Test
  public void testGetActualType_parameterizedType() {
    Type actualType = TypeInfoFactory.getActualType(new ParameterizedType() {
      @SuppressWarnings("unchecked")
      public Class<List<String>> getRawType() {
        return (Class<List<String>>) (List.class);
      }

      @SuppressWarnings("unchecked")
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }

      public Type getOwnerType() {
        return null;
      }
    }, null, Object.class);
    assertThat(actualType).isInstanceOf(List.class);
    assertThat(actualType).isInstanceOf(List.class);
    Type listType = ((ParameterizedType) actualType).getActualTypeArguments()[0];
    assertThat(listType).isInstanceOf(String.class);
  }

  @Test
  public void testGetActualType_genericArrayType() {
    Type actualType = TypeInfoFactory.getActualType(new GenericArrayTypeImpl(String.class), null, Object.class);
    assertThat(actualType).isInstanceOf(String[].class);
  }

  @Test
  public void testGetActualType_typeVariable() {
    Type actualType = TypeInfoFactory.getActualType(
        new TypeVariable<String>("T") {}, null, Object.class);
    assertThat(actualType).isInstanceOf(String.class);
  }

  @Test
  public void testGetActualType_wildcardType() {
    Type actualType = TypeInfoFactory.getActualType(
        new WildcardType(null, WildcardType.BOUND_UPPER), null, Object.class);
    assertThat(actualType).isInstanceOf(Object.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_unsupportedType() {
    Type actualType = TypeInfoFactory.getActualType(null, null, Object.class);
  }

  private static class TopLevelClass {
    int[] intArray;

    TopLevelClass(int[] intArray, int element) {
      this.intArray = intArray;
    }

    @SuppressWarnings("unused")
    static Class<?> toRawClass(Type type) {
      return TypeUtils.toRawClass(type);
    }

    @SuppressWarnings("unused")
    static Type unmodifiableWrapperType(Type type) {
      return TypeUtils.unmodifiableWrapperType(type);
    }
  }

  private static class TopLevelClassWithGenericField<T> {
    T[] arrayOfStrings;

    TopLevelClassWithGenericField(T[] arrayOfStrings) {
      this.arrayOfStrings = arrayOfStrings;
    }

    @SuppressWarnings("unused")
    static Class<?> toRawClass(Type type) {
      return TypeUtils.toRawClass(type);
    }

    @SuppressWarnings("unused")
    static Type unmodifiableWrapperType(Type type) {
      return TypeUtils.unmodifiableWrapperType(type);
    }
  }

  private static class TopLevelClassWithWildcardField {
    Object field;

    TopLevelClassWithWildcardField() {
    }
  }

  private static class TopLevelClassWithUnsupportedField<T> {
    @SuppressWarnings("unused")
    T unsupportedField;

    TopLevelClassWithUnsupportedField() {
    }
  }
}