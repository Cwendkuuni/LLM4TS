package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_ValidInput() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{
                {-0.414213709551261, 0.85748081515423, 0.20923259962885},
                {0.85748081515423, -0.414213709551261, 0.414213709551261},
                {0.20923259962885, 0.414213709551261, -0.414213709551261}
        });
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{
                {3.15463099572643, 1.57615213614206, 0.0},
                {1.57615213614206, 2.73205080756888, 1.57615213614206},
                {0.0, 1.57615213614206, 3.15463099572643}
        });

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_InvalidInput_NonSquareMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }

    @Test
    public void testGetP() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        }));
        RealMatrix actualP = transformer.getP();
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{
                {-0.414213709551261, 0.85748081515423, 0.20923259962885},
                {0.85748081515423, -0.414213709551261, 0.414213709551261},
                {0.20923259962885, 0.414213709551261, -0.414213709551261}
        });
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        }));
        RealMatrix actualPT = transformer.getPT();
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{
                {-0.414213709551261, 0.85748081515423, 0.20923259962885},
                {0.85748081515423, -0.414213709551261, 0.414213709551261},
                {0.20923259962885, 0.414213709551261, -0.414213709551261}
        });
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        }));
        RealMatrix actualT = transformer.getT();
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{
                {3.15463099572643, 1.57615213614206, 0.0},
                {1.57615213614206, 2.73205080756888, 1.57615213614206},
                {0.0, 1.57615213614206, 3.15463099572643}
        });
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransform_Convergence() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        }));
        transformer.transform();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxIterations() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        }));
        transformer.setMaxIterations(0);
        transformer.transform();
    }
}