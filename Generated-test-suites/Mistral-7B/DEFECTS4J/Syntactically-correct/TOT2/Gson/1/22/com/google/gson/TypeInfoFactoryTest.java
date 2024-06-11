import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Field;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import java.lang.reflect.NoSuchFieldException;
import java.lang.reflect.IllegalAccessException;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass();
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);
    Assert.assertEquals("int[]", infoArray.getType().getCanonicalName());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = nameField.getGenericType();
    TypeInfo expectedTypeInfo = new TypeInfo(String.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullField() throws NoSuchFieldException {
    Class<Person> clazz = Person.class;
    Field nameField = null;
    Type type = nameField.getGenericType();
    TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullTypeDefiningF() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = nameField.getGenericType();
    TypeInfoFactory.getTypeInfoForField(nameField, null);
  }

  @Test
  public void testTypeInfoForNonGenericField() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = nameField.getType();
    TypeInfo expectedTypeInfo = new TypeInfo(String.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonParameterizedType() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = clazz.getSuperclass();
    TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForInvalidField() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = null;
    TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForInvalidType() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = clazz.getType();
    TypeInfoFactory.getTypeInfoForField(nameField, type);
  }

  @Test
  public void testTypeInfoForArray() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type nameType = nameField.getGenericType();
    if (nameType instanceof GenericArrayType) {
      GenericArrayType nameArrayType = (GenericArrayType) nameType;
      Type componentType = nameArrayType.getGenericComponentType();
      TypeInfo expectedTypeInfo = new TypeInfo(String[].class);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(nameField, clazz.getType());
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullParentTypeAndRawParentClass() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    Type type = nameField.getGenericType();
    TypeInfoFactory.getTypeInfoForField(nameField, null, null);
  }

  static class Person {
    private String name;
  }
}