import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils; // Assuming FunctionUtils is a utility class for creating ComputableFunction
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleFunction() {
    // Given
    ComputableFunction function = FunctionUtils.createFunction(x -> Math.pow(x, 2) - 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    solver.findRoot(function, checker, 100, -2.0, -1.0);

    // Then
    double expectedRoot = -1.0;
    double actualRoot = solver.getRoot();
    assert(expectedRoot == actualRoot);
  }

  // Add more test cases for different functions, edge cases, and error scenarios

  // ...

  // Test end marker
  
}