package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private DifferentiableMultivariateVectorFunction mockFunction;
    private MultivariateMatrixFunction mockJacobian;
    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() throws Exception {
        mockFunction = mock(DifferentiableMultivariateVectorFunction.class);
        mockJacobian = mock(MultivariateMatrixFunction.class);
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException();
            }
        };

        when(mockFunction.getDimension()).thenReturn(3);
        when(mockFunction.value(any())).thenReturn(new double[] {1.0, 2.0, 3.0});
        when(mockFunction.jacobian()).thenReturn(mockJacobian);
        when(mockJacobian.value(any())).thenReturn(new double[][] {
                {1.1, 1.2, 1.3},
                {2.1, 2.2, 2.3},
                {3.1, 3.2, 3.3}
        });

        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
    }

    @Test
    public void testOptimize_Success() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0, 0.0};

        PointValuePair result = optimizer.optimize(mockFunction, target, weights, startPoint);

        assertNotNull(result);
        assertArrayEquals(new double[] {3.90243957, 4.99951023, 5.00048986}, result.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getRMS(), 0.001);
    }

    @Test
    public void testOptimize_MaxIterationsExceeded() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0, 0.0};

        optimizer.setMaxIterations(0);

        assertThrows(MaxIterationsExceededException.class, () -> {
            optimizer.optimize(mockFunction, target, weights, startPoint);
        });
    }

    @Test
    public void testOptimize_MaxEvaluationsExceeded() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0, 0.0};

        optimizer.setMaxEvaluations(0);

        assertThrows(MaxEvaluationsExceededException.class, () -> {
            optimizer.optimize(mockFunction, target, weights, startPoint);
        });
    }

    @Test
    public void testOptimize_FunctionEvaluationException() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0, 0.0};

        when(mockFunction.value(any())).thenThrow(new FunctionEvaluationException(""));

        assertThrows(FunctionEvaluationException.class, () -> {
            optimizer.optimize(mockFunction, target, weights, startPoint);
        });
    }

    @Test
    public void testOptimize_IllegalArgumentException() throws Exception {
        double[] target = new double[] {4.0};
        double[] weights = new double[] {1.0, 1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0, 0.0};

        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(mockFunction, target, weights, startPoint);
        });
    }

    @Test
    public void testGetRMS() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        optimizer.optimize(mockFunction, target, null, null);

        assertEquals(0.0, optimizer.getRMS(), 0.001);

        double[] residuals = new double[] {1.0, 2.0, 3.0};
        optimizer.updateResidualsAndCost();

        assertEquals(4.47213595528311, optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        optimizer.optimize(mockFunction, target, null, null);

        assertEquals(0.0, optimizer.getChiSquare(), 0.001);

        double[] residuals = new double[] {1.0, 2.0, 3.0};
        optimizer.updateResidualsAndCost();

        assertEquals(14.361646654134313, optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        optimizer.optimize(mockFunction, target, null, null);

        double[] errors = optimizer.guessParametersErrors();

        assertArrayEquals(new double[] {0.18381952, 0.20721822, 0.22522532}, errors, 0.001);
    }

    @Test
    public void testCovariances() throws Exception {
        double[] target = new double[] {4.0, 5.0, 6.0};
        optimizer.optimize(mockFunction, target, null, null);

        double[][] covariances = optimizer.getCovariances();

        RealVector actual = new Array2DRowRealMatrix(covariances).getColumnVector(0);
        RealVector expected = new Array2DRowRealMatrix(new double[][] {
                {0.00086376, -0.00113042, 0.00015309},
                {-0.00113042, 0.00745158, -0.00052422},
                {0.00015309, -0.00052422, 0.01047583}
        }).getColumnVector(0);

        assertArrayEquals(expected.toArray(), actual.toArray(), 0.001);

        actual = new Array2DRowRealMatrix(covariances).getColumnVector(1);
        expected = new Array2DRowRealMatrix(new double[][] {
                {-0.00113042, 0.00745158, 0.00052422},
                {0.00745158, 0.04159973, -0.02566238},
                {0.00052422, -0.02566238, 0.04425439}
        }).getColumnVector(1);

        assertArrayEquals(expected.toArray(), actual.toArray(), 0.001);

        actual = new Array2DRowRealMatrix(covariances).getColumnVector(2);
        expected = new Array2DRowRealMatrix(new double[][] {
                {0.00015309, -0.00052422, 0.04425439},
                {-0.00052422, 0.02566238, 0.04159973},
                {0.04425439, 0.04159973, 0.01047583}
        }).getColumnVector(2);

        assertArrayEquals(expected.toArray(), actual.toArray(), 0.001);
    }

}