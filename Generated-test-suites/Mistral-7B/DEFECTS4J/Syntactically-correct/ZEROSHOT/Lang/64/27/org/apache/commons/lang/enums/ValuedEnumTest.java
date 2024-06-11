import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static class TestValuedEnum extends ValuedEnum {
 private TestValuedEnum(String name, int value) {
 super(name, value);
 }

 public static TestValuedEnum getEnum(String name) {
 return (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, name);
 }

 public static TestValuedEnum getEnum(int value) {
 return (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, value);
 }

 public static List<TestValuedEnum> getEnumList() {
 return ValuedEnum.getEnumList(TestValuedEnum.class);
 }
 }

 @Test
 public void testGetEnumByName() {
 TestValuedEnum testEnum = TestValuedEnum.getEnum("TestEnum1");
 assertEquals("TestEnum1", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumByNameNull() {
 TestValuedEnum testEnum = TestValuedEnum.getEnum(null);
 }

 @Test
 public void testGetEnumByValue() {
 TestValuedEnum testEnum = TestValuedEnum.getEnum(1);
 assertEquals("TestEnum1", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumByValueWrongValue() {
 TestValuedEnum testEnum = TestValuedEnum.getEnum(2);
 }

 @Test
 public void testGetEnumMap() {
 Map<String, TestValuedEnum> map = TestValuedEnum.getEnumMap();
 TestValuedEnum testEnum = map.get("TestEnum1");
 assertEquals("TestEnum1", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 }

 @Test
 public void testGetEnumList() {
 List<TestValuedEnum> list = TestValuedEnum.getEnumList();
 TestValuedEnum testEnum = list.get(0);
 assertEquals("TestEnum1", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 }

 @Test
 public void testIterateEnumList() {
 List<TestValuedEnum> list = TestValuedEnum.getEnumList();
 Iterator<TestValuedEnum> iterator = list.iterator();
 TestValuedEnum testEnum = iterator.next();
 assertEquals("TestEnum1", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 testEnum = iterator.next();
 assertEquals("TestEnum2", testEnum.getName());
 assertEquals(2, testEnum.getValue());
 }

 @Test
 public void testCompareToSameEnum() {
 TestValuedEnum testEnum1 = TestValuedEnum.getEnum("TestEnum1");
 TestValuedEnum testEnum2 = TestValuedEnum.getEnum("TestEnum1");
 int result = testEnum1.compareTo(testEnum2);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToDifferentEnum() {
 TestValuedEnum testEnum1 = TestValuedEnum.getEnum("TestEnum1");
 TestValuedEnum testEnum2 = TestValuedEnum.getEnum("TestEnum2");
 int result = testEnum1.compareTo(testEnum2);
 assertEquals(-1, result);
 }

 @Test
 public void testToString() {
 TestValuedEnum testEnum = TestValuedEnum.getEnum("TestEnum1");
 String result = testEnum.toString();
 String expected = "TestValuedEnum[TestEnum1=1]";
 assertEquals(expected, result);
 }

 @Test
 public void testToStringDifferentPackage() throws NoSuchMethodException,
 InvocationTargetException, InstantiationException, IllegalAccessException {
 Class otherPackageEnumClass = Class.forName("org.apache.commons.lang.enums.OtherPackageValuedEnum");
 Object otherPackageEnum = otherPackageEnumClass.getDeclaredConstructor(String.class, int.class).newInstance("OtherTestEnum1", 3);
 Object otherPackageEnumMethod = otherPackageEnumClass.getMethod("iToString");
 String otherPackageEnumResult = (String) otherPackageEnumMethod.invoke(otherPackageEnum);
 String expected = "OtherPackageValuedEnum[OtherTestEnum1=3]";
 assertEquals(expected, otherPackageEnumResult);
 }

 private static class OtherPackageValuedEnum extends ValuedEnum {
 private OtherPackageValuedEnum(String name, int value) {
 super(name, value);
 }

 private String iToString;
 }

}