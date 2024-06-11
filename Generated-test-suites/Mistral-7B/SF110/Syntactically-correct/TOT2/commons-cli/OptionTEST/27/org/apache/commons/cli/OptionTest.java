
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {
    
    @Test
    public void testOptionWithValidArguments() {
        Option option = new Option("-o", "Option description");
        assertEquals("-o", option.getOpt());
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testOptionWithValidArgumentsAndHasArgFlag() {
        Option option = new Option("-o", false, "Option description");
        assertEquals("-o", option.getOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    public void testOptionWithHasArgFlagAndOneArgument() {
        Option option = new Option("-o", true, "Option description");
        assertEquals("-o", option.getOpt());
        assertEquals("Option description", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCharacter() {
        try {
            new Option('%', "Description");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription() {
        try {
            new Option("", "Empty description");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWhenListIsFull() {
        Option option = new Option("-o", 1, "Option description");
        String[] values = {"Value1", "Value2", "Value3"};

        for (String value : values) {
            option.addValueForProcessing(value);
        }

        try {
            option.addValueForProcessing("Value4");
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testGetValueFromEmptyList() {
        Option option = new Option("-o", 0, "Option description");
        assertEquals(null, option.getValue());
    }

    @Test
    public void testGetValueFromListWithOneValue() {
        Option option = new Option("-o", 1, "Option description");
        option.addValueForProcessing("Value1");
        assertEquals("Value1", option.getValue());
    }
}