package org.apache.commons.lang3.text;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedMessageFormatTest {
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        String expectedPattern = "{{0}}, {{1}}";
        String actualPattern = format.toPattern();
        assertEquals(expectedPattern, actualPattern);
    }
}