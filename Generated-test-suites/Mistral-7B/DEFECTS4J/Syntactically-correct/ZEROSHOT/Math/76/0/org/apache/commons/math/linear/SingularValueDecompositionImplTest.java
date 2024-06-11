import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SingularValueDecompositionImplTest {

 @Test
 public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

 RealMatrix U = svd.getU();
 RealMatrix S = svd.getS();
 RealMatrix V = svd.getV();
 RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.22151574, -0.91404471, 0.16533334}, {-0.89356858, 0.44525941, 0.1441285}, {0.41460132, 0.18526503, 0.81649658}});
 RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{13.76213689, 0.0}, {0.0, 3.91613814}});
 RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.03245463, 0.96919477, 0.24385908}, {-0.97338269, 0.08190042, -0.2458984}, {0.1931863, -0.23591726, 0.9598389}});

 assertEquals("U not equal to expected", expectedU, U);
 assertEquals("S not equal to expected", expectedS, S);
 assertEquals("V not equal to expected", expectedV, V);
 }

 @Test
 public void testSingularValueDecompositionImplSingular() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 0.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

 RealMatrix U = svd.getU();
 RealMatrix S = svd.getS();
 RealMatrix V = svd.getV();
 RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.25937237, -0.88634133, 0.40824826}, {-0.88634133, 0.44515261, 0.25937237}, {-0.40824826, 0.25937237, -0.88634133}});
 RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{13.23136781, 0.0}, {0.0, 0.0}});
 RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.52869487, 0.8132083, 0.0}, {-0.8132083, -0.52869487, 0.0}, {-0.0, 0.0, 1.0}});

 assertEquals("U not equal to expected", expectedU, U);
 assertEquals("S not equal to expected", expectedS, S);
 assertEquals("V not equal to expected", expectedV, V);
 }

 @Test
 public void testSingularValueDecompositionImplIdentity() throws InvalidMatrixException {
 RealMatrix matrix = new IdentityRealMatrix(3);
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 RealMatrix expectedU = matrix;
 RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0, 1.0});
 RealMatrix expectedV = expectedU;
 assertEquals("U not equal to expected", expectedU, U);
 assertEquals("S not equal to expected", expectedS, S);
 assertEquals("V not equal to expected", expectedV, V);
 }

 @Test
 public void testSingularValueDecompositionImplZero() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 0);
 assertTrue("Singular value decomposition should not be computed for empty matrix", svd.isIdentity());
 }

 @Test
 public void testGetRank() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 assertEquals("Rank should be 3", 3, svd.getRank());
 RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 0.0}});
 SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 3);
 assertEquals("Rank should be 2", 2, svd2.getRank());
 }

 @Test
 public void testGetCovariance() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 RealMatrix covariance = svd.getCovariance(5.0);
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.22151574, -0.91404471, 0.16533334}, {-0.89356858, 0.44525941, 0.1441285}, {0.41460132, 0.18526503, 0.81649658}});
 assertEquals("Covariance not equal to expected", expected, covariance);
 }

 @Test
 public void testGetNorm() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 assertEquals("Norm should be 13.76213689", 13.76213689, svd.getNorm(), 0.001);
 RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 0.0}});
 SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 3);
 assertEquals("Norm should be 13.23136781", 13.23136781, svd2.getNorm(), 0.001);
 }

 @Test
 public void testGetConditionNumber() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 assertEquals("Condition number should be 13.76213689 / 0.0009386 = 14424.7348", 14424.7348, svd.getConditionNumber(), 0.001);
 RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 0.0}});
 SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 3);
 assertEquals("Condition number should be 13.23136781 / 0.000627 = 21065.5873", 21065.5873, svd2.getConditionNumber(), 0.001);
 }

 @Test
 public void testGetSolver() throws InvalidMatrixException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
 DecompositionSolver solver = svd.getSolver();
 RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
 RealVector result = solver.solve(vector);
 double[] expectedResult = new double[]{1.0 / 13.76213689, 2.0 / 13.76213689, 3.0 / 13.76213689};
 assertArrayEquals("Solver result not equal to expected", expectedResult, result.toArray(), 0.001);
 }

}