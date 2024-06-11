import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BasePeriod;

public class BasePeriodTest {

  @Test
  public void testCreatePeriodWithIntArgs() {
    BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
    assertEquals(PeriodType.standard(), period.getPeriodType());
    int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    assertArrayEquals(expectedValues, period.getValues());
  }

  // Test other constructors similarly
}