import static org.junit.Assert.*; // for assertions
import edu.mscd.cs.jclo.*; // for JCLO class
import java.lang.reflect.*; // for AccessibleObject and Field
import java.util.Arrays; // for Arrays.toString()

public class JCLOTest {

    @Test
    public void testConstructorWithObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.object);
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(jclo.fields.length, getDeclaredFields(jclo).length);
    }

    @Test
    public void testConstructorWithObjectAndAliases() {
        String[][] aliases = new String[][]{{"alias1", "alias2"}, {"alias3", "alias4"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertNotNull(jclo.object);
        assertNotNull(jclo.aliases);
        assertEquals(jclo.aliases.length, 2);
    }

    @Test
    public void testConstructorWithPrefixAndObject() {
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, new Object());
        assertNotNull(jclo.object);
        assertEquals(jclo.prefix, prefix);
        assertNull(jclo.aliases);
        assertEquals(jclo.fields.length, getDeclaredFields(jclo).length);
    }

    @Test
    public void testConstructorWithPrefixObjectAndAliases() {
        String prefix = "prefix";
        String[][] aliases = new String[][]{{"alias1", "alias2"}, {"alias3", "alias4"}};
        JCLO jclo = new JCLO(prefix, new Object(), aliases);
        assertNotNull(jclo.object);
        assertEquals(jclo.prefix, prefix);
        assertNotNull(jclo.aliases);
        assertEquals(jclo.aliases.length, 2);
        assertEquals(jclo.fields.length, getDeclaredFields(jclo).length);
    }

    @Test
    public void testGetValueForExistingField() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        Field field = jclo.getClass().getDeclaredField("fieldName"); // replace 'fieldName' with the actual name of the field
        jclo.setFieldAccessible(field);
        Object expectedValue = new Object();
        field.set(jclo, expectedValue);
        Object actualValue = jclo.getValue("prefix_fieldName");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValueForNonExistingField() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        assertNull(jclo.getValue("nonExistingField"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueForPrefixWithNonExistingField() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        jclo.getValue("nonExistingField");
    }

    @Test
    public void testSetValueForExistingField() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        Field field = jclo.getClass().getDeclaredField("fieldName"); // replace 'fieldName' with the actual name of the field
        jclo.setFieldAccessible(field);
        Object originalValue = field.get(jclo);
        Object newValue = new Object();
        jclo.setValue("prefix_fieldName", newValue);
        assertEquals(newValue, field.get(jclo));
        assertEquals(newValue, jclo.getValue("prefix_fieldName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueForNonExistingField() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        jclo.setValue("nonExistingField", new Object());
    }

    @Test
    public void testGetUsageTypeForBooleanType() {
        JCLO jclo = new JCLO(null, null, null);
        String result = jclo.getUsageType(Boolean.TYPE);
        assertEquals("[=boolean]", result);
    }

    @Test
    public void testGetUsageTypeForArrayType() {
        JCLO jclo = new JCLO(null, null, null);
        String result = jclo.getUsageType(int[].class);
        assertEquals("int...", result);
    }

    @Test
    public void testGetUsageTypeForStringType() {
        JCLO jclo = new JCLO(null, null, null);
        String result = jclo.getUsageType(String.class);
        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUsageTypeForInvalidType() {
        JCLO jclo = new JCLO(null, null, null);
        jclo.getUsageType(int.class);
    }

    @Test
    public void testToStringMethod() {
        JCLO jclo = new JCLO("prefix", new Object(), null);
        Field field = jclo.getClass().getDeclaredField("fieldName"); // replace 'fieldName' with the actual name of the field
        jclo.setFieldAccessible(field);
        Object expectedValue = new Object();
        field.set(jclo, expectedValue);

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("int[]: ");
        expectedOutput.append("prefix_fieldName = ");
        expectedOutput.append("[");
        expectedOutput.append("expectedValue");
        expectedOutput.append("]");

        String actualOutput = jclo.toString();
        assertEquals(expectedOutput.toString(), actualOutput);
    }

    @Test
    public void testParseMethod() {
        String[] args = new String[]{"--double-dashes", "--prefix=prefix", "--prefix_fieldName=value", "--prefix_fieldName2=value2", "--equals=equalsField=true"};
        JCLO jclo = new JCLO("prefix", new Object(), null);
        jclo.parse(args);

        Field prefixField = jclo.getClass().getDeclaredField("prefix");
        Field fieldNameField = jclo.getClass().getDeclaredField("fieldName");
        Field equalsField = jclo.getClass().getDeclaredField("equalsField");
        jclo.setFieldAccessible(prefixField);
        jclo.setFieldAccessible(fieldNameField);
        jclo.setFieldAccessible(equalsField);

        String expectedPrefix = "prefix";
        String expectedValue1 = "value";
        String expectedValue2 = "value2";
        boolean expectedEquals = true;

        assertEquals(expectedPrefix, prefixField.get(jclo));
        assertEquals(expectedValue1, fieldNameField.get(jclo));
        assertEquals(expectedValue2, jclo.getValue("prefix_fieldName2"));
        assertEquals(expectedEquals, jclo.getBoolean("equalsField"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMethodWithInvalidArgument() {
        String[] args = new String[]{"--prefix_fieldName=value", "--prefix=prefix", "--nonExistingOption"};
        JCLO jclo = new JCLO("prefix", new Object(), null);
        jclo.parse(args);
    }

    private Field[] getDeclaredFields(Object obj) {
        try {
            return obj.getClass().getDeclaredFields();
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldAccessible(Field field) {
        try {
            field.setAccessible(true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}