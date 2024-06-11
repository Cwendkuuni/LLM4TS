import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.TestFields;

import static org.mockito.internal.util.reflection.TestFields.FIELD_STRING_LIST;
import static org.mockito.internal.util.reflection.TestFields.FIELD_FIELD_WITH_GENERIC_TYPE;
import static org.mockito.internal.util.reflection.TestFields.FIELD_GENERIC_CLASS_TYPE;
import static org.mockito.internal.util.reflection.TestFields.FIELD_OBJECT_TYPE;

public class GenericMasterTest {

 @Test
 public void testGetGenericType_FieldWithGenericType_ReturnsCorrectType() {
 Field field = TestFields.getField(GenericMaster.class, FIELD_FIELD_WITH_GENERIC_TYPE);
 Class expectedType = String[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 Assert.assertEquals("Test get generic type with correct field and type", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_FieldWithoutGenericType_ReturnsObject() {
 Field field = TestFields.getField(GenericMaster.class, FIELD_OBJECT_TYPE);
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);

 Assert.assertEquals("Test get generic type with non-generic field", expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_NullField_ThrowsNPE() {
 Field nullField = null;
 Class actualType = new GenericMaster().getGenericType(nullField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_NonFieldObject_ThrowsIAE() {
 Object nonFieldObject = new Object();
 Class actualType = new GenericMaster().getGenericType(nonFieldObject);
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericType_ReflectiveOperationFailed_ThrowsRE() {
 Field field = TestFields.getField(GenericMaster.class, FIELD_GENERIC_CLASS_TYPE);
 field.setAccessible(true);
 Class actualType = null;
 try {
 actualType = (Class) new GenericMaster().getGenericType(field);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Unexpected exception when trying to access private field: " + e.getMessage(), e);
 }

 Assert.assertNull("Test get generic type with inaccessible field", actualType);
 }
}