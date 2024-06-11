import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(TypeToken.get(type).getType());
    assertThat(typeInfo.getComponentType()).isInstanceOf(int[].class);
    assertThat(typeInfo.getRawType()).isInstanceOf(int[].class);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    List<String> list = new ArrayList<>();
    Class<?> clazz = list.getClass();

    Field field = clazz.getDeclaredField("list");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertThat(typeInfo.getType()).isInstanceOf(List.class);
    assertThat(typeInfo.getComponentType()).isInstanceOf(String.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForUnknownType() {
    Class<?> clazz = Object.class;
    Field field = clazz.getDeclaredField("someField");
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());

    // This should fail since 'Object' is not a ParameterizedType, GenericArrayType or TypeVariable.
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Type[] actualTypeArguments = new Type[]{String.class, Integer.class};
    Type parameterizedType = new Type[]{List.class, actualTypeArguments}.getClass();

    Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
    assertThat(actualType).isInstanceOf(List.class);

    List<String> stringList = new ArrayList<>();
    Type genericSuperclass = List.class.getGenericSuperclass();
    Type actualType2 = TypeInfoFactory.getActualType(genericSuperclass, stringList.getClass().getTypeName(), null);
    assertThat(actualType).isEqualTo(actualType2);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Type type = new int[3].getClass().getComponentType();
    Type genericArrayType = new Type[]{type}.getClass();

    Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);
    assertThat(actualType).isInstanceOf(int[].class);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<?> clazz = new ArrayList<Integer>(){}.getClass();
    TypeVariable<?> typeVariable = TypeToken.get(Integer.class).getType().getActualTypeArguments()[0];

    int index = TypeInfoFactory.getTypeInfoFactory().getIndex(clazz.getTypeParameters(), typeVariable);
    Type actualType = TypeInfoFactory.getActualType(clazz.getGenericSuperclass(), null, null);
    Type actualTypeArgument = ((ParameterizedType) actualType).getActualTypeArguments()[index];

    assertThat(actualTypeArgument).isInstanceOf(Integer.class);
  }
}