import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.LBFGSUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.SecantUnivariateSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() throws Exception {
        UnivariateRealFunction function = new FunctionWithRootAtZero();
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedSolution = 0.0;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(expectedSolution, result, 1e-6);
    }

    // Add more tests here for different solvers and different input functions
}

class FunctionWithRootAtZero implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return x * x - 1.0;
    }
}