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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;
    private RealVector startPoint;
    private double[] targetValues;

    @Before
    public void setUp() {
        double[][] weightMatrixData = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        weightMatrix = new BlockRealMatrix(weightMatrixData);

        double[] startPointData = new double[]{1.0, 1.0};
        startPoint = new ArrayRealVector(startPointData, false);

        double[] targetValuesData = new double[]{2.0, 2.0};
        this.targetValues = targetValuesData;

        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
    }

    @Test
    public void testOptimize() throws Exception {
        // Given
        double[] expectedResult = new double[]{3.0, 3.0};
        double[][] jacobian = new double[][]{{2.0, 2.0}, {2.0, 2.0}};
        double[] objectiveValues = new double[]{4.0, 4.0};

        when(optimizer.computeObjectiveValue(startPoint)).thenReturn(objectiveValues);
        when(optimizer.computeJacobian(startPoint)).thenReturn(new BlockRealMatrix(jacobian));

        // When
        PointValuePair result = optimizer.optimize();

        // Then
        Assert.assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, optimizer.getConvergenceChecker().getConvergenceCount(), 0.0);

        // Check that the methods have been called
        verify(optimizer, times(1)).computeObjectiveValue(startPoint);
        verify(optimizer, times(1)).computeJacobian(startPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullTarget() {
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeight() {
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() throws Exception {
        doReturn(new SimpleVectorValueChecker.ThrowingChecker(1.0, 1.0)).when(optimizer).getConvergenceChecker();

        when(optimizer.computeObjectiveValue(startPoint)).thenReturn(new double[]{0.0, 0.0});
        when(optimizer.computeJacobian(startPoint)).thenReturn(new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}));

        optimizer.optimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testOptimizeWithSingularMatrixException() throws Exception {
        when(optimizer.computeObjectiveValue(startPoint)).thenReturn(new double[]{0.0, 0.0});
        when(optimizer.computeJacobian(startPoint)).thenThrow(new SingularValueDecomposition.SingularValueDecompositionException());

        optimizer.optimize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithUnsupportedOperationException() throws Exception {
        when(optimizer.computeObjectiveValue(startPoint)).thenThrow(new UnsupportedOperationException("Method not supported"));

        optimizer.optimize();
    }
}