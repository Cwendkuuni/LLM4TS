import static org.junit.Assert.*;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type arrayType = new int[3].getClass().getComponentType();
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull(infoArray);
    assertEquals(arrayType, infoArray.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    TypeVariable<?> integerTypeVar = TypeVariable.class.getTypeVariables()[0];
    Class<?> rawParentClass = Integer.TYPE.getTypeName().parse().asSubTypeOf(Class.class);
    Field field = rawParentClass.getDeclaredField("VALUE");
    Type fieldType = field.getGenericType();
    TypeInfo infoField = TypeInfoFactory.getTypeInfoForField(field, rawParentClass.getGenericType());
    assertNotNull(infoField);
    assertEquals(Integer.TYPE, infoField.getType());
  }
}