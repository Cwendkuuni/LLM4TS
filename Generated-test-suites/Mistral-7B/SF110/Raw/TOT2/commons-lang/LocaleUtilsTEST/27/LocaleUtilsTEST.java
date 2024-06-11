import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        String str = "en_US";
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale(str);
        assertEquals(expected, actual);
    }
}