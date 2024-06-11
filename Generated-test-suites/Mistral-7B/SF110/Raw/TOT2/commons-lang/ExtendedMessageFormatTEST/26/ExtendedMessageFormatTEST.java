import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        assertEquals("", extendedMessageFormat.toPattern());
        assertNull(extendedMessageFormat.registry);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}");
        assertEquals("{}", extendedMessageFormat.toPattern());
        assertNull(extendedMessageFormat.registry);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}", Locale.US);
        assertEquals("{}", extendedMessageFormat.toPattern());
        assertNull(extendedMessageFormat.registry);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}", new HashMap<>());
        assertEquals("{}", extendedMessageFormat.toPattern());
        assertNotNull(extendedMessageFormat.registry);
    }

    @Test
    public void testConstructorWithPatternAndRegistryAndLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}", new HashMap<>(), Locale.US);
        assertEquals("{}", extendedMessageFormat.toPattern());
        assertNotNull(extendedMessageFormat.registry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1,choice,0#January|1#February|2#March|3#April|4#May|5#June|6#July|7#August|8#September|9#October|10#November|11#December}");
        assertEquals("{0,date} {1,choice,0#January|1#February|2#March|3#April|4#May|5#June|6#July|7#August|8#September|9#October|10#November|11#December}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{0,date} {1,choice,0#January|1#February|2#March|3#April|4#May|5#June|6#July|7#August|8#September|9#October|10#November|11#December}");
        String format = extendedMessageFormat.toPattern();
        Object[] args = new Object[] {"2023-03-25", "Spring"};
        String result = extendedMessageFormat.format(args);
        assertEquals("2023-03-25 Spring", result);
        assertEquals("{0,date} {1}", format);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}");
        extendedMessageFormat.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}");
        extendedMessageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}");
        MessageFormat formatter = new MessageFormat("{}");
        extendedMessageFormat.setFormats(formatter.getFormats());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("{}");
        MessageFormat[] formatter = new MessageFormat[1];
        extendedMessageFormat.setFormatsByArgumentIndex(formatter);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{}");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{}");
        assertTrue(extendedMessageFormat1.equals(extendedMessageFormat2));

        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{0}");
        assertFalse(extendedMessageFormat1.equals(extendedMessageFormat3));

        ExtendedMessageFormat extendedMessageFormat4 = new ExtendedMessageFormat("{0}", new HashMap<>());
        assertFalse(extendedMessageFormat1.equals(extendedMessageFormat4));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("{}");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("{}");
        assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat2.hashCode());

        ExtendedMessageFormat extendedMessageFormat3 = new ExtendedMessageFormat("{0}");
        assertNotEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat3.hashCode());

        ExtendedMessageFormat extendedMessageFormat4 = new ExtendedMessageFormat("{0}", new HashMap<>());
        assertNotEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat4.hashCode());
    }
}