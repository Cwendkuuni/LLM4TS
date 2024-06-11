import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithNullOptArgument() {
        try {
            new Option(null, "description");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptionConstructorWithEmptyOptArgument() {
        Option opt = new Option("", "description");
        assertNull(opt.getOpt());
        assertEquals("description", opt.getLongOpt());
        assertFalse(opt.hasArg());
        assertFalse(opt.hasOptionalArg());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.isRequired());
        assertNull(opt.getArgName());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasArgs());
        assertNotNull(opt.getType());
        assertEquals(0, opt.getArgs());
        assertTrue(opt.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithValidArguments() {
        Option opt = new Option("opt", "description", false, "argName");
        assertEquals("opt", opt.getOpt());
        assertNull(opt.getLongOpt());
        assertFalse(opt.hasArg());
        assertFalse(opt.hasOptionalArg());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.isRequired());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasArgs());
        assertNotNull(opt.getType());
        assertEquals(0, opt.getArgs());
        assertTrue(opt.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithLongOptArgument() {
        Option opt = new Option("opt", "longOptArgument", false, "argName");
        assertEquals("opt", opt.getOpt());
        assertEquals("longOptArgument", opt.getLongOpt());
        assertFalse(opt.hasArg());
        assertFalse(opt.hasOptionalArg());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.isRequired());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasArgs());
        assertNotNull(opt.getType());
        assertEquals(0, opt.getArgs());
        assertTrue(opt.hasNoValues());
    }

    @Test
    public void testOptionConstructorWithHasArgArgument() {
        Option opt = new Option("opt", "description", true, "argName");
        assertEquals("opt", opt.getOpt());
        assertNull(opt.getLongOpt());
        assertTrue(opt.hasArg());
        assertFalse(opt.hasOptionalArg());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.isRequired());
        assertEquals("argName", opt.getArgName());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasArgs());
        assertNotNull(opt.getType());
        assertEquals(1, opt.getArgs());
        assertTrue(opt.hasNoValues());
    }

    @Test
    public void testOptionIdGetters() {
        Option opt = new Option("opt", "description", false, "argName");
        assertEquals('o', opt.getId());
        opt = new Option("longOptArgument", "description", false, "argName");
        assertEquals('l', opt.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidArgument() {
        new Option("", "", true, "");
    }

    @Test
    public void testProcessValueMethod() {
        Option opt = new Option("opt", "description", false, "argName");
        opt.setValueSeparator('+');
        opt.addValueForProcessing("value1+value2+value3");
        List values = opt.getValuesList();
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingMethodWithNoArgsAllowed() {
        Option opt = new Option("opt", "description", -1, null);
        opt.addValueForProcessing("value");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueMethodWithIndexOutOfBounds() {
        Option opt = new Option("opt", "description", 1, null);
        opt.addValueForProcessing("value1");
        opt.addValueForProcessing("value2");
        try {
            opt.getValue(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
}