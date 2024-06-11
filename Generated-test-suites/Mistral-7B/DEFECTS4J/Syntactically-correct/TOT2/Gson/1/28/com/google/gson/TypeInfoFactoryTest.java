import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoFactory;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new GenericArrayTypeImpl(int[].class);
    TypeInfoFactory typeInfoFactory = mock(TypeInfoFactory.class);
    when(typeInfoFactory.getTypeInfoForArray(intArrayType)).thenReturn(new TypeInfoArray(intArrayType));

    TypeInfo result = typeInfoFactory.getTypeInfoForArray(intArrayType);

    assertInstanceOf(TypeInfoArray.class, result);
  }

  @Test
  public void testTypeInfoForField() {
    Field field = mock(Field.class);
    Type definingClassType = new Class<Object>() {}; // A mock class
    Type fieldType = new Class<Integer>() {};
    Type actualType = new Class<String>() {};

    TypeInfoFactory typeInfoFactory = mock(TypeInfoFactory.class);
    when(typeInfoFactory.getTypeForField(field, definingClassType)).thenReturn(actualType);

    TypeInfo result = typeInfoFactory.getTypeInfoForField(field, definingClassType);

    assertEquals(new TypeInfo(actualType), result);
  }

  // Test edge cases and exception handling scenarios here

}