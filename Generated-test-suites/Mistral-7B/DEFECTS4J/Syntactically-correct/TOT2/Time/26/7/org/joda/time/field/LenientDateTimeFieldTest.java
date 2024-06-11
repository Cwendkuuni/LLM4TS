import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1, Chronology.UTC), Chronology.UTC);
        assertTrue(lenientDateTimeField.isLenient());
    }
}