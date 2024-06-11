
package org.junit.tests.fastdateformat;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    // Test case 1: getInstance()
    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    // Test case 2: getInstance(String pattern)
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(fastDateFormat);
    }

    // Test case 3: getDateInstance(int style)
    @Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    // Test case 4: format(Date date)
    @Test
    public void testFormatDate() {
        Date date = new Date();
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
    }

    // Edge case 1: empty pattern
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithEmptyPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("");
    }

    // Edge case 2: null pattern
    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(null);
    }

    // Edge case 3: invalid style
    @Test(expected = IllegalArgumentException.class)
    public void testGetDateInstanceWithInvalidStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(-1);
    }

    // Edge case 4: null style
    @Test(expected = NullPointerException.class)
    public void testGetDateInstanceWithNullStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(null);
    }

    // Edge case 5: null date
    @Test(expected = NullPointerException.class)
    public void testFormatDateWithNullDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String formattedDate = fastDateFormat.format(null);
    }

    // Edge case 6: date from the future
    @Test
    public void testFormatFutureDate() {
        Date futureDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30); // 30 days in the future
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String formattedDate = fastDateFormat.format(futureDate);
        assertTrue(formattedDate.contains("30 days")); // or another way to check for the date being formatted correctly
    }

    // Exception handling: invalid pattern
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-zzz");
    }
}