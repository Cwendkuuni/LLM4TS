import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testGetValue() {
 final String key = "field1";
 final Object expectedValue = new Object();
 final Field field = new Field() {{
 name = key;
 type = Object.class;
 }};
 jclo.setObject(field, expectedValue);
 final Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueWithNullKey() {
 jclo.getValue(null);
 }

 @Test
 public void testGetValueWithNonExistentField() {
 final String key = "nonExistentField";
 jclo.getValue(key);
 }

 @Test
 public void testSetValue() {
 final String key = "field1";
 final Object expectedValue = new Object();
 jclo.setValue(key, expectedValue);
 final Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNullKey() {
 jclo.setValue(null, new Object());
 }

 @Test
 public void testSetValueWithNonExistentField() {
 jclo.setValue("nonExistentField", new Object());
 }

 @Test
 public void testToString() {
 final String prefix = "prefix";
 final Object obj = new Object();
 final String[][] aliases = new String[][] {{ "alias1", "alias2" }};
 final JCLO jclo = new JCLO(prefix, obj, aliases);
 final Field field1 = new Field() {{
 name = "field1";
 type = String.class;
 }};
 jclo.setObject(field1, "value1");
 final Field field2 = new Field() {{
 name = "field2";
 type = int[].class;
 }};
 jclo.setObject(field2, new int[] { 1, 2, 3 });
 final String expected = prefix + "\nfield1: java.lang.String = value1\nfield2: int[] = [1, 2, 3]\n";
 final String actual = jclo.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() {
 final Field inaccessibleField = new Field() {{
 name = "inaccessibleField";
 access = Field.MODIFIERS; // make it inaccessible
 type = Object.class;
 }};
 jclo.setObject(inaccessibleField, new Object());
 jclo.getValue(inaccessibleField.getName());
 }

 @Test
 public void testUsage() {
 final JCLO jclo = new JCLO(new Object());
 final String expected = "\n--prefix: String\n--field1: java.lang.String\n--field2: int\n--field3: boolean...\n";
 final String actual = jclo.usage();
 assertEquals(expected, actual);
 }

 @Test
 public void testParse() {
 final String[] args = new String[] { "--prefix=prefix", "--field1=value1" };
 final JCLO jclo = new JCLO(null, null);
 jclo.parse(args);
 final String prefix = jclo.getPrefix();
 final String field1 = jclo.get("field1");
 assertEquals("prefix", prefix);
 assertEquals("value1", field1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidOption() {
 final String[] args = new String[] { "--prefix=prefix", "--invalidOption=value" };
 final JCLO jclo = new JCLO(null, null);
 jclo.parse(args);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseWithTooFewArguments() {
 final String[] args = new String[] { "--prefix=prefix" };
 final JCLO jclo = new JCLO(null, null);
 jclo.parse(args);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseWithTooManyArguments() {
 final String[] args = new String[] { "--prefix=prefix", "--field1=value1", "extraArgument" };
 final JCLO jclo = new JCLO(null, null);
 jclo.parse(args);
 }
}