import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("optionName", "description");
        assertNotNull(option);
        assertEquals("optionName", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionNullOpt() {
        new Option(null, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionNullDescription() {
        new Option("optionName", null);
    }

    @Test
    public void testOptionConstructionEmptyOpt() {
        Option option = new Option("", "description");
        assertNotNull(option);
        assertEquals("", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionEmptyDescription() {
        new Option("optionName", "");
    }

    @Test
    public void testOptionConstructionDuplicateOpt() {
        Option option1 = new Option("optName", "description1");
        Option option2 = new Option("optName", "description2");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testOptionGetters() {
        Option option = new Option("optionName", "description");
        assertEquals("optionName", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionSetters() {
        Option option = new Option("optionName", "description");
        option.setLongOpt("longOptName");
        assertEquals("longOptName", option.getLongOpt());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        option.setDescription("updatedDescription");
        assertEquals("updatedDescription", option.getDescription());
        option.setRequired(true);
        assertTrue(option.isRequired());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        option.setArgs(5);
        assertEquals(5, option.getArgs());
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
    }
}