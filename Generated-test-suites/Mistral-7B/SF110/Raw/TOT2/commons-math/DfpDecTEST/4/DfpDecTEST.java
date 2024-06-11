import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        Dfp d = new DfpDec(BigDecimal.ONE);
        DfpDec dfpDec = new DfpDec(d);
        assertEquals(d, dfpDec);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(BigDecimal.valueOf(1.5));
        int result = dfpDec.round(0);
        assertEquals(0, result);

        dfpDec = new DfpDec(BigDecimal.valueOf(2.5));
        result = dfpDec.round(0);
        assertEquals(0, result);

        dfpDec = new DfpDec(BigDecimal.valueOf(3.5));
        result = dfpDec.round(0);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundWithIllegalArgumentException() {
        DfpDec dfpDec = new DfpDec(BigDecimal.ZERO);
        dfpDec.round(-1);
    }

    @Test
    public void testRoundWithDifferentRoundingModes() {
        DfpDec dfpDec = new DfpDec(BigDecimal.valueOf(3.5));

        // Round down
        int resultDown = dfpDec.round(0);
        assertEquals(1, resultDown);

        // Round up
        int resultUp = dfpDec.round(1);
        assertEquals(2, resultUp);

        // Round half up
        int resultHalfUp = dfpDec.round(2);
        assertEquals(2, resultHalfUp);

        // Round half down
        int resultHalfDown = dfpDec.round(3);
        assertEquals(1, resultHalfDown);

        // Round half even
        int resultHalfEven = dfpDec.round(4);
        assertEquals(1, resultHalfEven);

        // Round half odd
        int resultHalfOdd = dfpDec.round(5);
        assertEquals(2, resultHalfOdd);

        // Round ceiling
        int resultCeil = dfpDec.round(6);
        assertEquals(2, resultCeil);

        // Round floor
        int resultFloor = dfpDec.round(7);
        assertEquals(1, resultFloor);
    }

    @Test
    public void testRoundWithNegativeNumbers() {
        DfpDec dfpDec = new DfpDec(BigDecimal.valueOf(-3.5));

        // Round down
        int resultDown = dfpDec.round(0);
        assertEquals(-4, resultDown);

        // Round up
        int resultUp = dfpDec.round(1);
        assertEquals(-3, resultUp);

        // Round half up
        int resultHalfUp = dfpDec.round(2);
        assertEquals(-3, resultHalfUp);

        // Round half down
        int resultHalfDown = dfpDec.round(3);
        assertEquals(-3, resultHalfDown);

        // Round half even
        int resultHalfEven = dfpDec.round(4);
        assertEquals(-3, resultHalfEven);

        // Round half odd
        int resultHalfOdd = dfpDec.round(5);
        assertEquals(-3, resultHalfOdd);

        // Round ceiling
        int resultCeil = dfpDec.round(6);
        assertEquals(-3, resultCeil);

        // Round floor
        int resultFloor = dfpDec.round(7);
        assertEquals(-4, resultFloor);
    }

    @Test
    public void testRoundWithLargeNumbers() {
        DfpDec dfpDec = new DfpDec(BigDecimal.valueOf(Double.MAX_VALUE));

        // Round down
        int resultDown = dfpDec.round(0);
        Assert.assertEquals(Integer.MAX_VALUE, resultDown);

        // Round up
        int resultUp = dfpDec.round(1);
        Assert.assertEquals(Integer.MAX_VALUE + 1, resultUp);

        // Round half up
        int resultHalfUp = dfpDec.round(2);
        Assert.assertEquals(Integer.MAX_VALUE + 1, resultHalfUp);

        // Round half down
        int resultHalfDown = dfpDec.round(3);
        Assert.assertEquals(Integer.MAX_VALUE, resultHalfDown);

        // Round half even
        int resultHalfEven = dfpDec.round(4);
        Assert.assertEquals(Integer.MAX_VALUE, resultHalfEven);

        // Round half odd
        int resultHalfOdd = dfpDec.round(5);
        Assert.assertEquals(Integer.MAX_VALUE + 1, resultHalfOdd);

        // Round ceiling
        int resultCeil = dfpDec.round(6);
        Assert.assertEquals(Integer.MAX_VALUE + 1, resultCeil);

        // Round floor
        int resultFloor = dfpDec.round(7);
        Assert.assertEquals(Integer.MAX_VALUE, resultFloor);
    }
}