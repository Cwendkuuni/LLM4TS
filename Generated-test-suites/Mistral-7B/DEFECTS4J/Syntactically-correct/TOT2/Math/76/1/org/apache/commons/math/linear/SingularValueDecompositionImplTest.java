package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2},
            {2, 1}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealVector singularValues = svd.getSingularValues();
        assertEquals(2, u.getRowDimension());
        assertEquals(2, u.getColumnDimension());
        assertEquals(2, s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertEquals(2, v.getRowDimension());
        assertEquals(2, v.getColumnDimension());
        assertEquals(2, ut.getRowDimension());
        assertEquals(2, ut.getColumnDimension());
        assertEquals(2, vt.getRowDimension());
        assertEquals(2, vt.getColumnDimension());
        assertEquals(2, singularValues.getDimension());
        assertArrayEquals(new double[]{-0.57735026918962577, 0.81649658092772603,
                                        0.57735026918962577, -0.81649658092772603},
                          s.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {-0.57735026918962577, 0.81649658092772603},
            {0.57735026918962577, -0.81649658092772603}},
            v.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {0.57735026918962577, 0.81649658092772603},
            {-0.57735026918962577, -0.81649658092772603}},
            ut.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {-0.57735026918962577, -0.81649658092772603},
            {0.57735026918962577, 0.81649658092772603}},
            vt.getData(), 0.001);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {-0.57735026918962577, 0.57735026918962577},
            {0.81649658092772603, -0.81649658092772603}
        });
        assertEquals(expected, u.multiply(v));
        expected = new Array2DRowRealMatrix(new double[][]{
            {-0.57735026918962577, 0.57735026918962577},
            {0.81649658092772603, -0.81649658092772603}
        });
        assertEquals(expected, v.multiply(ut));
        expected = new Array2DRowRealMatrix(new double[][]{
            {-0.57735026918962577, 0.57735026918962577},
            {0.81649658092772603, -0.81649658092772603}
        });
        assertEquals(expected, ut.multiply(vt));
        RealVector sv = svd.getSingularValues();
        assertEquals(2, sv.getDimension());
        assertEquals(1.0, Precision.round(sv.getEntry(0), 6), 0.00001);
        assertEquals(0.617281625, Precision.round(sv.getEntry(1), 6), 0.00001);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2},
            {2, 1},
            {3, 4}
        });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } finally {
            if (svd != null) {
                svd.getU();
            }
        }
        fail("Should have thrown an InvalidMatrixException");
    }

    @Test
    public void testSingularValueDecompositionWithSmallMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 1}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealVector singularValues = svd.getSingularValues();
        assertEquals(1, u.getRowDimension());
        assertEquals(1, u.getColumnDimension());
        assertEquals(1, s.getRowDimension());
        assertEquals(1, s.getColumnDimension());
        assertEquals(1, v.getRowDimension());
        assertEquals(1, v.getColumnDimension());
        assertEquals(1, ut.getRowDimension());
        assertEquals(1, ut.getColumnDimension());
        assertEquals(1, vt.getRowDimension());
        assertEquals(1, vt.getColumnDimension());
        assertEquals(1, singularValues.getDimension());
        assertEquals(1.0, Precision.round(singularValues.getEntry(0), 6), 0.00001);
        assertArrayEquals(new double[][]{{1.0}}, u.getData(), 0.001);
        assertArrayEquals(new double[][]{{1.0}}, v.getData(), 0.001);
        assertArrayEquals(new double[][]{{1.0}}, ut.getData(), 0.001);
        assertArrayEquals(new double[][]{{1.0}}, vt.getData(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testMathRuntimeException() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 1},
            {1, 1},
            {1, 1}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            svd.getU();
        } finally {
            try {
                svd.getS();
            } finally {
                try {
                    svd.getV();
                } finally {
                    try {
                        svd.getUT();
                    } finally {
                        try {
                            svd.getVT();
                        } finally {
                            try {
                                svd.getSingularValues();
                            } finally {
                                try {
                                    svd.getRank();
                                } finally {
                                    try {
                                        svd.getSolver();
                                    } catch (IllegalStateException e) {
                                        // expected
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        fail("Should have thrown a MathRuntimeException");
    }

    @Test
    public void testSingularValueDecompositionWithIdentityMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 0},
            {0, 1}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        RealVector singularValues = svd.getSingularValues();
        assertEquals(2, u.getRowDimension());
        assertEquals(2, u.getColumnDimension());
        assertEquals(2, s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertEquals(2, v.getRowDimension());
        assertEquals(2, v.getColumnDimension());
        assertEquals(2, ut.getRowDimension());
        assertEquals(2, ut.getColumnDimension());
        assertEquals(2, vt.getRowDimension());
        assertEquals(2, vt.getColumnDimension());
        assertEquals(2, singularValues.getDimension());
        assertEquals(1.0, Precision.round(singularValues.getEntry(0), 6), 0.00001);
        assertEquals(1.0, Precision.round(singularValues.getEntry(1), 6), 0.00001);
        assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }, u.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }, v.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }, ut.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }, vt.getData(), 0.001);
    }

    @Test
    public void testCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.5, 0.5, 0.0},
            {0.5, 1.5, 0.0},
            {0.0, 0.0, 1.5}
        });
        assertEquals(3, covariance.getRowDimension());
        assertEquals(3, covariance.getColumnDimension());
        assertArrayEquals(expected.getData(), covariance.getData(), 0.001);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        assertEquals(Math.sqrt(36.0 + 36.0 + 36.0), norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        assertEquals(Math.sqrt(36.0) / Math.sqrt(3.0), conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        DecompositionSolver solver = svd.getSolver();
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{6.56108, -2.18135, 1.96153});
        assertEquals(3, x.getDimension());
        assertArrayEquals(expected.toArray(), x.toArray(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithWrongDimensionVector() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3},
            {1, 2, 3}
        });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1, 1, 1});
        DecompositionSolver solver = svd.getSolver();
        solver.solve(b);
        fail("Should have thrown an IllegalArgumentException");
    }

}