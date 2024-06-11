package org.apache.commons.lang3.text;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat_String() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        String formattedMessage = messageFormat.format(new Object[]{"John Doe", 30});
        Assert.assertEquals("Hello John Doe, your age is 30.", formattedMessage);
    }
}