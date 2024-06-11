import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the ContinuedFraction abstract class.
 */
public class ContinuedFractionTest {

    /**
     * Test for the evaluate method with positive arguments.
     */
    @Test
    public void testEvaluatePositive() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double actual = continuedFraction.evaluate(1.0);
        double expected = 3.0;
        Assert.assertEquals(expected, actual, 1e-9);
    }

    /**
     * Test for the evaluate method with negative arguments.
     */
    @Test
    public void testEvaluateNegative() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double actual = continuedFraction.evaluate(-1.0);
        double expected = 2.0;
        Assert.assertEquals(expected, actual, 1e-9);
    }

    /**
     * Test for the evaluate method with zero arguments.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateZero() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(0.0);
    }

    /**
     * Test for the evaluate method with epsilon smaller than the default value.
     */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double actual = continuedFraction.evaluate(1.0, 1e-12);
        double expected = 3.0;
        Assert.assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test for the evaluate method with maxIterations smaller than the default value.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateSmallMaxIterations() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(1.0, 10);
    }

    /**
     * Test for the evaluate method with infinite arguments.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinite() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test for the evaluate method with NaN arguments.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaN() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(Double.NaN);
    }

}