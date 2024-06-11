
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstance_NullPattern() {
        assertThrows(NullPointerException.class, () -> FastDateFormat.getInstance(null));
    }

    @Test
    public void testGetInstance_EmptyPattern() {
        assertThrows(IllegalArgumentException.class, () -> FastDateFormat.getInstance(""));
    }

    @Test
    public void testGetInstance_InvalidPattern() {
        assertThrows(IllegalArgumentException.class, () -> FastDateFormat.getInstance("Xxxx"));
    }

    @Test
    public void testGetInstance_NullTimeZone() {
        assertThrows(NullPointerException.class, () -> FastDateFormat.getInstance("dd/MM/yyyy", null));
    }

    @Test
    public void testGetInstance_NullLocale() {
        assertThrows(NullPointerException.class, () -> FastDateFormat.getInstance("dd/MM/yyyy", null, null));
    }

    @Test
    public void testGetInstance_NullCache() {
        FormatCache<FastDateFormat> mockCache = mock(FormatCache.class);
        when(mockCache.getInstance()).thenThrow(new RuntimeException("MockException"));
        try {
            FastDateFormat.cache = mockCache;
            FastDateFormat fastDateFormat = FastDateFormat.getInstance();
            fail("Expected MockException");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    // Add test cases for remaining methods here

    static {
        FastDateFormat.cache = new FormatCache<FastDateFormat>() {
            @Override
            protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
                return new FastDateFormat(pattern, timeZone, locale);
            }
        };
    }
}
