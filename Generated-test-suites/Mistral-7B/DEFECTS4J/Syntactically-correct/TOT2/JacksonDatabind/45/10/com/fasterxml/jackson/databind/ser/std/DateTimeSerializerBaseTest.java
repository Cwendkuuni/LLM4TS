import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Class<String> stringType = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, useTimestamp, customFormat);
        assertNotNull(serializer);
    }
}