import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        // Given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        writeableCommandLine.addOption(option);

        // Then
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        // Given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        writeableCommandLine.addValue(option, "value");

        // Then
        List values = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        // Given
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        writeableCommandLine.addSwitch(option, true);

        // Then
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        // Given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        writeableCommandLine.addOption(option);

        // Then
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddSwitch_AlreadySet() {
        // Given
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addSwitch(option, true);

        // When
        writeableCommandLine.addSwitch(option, true);

        // Then - Exception is thrown
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        // Given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property", "value");

        // When
        String property = writeableCommandLine.getProperty("property", "defaultValue");

        // Then
        Assert.assertEquals("value", property);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperties() {
        // Given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        // When
        Set properties = writeableCommandLine.getProperties();

        // Then
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        // Given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addValue(Option.builder("arg").build(), "value");

        // When
        String toString = writeableCommandLine.toString();

        // Then
        Assert.assertEquals("value", toString);
    }
}