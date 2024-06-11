import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();

        Assert.same(fastDateFormat1, fastDateFormat2);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Assert.assertNotNull(fastDateFormat);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetInstanceWithFormatCacheNotInitialized() {
        FormatCache<FastDateFormat> cache = null;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidPattern() {
        FormatCache<FastDateFormat> cache = new FormatCache<FastDateFormat>() {
            @Override
            protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
                throw new IllegalArgumentException("Invalid pattern");
            }
        };
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
    }

    // Add other test cases for other methods here
}