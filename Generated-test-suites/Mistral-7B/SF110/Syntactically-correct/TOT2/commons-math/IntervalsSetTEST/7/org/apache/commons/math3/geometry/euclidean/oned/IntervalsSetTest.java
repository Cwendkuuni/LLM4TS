import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {
    @Test
    public void testEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        // Basic test
        assertTrue(intervalsSet.isEmpty());
        // Edge case and exception handling
        assertNull(intervalsSet.getInf());
        assertNull(intervalsSet.getSup());
        assertFalse(intervalsSet.contains(0.0));
    }
}