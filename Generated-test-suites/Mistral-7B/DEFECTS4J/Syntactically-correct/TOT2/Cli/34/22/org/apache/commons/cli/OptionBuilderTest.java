import org.apache.commons.cli.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionName");
        Option option = optionBuilder.create();
        assertNotNull(option);
        assertEquals("optionName", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithUnsetLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        fail("create should have thrown an IllegalArgumentException");
    }

    @Test
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        Option option = null;
        try {
            option = optionBuilder.create();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(option);
    }

    @Test
    public void testCreateWithSpecialCharacterLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option#Name");
        Option option = null;
        try {
            option = optionBuilder.create();
        } catch (IllegalArgumentException e) {
            fail("create should not have thrown an IllegalArgumentException");
        }
        assertNotNull(option);
        assertEquals("option#Name", option.getLongOpt());
    }
}