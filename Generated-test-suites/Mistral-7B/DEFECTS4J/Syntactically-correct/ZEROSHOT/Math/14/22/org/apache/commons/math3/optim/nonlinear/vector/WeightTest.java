import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expected = new Array2DRowRealMatrix(new double[][] { {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0} });
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightNonSquareConstruction() {
 new Weight(new double[] {1.0, 2.0});
 }

 @Test
 public void testWeightMatrixCopy() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix original = weight.getWeight();
 weight = new Weight(original);
 RealMatrix expected = original.copy();
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testWeightSingularValueDecomposition() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix actualWeight = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(actualWeight);
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(new double[][] { {svd.getDiagonal().getEntry(0, 0), 0.0, 0.0}, {0.0, svd.getDiagonal().getEntry(1, 1), 0.0}, {0.0, 0.0, svd.getDiagonal().getEntry(2, 2)} });
 RealMatrix actualDiagonal = new Weight(svd.getDiagonal().getMatrix()).getWeight();
 assertEquals(expectedDiagonal, actualDiagonal);
 }

}