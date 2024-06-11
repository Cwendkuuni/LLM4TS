import static org.junit.Assert.assertEquals;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;

public class TypeInfoFactoryTest {

  @ org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass();
    TypeInfo expectedTypeInfo = new TypeInfo(int[].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }
}