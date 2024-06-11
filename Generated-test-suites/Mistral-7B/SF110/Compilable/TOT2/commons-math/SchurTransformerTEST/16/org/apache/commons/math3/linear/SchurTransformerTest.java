package org.apache.commons.math3.linear;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.9997313906751, 0.0067816223115}, {-0.0067816223115, 0.9997313906751}});
        RealMatrix actualP = transformer.getP();
        Assert.assertEquals(expectedP, actualP);
    }
}