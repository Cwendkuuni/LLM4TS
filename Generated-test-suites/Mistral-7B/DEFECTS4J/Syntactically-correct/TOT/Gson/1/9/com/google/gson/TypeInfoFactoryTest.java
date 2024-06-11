import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getGenericComponentType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForArray(type);
    assertInstanceOf(TypeInfoArray.class, info);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Type typeDefiningF = List.class.getDeclaredField("list").getGenericType();
    Field field = List.class.getDeclaredField("size");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, typeDefiningF, List.class);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertEquals(TypeToken.of(int.class).getType(), actualType);
    assertEquals(TypeToken.of(int.class), info.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForNotSupportedType() {
    Type typeDefiningF = List.class.getDeclaredField("hashCode").getGenericType();
    Field field = List.class.getDeclaredField("hashCode");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, typeDefiningF, List.class);
  }

  @Test
  public void testTypeInfoForParameterizedType() {
    Type typeDefiningF = new ArrayList<String>().getClass();
    Field field = typeDefiningF.getDeclaredField("E");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, typeDefiningF, ArrayList.class);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertEquals(String.class, actualType);
    assertEquals(TypeToken.of(String.class), info.getType());
  }

  @Test
  public void testTypeInfoForGenericArray() {
    Type typeDefiningF = int[].class;
    Field field = Object[].class.getDeclaredField("length");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, typeDefiningF, Object.class);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertEquals(TypeToken.of(int[].class), info.getType());
  }

  @Test
  public void testTypeInfoForTypeVariable() throws NoSuchFieldException, IllegalAccessException {
    Type typeDefiningF = new ArrayList<String>().getClass();
    Field field = typeDefiningF.getDeclaredField("E");
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(field.getGenericType(), typeDefiningF, ArrayList.class);
    assertEquals(String.class, actualType);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertEquals(TypeToken.of(String.class), info.getType());
  }

  @Test
  public void testTypeInfoForWildcardType() {
    Type typeDefiningF = ArrayList.class;
    Field field = Collection.class.getDeclaredField("List");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, typeDefiningF, Collection.class);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    TypeToken<?> expectedType = TypeToken.getParameterized(List.class, WildcardType.class);
    assertEquals(expectedType.getType(), info.getType());
  }
}