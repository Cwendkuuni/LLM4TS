package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test for {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test for {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#updateJacobian()}.
     */
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateJacobian();
        assertEquals(6, estimator.getJacobianEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#updateResidualsAndCost()}.
     */
    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
        assertEquals(3, estimator.getJacobianEvaluations());
        assertEquals(3, estimator.getRows());
        assertEquals(3, estimator.getCols());
    }

    /**
     * Test for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(createProblem());
        assertEquals(1.0, rms, 1e-9);
    }

    /**
     * Test for {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(createProblem());
        assertEquals(2.0, chiSquare, 1e-9);
    }

    /**
     * Test for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testgetCovariances() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        RealMatrix covariances = estimator.getCovariances(createProblem());
        assertEquals(3, covariances.getRowDimension());
        assertEquals(3, covariances.getColumnDimension());
        SingularValueDecomposition svd = new SingularValueDecomposition(covariances);
        assertEquals(3, svd.getNumberOfModes());
        double[][] data = svd.getMatrixA().getData();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(1.0 / Math.sqrt(3.0), data[i][j], 1e-9);
            }
        }
    }

    /**
     * Test for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(createProblem());
        assertEquals(3, errors.length);
        for (int i = 0; i < 3; ++i) {
            assertEquals(1.0 / Math.sqrt(3.0), errors[i], 1e-9);
        }
    }

    /**
     * Test for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        assertThrows(IllegalArgumentException.class, () -> estimator.initializeEstimate(null));
        estimator.initializeEstimate(createProblem());
        assertEquals(3, estimator.getRows());
        assertEquals(3, estimator.getCols());
    }

    /**
     * Test for {@link AbstractEstimator#estimate(EstimationProblem)}.
     * This test does not check the correctness of the estimation, only
     * the fact that the method does not throw an exception.
     */
    @Test
    public void testEstimate() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createProblem());
        estimator.estimate(createProblem());
    }

    /**
     * Creates an estimation problem.
     * @return a new estimation problem.
     */
    private EstimationProblem createProblem() {
        double[][] data = new double[][] {
            {1.0, 1.0},
            {2.0, 2.0},
            {3.0, 3.0}
        };
        double[] constants = new double[] {2.0, 3.0, 4.0};
        EstimationProblem problem = new EstimationProblem(2, 3);
        problem.setData(data);
        problem.setConstants(constants);
        return problem;
    }

}

/**
 * Test implementation of {@link AbstractEstimator}.
 */
class TestAbstractEstimator extends AbstractEstimator {

    /**
     * Constructor.
     */
    TestAbstractEstimator() {
        super();
    }

    /**
     * Solves an estimation problem.
     * {@inheritDoc}
     * <p>This test implementation does nothing.</p>
     */
    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // empty implementation
    }

    /**
     * Initializes the common parts of the estimation.
     * {@inheritDoc}
     * <p>This test implementation sets the maximum allowed number of cost
     * evaluations to 1.</p>
     */
    @Override
    protected void initializeEstimate(EstimationProblem problem) {
        super.initializeEstimate(problem);
        this.setMaxCostEval(1);
    }

}