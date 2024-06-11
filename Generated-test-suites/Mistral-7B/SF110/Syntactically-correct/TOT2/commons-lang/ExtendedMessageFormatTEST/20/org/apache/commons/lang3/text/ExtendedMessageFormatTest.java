import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatDefaultConstructor() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat();
        String pattern = messageFormat.toPattern();
        assertTrue(pattern.isEmpty());
    }
}