import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        estimator.initializeEstimate(new EstimationProblem(new double[]{}, new double[]{}));
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        assertEquals(12, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        estimator.initializeEstimate(new EstimationProblem(new double[]{}, new double[]{}));
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();

        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();

        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(14, estimator.getJacobianEvaluations());
    }

    @Test
    public void testGetRMS() throws Exception {
        double[] measurements = {1.0, 2.0};
        double[] parameters = {3.0, 4.0};
        EstimationProblem problem = new EstimationProblem(measurements, parameters);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        assertEquals(1.5, estimator.getRMS(problem), 0.01);
    }

    @Test(expected = EstimationException.class)
    public void testEstimationException() throws Exception {
        double[] measurements = {1.0, 2.0};
        double[] parameters = {3.0, 4.0};
        EstimationProblem problem = new EstimationProblem(measurements, parameters);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Error message");
            }
        };
        estimator.initializeEstimate(problem);
    }
}