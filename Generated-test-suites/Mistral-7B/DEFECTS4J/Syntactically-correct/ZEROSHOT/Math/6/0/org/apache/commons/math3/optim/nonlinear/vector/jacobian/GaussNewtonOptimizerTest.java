import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.AbstractOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private RealMatrix weightMatrix;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-4, 1e-4));
        convergenceChecker = optimizer.getConvergenceChecker();

        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        startPoint = new double[]{1.0, 1.0};
    }

    @Test
    public void testConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new GaussNewtonOptimizer(false);
        assertEquals(false, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(1e-4, 1e-4));
        assertNotNull(optimizer);
        assertEquals(true, optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceCheckerAfterConstruction() {
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testDoOptimizeWithValidData() throws ConvergenceException, SingularMatrixException {
        double[] expectedPoint = new double[]{2.0, 2.0};
        double[] expectedObjectiveValue = new double[]{4.0, 4.0};
        double[] expectedCost = new double[]{1.0, 1.0};

        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(new double[]{3.0, 3.0});
        optimizer.setWeight(weightMatrix);

        PointValuePair result = optimizer.optimize();

        assertArrayEquals(expectedPoint, result.getPoint(), 1e-8);
        assertArrayEquals(expectedObjectiveValue, result.getValue(), 1e-8);
        assertEquals(expectedCost[0], optimizer.getCost(), 1e-8);
        assertEquals(expectedCost[1], optimizer.getCost(), 1e-8);

        assertTrue(convergenceChecker.isConverged(1, previousPoint(result), result));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.setStartPoint(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        optimizer.setTarget(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        optimizer.setWeight(null);
    }

    @Test
    public void testDoOptimizeWithInvalidData() {
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setTarget(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException ignored) {
        }

        optimizer.setTarget(new double[]{1.0, 1.0});
        optimizer.setWeight(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException ignored) {
        }

        optimizer.setStartPoint(null);
        optimizer.setTarget(new double[]{1.0, 1.0});
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException ignored) {
        }

        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setTarget(new double[]{1.0});
        try {
            optimizer.optimize();
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0});
        try {
            optimizer.optimize();
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithNonConvergedData() throws ConvergenceException, SingularMatrixException {
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setTarget(new double[]{3.0, 3.0});
        optimizer.setWeight(weightMatrix);

        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        });

        optimizer.optimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testSolveSingularLinearEquation() throws ConvergenceException, SingularMatrixException {
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        optimizer.setTarget(new double[]{3.0, 3.0});
        optimizer.setWeight(new SingularValueDecomposition(new BlockRealMatrix(new double[][]{
                {0.0, 1.0},
                {1.0, 0.0}
            })).getMatrixS().transpose());

        optimizer.optimize();
    }

    private PointValuePair previousPoint(PointValuePair current) {
        if (current.getValue() instanceof double[]) {
            return new PointValuePair(current.getPoint(), ((double[]) current.getValue())[0]);
        } else {
            return current;
        }
    }

    @Test
    public void testToString() {
        String expected = "GaussNewtonOptimizer[useLU=true, convergenceChecker=SimpleVectorValueChecker[maximumFunctionValueChange=1.0e-4, maximumStepSize=1.0e-4]]";
        assertEquals(expected, optimizer.toString());
    }

    @Test
    public void testIsUseLU() {
        assertEquals(true, optimizer.isUseLU());
        optimizer.setUseLU(false);
        assertEquals(false, optimizer.isUseLU());
    }

    @Test
    public void testSetUseLU() {
        optimizer.setUseLU(false);
        assertEquals(false, optimizer.isUseLU());

        try {
            optimizer.setUseLU(-1);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}