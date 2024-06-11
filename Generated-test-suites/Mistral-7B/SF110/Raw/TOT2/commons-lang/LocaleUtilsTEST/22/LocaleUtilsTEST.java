import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr_CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("de_DE"), LocaleUtils.toLocale("de_DE"));
    }
}