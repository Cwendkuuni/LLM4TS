package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JCLOTest {

    private JCLO jclo;
    private Object testObject;

    @Before
    public void setUp() {
        testObject = new Object() {
            private String name;
            private int age;
            private boolean isMarried;
            private String[] hobbies;
        };
        jclo = new JCLO(testObject);
    }

    @Test
    public void testJCLOConstructorWithObject() {
        JCLO jclo = new JCLO(testObject);
        Assert.assertNotNull(jclo.fields);
        Assert.assertEquals(testObject.getClass(), jclo.object.getClass());
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        String[][] aliases = {{"name", "n"}, {"age", "a"}, {"isMarried", "m"}};
        JCLO jclo = new JCLO(testObject, aliases);
        Assert.assertNotNull(jclo.fields);
        Assert.assertEquals(testObject.getClass(), jclo.object.getClass());
        Assert.assertEquals(aliases, jclo.aliases);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        String prefix = "test_";
        JCLO jclo = new JCLO(prefix, testObject);
        Assert.assertEquals("", jclo.prefix);
        Assert.assertEquals(testObject.getClass(), jclo.object.getClass());
        Assert.assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        String prefix = "test_";
        String[][] aliases = {{"name", "n"}, {"age", "a"}, {"isMarried", "m"}};
        JCLO jclo = new JCLO(prefix, testObject, aliases);
        Assert.assertEquals(prefix, jclo.prefix);
        Assert.assertEquals(testObject.getClass(), jclo.object.getClass());
        Assert.assertEquals(aliases, jclo.aliases);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new JCLO(null);
    }

    @Test
    public void testGetValueWithValidKey() {
        Field nameField = Reflections.getField(testObject.getClass(), "name");
        String name = "John Doe";
        Reflections.setFieldValue(testObject, nameField, name);
        String value = jclo.getValue("name");
        Assert.assertEquals(name, value);
    }

    @Test
    public void testGetValueWithInvalidKey() {
        Assert.assertNull(jclo.getValue("invalidKey"));
    }

    @Test
    public void testGetValueWithArrayKey() {
        Field hobbiesField = Reflections.getField(testObject.getClass(), "hobbies");
        String[] hobbies = {"swimming", "reading"};
        Reflections.setFieldValue(testObject, hobbiesField, hobbies);
        String[] hobbiesValue = (String[]) jclo.getValue("hobbies");
        Assert.assertArrayEquals(hobbies, hobbiesValue);
    }

    @Test
    public void testGetValueWithNestedArrayKey() {
        Field hobbiesField = Reflections.getField(testObject.getClass(), "hobbies");
        String[] hobbies = {"swimming", "reading"};
        Reflections.setFieldValue(testObject, hobbiesField, hobbies);
        String[][] nestedHobbies = {{"morning", "morningSwimming"}, {"evening", "eveningReading"}};
        Reflections.setFieldValue(testObject, Reflections.getField(testObject.getClass(), "nestedHobbies"), nestedHobbies);
        String[][] nestedHobbiesValue = (String[][]) jclo.getValue("nestedHobbies[0]");
        Assert.assertEquals("morning", nestedHobbiesValue[0][0]);
        Assert.assertEquals("morningSwimming", nestedHobbiesValue[0][1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithNegativeIndex() {
        jclo.getValue(-1);
    }

    @Test
    public void testSetValueWithValidKeyAndValue() {
        String newName = "Jane Doe";
        jclo.setValue("name", newName);
        Field nameField = Reflections.getField(testObject.getClass(), "name");
        String name = (String) Reflections.getFieldValue(testObject, nameField);
        Assert.assertEquals(newName, name);
    }

    @Test
    public void testSetValueWithInvalidKey() {
        jclo.setValue("invalidKey", "test");
    }

    @Test
    public void testSetValueWithArrayKeyAndValue() {
        Object newHobby = new Object() {};
        jclo.setValue("hobbies[0]", newHobby);
        Object hobby = (Object) jclo.getValue("hobbies[0]");
        Assert.assertSame(newHobby, hobby);
    }

    @Test
    public void testSetValueWithNestedArrayKeyAndValue() {
        Object newNestedHobby = new Object() {};
        jclo.setValue("nestedHobbies[0][0]", newNestedHobby);
        Object nestedHobby = (Object) jclo.getValue("nestedHobbies[0][0]");
        Assert.assertSame(newNestedHobby, nestedHobby);
    }

    @Test
    public void testUsage() {
        String expectedUsage = "--name=Name\n" +
                "--age=Age\n" +
                "--isMarried=Boolean\n" +
                "--hobbies=Hobby...\n" +
                "--nestedHobbies[0]=NestedHobby[Morning=MorningHobby, Evening=EveningHobby]\n";
        String usage = jclo.usage();
        Assert.assertEquals(expectedUsage, usage);
    }

    @Test
    public void testParseWithValidArguments() {
        String[] arguments = {"--name=John Doe", "--age=30", "--isMarried=true", "--hobbies=swimming,reading", "--nestedHobbies[0][0]=Morning,MorningSwimming", "--nestedHobbies[0][1]=Evening,EveningReading"};
        jclo.parse(arguments);
        Field nameField = Reflections.getField(testObject.getClass(), "name");
        String name = (String) Reflections.getFieldValue(testObject, nameField);
        Assert.assertEquals("John Doe", name);
        Field ageField = Reflections.getField(testObject.getClass(), "age");
        int age = (int) Reflections.getFieldValue(testObject, ageField);
        Assert.assertEquals(30, age);
        Field isMarriedField = Reflections.getField(testObject.getClass(), "isMarried");
        boolean isMarried = (boolean) Reflections.getFieldValue(testObject, isMarriedField);
        Assert.assertTrue(isMarried);
        Field hobbiesField = Reflections.getField(testObject.getClass(), "hobbies");
        String[] hobbies = (String[]) Reflections.getFieldValue(testObject, hobbiesField);
        Assert.assertArrayEquals(new String[] {"swimming", "reading"}, hobbies);
        Field nestedHobbiesField = Reflections.getField(testObject.getClass(), "nestedHobbies");
        Object[][] nestedHobbies = (Object[][]) Reflections.getFieldValue(testObject, nestedHobbiesField);
        Assert.assertEquals(2, nestedHobbies.length);
        for (int i = 0; i < nestedHobbies.length; i++) {
            Object[] nestedHobby = nestedHobbies[i];
            String morning = (String) Reflections.getFieldValue(nestedHobby[0], "Morning");
            String morningHobby = (String) Reflections.getFieldValue(nestedHobby[1], "MorningHobby");
            Assert.assertEquals("Morning", morning);
            Assert.assertEquals("MorningSwimming", morningHobby);
            String evening = (String) Reflections.getFieldValue(nestedHobby[0], "Evening");
            String eveningHobby = (String) Reflections.getFieldValue(nestedHobby[1], "EveningHobby");
            Assert.assertEquals("Evening", evening);
            Assert.assertEquals("EveningReading", eveningHobby);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArguments() {
        String[] arguments = {"--name=John=Doe", "--age=30", "--isMarried=true", "--hobbies=swimming,reading,--", "--nestedHobbies[0][0]=Morning,MorningSwimming", "--nestedHobbies[0][1]=Evening,EveningReading--"};
        jclo.parse(arguments);
    }

    @Test
    public void testParseWithBooleanArguments() {
        String[] arguments = {"--isMarried=true", "--isMarried=false"};
        jclo.parse(arguments);
        Field isMarriedField = Reflections.getField(testObject.getClass(), "isMarried");
        boolean isMarried1 = (boolean) Reflections.getFieldValue(testObject, isMarriedField);
        Assert.assertTrue(isMarried1);
        Reflections.setFieldValue(testObject, isMarriedField, false);
        boolean isMarried2 = (boolean) Reflections.getFieldValue(testObject, isMarriedField);
        Assert.assertFalse(isMarried2);
    }

    @Test
    public void testParseWithNestedArrayArguments() {
        String[] arguments = {"--nestedHobbies[0][0]=Morning,MorningSwimming", "--nestedHobbies[0][1]=Evening,EveningReading"};
        jclo.parse(arguments);
        Field nestedHobbiesField = Reflections.getField(testObject.getClass(), "nestedHobbies");
        Object[][] nestedHobbies = (Object[][]) Reflections.getFieldValue(testObject, nestedHobbiesField);
        Assert.assertEquals(2, nestedHobbies.length);
        for (int i = 0; i < nestedHobbies.length; i++) {
            Object[] nestedHobby = nestedHobbies[i];
            String morning = (String) Reflections.getFieldValue(nestedHobby[0], "Morning");
            String morningHobby = (String) Reflections.getFieldValue(nestedHobby[1], "MorningHobby");
            Assert.assertEquals("Morning", morning);
            Assert.assertEquals("MorningSwimming", morningHobby);
            String evening = (String) Reflections.getFieldValue(nestedHobby[0], "Evening");
            String eveningHobby = (String) Reflections.getFieldValue(nestedHobby[1], "EveningHobby");
            Assert.assertEquals("Evening", evening);
            Assert.assertEquals("EveningReading", eveningHobby);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidKeyAndValue() {
        String[] arguments = {"--name=John Doe=", "--age=-1", "--isMarried=hello"};
        jclo.parse(arguments);
    }

    @Test
    public void testToString() {
        Field nameField = Reflections.getField(testObject.getClass(), "name");
        String name = "John Doe";
        Reflections.setFieldValue(testObject, nameField, name);
        Field ageField = Reflections.getField(testObject.getClass(), "age");
        int age = 30;
        Reflections.setFieldValue(testObject, ageField, age);
        Field isMarriedField = Reflections.getField(testObject.getClass(), "isMarried");
        boolean isMarried = true;
        Reflections.setFieldValue(testObject, isMarriedField, isMarried);
        Field hobbiesField = Reflections.getField(testObject.getClass(), "hobbies");
        String[] hobbies = {"swimming", "reading"};
        Reflections.setFieldValue(testObject, hobbiesField, hobbies);
        Field nestedHobbiesField = Reflections.getField(testObject.getClass(), "nestedHobbies");
        Object[][] nestedHobbies = {{"Morning", new Object() {
            String Morning = "Morning";
            String MorningHobby = "MorningSwimming";
        }}, {"Evening", new Object() {
            String Evening = "Evening";
            String EveningHobby = "EveningReading";
        }}};
        Reflections.setFieldValue(testObject, nestedHobbiesField, nestedHobbies);
        String expectedToString = "[name=John Doe, age=30, isMarried=true, hobbies=[swimming, reading], nestedHobbies=[[Morning=Morning, MorningHobby=MorningSwimming], [Evening=Evening, EveningHobby=EveningReading]]]";
        String toString = jclo.toString();
        Assert.assertEquals(expectedToString, toString);
    }

}

class Reflections {

    public static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object obj, Field field, Object newValue) {
        try {
            field.setAccessible(true);
            field.set(obj, newValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}