package org.apache.commons.lang3.text;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, messageFormat.toPattern());
    }

    // Edge cases and exception handling test cases will be added here.
}