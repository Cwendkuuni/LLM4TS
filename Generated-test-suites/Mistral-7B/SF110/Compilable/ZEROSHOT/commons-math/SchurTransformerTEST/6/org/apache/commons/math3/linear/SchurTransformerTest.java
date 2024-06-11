package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        // Test getP() method
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.7363870588366362, 0.435355296232175, 0.0},
                {-0.435355296232175, 0.7363870588366362, 0.435355296232175},
                {0.0, -0.435355296232175, 0.7363870588366362}});
        RealMatrix actualP = schurTransformer.getP();
        assertEquals("P matrix is not correct.", expectedP, actualP);

        // Test getPT() method
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.7363870588366362, -0.435355296232175, 0.0},
                {0.435355296232175, 0.7363870588366362, -0.435355296232175},
                {0.0, 0.435355296232175, 0.7363870588366362}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals("PT matrix is not correct.", expectedPT, actualPT);

        // Test getT() method
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{5.112252417267418, -2.615474316554625, 1.22256323323126},
                {-2.615474316554625, 4.128903977291881, 1.338624735767647},
                {1.22256323323126, 1.338624735767647, 6.10838615521646}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals("T matrix is not correct.", expectedT, actualT);
    }
}