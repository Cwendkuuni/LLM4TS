import org.apache.commons.lang.EnumUtils;
import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.2");
 ValuedEnum java1_3 = ValuedEnum.getEnum("JavaVersionEnum", "Java 1.3");

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());
 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());
 assertEquals("Java 1.2", java1_2.getName());
 assertEquals(120, java1_2.getValue());
 assertEquals("Java 1.3", java1_3.getName());
 assertEquals(130, java1_3.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidName() {
 ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", "InvalidName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidValue() {
 ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", 150);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);
 Map<String, ValuedEnum> enumMap = EnumUtils.toMap(enumList);

 assertEquals(4, enumMap.size());
 assertEquals("Java 1.0", enumMap.get("JavaVersionEnum.JAVA1_0").getName());
 assertEquals(100, enumMap.get("JavaVersionEnum.JAVA1_0").getValue());
 assertEquals("Java 1.1", enumMap.get("JavaVersionEnum.JAVA1_1").getName());
 assertEquals(110, enumMap.get("JavaVersionEnum.JAVA1_1").getValue());
 //... and so on
 }
}