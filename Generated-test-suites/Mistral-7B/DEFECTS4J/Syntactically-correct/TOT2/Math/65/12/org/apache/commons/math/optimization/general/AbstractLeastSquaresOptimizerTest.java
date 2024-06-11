import org.apache.commons.math3.optim.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getFunction());
        assertNull(optimizer.getJacobianFunction());
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertNotNull(optimizer.getConvergenceChecker());
        assertArrayEquals(new double[0], optimizer.getJacobian(), 0);
    }

    @Test
    public void testSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        optimizer.setFunction(function);
        assertSame(function, optimizer.getFunction());
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
        optimizer.setMaxEvaluations(50);
        assertEquals(50, optimizer.getMaxEvaluations());
        VectorialConvergenceChecker convergenceChecker = new SimpleVectorialValueChecker(0.001);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimize() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        when(function.value(anyDoubleArray())).thenReturn(new double[]{1.0, 2.0, 3.0});
        when(function.jacobian()).thenReturn(mock(MultivariateMatrixFunction.class));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        assertEquals(3, result.getPoint().length);
        assertEquals(3, result.getValue().length);
        verify(function).value(anyDoubleArray());
        verify(function).jacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        when(function.value(anyDoubleArray())).thenThrow(new OptimizationException(""));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        when(function.value(anyDoubleArray())).thenThrow(new FunctionEvaluationException(""));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = mock(DifferentiableMultivariateVectorialFunction.class);
        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 2.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        try {
            optimizer.optimize(function, target, weights, startPoint);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("FunctionEvaluationException should not be thrown");
        } catch (OptimizationException e) {
            throw new AssertionError("OptimizationException should not be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("target and weights arrays have different lengths", e.getMessage());
        }
    }
}