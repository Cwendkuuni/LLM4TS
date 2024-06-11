import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SchurTransformerTest {

 @Test
 public void testSchurTransformerConstruction() {
 RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
  {1.0, 2.0, 3.0},
  {2.0, 3.0, 4.0},
  {3.0, 4.0, 5.0}
 });

 SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
 RealMatrix P = schurTransformer.getP();
 RealMatrix T = schurTransformer.getT();
 RealMatrix PT = schurTransformer.getPT();

 Assert.assertNotNull(schurTransformer.getCachedP());
 Assert.assertNotNull(schurTransformer.getCachedT());
 Assert.assertNotNull(schurTransformer.getCachedPt());

 // Basic checks on the computed matrices
 Assert.assertEquals(new Array2DRowRealMatrix(new double[][]{
  {1.0, 0.0, 0.0},
  {0.0, 1.0, 0.0},
  {0.0, 0.0, 1.0}
 }), P);
 Assert.assertEquals(new Array2DRowRealMatrix(new double[][]{
  {1.0, 0.0, 0.0},
  {-0.714285714, 0.612322571, 0.235622475},
  {0.235622475, 0.612322571, 0.682123322}
 }), T);
 Assert.assertEquals(new Array2DRowRealMatrix(new double[][]{
  {1.0, 0.612322571, 0.682123322},
  {0.612322571, 1.0, 0.714285714},
  {0.682123322, 0.714285714, 1.0}
 }), PT);
 }
}