package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);
        Assert.assertNotNull(jclo.object);
        Assert.assertNull(jclo.prefix);
        Assert.assertNull(jclo.aliases);
        Assert.assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        Object testObject = new TestObject();
        String[][] aliases = new String[][]{{"alias1", "alias2"}, {"alias3", "alias4"}};
        JCLO jclo = new JCLO(testObject, aliases);
        Assert.assertNotNull(jclo.object);
        Assert.assertNotNull(jclo.aliases);
        Assert.assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        Object testObject = new TestObject();
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, testObject);
        Assert.assertNotNull(jclo.prefix);
        Assert.assertEquals(prefix, jclo.prefix);
        Assert.assertNull(jclo.aliases);
        Assert.assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        Object testObject = new TestObject();
        String prefix = "prefix_";
        String[][] aliases = new String[][]{{"alias1", "alias2"}, {"alias3", "alias4"}};
        JCLO jclo = new JCLO(prefix, testObject, aliases);
        Assert.assertNotNull(jclo.prefix);
        Assert.assertEquals(prefix, jclo.prefix);
        Assert.assertNotNull(jclo.aliases);
        Assert.assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
    }

    @Test
    public void testGetObject() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        Field field = testObject.getClass().getDeclaredField("field1");
        Object fieldValue = jclo.getObject(field);

        Assert.assertEquals(fieldValue, field.get(testObject));
    }

    @Test
    public void testGetField() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        Field field = jclo.getField("field1");
        Assert.assertNotNull(field);
        Assert.assertEquals(testObject.getClass().getDeclaredField("field1"), field);

        Field nonExistentField = jclo.getField("nonExistentField");
        Assert.assertNull(nonExistentField);
    }

    @Test
    public void testGetValue() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        Field field = testObject.getClass().getDeclaredField("field1");
        Object fieldValue = jclo.getValue("field1");

        Assert.assertEquals(fieldValue, field.get(testObject));

        Field nonExistentField = testObject.getClass().getDeclaredField("nonExistentField");
        Assert.assertNull(jclo.getValue("nonExistentField"));
    }

    @Test
    public void testSetObject() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        Field field = testObject.getClass().getDeclaredField("field1");
        Object newValue = new Integer(123);

        jclo.setObject(field, newValue);
        Assert.assertEquals(newValue, field.get(testObject));
    }

    @Test
    public void testToString() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("class edu.mscd.cs.jclo.TestObject {\n");
        expectedToString.append("\tfield1: int = 0;\n");
        expectedToString.append("\tfield2: byte = 0;\n");
        expectedToString.append("\tfield3: short = 0;\n");
        expectedToString.append("\tfield4: int = 0;\n");
        expectedToString.append("\tfield5: float = 0.0;\n");
        expectedToString.append("\tfield6: double = 0.0;\n");
        expectedToString.append("\tfield7: long = 0;\n");
        expectedToString.append("\tfield8: String = null;\n");
        expectedToString.append("\tfield9: char = '\u0000';\n");
        expectedToString.append("}\n");

        String actualToString = jclo.toString();
        Assert.assertEquals(expectedToString.toString(), actualToString);
    }

    @Test
    public void testUsage() {
        Object testObject = new TestObject();
        JCLO jclo = new JCLO(testObject);

        String expectedUsage = "-field1=int --field2=byte --field3=short --field4=int --field5=float --field6=double --field7=long --field8=String --field9=char\n";
        String actualUsage = jclo.usage();
        Assert.assertEquals(expectedUsage, actualUsage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArgument() {
        Object testObject = new TestObject();
        String[] args = {"--nonExistentOption"};
        new JCLO(testObject).parse(args);
    }

    @Test
    public void testParseWithBoolean() {
        Object testObject = new TestObject();
        String[] args = {"--boolean_flag=true"};
        new JCLO(testObject).parse(args);
        Assert.assertTrue(testObject.getBoolean("boolean_flag"));
    }

    @Test
    public void testParseWithByte() {
        Object testObject = new TestObject();
        String[] args = {"--byte_flag=10"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals((byte)10, testObject.getByte("byte_flag"));
    }

    @Test
    public void testParseWithChar() {
        Object testObject = new TestObject();
        String[] args = {"--char_flag=A"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals('A', testObject.getChar("char_flag"));
    }

    @Test
    public void testParseWithShort() {
        Object testObject = new TestObject();
        String[] args = {"--short_flag=20"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals((short)20, testObject.getShort("short_flag"));
    }

    @Test
    public void testParseWithInt() {
        Object testObject = new TestObject();
        String[] args = {"--int_flag=30"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals(30, testObject.getInt("int_flag"));
    }

    @Test
    public void testParseWithFloat() {
        Object testObject = new TestObject();
        String[] args = {"--float_flag=30.5"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals(30.5f, testObject.getFloat("float_flag"), 0.01f);
    }

    @Test
    public void testParseWithDouble() {
        Object testObject = new TestObject();
        String[] args = {"--double_flag=30.5"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals(30.5, testObject.getDouble("double_flag"), 0.01);
    }

    @Test
    public void testParseWithLong() {
        Object testObject = new TestObject();
        String[] args = {"--long_flag=30L"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals(30L, testObject.getLong("long_flag"));
    }

    @Test
    public void testParseWithString() {
        Object testObject = new TestObject();
        String[] args = {"--string_flag=test"};
        new JCLO(testObject).parse(args);
        Assert.assertEquals("test", testObject.getString("string_flag"));
    }

    @Test
    public void testParseWithArrayOfByte() {
        Object testObject = new ArrayOfByteTestObject();
        String[] args = {"--array_of_byte_flag=1,2,3,4,5"};
        new JCLO(testObject).parse(args);
        byte[] expectedArray = {1, 2, 3, 4, 5};
        byte[] actualArray = testObject.getBytes("array_of_byte_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testParseWithArrayOfChar() {
        Object testObject = new ArrayOfCharTestObject();
        String[] args = {"--array_of_char_flag=A,B,C,D,E"};
        new JCLO(testObject).parse(args);
        char[] expectedArray = {'A', 'B', 'C', 'D', 'E'};
        char[] actualArray = testObject.getChars("array_of_char_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testParseWithArrayOfShort() {
        Object testObject = new ArrayOfShortTestObject();
        String[] args = {"--array_of_short_flag=10,20,30,40,50"};
        new JCLO(testObject).parse(args);
        short[] expectedArray = {10, 20, 30, 40, 50};
        short[] actualArray = testObject.getShorts("array_of_short_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testParseWithArrayOfInt() {
        Object testObject = new ArrayOfIntTestObject();
        String[] args = {"--array_of_int_flag=10,20,30,40,50"};
        new JCLO(testObject).parse(args);
        int[] expectedArray = {10, 20, 30, 40, 50};
        int[] actualArray = testObject.getInts("array_of_int_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testParseWithArrayOfFloat() {
        Object testObject = new ArrayOfFloatTestObject();
        String[] args = {"--array_of_float_flag=10.5,20.5,30.5,40.5,50.5"};
        new JCLO(testObject).parse(args);
        float[] expectedArray = {10.5f, 20.5f, 30.5f, 40.5f, 50.5f};
        float[] actualArray = testObject.getFloats("array_of_float_flag");
        Assert.assertArrayEquals(expectedArray, actualArray, 0.01f);
    }

    @Test
    public void testParseWithArrayOfDouble() {
        Object testObject = new ArrayOfDoubleTestObject();
        String[] args = {"--array_of_double_flag=10.5,20.5,30.5,40.5,50.5"};
        new JCLO(testObject).parse(args);
        double[] expectedArray = {10.5, 20.5, 30.5, 40.5, 50.5};
        double[] actualArray = testObject.getDoubles("array_of_double_flag");
        Assert.assertArrayEquals(expectedArray, actualArray, 0.01);
    }

    @Test
    public void testParseWithArrayOfLong() {
        Object testObject = new ArrayOfLongTestObject();
        String[] args = {"--array_of_long_flag=10L,20L,30L,40L,50L"};
        new JCLO(testObject).parse(args);
        long[] expectedArray = {10L, 20L, 30L, 40L, 50L};
        long[] actualArray = testObject.getLongs("array_of_long_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testParseWithArrayOfString() {
        Object testObject = new ArrayOfStringTestObject();
        String[] args = {"--array_of_string_flag=test1,test2,test3,test4,test5"};
        new JCLO(testObject).parse(args);
        String[] expectedArray = {"test1", "test2", "test3", "test4", "test5"};
        String[] actualArray = testObject.getStrings("array_of_string_flag");
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    private static class TestObject {
        int field1;
        byte field2;
        short field3;
        int field4;
        float field5;
        double field6;
        long field7;
        String field8;
        char field9;
    }

    private static class ArrayOfByteTestObject {
        byte[] array_of_byte_flag;
    }

    private static class ArrayOfCharTestObject {
        char[] array_of_char_flag;
    }

    private static class ArrayOfShortTestObject {
        short[] array_of_short_flag;
    }

    private static class ArrayOfIntTestObject {
        int[] array_of_int_flag;
    }

    private static class ArrayOfFloatTestObject {
        float[] array_of_float_flag;
    }

    private static class ArrayOfDoubleTestObject {
        double[] array_of_double_flag;
    }

    private static class ArrayOfLongTestObject {
        long[] array_of_long_flag;
    }

    private static class ArrayOfStringTestObject {
        String[] array_of_string_flag;
    }
}