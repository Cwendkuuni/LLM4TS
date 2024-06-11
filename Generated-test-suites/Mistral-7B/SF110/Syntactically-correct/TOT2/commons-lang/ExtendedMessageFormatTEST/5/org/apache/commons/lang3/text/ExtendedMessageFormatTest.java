import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testApplyPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat("dd-MM-yyyy"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("Hello {0}, your birthday is {1}", Locale.US, registry);
        fmt.applyPattern("Hello {1}, your birthday is {0}");
        String expectedPattern = "Hello {1}, your birthday is {0}";
        Assert.assertEquals(expectedPattern, fmt.toPattern());
    }
}