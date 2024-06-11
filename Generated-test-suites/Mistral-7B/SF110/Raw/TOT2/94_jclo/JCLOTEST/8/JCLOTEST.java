
package edu.mscd.cs.jclo;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class JCLOTest {

    @Test
    public void testDefaultConstructor() {
        JCLO jclo = new JCLO(null);
        assertNull(jclo.getObject());
        assertArrayNullSize(jclo.getAliases(), 0);
        assertFalse(jclo.hasEquals);
        assertNull(jclo.prefix);
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testConstructorWithObject() {
        String objStr = "Test Object";
        JCLO jclo = new JCLO(new Object(){});
        assertEquals(objStr, jclo.toString());
    }

    @Test
    public void testConstructorWithObjectAndAliases() {
        String objStr = "Test Object";
        String[][] aliases = {{"additional", "a"}};
        JCLO jclo = new JCLO(new Object(){}, aliases);
        assertEquals(objStr, jclo.toString());
        assertEquals(aliases, jclo.getAliases());
        assertTrue(jclo.hasEquals);
        assertNull(jclo.prefix);
        assertEquals(1, jclo.fields.length);
    }

    @Test
    public void testConstructorWithPrefixAndObject() {
        String prefix = "Test_";
        String objStr = "Test Object";
        JCLO jclo = new JCLO(prefix, new Object(){});
        assertEquals(prefix + objStr, jclo.toString());
    }

    @Test
    public void testGetValue() {
        String key = "testField";
        Object testObj = new Object(){};
        JCLO jclo = new JCLO(testObj);
        Field testField = testObj.getClass().getDeclaredField(key);
        testField.setAccessible(true);
        Object testValue = new Object(){};
        testField.set(testObj, testValue);
        Object result = jclo.getValue(key);
        assertEquals(testValue, result);
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object[]{});
        String expectedUsage = "--testField: int --testField2: long --additional: arg1 arg2\n";
        String result = jclo.usage();
        assertEquals(expectedUsage, result);
    }

    @Test
    public void testParseTypicalUseCases() {
        String[] args = {"--testField1: int=1", "--testField2: long=-123456789012345ll", "--additional: arg1 arg2"};
        JCLO jclo = new JCLO(new Object[]{});
        jclo.parse(args);
        Object intValue = jclo.getValue("testField1");
        assertEquals(1, intValue);
        Long longValue = (Long) jclo.getValue("testField2");
        assertEquals(-123456789012345L, longValue);
        String[] additional = (String[]) jclo.getValue("additional");
        assertArrayEquals(new String[]{"arg1", "arg2"}, additional);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEdgeCases() {
        String[] args = {"--testField: int=1 intValue", "--testField: boolean=true false", "--testField: float=1.1 foo"};
        JCLO jclo = new JCLO(new Object[]{});
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseExceptionScenarios() {
        String[] args = {"--testField: int=1=1", "--testField: int=1=true", "--testField: int=1=foo"};
        JCLO jclo = new JCLO(new Object[]{});
        jclo.parse(args);
    }
}
