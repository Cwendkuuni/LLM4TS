package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals("Default ranking strategy", NaturalRanking.DEFAULT, mannWhitneyUTest.getNaturalRanking());
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testConstructorWithCustomStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.REMOVE, TiesStrategy.DENSE);
        assertEquals("Custom ranking strategy for NaN's and ties",
                NaNStrategy.REMOVE, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals("Custom ranking strategy for ties",
                TiesStrategy.DENSE, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        double expected = 10.5;
        double actual = mannWhitneyUTest.mannWhitneyU(a, b);
        assertEquals("Mann-Whitney U statistic", expected, actual, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException,
            MaxCountExceededException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        double expected = 0.015;
        double actual = mannWhitneyUTest.mannWhitneyUTest(a, b);
        assertEquals("Asymptotic p-value", expected, actual, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithNaN() throws ConvergenceException,
            MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] a = {1.0, 2.0, Double.NaN};
        double[] b = {4.0, 5.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(a, b);
            fail("Expected NullArgumentException for NaN in x");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithEmptyArrays() throws ConvergenceException,
            MaxCountExceededException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] a = {};
        double[] b = {};
        try {
            mannWhitneyUTest.mannWhitneyUTest(a, b);
            fail("Expected NoDataException for empty arrays");
        } catch (NoDataException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithNullArrays() throws ConvergenceException,
            MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
            fail("Expected NullArgumentException for null x");
        } catch (NullArgumentException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
            fail("Expected NullArgumentException for null y");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithDifferentLengths() throws ConvergenceException,
            MaxCountExceededException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0, 7.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(a, b);
            fail("Expected IllegalArgumentException for different lengths");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithSameElements() throws ConvergenceException,
            MaxCountExceededException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {3.0, 2.0, 1.0};
        double expected = 0.05;
        double actual = mannWhitneyUTest.mannWhitneyUTest(a, b);
        assertEquals("Asymptotic p-value", expected, actual, 0.001);
    }
}