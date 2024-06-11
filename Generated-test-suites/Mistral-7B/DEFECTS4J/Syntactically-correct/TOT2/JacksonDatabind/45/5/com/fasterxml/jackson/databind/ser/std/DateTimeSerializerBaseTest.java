import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<Date> dateType = Date.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, useTimestamp, customFormat);

        assertEquals(dateType, serializer.getType());
        assertFalse(serializer.useTimestamp());
        assertEquals(customFormat, serializer.getCustomFormat());
    }
}