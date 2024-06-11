package org.apache.commons.math3.distribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistributionConstructorValidArguments() {
        try {
            new HypergeometricDistribution(5, 2, 3);
            assertTrue(true);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertTrue(false);
        }
    }
}