import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import java.text.MessageFormatException;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatArguments, Locale locale) {
                return new MessageFormat(formatName, locale);
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
            put("MessageFormat", factory);
        }});
        assertEquals("{0}, {1}", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertTrue(format.registry != null);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatArguments, Locale locale) {
                return new MessageFormat(formatName, locale);
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
            put("MessageFormat", factory);
        }}, "{MessageFormat, date, short}", new Object[]{"{0}", "{1}"});
        assertEquals("{MessageFormat, date, short},{0}, {1}", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertTrue(format.registry != null);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());

        format = new ExtendedMessageFormat("{MessageFormat, date, short},{0}, {1}", new HashMap<String, FormatFactory>() {{
            put("MessageFormat", new FormatFactory() {
                @Override
                public Format getFormat(String formatName, String formatArguments, Locale locale) {
                    return new MessageFormat(formatName, locale, new String[]{"date", "short"});
                }
            });
        }}, new Object[]{"{0}", "{1}"});
        assertEquals("{MessageFormat, date, short},{0}, {1}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormat(0, new MessageFormat("{0}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatByArgumentIndex(0, new MessageFormat("{0}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormats(new MessageFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        format.setFormatsByArgumentIndex(new MessageFormat[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {2}");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format5 = format1;

        assertTrue(new EqualsBuilder()
                .append(format1, format2)
                .isEquals());

        assertFalse(new EqualsBuilder()
                .append(format1, format3)
                .isEquals());

        assertFalse(new EqualsBuilder()
                .append(format1, format4)
                .isEquals());

        assertTrue(new EqualsBuilder()
                .append(format1, format5)
                .isEquals());
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {2}");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}, {1}");
        ExtendedMessageFormat format5 = format1;

        int hashcode1 = new HashCodeBuilder()
                .append(format1.getClass())
                .append(format1.registry)
                .append(format1.toPattern)
                .toHashCode();

        int hashcode2 = new HashCodeBuilder()
                .append(format2.getClass())
                .append(format2.registry)
                .append(format2.toPattern)
                .toHashCode();

        int hashcode3 = new HashCodeBuilder()
                .append(format3.getClass())
                .append(format3.registry)
                .append(format3.toPattern)
                .toHashCode();

        int hashcode4 = new HashCodeBuilder()
                .append(format4.getClass())
                .append(format4.registry)
                .append(format4.toPattern)
                .toHashCode();

        int hashcode5 = new HashCodeBuilder()
                .append(format5.getClass())
                .append(format5.registry)
                .append(format5.toPattern)
                .toHashCode();

        assertEquals(hashcode1, hashcode5);
        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
            put("MessageFormat", new FormatFactory() {
                @Override
                public Format getFormat(String formatName, String formatArguments, Locale locale) {
                    throw new IllegalArgumentException();
                }
            });
        }}, new Object[]{"{0}", "{1}"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
            put("MessageFormat", new FormatFactory() {
                @Override
                public Format getFormat(String formatName, String formatArguments, Locale locale) {
                    return new MessageFormat(formatName, locale);
                }
            });
        }}, new Object[]{"{0}", "{1}", "invalidIndex"});
    }
}