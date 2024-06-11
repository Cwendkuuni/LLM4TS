import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import static edu.mscd.cs.jclo.JCLO.*;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() {
 testObject = new Object() {{
 // initialization of the test object with some fields and values
 }};
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo = new JCLO(testObject);
 Assert.assertNotNull(jclo.getObject(jclo.getField("field1")));
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = {{ "field1", "alias1" }, { "field2", "alias2" }};
 JCLO jclo = new JCLO(testObject, aliases);
 Assert.assertEquals("alias1", jclo.getValue("field1"));
 Assert.assertEquals("alias2", jclo.getValue("field2"));
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo = new JCLO(prefix, testObject);
 Assert.assertEquals("prefix_field1", jclo.getValue("field1"));
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = {{ "field1", "alias1" }, { "field2", "alias2" }};
 JCLO jclo = new JCLO(prefix, testObject, aliases);
 Assert.assertEquals("prefix_alias1", jclo.getValue("field1"));
 Assert.assertEquals("prefix_alias2", jclo.getValue("field2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNullKey() {
 jclo.getValue(null);
 }

 @Test
 public void testGetValueWithExistsKey() {
 jclo.setObject(jclo.getField("field1"), "value1");
 Assert.assertEquals("value1", jclo.getValue("field1"));
 }

 @Test
 public void testGetValueWithNullField() {
 Assert.assertNull(jclo.getValue("nonExistentField"));
 }

 @Test
 public void testSetObjectWithNullFieldAndNullValue() {
 jclo.setObject(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetObjectWithNullFieldAndValidValue() {
 Field nonExistentField = jclo.getField("nonExistentField");
 jclo.setObject(nonExistentField, new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetObjectWithValidFieldAndNullValue() {
 Field field1 = jclo.getField("field1");
 jclo.setObject(field1, null);
 }

 @Test
 public void testSetObjectWithValidFieldAndValidValue() {
 Field field1 = jclo.getField("field1");
 jclo.setObject(field1, "value2");
 Assert.assertEquals("value2", jclo.getValue("field1"));
 }

 @Test
 public void testGetArrayTypeWithArrayType() {
 Assert.assertEquals("int[]", getArrayType(int[].class));
 }

 @Test
 public void testGetArrayTypeWithNonArrayType() {
 Assert.assertEquals("String", getArrayType(String.class));
 }

 @Test
 public void testToString() {
 String expected = "int: field1 = value1\n"
 + "String: field2 = testObject\n";
 Assert.assertEquals(expected, jclo.toString());
 }

 @Test
 public void testUsage() {
 String expected = "--int--field1\n"
 + "--String--field2\n";
 String actual = jclo.usage();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseAdditional() {
 String[] args = {"--additional", "arg1", "arg2"};
 jclo.parse(args);
 String[] expected = {"arg1", "arg2"};
 Assert.assertArrayEquals(expected, (String[])jclo.getObject(jclo.getField("additional")));
 }

 @Test
 public void testAddToArray() {
 int size = 3;
 int[] array = new int[size];
 Field field = jclo.getField("arrayField");
 for (int i = 0; i < size; ++i) {
 Array.set(array, i, i);
 jclo.setObject(field, array);
 }
 Object obj = jclo.getObject(field);
 Object[] expected = new Object[] {1, 2, 3};
 Assert.assertArrayEquals(expected, (Object[])obj);
 }

 @Test
 public void testParseWithBooleanTypeAndValue() {
 String[] args = {"--boolean--boolean=true"};
 jclo.parse(args);
 Assert.assertTrue((boolean)jclo.getValue("boolean"));
 }

 @Test
 public void testParseWithBooleanTypeAndNullValue() {
 String[] args = {"--boolean--boolean"};
 jclo.parse(args);
 Assert.assertFalse((boolean)jclo.getValue("boolean"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownType() {
 String[] args = {"--unknown--unknown=value"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithStringTypeAndValue() {
 String[] args = {"--String--String=test"};
 jclo.parse(args);
 Assert.assertEquals("test", jclo.getValue("String"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNumberTypeAndNoEquals() {
 String[] args = {"--int--integer"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNumberTypeAndTwoValues() {
 String[] args = {"--int--integer=value1", "--int--integer=value2"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNumberTypeAndNegativeValue() {
 String[] args = {"--int--integer=-1"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndNoEquals() {
 String[] args = {"--int[]--array"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndOneValue() {
 String[] args = {"--int[]--array=value"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndTwoValues() {
 String[] args = {"--int[]--array=value1", "--int[]--array=value2"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithArrayTypeAndValidValues() {
 String[] args = {"--int[]--array=1", "--int[]--array=2", "--int[]--array=3"};
 int[] expected = {1, 2, 3};
 jclo.parse(args);
 Object obj = jclo.getObject(jclo.getField("arrayField"));
 Assert.assertArrayEquals(expected, (int[])obj);
 }

 @Test
 public void testParseWithArrayTypeAndEmptyStringValue() {
 String[] args = {"--int[]--array="};
 jclo.parse(args);
 Object obj = jclo.getObject(jclo.getField("arrayField"));
 Assert.assertArrayEquals(new int[0], (int[])obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndEmptyArrayValue() {
 String[] args = {"--int[]--array="};
 jclo.parse(args);
 jclo.parse(args); // second call to parse should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndNullValue() {
 String[] args = {"--int[]--array=null"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithArrayTypeAndInvalidValue() {
 String[] args = {"--int[]--array=invalid"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithUnknownKey() {
 String[] args = {"--unknown--unknown=value"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithEqualSignsAtTheBeginning() {
 String[] args = {"--==int--integer=value"};
 jclo.parse(args);
 Assert.assertEquals("integer", jclo.getValue("==int"));
 }

 @Test
 public void testParseWithEqualSignsAtTheEnd() {
 String[] args = {"--int--integer="};
 jclo.parse(args);
 Assert.assertEquals("integer", jclo.getValue("integer"));
 }

 @Test
 public void testParseWithBothDoubleDashesAndEquals() {
 String[] args = {"--==boolean--boolean=true"};
 jclo.parse(args);
 Assert.assertTrue((boolean)jclo.getValue("boolean"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithBothDoubleDashesAndNoEquals() {
 String[] args = {"--==boolean--"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithBothDoubleDashesAndTwoKeys() {
 String[] args = {"--==boolean--boolean", "--==boolean--boolean2"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithBothDoubleDashesAndNonBooleanKey() {
 String[] args = {"--==boolean--integer"};
 jclo.parse(args);
 }
}