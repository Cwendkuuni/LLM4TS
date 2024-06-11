import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_ValidLocale() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    // Add more test cases for other scenarios
}