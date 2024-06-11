import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.Partial;

public class PartialTest {

    @Test
    public void testSize() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());

        partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(1, partial.size());

        partial = new Partial(DateTimeFieldType.dayOfYear(), 365);
        assertEquals(1, partial.size());

        partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 31});
        assertEquals(3, partial.size());

        partial = new Partial(new Partial(DateTimeFieldType.year(), 2022), new int[]{DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{1, 31});
        assertEquals(3, partial.size());
    }
}