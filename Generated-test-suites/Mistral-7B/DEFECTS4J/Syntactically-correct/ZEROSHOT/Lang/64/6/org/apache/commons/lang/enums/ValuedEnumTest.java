import org.apache.commons.lang.EnumUtils;
import org.apache.commons.lang.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> ValuedEnumClass = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersion", "JAVA1_0_VALUE");
 assertNotNull(javaVersionEnum);
 assertEquals("Java 1.0", javaVersionEnum.getName());
 assertEquals(100, javaVersionEnum.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullName() {
 ValuedEnum.getEnum(ValuedEnumClass, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumInvalidName() {
 ValuedEnum.getEnum(ValuedEnumClass, "InvalidEnumName");
 }

 @Test
 public void testGetEnumInvalidValue() {
 ValuedEnum javaVersionEnum = null;
 try {
 javaVersionEnum = ValuedEnum.getEnum(ValuedEnumClass, 101);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(javaVersionEnum);
 }

 @Test
 public void testGetEnumMap() {
 Map<ValuedEnum, String> map = ValuedEnum.getEnumMap(ValuedEnumClass);
 assertEquals(12, map.size());
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> list = ValuedEnum.getEnumList(ValuedEnumClass);
 assertEquals(12, list.size());
 }

 @Test
 public void testIterator() {
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(ValuedEnumClass);
 assertEquals(12, iterator.nextCount());
 }

 @Test
 public void testCompareTo() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "JAVA1_0_VALUE");
 ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersion", "JAVA1_1_VALUE");
 ValuedEnum javaVersion1_2 = ValuedEnum.getEnum("JavaVersion", "JAVA1_2_VALUE");

 assertTrue(javaVersion1_0.compareTo(javaVersion1_1) < 0);
 assertTrue(javaVersion1_1.compareTo(javaVersion1_2) < 0);
 assertTrue(javaVersion1_2.compareTo(javaVersion1_0) > 0);
 }
}