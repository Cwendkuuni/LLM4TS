import org.junit.Test;
import org.junit.Assert;
import edu.mscd.cs.jclo.JCLO;
import java.util.Arrays;
import java.lang.reflect.Field;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() throws IllegalAccessException, InstantiationException {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(testObject);
 Assert.assertNotNull(jclo1);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][] {{ "a", "aliasA" }, { "b", "aliasB" }};
 JCLO jclo2 = new JCLO(testObject, aliases);
 Assert.assertNotNull(jclo2);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, testObject);
 Assert.assertEquals(prefix, jclo3.getPrefix());
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][] {{ "a", "aliasA" }, { "b", "aliasB" }};
 JCLO jclo4 = new JCLO(prefix, testObject, aliases);
 Assert.assertEquals(prefix, jclo4.getPrefix());
 Assert.assertArrayEquals(aliases, jclo4.getAliases());
 }

 @Test
 public void testGetObject() throws IllegalAccessException {
 Field field = testObject.getClass().getDeclaredField("field1");
 field.setAccessible(true);
 Object value = field.get(testObject);
 Object valueFromJclo = jclo.getValue("field1");
 Assert.assertEquals(value, valueFromJclo);
 }

 @Test
 public void testGetField() {
 Field field = jclo.getField("field1");
 Assert.assertNotNull(field);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNullKey() {
 jclo.getValue(null);
 }

 @Test
 public void testToString() {
 String expected = "class TestClass:\nfield1 = value1\nfield2 = value2\n";
 String actual = jclo.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testUsage() {
 String expected = "--prefix_field1: boolean\n" +
 "--prefix_field2: java.lang.String[]\n";
 String actual = jclo.usage();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseAdditional() {
 String[] args = new String[] { "--additional=value1", "--additional=value2" };
 jclo.parse(args);
 Object[] expectedAdditional = new Object[] { "value1", "value2" };
 Object[] actualAdditional = (Object[]) jclo.getValue("additional");
 Assert.assertArrayEquals(expectedAdditional, actualAdditional);
 }

 @Test
 public void testParse() {
 String[] args = new String[] { "--doubleDashes", "--hasEquals", "--prefix_field1=value1", "--prefix_field2=value2" };
 jclo.parse(args);
 Assert.assertTrue(jclo.isDoubleDashes());
 Assert.assertTrue(jclo.isHasEquals());
 Object value1 = jclo.getValue("field1");
 Object value2 = jclo.getValue("field2");
 Assert.assertEquals("value1", value1.toString());
 Assert.assertEquals("value2", value2.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 String[] args = new String[] { "--invalidOption=value" };
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 String[] args = new String[] { "--prefix_field1" };
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidValueType() {
 String[] args = new String[] { "--prefix_field1=invalidType" };
 jclo.parse(args);
 }

 @Test
 public void testGetters() {
 Assert.assertTrue(jclo.isDoubleDashes());
 Assert.assertTrue(jclo.isHasEquals());
 Assert.assertEquals("", jclo.getPrefix());
 Assert.assertArrayEquals(new String[0], jclo.getAliases());
 Assert.assertNull(jclo.getValue("nonExistentField"));
 Assert.assertTrue(jclo.getBoolean("field1"));
 Assert.assertEquals((byte) 1, jclo.getByte("field2"));
 Assert.assertEquals((char) 'a', jclo.getChar("field3"));
 Assert.assertEquals((short) 1, jclo.getShort("field4"));
 Assert.assertEquals(1, jclo.getInt("field5"));
 Assert.assertEquals(1.0f, jclo.getFloat("field6"), 0.001f);
 Assert.assertEquals(1.0d, jclo.getDouble("field7"), 0.001d);
 Assert.assertEquals(1L, jclo.getLong("field8"));
 Assert.assertEquals("testString", jclo.getString("field9"));
 Assert.assertArrayEquals(new byte[] { 1, 2, 3 }, jclo.getBytes("field10"));
 Assert.assertArrayEquals(new char[] { 'a', 'b', 'c' }, jclo.getChars("field11"));
 Assert.assertArrayEquals(new short[] { 1, 2, 3 }, jclo.getShorts("field12"));
 Assert.assertArrayEquals(new int[] { 1, 2, 3 }, jclo.getInts("field13"));
 Assert.assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, jclo.getFloats("field14"));
 Assert.assertArrayEquals(new double[] { 1.0d, 2.0d, 3.0d }, jclo.getDoubles("field15"));
 Assert.assertArrayEquals(new long[] { 1L, 2L, 3L }, jclo.getLongs("field16"));
 Assert.assertArrayEquals(new String[] { "testString1", "testString2" }, jclo.getStrings("field17"));
 }

}

class TestClass {
 private boolean field1;
 private byte field2;
 private char field3;
 private short field4;
 private int field5;
 private float field6;
 private double field7;
 private long field8;
 private String field9;
 private byte[] field10;
 private char[] field11;
 private short[] field12;
 private int[] field13;
 private float[] field14;
 private double[] field15;
 private long[] field16;
 private String[] field17;
}