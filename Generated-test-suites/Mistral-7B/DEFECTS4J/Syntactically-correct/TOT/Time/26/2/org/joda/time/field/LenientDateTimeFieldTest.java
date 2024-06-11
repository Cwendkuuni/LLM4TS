import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

 private static final Chronology ISO_INSTANT = ISOChronology.INSTANCE;

 @Test
 public void testLenientDateTimeField() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfYear(), ISO_INSTANT);
 MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, ISO_INSTANT);
 int value = 37; // out of bounds for Jan 1, 2000

 // Test setting a value out of bounds
 long newInstant = lenientField.set(dateTime.toLong(), value);
 assertEquals("Expected Feb 2, 2000 as result of setting a value out of bounds", new BaseDateTime(newInstant, ISO_INSTANT).toString("yyyy-MM-dd"), "Feb 2, 2000");

 // Test getting the value after setting it out of bounds
 assertEquals("Expected 37 as value after setting out of bounds", lenientField.get(newInstant), value);

 // Test setting a value within bounds
 value = 31;
 lenientField.set(dateTime.toLong(), value);
 assertEquals("Expected Jan 31, 2000 as result of setting a value within bounds", new BaseDateTime(dateTime.toLong(), ISO_INSTANT).toString("yyyy-MM-dd"), "Jan 31, 2000");

 // Test getting the value within bounds
 assertEquals("Expected 31 as value within bounds", lenientField.get(dateTime.toLong()), value);
 }
}