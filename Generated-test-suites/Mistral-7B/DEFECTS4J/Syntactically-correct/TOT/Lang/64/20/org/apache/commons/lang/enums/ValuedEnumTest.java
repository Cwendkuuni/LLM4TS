import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 assertNotNull(java1_0);
 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullName() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, null);
 fail("Expected NullPointerException, but got: " + javaVersionEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumInvalidName() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, "INVALID_ENUM");
 fail("Expected IllegalArgumentException, but got: " + javaVersionEnum);
 }

 @Test
 public void testGetEnumInvalidClass() {
 try {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(Object.class, "JAVA1_0");
 fail("Expected IllegalArgumentException, but got: " + javaVersionEnum);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetEnumValue() {
 int java1_0Value = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0").getValue();
 assertEquals(100, java1_0Value);
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumValueNullName() {
 int value = ValuedEnum.getEnumValue(ValuedEnum.class, null);
 fail("Expected NullPointerException, but got: " + value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumValueInvalidName() {
 int value = ValuedEnum.getEnumValue(ValuedEnum.class, "INVALID_ENUM");
 fail("Expected IllegalArgumentException, but got: " + value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumValueInvalidClass() {
 try {
 int value = ValuedEnum.getEnumValue(Object.class, "JAVA1_0");
 fail("Expected IllegalArgumentException, but got: " + value);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCompareToSameValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_0Copy = new ValuedEnum("JavaVersionEnum", 100) {
 @Override
 public String getName() {
 return "Java 1.0";
 }
 };
 int compareResult = java1_0.compareTo(java1_0Copy);
 assertEquals(0, compareResult);
 }

 @Test
 public void testCompareToDifferentValues() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 int compareResult = java1_0.compareTo(java1_1);
 assertTrue(compareResult < 0);
 }

 @Test
 public void testToString() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 }

 @Test
 public void testCompareToDifferentClasses() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum1", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum2", "JAVA1_1");
 int compareResult = java1_0.compareTo(java1_1);
 fail("Expected IllegalStateException, but got: " + compareResult);
 }
}