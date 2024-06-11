import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentSolverTest {
    
  @Test
  public void testBrentSolverConstructor() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }
}