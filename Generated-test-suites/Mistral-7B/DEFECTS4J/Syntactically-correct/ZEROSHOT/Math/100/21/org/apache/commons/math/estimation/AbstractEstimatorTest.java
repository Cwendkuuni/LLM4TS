package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition.UDEXTRA;
import org.apache.commons.math3.stat.descript.DescriptiveStatistics;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new TestAbstractEstimator();
        problem = new TestEstimationProblem(new double[][]{{1, 2}, {3, 4}}, new double[]{-1, -1});
        estimator.initializeEstimate(problem);
    }

    @Test
    public void testGetRMS() {
        assertEquals(Math.sqrt(11d / 2d), estimator.getRMS(problem), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        assertEquals(16d, estimator.getChiSquare(problem), 1e-6);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new RealMatrixImpl(new double[][]{
                {2.5, -0.5},
                {-0.5, 2.5}
        });
        SingularValueDecomposition svd = new SingularValueDecomposition(expectedCovariances, UDEXTRA.COMPUTE_SINGULAR_VECTORS);
        RealMatrix expectedInvSvd = svd.getS().inverse();
        RealMatrix expectedCovariancesInv = new RealMatrixImpl(expectedInvSvd.getV().transpose().getData());

        RealMatrix actualCovariancesInv = new RealMatrixImpl(covariances).inverse();
        assertArrayEquals(expectedCovariancesInv.getData(), actualCovariancesInv.getData());
    }

    @Test
    public void testGuessParametersErrors() {
        DescriptiveStatistics stats = new DescriptiveStatistics(new double[]{1, 2, 3, 4, 5});
        double rms = Math.sqrt(stats.getMean() / 5);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[]{rms, rms}, errors, 1e-6);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() {
        estimator.setMaxCostEval(0);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsThrowsException() {
        problem = new TestEstimationProblem(new double[][]{{1, 2}, {3, 4}}, new double[]{-1, 3});
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        double[] expectedJacobian = new double[]{
                -0.5, 0.5,
                -1.5, -1.5
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian, 1e-6);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.updateResidualsAndCost();
        double[] expectedResiduals = new double[]{-2.828427125, 1.414213563};
        assertArrayEquals(expectedResiduals, estimator.residuals, 1e-6);
        assertEquals(2.828427125, estimator.cost, 1e-6);
    }

}

class TestAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // no implementation needed here
    }

}

class TestEstimationProblem implements EstimationProblem {

    private final double[][] data;
    private final double[] parameters;

    public TestEstimationProblem(double[][] data, double[] parameters) {
        this.data = data;
        this.parameters = parameters;
    }

    @Override
    public double[][] getMeasurements() {
        return data;
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        return new EstimatedParameter[]{new TestEstimatedParameter(0, "p1", 0), new TestEstimatedParameter(1, "p2", 1)};
    }

    @Override
    public RealVector getAllParameters() {
        return new ArrayRealVector(parameters);
    }

    @Override
    public double[] getAllParametersCopy() {
        return parameters.clone();
    }

}

class TestEstimatedParameter implements EstimatedParameter {

    private final int index;
    private final String name;
    private final double initialValue;

    public TestEstimatedParameter(int index, String name, double initialValue) {
        this.index = index;
        this.name = name;
        this.initialValue = initialValue;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getInitialValue() {
        return initialValue;
    }

    @Override
    public double getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(double value) {
        throw new UnsupportedOperationException();
    }

}