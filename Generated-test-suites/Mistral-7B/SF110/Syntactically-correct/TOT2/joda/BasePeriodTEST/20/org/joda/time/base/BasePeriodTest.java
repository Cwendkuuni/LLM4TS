import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructor() {
        // Test default constructor
        BasePeriod period = new BasePeriod();
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);

        // Test constructor with specific PeriodType and Chronology
        PeriodType customPeriodType = PeriodType.days();
        Chronology customChronology = ISOChronology.getInstanceUTC();
        BasePeriod customPeriod = new BasePeriod(0, customPeriodType, customChronology);
        PeriodType customResultPeriodType = customPeriod.getPeriodType();
        assertEquals(customPeriodType, customResultPeriodType);
    }
}