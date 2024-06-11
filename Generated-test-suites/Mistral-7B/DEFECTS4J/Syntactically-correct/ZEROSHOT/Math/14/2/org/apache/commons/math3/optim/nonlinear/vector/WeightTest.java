import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 RealVector weights = RealVectorUtils.createVector(new double[]{1.0, 2.0, 3.0});
 Weight weightObj = new Weight(weights.toArray());
 RealMatrix expectedWeightMatrix = RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
 RealMatrix actualWeightMatrix = weightObj.getWeight();

 assertEquals(expectedWeightMatrix.getRowDimension(), actualWeightMatrix.getRowDimension());
 assertEquals(expectedWeightMatrix.getColumnDimension(), actualWeightMatrix.getColumnDimension());
 assertEquals(expectedWeightMatrix, actualWeightMatrix);
 }

 @Test
 public void testWeightNonSquareMatrixConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
 assertThrows(NonSquareMatrixException.class, () -> new Weight(weightMatrix));
 }

 @Test
 public void testWeightGetters() {
 Weight weightObj = new Weight(RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}));
 RealMatrix weightMatrix = weightObj.getWeight();
 assertNotNull(weightMatrix);

 assertFalse(weightMatrix.isSymmetric());
 assertFalse(weightMatrix.isSquare());
 }

 @Test
 public void testWeightEquals() {
 Weight weightObj1 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}));
 Weight weightObj2 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}));
 Weight weightObj3 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{2.0, 0.0}, {0.0, 2.0}}));

 assertEquals(weightObj1, weightObj1);
 assertEquals(weightObj1, weightObj2);
 assertNotEquals(weightObj1, weightObj3);
 }

 @Test
 public void testWeightHashCode() {
 Weight weightObj1 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}));
 Weight weightObj2 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}));
 Weight weightObj3 = new Weight(RealMatrixUtils.createMatrix(new double[][]{{2.0, 0.0}, {0.0, 2.0}}));

 int hashCode1 = weightObj1.hashCode();
 int hashCode2 = weightObj2.hashCode();
 int hashCode3 = weightObj3.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}