import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Assert.assertEquals("", format.toPattern());
        format.applyPattern(pattern);
        MessageFormat mf = format.getFormats();
        Assert.assertEquals(2, mf.getNumberOfArguments());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        format.applyPattern(pattern);
        MessageFormat mf = format.getFormats();
        Assert.assertEquals(2, mf.getNumberOfArguments());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "Hello {0,choice,0#Mr.|1#Ms.|1#Mrs.|1#Miss} {1}, your age is {2,number,integer}. ";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory choiceFactory = new ChoiceFormatFactory();
        registry.put("choice", choiceFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
        format.applyPattern(pattern);
        MessageFormat mf = format.getFormats();
        Assert.assertEquals(3, mf.getNumberOfArguments());
    }

    @Test
    public void testConstructorsWithNullPattern() {
        try {
            new ExtendedMessageFormat(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ExtendedMessageFormat(null, Locale.US);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ExtendedMessageFormat(null, null, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyPatternWithNullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        try {
            format.applyPattern(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0)");
        try {
            format.applyPattern(format.toPattern());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for the methods: toPattern(), getFormat(), setFormat(), setFormatByArgumentIndex(), setFormats(), and setFormatsByArgumentIndex().
}