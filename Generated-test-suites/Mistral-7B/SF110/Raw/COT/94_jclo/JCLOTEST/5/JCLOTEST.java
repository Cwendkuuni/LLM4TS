import static org.junit.Assert.*;
import edu.mscd.cs.jclo.*;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setup() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNull(jclo.prefix);
 assertNull(jclo.aliases);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 jclo = new JCLO(new Object(), new String[][]{{"a", "b"}, {"c", "d"}});
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNotNull(jclo.aliases);
 assertEquals(2, jclo.aliases.length);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix", new Object());
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNotNull(jclo.prefix);
 assertNull(jclo.aliases);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"a", "b"}, {"c", "d"}});
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNotNull(jclo.prefix);
 assertNotNull(jclo.aliases);
 assertEquals(2, jclo.aliases.length);
 }

 @Test
 public void testGetObject() {
 final Field field = new Field() {{
 name = "testField";
 type = int.class;
 }};
 jclo.fields = new Field[]{field};
 field.set(jclo.object, 1);
 final int value = (int)jclo.getObject(field);
 assertEquals(1, value);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithIllegalAccessException() {
 final Field field = new Field() {{
 name = "testField";
 type = int.class;
 }};
 jclo.fields = new Field[]{field};
 assertThrows(IllegalAccessException.class, () -> jclo.getObject(field));
 }

 @Test
 public void testGetField() {
 final Field field = new Field() {{
 name = "testField";
 }};
 jclo.fields = new Field[]{field};
 final Field result = jclo.getField("testField");
 assertEquals(field, result);
 }

 @Test
 public void testGetFieldWithNullResult() {
 final Field field = new Field() {{
 name = "nonExistentField";
 }};
 jclo.fields = new Field[]{field};
 final Field result = jclo.getField("nonExistentField");
 assertNull(result);
 }

 @Test
 public void testGetValue() {
 final Field field = new Field() {{
 name = "testField";
 type = int.class;
 }};
 jclo.fields = new Field[]{field};
 field.set(jclo.object, 1);
 final int value = (int)jclo.getValue("testField");
 assertEquals(1, value);
 }

 @Test
 public void testGetValueWithNullResult() {
 final Field field = new Field() {{
 name = "nonExistentField";
 }};
 jclo.fields = new Field[]{field};
 final int value = (int)jclo.getValue("nonExistentField");
 assertEquals(0, value);
 }

 @Test
 public void testSetObject() {
 final Field field = new Field() {{
 name = "testField";
 type = int.class;
 }};
 jclo.fields = new Field[]{field};
 jclo.setObject(field, 1);
 final int value = (int)jclo.getValue("testField");
 assertEquals(1, value);
 }

 @Test
 public void testToString() {
 final int value1 = 1;
 final int value2 = 2;
 final int[] values = {value1, value2};
 final Field field1 = new Field() {{
 name = "testField1";
 type = int.class;
 }};
 final Field field2 = new Field() {{
 name = "testField2";
 type = int[].class;
 }};jclo.fields = new Field[]{field1, field2};
 jclo.setObject(field1, value1);
 jclo.setObject(field2, values);String expected = "int: testField1 = 1int[]: testField2 = [1, 2]";
 String result = jclo.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testGetUsageTypeWithBooleanType() {
 String result = jclo.getUsageType(boolean.class);
 assertEquals("boolean[]", result);
 }

 @Test
 public void testGetUsageTypeWithoutDoubleDashes() {
 String result = jclo.getUsageType(int.class);
 assertEquals("int", result);
 }

 @Test
 public void testGetUsageTypeWithDoubleDashes() {
 String result = jclo.getUsageType(int.class);
 assertEquals("int...", result);
 }

 @Test
 public void testUsage() {
 final int value1 = 1;
 final int value2 = 2;
 final int[] values = {value1, value2};
 final Field field1 = new Field() {{
 name = "testField1";
 type = int.class;
 }};
 final Field field2 = new Field() {{
 name = "testField2";
 type = int[].class;
 }};
 jclo.fields = new Field[]{field1, field2};
 jclo.setObject(field1, value1);
 jclo.setObject(field2, values);String expected = "--testField1: int\n--testField2: int[]\n";
 String result = jclo.usage();
 assertEquals(expected, result);
 }

 @Test
 public void testParseAdditional() {
 final String[] args = new String[]{"--additional", "arg1", "arg2"};
 jclo.parse(args);
 Field additionalField = jclo.getField("additional");
 assertNotNull(additionalField);
 Object additionalValue = jclo.getObject(additionalField);
 assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additionalValue);
 }

 @Test
 public void testParseAddToArray() {
 final String[] args = new String[]{"--testField", "newValue"};
 final Field testField = new Field() {{
 name = "testField";
 type = int[].class;
 }};
 jclo.fields = new Field[]{testField};
 jclo.parseAdditional(args, 0);
 Object arrayValue = jclo.getObject(testField);
 assertArrayEquals(new int[]{0, newValue}, (int[])arrayValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidArgument() {
 final String[] args = new String[]{"--testField=newValue"};
 jclo.parse(args);
 }

 @Test
 public void testParseBooleanValue() {
 final String[] args = new String[]{"--testField=true"};
 jclo.parse(args);
 assertTrue((boolean)jclo.getValue("testField"));
 }

 @Test
 public void testParseBooleanValueWithYes() {
 final String[] args = new String[]{"--testField=yes"};
 jclo.parse(args);
 assertTrue((boolean)jclo.getValue("testField"));
 }

 @Test
 public void testParseBooleanValueWithFalse() {
 final String[] args = new String[]{"--testField=false"};
 jclo.parse(args);
 assertFalse((boolean)jclo.getValue("testField"));
 }

 @Test
 public void testParseIntValue() {
 final String[] args = new String[]{"--testField=123"};
 jclo.parse(args);
 assertEquals(123, (int)jclo.getValue("testField"));
 }

 @Test
 public void testParseStringValue() {
 final String[] args = new String[]{"--testField=Hello, World!"};
 jclo.parse(args);
 assertEquals("Hello, World!", (String)jclo.getValue("testField"));
 }

 @Test
 public void testParseDoubleValue() {
 final String[] args = new String[]{"--testField=3.14159"};
 jclo.parse(args);
 assertEquals(3.14159, (double)jclo.getValue("testField"), 0.001);
 }
}