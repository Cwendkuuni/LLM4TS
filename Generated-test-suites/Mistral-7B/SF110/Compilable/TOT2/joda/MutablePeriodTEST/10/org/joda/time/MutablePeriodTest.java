package org.joda.time;
import org.junit.Test;
import org.joda.time.MutablePeriod;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String input = "P1Y2M3D";
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 0, 0, 0, 0, 0);
        MutablePeriod actual = MutablePeriod.parse(input);
        assertEquals(expected, actual);
    }
}