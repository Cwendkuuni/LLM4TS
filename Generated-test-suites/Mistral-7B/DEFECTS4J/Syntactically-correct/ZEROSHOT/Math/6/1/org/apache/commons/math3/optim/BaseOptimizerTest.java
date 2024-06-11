import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = getDummyOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = getDummyOptimizer();
        optimizer.optimize(new DummyObjectiveFunction(), new DummyInitialGuess(), new DummyConvergenceChecker());
        int evaluations = optimizer.getEvaluations();
        Assert.assertNotNull(evaluations);
        Assert.assertGreaterEq(evaluations, 0);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = getDummyOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    private BaseOptimizer<Object> getDummyOptimizer() {
        return new DummyBaseOptimizer();
    }

    private static class DummyBaseOptimizer implements BaseOptimizer<Object> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<Object> convergenceChecker;

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<Object> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public Object optimize(OptimizationFunction<Object> objectiveFunction, Object initialGuess, ConvergenceChecker<Object> convergenceChecker) {
            return null;
        }
    }

    private interface OptimizationFunction<T> {

    }

    private static class DummyObjectiveFunction implements OptimizationFunction<Object> {

    }

    private static class DummyInitialGuess implements Object {

    }

    private static class DummyConvergenceChecker implements ConvergenceChecker<Object> {

    }
}