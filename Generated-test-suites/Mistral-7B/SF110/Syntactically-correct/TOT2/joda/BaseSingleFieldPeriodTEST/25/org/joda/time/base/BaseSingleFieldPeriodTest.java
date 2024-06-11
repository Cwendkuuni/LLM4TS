import org.joda.time.*;
import static org.junit.Assert.*; // for assert statements

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetweenMethod() {
        // Given
        ReadableInstant inst1 = new DateTime(2022, 1, 1, 0, 0, 0).toInstant();
        ReadableInstant inst2 = new DateTime(2022, 1, 2, 0, 0, 0).toInstant();
        DurationFieldType durationFieldType = DateTimeFields.MILLISECONDS;

        // When
        int result = BaseSingleFieldPeriod.between(inst1, inst2, durationFieldType);

        // Then
        int expected = 86400000; // 1 day in milliseconds
        assertEquals(expected, result);
    }

    // Edge case and exception handling scenarios will be added in next steps
}