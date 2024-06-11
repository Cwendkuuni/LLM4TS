import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.AbstractOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.TestObjectiveFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

 private GaussNewtonOptimizer optimizer;
 private TestObjectiveFunction objectiveFunction;
 private RealMatrix weightMatrix;
 private double[] startPoint;
 private double[] targetValues;

 @Before
 public void setUp() {
 optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
 objectiveFunction = new TestObjectiveFunction();
 weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 targetValues = new double[]{1.0, 1.0};
 startPoint = new double[]{0.0, 0.0};
 }

 @Test
 public void testSimpleFunctionality() {
 PointValuePair result = optimizer.optimize();
 assertNotNull(result);
 assertEquals(SimplePointValuePair.class, result.getClass());
 assertArrayEquals(new double[]{1.0, 1.0}, result.getValue(), 0.001);
 }

 @Test(expected = NullArgumentException.class)
 public void testNullObjectiveFunction() {
 optimizer.setObjectiveFunction(null);
 optimizer.optimize();
 }

 @Test(expected = NullArgumentException.class)
 public void testNullWeightMatrix() {
 optimizer.setWeight(null);
 optimizer.optimize();
 }

 @Test(expected = NullArgumentException.class)
 public void testNullStartPoint() {
 optimizer.setStartPoint(null);
 optimizer.optimize();
 }

 @Test
 public void testLUDecomposition() {
 optimizer.setWeight(weightMatrix);
 optimizer.setObjectiveFunction(objectiveFunction);
 optimizer.setStartPoint(startPoint);
 optimizer.optimize();
 }

 @Test
 public void testQRDecomposition() {
 optimizer.setWeight(new SingularValueDecomposition(new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})).getS().transpose());
 optimizer.setObjectiveFunction(objectiveFunction);
 optimizer.setStartPoint(startPoint);
 optimizer.optimize();
 }

 @Test(expected = ConvergenceException.class)
 public void testExceptionWhenSingular() {
 optimizer.setWeight(new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}}));
 optimizer.setObjectiveFunction(objectiveFunction);
 optimizer.setStartPoint(startPoint);
 optimizer.optimize();
 }

 @Test(expected = SingularMatrixException.class)
 public void testExceptionWhenSingularDecomposition() {
 optimizer.setWeight(new SingularValueDecomposition(new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}})).getS());
 optimizer.setObjectiveFunction(objectiveFunction);
 optimizer.setStartPoint(startPoint);
 optimizer.optimize();
 }
}