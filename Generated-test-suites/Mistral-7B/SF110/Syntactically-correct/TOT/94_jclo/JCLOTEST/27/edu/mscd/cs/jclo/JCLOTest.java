import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testGetterAndSetter() {
 // Test setting a value and getting it back
 jclo.setObject(jclo.getField("field1"), "value1");
 assertEquals("value1", jclo.getValue("field1"));

 // Test getting a non-existent field
 assertNull(jclo.getValue("nonExistentField"));

 // Test setting an array field
 Object[] array = {1, 2, 3};
 jclo.setObject(jclo.getField("arrayField"), array);
 assertArrayEquals(array, (Object[]) jclo.getValue("arrayField"));

 // Test setting an array field with an additional argument
 jclo.parse("-arrayField=--1,2,3");
 Object[] newArray = {4, 5, 6};
 jclo.setObject(jclo.getField("arrayField"), newArray);
 assertArrayEquals(newArray, (Object[]) jclo.getValue("arrayField"));
 assertArrayEquals(new Object[3][], (Object[]) jclo.getValue("arrayField").clone());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 jclo.parse("-invalidOption=value");
 }

 @Test
 public void testGetUsage() {
 assertEquals(jclo.usage(), "boolean[]: field1 = \nshort: field2 = \nint: field3 = \nfloat: field4 = \ndouble: field5 = \nlong: field6 = \njava.lang.String: field7 = \nchar: field8 = \n");
 }

 @Test
 public void testParse() {
 String[] args = {"--doubleDashes", "--hasEquals=value1", "--field1=value2", "--arrayField=--1,2,3"};
 jclo = new JCLO(null, new Object(), null);
 jclo.parse(args);

 assertEquals(true, jclo.getBoolean("doubleDashes"));
 assertEquals("value1", jclo.getValue("hasEquals"));
 Object[] array = (Object[]) jclo.getValue("arrayField");
 assertArrayEquals(new Object[]{1, 2, 3}, array);
 }
}