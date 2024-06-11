package org.apache.commons.math3.linear;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.RealMatrixUtils.round;
import static org.apache.commons.math3.util.Precision.EQUALS;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedResult = createRealMatrix(new double[][]{{0.4495, 0.8990}, {-0.8990, 0.4495}});
        RealMatrix result = transformer.getP();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedResult = createRealMatrix(new double[][]{{0.4495, -0.8990}, {0.8990, 0.4495}});
        RealMatrix result = transformer.getPT();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedResult = createRealMatrix(new double[][]{{3.0225, 2.6181}, {2.6181, 2.8180}});
        RealMatrix result = transformer.getT();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testTransform_SmallMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedResult = createRealMatrix(new double[][]{{0.4495, 0.8990}, {-0.8990, 0.4495}});
        RealMatrix resultP = transformer.getP();
        RealMatrix resultT = transformer.getT();

        Assert.assertEquals(expectedResult, resultP);
        Assert.assertEquals(expectedResult.transpose(), resultT);

        Assert.assertArrayEquals(new double[]{0.4495, -0.8990}, round(resultP.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{-0.8990, 0.4495}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{3.0225, 2.6181}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{2.6181, 2.8180}, round(resultT.getData(), 4), 0.0001);
    }

    @Test
    public void testTransform_LargeMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 1.0, 3.0, 4.0, 5.0},
                {3.0, 3.0, 1.0, 2.0, 1.0}, {4.0, 4.0, 2.0, 1.0, 3.0}, {5.0, 5.0, 1.0, 3.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedResult = createRealMatrix(new double[][]{{0.0922, 0.5419, 0.8363, -0.0386, 0.0193},
                {-0.5419, 0.0922, 0.0646, 0.8363, -0.0646}, {0.8363, 0.0646, 0.0922, -0.5419, 0.0646},
                {-0.0386, 0.8363, -0.5419, 0.0922, 0.5419}, {0.0193, -0.0646, 0.0646, 0.5419, 0.0922}});
        RealMatrix resultP = transformer.getP();
        RealMatrix resultT = transformer.getT();

        Assert.assertEquals(expectedResult, resultP);
        Assert.assertEquals(expectedResult.transpose(), resultT);

        Assert.assertArrayEquals(new double[]{0.0922, -0.5419, 0.8363, -0.0386, 0.0193}, round(resultP.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{-0.5419, 0.0922, 0.0646, 0.8363, -0.0646}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{0.0922, 0.0, 2.0130, 0.9986, 0.0}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{2.0130, 0.9986, 0.0, -1.9963, 0.9986}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{0.9986, -0.9986, 0.0, -0.9986, 0.9986}, round(resultT.getData(), 4), 0.0001);
        Assert.assertArrayEquals(new double[]{-0.9986, 0.9986, 0.0, 0.9986, -0.9986}, round(resultT.getData(), 4), 0.0001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testTransform_NonSquareMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}});
        new SchurTransformer(matrix);
    }
}