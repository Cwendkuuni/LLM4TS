import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

public class TypeInfoFactoryTest {

  private static final Class<?> CLASS_WITH_FIELD = new Class<>() {
    private int field;
  };

  private static final Class<?> PARAMETERIZED_CLASS = new Class<>() {
    public static <A> void method(A a) {}
  };

  @Test
  public void testGetTypeInfoForArray() {
    Type type = Integer[].class.getGenericComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForArray(type);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = CLASS_WITH_FIELD.getDeclaredField("field");
    Type type = field.getType();
    Type definingType = CLASS_WITH_FIELD.getDeclaredField("field").getType();
    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, definingType);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Type type = new TypeToken<List<String>>() {}.getType();
    TypeInfo expected = new TypeInfo(List.class, new Type[] { String.class });
    TypeInfo actual =
        (TypeInfo) TypeInfoFactory.getTypeInfoForField(
            ReflectUtils.getDeclaredField(List.class, "TYPE"), List.class);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForNonParametrizedType() {
    Type type = Integer.TYPE;
    TypeInfoFactory.getActualType(type, null, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    Type type = new TypeToken<List<?>>() {}.getType();
    TypeInfoFactory.getActualType(type, null, null);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Type type = new TypeToken<List<A>>() {}.getType();
    TypeVariable<?> typeVariable = type.getTypeParameters()[0];
    Type definingType = List.class;
    int index = 0;
    Type actualType = TypeInfoFactory.getActualType(typeVariable, definingType, null);
    TypeInfo expected = new TypeInfo(null); // TypeVariable should not have an actual type in this context
    assertThat(actualType).isNull();
    assertThat(actualType).isEqualTo(expected.getType());
  }

  @Test
  public void testGetActualTypeForNullParentType() {
    Type type = null;
    assertThrows(NullPointerException.class, () -> TypeInfoFactory.getActualType(type, null, null));
  }

  @Test
  public void testGetActualTypeForNullRawParentClass() {
    Class<?> rawParentClass = null;
    Type type = new TypeToken<List<String>>() {}.getType();
    assertThrows(NullPointerException.class, () -> TypeInfoFactory.getActualType(type, null, rawParentClass));
  }

  @Test
  public void testGetActualTypeForParameterizedTypeWithRawType() {
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Class<?> rawParentClass = List.class;
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    Type expected = new TypeInfoParameterizedTypeImpl(ArrayList.class, new Type[] { String.class });
    TypeInfo actual = (TypeInfo) TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetActualTypeForArrayOfArray() {
    Type parentType = new TypeToken<ArrayList<Object[]>>() {}.getType();
    Class<?> rawParentClass = ArrayList.class;
    Type type = new TypeToken<Object[][]>() {}.getType();
    TypeInfo expected = new TypeInfoArray(new TypeInfoArray(Object[].class));
    TypeInfo actual = (TypeInfo) TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetActualTypeForParameterizedTypeWithWildcard() {
    Type parentType = new TypeToken<Map<String, ?>>() {}.getType();
    Class<?> rawParentClass = Map.class;
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    Type expected = new TypeInfoWildcardTypeImpl(Object.class);
    TypeInfo actual = (TypeInfo) TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertThat(actual).isEqualTo(expected);
  }
}