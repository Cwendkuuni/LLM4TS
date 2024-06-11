import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", null);

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());

 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());

 assertNull(nullEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum.getEnum(null, "JAVA1_0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidEnumName() {
 ValuedEnum.getEnum("JavaVersionEnum", "INVALID_ENUM");
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", -1);
 assertNull(invalidEnum);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 Map<String, ValuedEnum> javaVersionMap = ValuedEnum.getEnumMap(ValuedEnum.class);

 assertEquals(javaVersions.size(), javaVersionMap.size());

 for (ValuedEnum javaVersion : javaVersions) {
 assertEquals(javaVersion, javaVersionMap.get(javaVersion.getName()));
 }
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);

 assertEquals(3, javaVersions.size());

 for (ValuedEnum javaVersion : javaVersions) {
 System.out.println(javaVersion.toString());
 }
 }

 @Test
 public void testGetEnumIterator() {
 List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
 Iterator<ValuedEnum> javaVersionIterator = ValuedEnum.iterator(ValuedEnum.class);

 assertEquals(javaVersions.size(), 3);

 assertTrue(javaVersionIterator.hasNext());
 ValuedEnum javaVersion1 = javaVersionIterator.next();

 assertTrue(javaVersionIterator.hasNext());
 ValuedEnum javaVersion2 = javaVersionIterator.next();

 assertTrue(javaVersionIterator.hasNext());
 ValuedEnum javaVersion3 = javaVersionIterator.next();

 assertFalse(javaVersionIterator.hasNext());
 }
}