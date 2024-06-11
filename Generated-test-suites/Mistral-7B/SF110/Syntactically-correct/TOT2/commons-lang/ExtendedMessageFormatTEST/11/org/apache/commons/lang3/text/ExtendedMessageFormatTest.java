import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertNotNull(format);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", null, new HashMap<>());
        assertNotNull(format);
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternLocaleAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US, new HashMap<>());
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        String pattern = format.toPattern();
        assertEquals("{0}, {1}", pattern);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        String pattern = "Hello {0}, this is {1}";
        format.applyPattern(pattern);
        String formatted = format.format(new Object[]{"John", "Doe"});
        assertEquals("Hello John, this is Doe", formatted);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormats(new Format[]{new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("MM/dd/yyyy")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("MM/dd/yyyy")});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        assertEquals(format1.hashCode(), format2.hashCode());
    }
}