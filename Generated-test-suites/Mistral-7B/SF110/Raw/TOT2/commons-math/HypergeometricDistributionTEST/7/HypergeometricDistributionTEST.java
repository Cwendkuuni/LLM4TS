import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        try {
            new HypergeometricDistribution(-1, 1, 1);
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // expected NotPositiveException
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(1, -1, 1);
        } catch (NotStrictlyPositiveException | NumberIsTooLargeException | NotPositiveException e) {
            // expected NotPositiveException
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(1, 2, 1);
        } catch (NotPositiveException | NumberIsTooLargeException e) {
            // expected NumberIsTooLargeException
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(1, 1, 2);
        } catch (NotPositiveException | NumberIsTooLargeException e) {
            // expected NumberIsTooLargeException
        }
    }

    @Test
    public void testConstructorWithValidArguments() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(10, distribution.getPopulationSize());
        Assert.assertEquals(5, distribution.getNumberOfSuccesses());
        Assert.assertEquals(5, distribution.getSampleSize());
    }
}