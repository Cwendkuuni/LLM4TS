import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");
 ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", "unknown");

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());
 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());
 assertEquals("Java 1.2", java1_2.getName());
 assertEquals(120, java1_2.getValue());
 assertNull(nullEnum);
 }

 @Test
 public void testGetEnumWithInt() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", 100);
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", 110);
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", 120);
 ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", 400);

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());
 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());
 assertEquals("Java 1.2", java1_2.getName());
 assertEquals(120, java1_2.getValue());
 assertNull(nullEnum);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 Map<String, ValuedEnum> javaVersionMap = ValuedEnum.getEnumMap(ValuedEnum.class);

 assertEquals(3, javaVersions.size());
 assertEquals(javaVersions.get(0), javaVersionMap.get("JAVA1_0"));
 assertEquals(javaVersions.get(1), javaVersionMap.get("JAVA1_1"));
 assertEquals(javaVersions.get(2), javaVersionMap.get("JAVA1_2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum.getEnum(null, "JAVA1_0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullName() {
 ValuedEnum.getEnum("JavaVersionEnum", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidName() {
 ValuedEnum.getEnum("JavaVersionEnum", "INVALID");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNegativeValue() {
 ValuedEnum.getEnum("JavaVersionEnum", -5);
 }

 @Test
 public void testCompareTo() {
 ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", 100) {
 @Override
 public int compareTo(Object other) {
 return 0;
 }
 };
 ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum", 110) {
 @Override
 public int compareTo(Object other) {
 return 1;
 }
 };
 ValuedEnum java1_2 = new ValuedEnum("JavaVersionEnum", 120) {
 @Override
 public int compareTo(Object other) {
 return -1;
 }
 };

 assertEquals(java1_0, java1_0);
 assertTrue(java1_0.compareTo(java1_1) < 0);
 assertTrue(java1_1.compareTo(java1_0) > 0);
 assertTrue(java1_0.compareTo(java1_2) < 0);
 assertTrue(java1_2.compareTo(java1_0) > 0);
 assertTrue(java1_1.compareTo(java1_2) > 0);
 assertTrue(java1_2.compareTo(java1_1) < 0);
 }
}