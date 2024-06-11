
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

    @Test
    public void testJCLOWithObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.getValue("fieldName"));
    }

    @Test
    public void testJCLOWithObjectAndAliases() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"fieldName1", "alias1"}, {"fieldName2", "alias2"}});
        assertNotNull(jclo.getValue("fieldName1"));
        assertNotNull(jclo.getValue("alias1"));
        assertNotNull(jclo.getValue("fieldName2"));
        assertNotNull(jclo.getValue("alias2"));
    }

    @Test
    public void testJCLOWithPrefix() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo.getValue("prefix.fieldName"));
    }

    @Test
    public void testJCLOWithPrefixObjectAndAliases() {
        JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"fieldName1", "alias1"}, {"fieldName2", "alias2"}});
        assertNotNull(jclo.getValue("prefix.fieldName1"));
        assertNotNull(jclo.getValue("prefix.alias1"));
        assertNotNull(jclo.getValue("prefix.fieldName2"));
        assertNotNull(jclo.getValue("prefix.alias2"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullObject() {
        new JCLO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldWithNonExistentField() {
        JCLO jclo = new JCLO(new Object());
        Field nonExistentField = jclo.getClass().getDeclaredField("nonExistentField");
        nonExistentField.setAccessible(true);
        assertNull(jclo.getField("nonExistentField"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldWithInvalidAlias() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"fieldName1", "alias1"}, {"fieldName2", "alias2"}});
        assertNull(jclo.getField("invalidAlias"));
    }

    @Test(expected = IllegalAccessException.class)
    public void testAccessingPrivateField() {
        JCLO jclo = new JCLO(new Object());
        Field privateField = JCLO.class.getDeclaredField("object");
        privateField.setAccessible(true);
        try {
            privateField.set(jclo, new Object());
            fail("IllegalAccessException expected but not thrown.");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetterValue() {
        JCLO jclo = new JCLO(new Object(), null);
        try {
            jclo.setObject(jclo.getField("fieldName"), new Integer(Integer.MAX_VALUE + 1));
            fail("IllegalArgumentException expected but not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetObjectWithNullField() {
        JCLO jclo = new JCLO(new Object(), null);
        Field nullField = null;
        try {
            jclo.setObject(nullField, new Object());
            fail("IllegalArgumentException expected but not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetObjectWithInvalidValueType() {
        JCLO jclo = new JCLO(new Object(), null);
        try {
            jclo.setObject(jclo.getField("fieldName"), new String("invalidValue"));
            fail("IllegalArgumentException expected but not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}