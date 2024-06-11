import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import edu.mscd.cs.jclo.*;

@RunWith(JUnit4.class)
public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setup() {
 testObject = new Object() {
 };
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(testObject);
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNull(jclo.prefix);
 assertNull(jclo.aliases);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
 jclo = new JCLO(testObject, aliases);
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNotNull(jclo.aliases);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix", testObject);
 assertNotNull(jclo.prefix);
 assertNotNull(jclo.object);
 assertNull(jclo.aliases);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
 jclo = new JCLO("prefix", testObject, aliases);
 assertNotNull(jclo.prefix);
 assertNotNull(jclo.object);
 assertNotNull(jclo.aliases);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() {
 Field inaccessibleField = Reflections.getField(JCLO.class, "prefix");
 inaccessibleField.setAccessible(true);
 jclo = new JCLO(testObject);
 inaccessibleField.set(jclo, "test");
 Field testField = jclo.getField("prefix");
 Object obj = jclo.getObject(testField);
 assertNull(obj);
 }

 @Test
 public void testGetFieldWithValidKey() {
 Field field = jclo.getField("fields");
 assertNotNull(field);
 }

 @Test
 public void testGetFieldWithInvalidKey() {
 Field field = jclo.getField("nonexistent");
 assertNull(field);
 }

 @Test
 public void testGetValueWithValidKey() {
 Object value = new Object();
 Field field = Reflections.getField(testObject.getClass(), "test");
 Reflections.setFieldValue(testObject, field, value);
 jclo = new JCLO(testObject);
 assertEquals(value, jclo.getValue("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidKey() {
 jclo = new JCLO(testObject);
 jclo.getValue("nonexistent");
 }

 @Test
 public void testToString() {
 Field field = Reflections.getField(testObject.getClass(), "test");
 Reflections.setFieldValue(testObject, field, new int[]{1, 2, 3});
 jclo = new JCLO(testObject);
 String expected = "[int[]: test = [1, 2, 3]";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testGetUsageTypeWithBooleanPrimitiveType() {
 assertEquals("boolean", jclo.getUsageType(boolean.class));
 }

 @Test
 public void testGetUsageTypeWithBooleanWrapperClass() {
 assertEquals("boolean", jclo.getUsageType(Boolean.class));
 }

 @Test
 public void testGetUsageTypeWithArrayType() {
 assertEquals("int...", jclo.getUsageType(int[].class));
 }

 @Test
 public void testUsage() {
 String expected = "-int: test\n"
 + "-float: anotherTest\n"
 + "-java.lang.String: stringTest\n";
 jclo = new JCLO(testObject, new String[][]{{"int", "test"}, {"float", "anotherTest"}, {"java.lang.String", "stringTest"}});
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testParseAdditionalWithValidArguments() {
 String[] args = new String[]{"--additional", "arg1", "arg2"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])jclo.getObject(jclo.getField("additional")));
 }

 @Test
 public void testParseAdditionalWithInvalidArguments() {
 String[] args = new String[]{"--additional", "arg1"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseArrayWithValidArguments() {
 String[] args = new String[]{"--array:test:1", "val1", "--array:test:2", "val2"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 Object array = jclo.getValue("test");
 assertArrayEquals(new Object[]{val1, val2}, array);
 }

 @Test
 public void testParseArrayWithInvalidArguments() {
 String[] args = new String[]{"--array:test:1", "val1"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseBooleanWithValidArguments() {
 String[] args = new String[]{"--boolean:test:true"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertTrue((boolean)jclo.getValue("test"));
 }

 @Test
 public void testParseBooleanWithInvalidArguments() {
 String[] args = new String[]{"--boolean:test:false=invalid"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseByteWithValidArguments() {
 String[] args = new String[]{"--byte:test:1"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals((byte)1, (byte)jclo.getValue("test"));
 }

 @Test
 public void testParseByteWithInvalidArguments() {
 String[] args = new String[]{"--byte:test:100"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseCharWithValidArguments() {
 String[] args = new String[]{"--char:test:a"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals((char)'a', (char)jclo.getValue("test"));
 }

 @Test
 public void testParseCharWithInvalidArguments() {
 String[] args = new String[]{"--char:test:⋮"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseShortWithValidArguments() {
 String[] args = new String[]{"--short:test:1"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals((short)1, (short)jclo.getValue("test"));
 }

 @Test
 public void testParseShortWithInvalidArguments() {
 String[] args = new String[]{"--short:test:32768"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseIntWithValidArguments() {
 String[] args = new String[]{"--int:test:1"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals(1, (int)jclo.getValue("test"));
 }

 @Test
 public void testParseIntWithInvalidArguments() {
 String[] args = new String[]{"--int:test:-1"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseFloatWithValidArguments() {
 String[] args = new String[]{"--float:test:3.14"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals(3.14f, (float)jclo.getValue("test"), 0.01f);
 }

 @Test
 public void testParseFloatWithInvalidArguments() {
 String[] args = new String[]{"--float:test:NaN"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseDoubleWithValidArguments() {
 String[] args = new String[]{"--double:test:3.14159265"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals(3.14159265, (double)jclo.getValue("test"), 0.000001);
 }

 @Test
 public void testParseDoubleWithInvalidArguments() {
 String[] args = new String[]{"--double:test:Inf"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseLongWithValidArguments() {
 String[] args = new String[]{"--long:test:1"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals(1L, (long)jclo.getValue("test"));
 }

 @Test
 public void testParseLongWithInvalidArguments() {
 String[] args = new String[]{"--long:test:-1"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseStringWithValidArguments() {
 String[] args = new String[]{"--java.lang.String:test:Hello World"};
 jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals("Hello World", (String)jclo.getValue("test"));
 }

 @Test
 public void testParseStringWithInvalidArguments() {
 String[] args = new String[]{"--java.lang.String:test:<HTML>"};
 jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}