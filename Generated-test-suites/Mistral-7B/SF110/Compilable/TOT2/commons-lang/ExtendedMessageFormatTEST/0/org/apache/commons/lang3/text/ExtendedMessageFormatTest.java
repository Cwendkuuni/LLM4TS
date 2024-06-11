package org.apache.commons.lang3.text;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatWithStringPattern() {
        // Given
        String pattern = "Hello {0}, your age is {1}.";

        // When
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        // Then
        assertEquals(pattern, format.toPattern());
    }
}