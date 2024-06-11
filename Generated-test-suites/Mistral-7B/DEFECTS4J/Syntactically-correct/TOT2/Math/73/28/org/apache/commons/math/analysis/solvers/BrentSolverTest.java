
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ComputableFunction mockFunction;
  private ConvergenceChecker mockChecker;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    mockFunction = new MockComputableFunction(0.0);
    mockChecker = mock(ConvergenceChecker.class);
  }

  @Test
  public void testFindRoot_Success() throws FunctionException {
    when(mockChecker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(ConvergenceChecker.CONVERGED);

    assertTrue(brentSolver.findRoot(mockFunction, mockChecker, 10, 1.0, 1.5, 2.0, 0.5));
  }

  @Test
  public void testFindRoot_NoRoot() throws Exception {
    // Arrange
    ComputableFunction function = mock(ComputableFunction.class);
    when(function.valueAt(anyDouble())).thenThrow(new FunctionException("No root found"));
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    // Act
    boolean result = brentSolver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 0.0);
    // Assert
    assertFalse(result);
  }

  @Test
  public void testFindRoot_NotConverged() throws Exception {
    // Arrange
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenAnswer(i -> i.getArguments()[3] < 5.0);
    // Act
    boolean result = brentSolver.findRoot(function, checker, 10, 1.0, 1.5, 2.0, 0.5);
    // Assert
    assertFalse(result);
  }

  @Test
  public void testFindRoot_MultipleRoots() throws Exception {
    // Arrange
    ComputableFunction function = new MockComputableFunction(1.0, 2.0);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    // Act
    boolean result = brentSolver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 0.0);
    // Assert
    assertFalse(result);
  }

  // Test the vertical asymptote edge case
  // Replace this test case with the one provided above for the multiple roots edge case
  @Test
  public void testFindRoot_VerticalAsymptote() throws Exception {
    // Arrange
    ComputableFunction function = new MockComputableFunction(Double.POSITIVE_INFINITY, 0.0);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    // Act & Assert
    assertThrows(FunctionException.class, () -> brentSolver.findRoot(function, checker, 10, 0.0, 0.1, 0.5, 0.0));
  }

  @Test
  public void testFindRoot_NullInputs() throws Exception {
    // Arrange
    ComputableFunction function = null;
    ConvergenceChecker checker = null;
    // Act & Assert
    assertThrows(NullPointerException.class, () -> brentSolver.findRoot(function, checker, 10, 1.0, 2.0, 3.0, 0.0));
  }

  @Test
  public void testFindRoot_MaxIterExceeded() throws Exception {
    // Arrange
    ComputableFunction function = mock(ComputableFunction.class);
    ConvergenceChecker checker = mock(ConvergenceChecker.class);
    when(checker.converged(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenAnswer(i -> i.getArguments()[3] > 10);
    // Act
    boolean result = brentSolver.findRoot(function, checker, 10, 1.0, 1.5, 2.0, 0.5);
    // Assert
    assertFalse(result);
  }
}