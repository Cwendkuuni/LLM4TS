import org.junit.Test;
import static org.junit.Assert.*;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setup() {
 testObject = new Object() {
 private int field1 = 1;
 private String field2 = "test";
 private boolean field3 = true;
 private int[] field4 = {1, 2, 3};
 };
 jclo = new JCLO(testObject);
 }

 @Test
 public void testGetValueString() {
 assertEquals(1, jclo.getValue("field1").intValue());
 assertEquals("test", jclo.getValue("field2"));
 assertTrue(jclo.getValue("field3").booleanValue());
 assertArrayEquals(new int[]{1, 2, 3}, jclo.getValue("field4"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueUnknownField() {
 jclo.getValue("unknownField");
 }

 @Test
 public void testSetValueStringInt() {
 jclo.setValue("field1", 2);
 assertEquals(2, jclo.getValue("field1").intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueUnknownField() {
 jclo.setValue("unknownField", 2);
 }

 @Test
 public void testSetValueStringBoolean() {
 jclo.setValue("field3", false);
 assertFalse(jclo.getValue("field3").booleanValue());
 }

 @Test
 public void testSetValueStringArrayInt() {
 int[] expected = {1, 2, 3, 4};
 jclo.setValue("field4", expected);
 assertArrayEquals(expected, jclo.getValue("field4"));
 }

 @Test
 public void testUsage() {
 String output = jclo.usage();
 String expected = "-field1: int\n" +
 "-field2: java.lang.String\n" +
 "-field3: boolean\n" +
 "-field4: int...\n";
 assertEquals(expected, output);
 }

 @Test
 public void testParse() {
 String[] args = {"--doubleDashes", "--field1=2", "--field2=test", "--field3=false", "--field4=1,2,3"};
 jclo.parse(args);
 assertEquals(2, jclo.getInteger("field1"));
 assertEquals("test", jclo.getString("field2"));
 assertFalse(jclo.getBoolean("field3"));
 assertArrayEquals(new int[]{1, 2, 3}, jclo.getInts("field4"));
 }
}