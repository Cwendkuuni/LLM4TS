import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBoundingBox;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.convergence.RStopping;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }
        };
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            private int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            public void optimize(PointValuePair initialGuess) {
                evaluations++;
            }
        };
        optimizer.optimize(new PointValuePair(new double[0], 0.0));
        optimizer.optimize(new PointValuePair(new double[0], 0.0));
        optimizer.optimize(new PointValuePair(new double[0], 0.0));
        assertEquals(3, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new RStopping(1.0E-6, MaxEval.UNLIMITED);
            }
        };
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof RStopping);
        assertEquals(1.0E-6, ((RStopping) convergenceChecker).getRelativeChange(), 1E-9);
        assertEquals(MaxEval.UNLIMITED, ((RStopping) convergenceChecker).getMaxEvaluations());
    }

    @Test
    public void testBaseOptimizerWithSimpleBoundingBox() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new RStopping(1.0E-6, MaxEval.UNLIMITED);
            }

            @Override
            public PointValuePair optimize(PointValuePair initialGuess) {
                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public void initialize(PointValuePair initialGuess) {
            }

            @Override
            public SimpleBoundingBox getRange() {
                return new SimpleBoundingBox(new double[]{-1.0, 1.0}, new double[]{-1.0, 1.0});
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(1.0E-6, convergenceChecker.getRelativeChange(), 1E-9);
        assertEquals(MaxEval.UNLIMITED, convergenceChecker.getMaxEvaluations());

        PointValuePair initialGuess = new PointValuePair(new double[]{0.0}, 0.0);
        optimizer.initialize(initialGuess);

        try {
            optimizer.optimize(initialGuess);
            fail("optimize method must throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}