import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private Field testField;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 testField = FieldTestUtils.getField(MyClass.class, "testField");
 }

 @Test
 public void testGetGenericTypeWithGenericField() {
 Class expectedType = String.class;
 Type genericType = testField.getGenericType();
 Class actualType = genericMaster.getGenericType(testField);

 assertThat(actualType, is(notNullValue()));
 assertThat(actualType, is(expectedType));
 }

 @Test
 public void testGetGenericTypeWithNonGenericField() {
 Field nonGenericField = FieldTestUtils.getField(AnotherClass.class, "testField");
 Class actualType = genericMaster.getGenericType(nonGenericField);

 assertThat(actualType, is(Object.class));
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 Class actualType = genericMaster.getGenericType(null);

 assertThat(actualType, is(Object.class));
 }
}