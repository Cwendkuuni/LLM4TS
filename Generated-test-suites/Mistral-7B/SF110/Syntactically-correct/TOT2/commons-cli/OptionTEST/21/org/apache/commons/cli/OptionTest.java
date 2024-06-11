import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() {
        Option option = new Option("-a", " Option A description");
        assertEquals("-a", option.getOpt());
        assertEquals(" Option A description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithNullOpt() {
        new Option(null, " Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithEmptyOpt() {
        new Option("", " Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() {
        new Option("&a", " Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithNullDescription() {
        new Option("option", null);
    }
}