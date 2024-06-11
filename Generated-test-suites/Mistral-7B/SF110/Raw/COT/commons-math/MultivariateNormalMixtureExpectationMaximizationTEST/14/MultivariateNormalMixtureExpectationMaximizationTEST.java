package org.junit.tests.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.deistribution.*;
import java.util.*;

@RunWith(JUnit4.class)
public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization mnmem;
    private double[][] testData;
    private MixtureMultivariateNormalDistribution initialMixture;

    @Before
    public void setUp() throws Exception {
        testData = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(2))),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(2)))
        ));
    }

    

    // Test constructor
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeLengthData() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{-1.0, {1.0, 2.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorMismatchedColumns() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {1.5, 3.0}});
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorDataWithFewerThanTwoColumns() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}});
    }

    @Test
    public void testConstructor() throws Exception {
        mnmem = new MultivariateNormalMixtureExpectationMaximization(testData);
        assertEquals(3, mnmem.getData().length);
        assertEquals(2, mnmem.getData()[0].length);
    }

    // Test fit method with valid inputs
    @Test
    public void testFit() throws Exception {
        mnmem = new MultivariateNormalMixtureExpectationMaximization(testData);
        mnmem.fit(initialMixture);
        assertNotNull(mnmem.getFittedModel());
    }

    @Test
    public void testFitWithDefaultInitialMixture() throws Exception {
        mnmem = new MultivariateNormalMixtureExpectationMaximization(testData);
        mnmem.fit();
        assertNotNull(mnmem.getFittedModel());
    }

    // Test fit method with invalid inputs
    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrixException() throws Exception {
        double[][] data = testData.clone();
        data[1][1] = 0.0;
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(2))),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(2)))
        ));
        mnm.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() throws Exception {
        mnmem = new MultivariateNormalMixtureExpectationMaximization(testData);
        mnmem.fit(initialMixture, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws Exception {
        mnmem = new MultivariateNormalMixtureExpectationMaximization(testData);
        mnmem.fit(initialMixture, 1000, -1.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitMismatchedMeans() throws Exception {
        double[][] data = testData.clone();
        int numCols = data[0].length + 1;
        mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(numCols))),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new WishartDistribution(5.0, new IdentityMatrix(numCols - 1)))
        ));
        mnmem.fit(initialMixture);
    }

    // Test estimate method with valid inputs
    @Test
    public void testEstimate() throws Exception {
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);
        assertEquals(2, estimatedModel.getComponents().size());
    }

    @Test
    public void testEstimateDefaultNumberOfComponents() throws Exception {
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData);
        assertEquals(2, estimatedModel.getComponents().size());
    }

    @Test
    public void testEstimateWithTooFewData() throws Exception {
        double[][] data = new double[1][];
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testEstimateWithTooManyData() throws Exception {
        double[][] data = {testData, {1.1, 2.1}, {1.2, 2.2}};
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail();
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    
}