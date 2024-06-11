package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit_ValidData_Success() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(Pair.of(0.2, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))))));
        assertEquals(1.0, model.getLogLikelihood(), 0.01);
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertEquals(1, fittedModel.getComponents().size());
        Pair<Double, MultivariateNormalDistribution> component = fittedModel.getComponents().get(0);
        double weight = component.getFirst();
        MultivariateNormalDistribution distribution = component.getSecond();
        assertEquals(0.2, weight, 0.01);
        assertEquals(0.0, distribution.getMeans()[0], 0.01);
        assertEquals(0.0, distribution.getMeans()[1], 0.01);
        RealMatrix covarianceMatrix = distribution.getCovarianceMatrix();
        assertEquals(2, covarianceMatrix.getRowDimension());
        assertEquals(2, covarianceMatrix.getColumnDimension());
        assertEquals(1.0, covarianceMatrix.getEntry(0, 0), 0.01);
        assertEquals(1.0, covarianceMatrix.getEntry(1, 1), 0.01);
        assertEquals(0.0, covarianceMatrix.getEntry(0, 1), 0.01);
        assertEquals(0.0, covarianceMatrix.getEntry(1, 0), 0.01);
    }

    @Test
    public void testFit_InvalidData_ThrowsException() throws Exception {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(Pair.of(0.2, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))))));
            fail("Should have thrown an exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        data = new double[][]{{1.0, 2.0}, {-1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(Pair.of(0.2, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))))));
            fail("Should have thrown an exception");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}, {3.5, 4.5}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(Pair.of(0.2, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))))));
            fail("Should have thrown an exception");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testFit_ValidDataWithInitialModel_Success() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Arrays.asList(Pair.of(0.4, new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))),
                Pair.of(0.6, new MultivariateNormalDistribution(new double[]{-0.5, -0.5}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} })))));
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialModel);
        assertEquals(1.0, model.getLogLikelihood(), 0.01);
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());
        assertFalse(fittedModel.getComponents().get(0).getFirst() > 0.4);
        assertFalse(fittedModel.getComponents().get(1).getFirst() > 0.6);
    }

    @Test
    public void testEstimate_ValidData_Success() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(2, model.getComponents().size());
    }

    @Test
    public void testEstimate_InvalidData_ThrowsException() throws Exception {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail("Should have thrown an exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        data = new double[][]{{1.0, 2.0}, {-1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail("Should have thrown an exception");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}, {3.5, 4.5}, {4.0, 5.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail("Should have thrown an exception");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0}, {3.5, 4.5}, {4.0, 5.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            fail("Should have thrown an exception");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
}