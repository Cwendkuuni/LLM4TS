import org.junit.jupiter.api.Assertions.*;//for Assertions class
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

 @Test
 void testGetGenericTypeWithNullField() {
 GenericMaster genericMaster = new GenericMaster();
 assertNull(genericMaster.getGenericType(null));
 }

 @Test
 void testGetGenericTypeWithNonGenericField() {
 Class clazz = String.class;
 Field field = null; //this field doesn't exist
 GenericMaster genericMaster = new GenericMaster();
 assertNotNull(genericMaster);
 assertSame(Object.class, genericMaster.getGenericType(field));
 }

 @Test
 void testGetGenericTypeWithSimpleField() {
 Class clazz = List.class;
 Field field = null; //assuming a simple field of List type within this class
 try {
 Field declaredField = clazz.getDeclaredField("listField"); //replace listField with actual field name
 declaredField.setAccessible(true);
 GenericMaster genericMaster = new GenericMaster();
 assertSame(List.class, genericMaster.getGenericType(declaredField));
 } catch (NoSuchFieldException e) {
 fail("Field does not exist in the class");
 } catch (IllegalAccessException e) {
 fail("Failed to access the field");
 }
 }

 @Test
 void testGetGenericTypeWithNestedField() {
 Class clazz = Map.class;
 Field outerField = null; //assuming a field of Map type within this class
 try {
 Field outerField = clazz.getDeclaredField("mapField");
 outerField.setAccessible(true);
 Field innerField = null; //assuming a nested field of List type within the Map
 try {
 Field innerField = outerField.get(new HashMap<>()).getClass().getDeclaredField("listField"); //replace listField with actual nested field name
 innerField.setAccessible(true);
 GenericMaster genericMaster = new GenericMaster();
 assertSame(List.class, genericMaster.getGenericType(innerField));
 } catch (NoSuchFieldException e) {
 fail("Nested field does not exist in the Map");
 } catch (IllegalAccessException e) {
 fail("Failed to access the nested field");
 }
 } catch (NoSuchFieldException e) {
 fail("Outer field does not exist in the class");
 } catch (IllegalAccessException e) {
 fail("Failed to access the outer field");
 }
 }

 @Test
 void testGetGenericTypeWithArrayField() {
 Class clazz = String[].class;
 Field field = null; //assuming a field of String array type within this class
 try {
 Field declaredField = clazz.getDeclaredField("stringArrayField");
 declaredField.setAccessible(true);
 GenericMaster genericMaster = new GenericMaster();
 assertSame(String[].class, genericMaster.getGenericType(declaredField));
 } catch (NoSuchFieldException e) {
 fail("Field does not exist in the class");
 } catch (IllegalAccessException e) {
 fail("Failed to access the field");
 }
 }

 @Test
 void testGetGenericTypeWithParametrizedField() {
 Class clazz = Map.class;
 Type mapType = Map.class.getGenericType();
 Type mapListType = ((ParameterizedType) mapType).getActualTypeArguments()[1];
 Class listClass = (Class) mapListType;
 Field field = null; //assuming a field of List type within this class, parameterized with String
 try {
 Field declaredField = clazz.getDeclaredField("mapField");
 declaredField.setAccessible(true);
 GenericMaster genericMaster = new GenericMaster();
 assertSame(listClass, genericMaster.getGenericType(declaredField));
 } catch (NoSuchFieldException e) {
 fail("Field does not exist in the class");
 } catch (IllegalAccessException e) {
 fail("Failed to access the field");
 }
 }
}