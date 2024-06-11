package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
public class JCLOTest {

 private static final Object TEST_OBJECT = new Object() {
 };

 @Test
 public void testConstructorWithObject() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Assert.assertNotNull(jclo.object);
 Assert.assertNull(jclo.prefix);
 Assert.assertNull(jclo.aliases);
 Assert.assertEquals(TEST_OBJECT.getClass(), jclo.object.getClass());
 Assert.assertEquals(TEST_OBJECT.getClass().getDeclaredFields().length, jclo.fields.length);
 PowerMockito.verifyStatic(JCLO.class);
 JCLO jcloInstance = new JCLO(TEST_OBJECT);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][]{{"a1", "alias1"}, {"a2", "alias2"}};
 JCLO jclo = new JCLO(TEST_OBJECT, aliases);
 Assert.assertNotNull(jclo.object);
 Assert.assertNull(jclo.prefix);
 Assert.assertEquals(aliases, jclo.aliases);
 Assert.assertEquals(TEST_OBJECT.getClass(), jclo.object.getClass());
 Assert.assertEquals(TEST_OBJECT.getClass().getDeclaredFields().length, jclo.fields.length);
 PowerMockito.verifyStatic(JCLO.class);
 JCLO jcloInstance = new JCLO(TEST_OBJECT, aliases);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo = new JCLO(prefix, TEST_OBJECT);
 Assert.assertNull(jclo.object);
 Assert.assertEquals(prefix, jclo.prefix);
 Assert.assertNull(jclo.aliases);
 PowerMockito.verifyStatic(JCLO.class);
 JCLO jcloInstance = new JCLO(prefix, TEST_OBJECT);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][]{{"a1", "alias1"}, {"a2", "alias2"}};
 JCLO jclo = new JCLO(prefix, TEST_OBJECT, aliases);
 Assert.assertNull(jclo.object);
 Assert.assertEquals(prefix, jclo.prefix);
 Assert.assertEquals(aliases, jclo.aliases);
 PowerMockito.verifyStatic(JCLO.class);
 JCLO jcloInstance = new JCLO(prefix, TEST_OBJECT, aliases);
 }

 @Test
 public void testGetField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = jclo.getClass().getDeclaredField("fields");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "fields").thenReturn(field);
 Field actualField = jclo.getField("fields");
 Assert.assertEquals(field, actualField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNull() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Assert.assertNull(jclo.getField(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFieldWrongName() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Assert.assertThrows(IllegalArgumentException.class, () -> jclo.getField("wrongKey"));
 }

 @Test
 public void testGetValue() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = jclo.getClass().getDeclaredField("object");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "object").thenReturn(field);
 Object expectedObject = new Object();
 PowerMockito.when(field.get(jclo)).thenReturn(expectedObject);
 Object actualValue = jclo.getValue("object");
 Assert.assertEquals(expectedObject, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNull() {
 JCLO jclo = new JCLO(TEST_Object);
 Assert.assertThrows(NullPointerException.class, () -> jclo.getValue(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWrongName() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Assert.assertThrows(IllegalArgumentException.class, () -> jclo.getValue("wrongKey"));
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectIllegalAccess() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = jclo.getClass().getDeclaredField("object");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "object").thenReturn(field);
 PowerMockito.when(field.get(jclo)).thenThrow(new IllegalAccessException());
 Assert.assertThrows(IllegalAccessException.class, () -> jclo.getValue("object"));
 }

 @Test
 public void testToString() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field1 = jclo.getClass().getDeclaredField("object");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "object").thenReturn(field1);
 Object value1 = new Object();
 PowerMockito.when(field1.get(jclo)).thenReturn(value1);
 Field field2 = jclo.getClass().getDeclaredField("fields");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "fields").thenReturn(field2);
 Object fieldsValue = new Object[]{field1, field2};
 PowerMockito.when(field2.get(jclo)).thenReturn(fieldsValue);
 String expectedToString = "object: object = " + value1.toString() + "\nfields: fields = [\nfield1: object = " + value1.toString() + ",\nfield2: ";
 PowerMockito.when(field2.getType()).thenReturn(JCLO.class);
 PowerMockito.when(field2.getType().getComponentType()).thenReturn(Object[].class);
 PowerMockito.when(Arrays.toString((Object[])fieldsValue[0])).thenReturn("field1: object = " + value1.toString());
 String actualToString = jclo.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectIllegalAccess() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = jclo.getClass().getDeclaredField("object");
 PowerMockito.mockStatic(Field.class);
 PowerMockito.whenNew(Field.class).withArguments(jclo.getClass(), "object").thenReturn(field);
 Object valueToSet = new Object();
 PowerMockito.doThrow(new IllegalAccessException()).when(field).set(jclo, valueToSet);
 Assert.assertThrows(IllegalAccessException.class, () -> jclo.setValue("object", valueToSet));
 }

 @Test
 public void testParseWithValidArguments() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 String[] args = {"--doubleDashes", "--hasEquals", "--prefix=prefix_", "--field1=value1", "--field2=value2", "--additional=arg1,arg2"};
 jclo.parse(args);
 Assert.assertTrue(jclo.doubleDashes);
 Assert.assertTrue(jclo.hasEquals);
 Assert.assertEquals("prefix_", jclo.prefix);
 Field field1 = jclo.getField("field1");
 Assert.assertNotNull(field1);
 Object value1 = new Object();
 Assert.assertEquals(value1, jclo.getObject(field1));
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])jclo.getObject(jclo.getField("additional")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithWrongOption() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 String[] args = {"--wrongOption=value"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValue() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 String[] args = {"--prefix=prefix_", "--field1="};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingPrefix() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 String[] args = {"--field1=value1"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithBooleanField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("booleanField");
 PowerMockito.when(field.getType()).thenReturn(Boolean.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--booleanDashes", "--booleanField=true"};
 jclo.parse(args);
 Assert.assertEquals(true, jclo.getBoolean("booleanField"));
 }

 @Test
 public void testParseWithByteField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("byteField");
 PowerMockito.when(field.getType()).thenReturn(Byte.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--byteField=5"};
 jclo.parse(args);
 Assert.assertEquals((byte)5, jclo.getByte("byteField"));
 }

 @Test
 public void testParseWithCharField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("charField");
 PowerMockito.when(field.getType()).thenReturn(Character.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--charField=c"};
 jclo.parse(args);
 Assert.assertEquals('c', jclo.getChar("charField"));
 }

 @Test
 public void testParseWithShortField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("shortField");
 PowerMockito.when(field.getType()).thenReturn(Short.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--shortField=3"};
 jclo.parse(args);
 Assert.assertEquals((short)3, jclo.getShort("shortField"));
 }

 @Test
 public void testParseWithIntField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("intField");
 PowerMockito.when(field.getType()).thenReturn(Integer.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--intField=5"};
 jclo.parse(args);
 Assert.assertEquals(5, jclo.getInt("intField"));
 }

 @Test
 public void testParseWithFloatField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("floatField");
 PowerMockito.when(field.getType()).thenReturn(Float.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--floatField=3.5f"};
 jclo.parse(args);
 Assert.assertEquals(3.5f, jclo.getFloat("floatField"), 0.01f);
 }

 @Test
 public void testParseWithDoubleField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("doubleField");
 PowerMockito.when(field.getType()).thenReturn(Double.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--doubleField=3.5"};
 jclo.parse(args);
 Assert.assertEquals(3.5, jclo.getDouble("doubleField"), 0.01);
 }

 @Test
 public void testParseWithLongField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("longField");
 PowerMockito.when(field.getType()).thenReturn(Long.TYPE);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--longField=5L"};
 jclo.parse(args);
 Assert.assertEquals(5L, jclo.getLong("longField"));
 }

 @Test
 public void testParseWithStringField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("stringField");
 PowerMockito.when(field.getType()).thenReturn(String.class);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--stringField=testString"};
 jclo.parse(args);
 Assert.assertEquals("testString", jclo.getString("stringField"));
 }

 @Test
 public void testParseWithArrayField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("arrayField");
 PowerMockito.when(field.getType()).thenReturn(int[].class);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--arrayField=1,2,3"};
 jclo.parse(args);
 int[] expectedArray = {1, 2, 3};
 Assert.assertArrayEquals(expectedArray, (int[])jclo.getObject(field));
 }

 @Test
 public void testParseWithArrayListField() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 Field field = Mockito.mock(Field.class);
 PowerMockito.when(field.getName()).thenReturn("listField");
 PowerMockito.when(field.getType()).thenReturn(ArrayList.class);
 jclo.fields = new Field[]{field};
 String[] args = {"--doubleDashes", "--listField=[1,2,3]"};
 jclo.parse(args);
 List expectedList = Arrays.asList(1, 2, 3);
 Assert.assertEquals(expectedList, jclo.getObject(field));
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO(TEST_OBJECT);
 String expectedUsage = "--doubleDashes\n" +
         "--hasEquals\n" +
         "--prefix=prefix_\n" +
         "field1: boolean\n" +
         "field2: object\n" +
         "--additional:\n" +
         " arg1: [=boolean]\n" +
         " arg2: [=boolean]";
 String actualUsage = jclo.usage();
 Assert.assertEquals(expectedUsage, actualUsage);
 }
}