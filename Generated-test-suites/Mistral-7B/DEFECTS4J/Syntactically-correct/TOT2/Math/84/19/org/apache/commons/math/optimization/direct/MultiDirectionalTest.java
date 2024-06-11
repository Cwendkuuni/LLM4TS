import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
        MultiDirectional md2 = new MultiDirectional(3.0, 0.6);
        assertNotNull(md2);
    }

    @Test
    public void testIterateSimplex_Default() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        double[] input = {1.0, 2.0};
        RealPointValuePair[] simplex = md.optimize(input, comparator, new RealConvergenceChecker(1e-8), 100);
        assertFalse(Double.isNaN(simplex[0].getValue()));
        assertEquals(2, simplex.length);
    }

    @Test
    public void testIterateSimplex_Custom() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional(3.0, 0.6);
        double[] input = {1.0, 2.0};
        RealPointValuePair[] simplex = md.optimize(input, comparator, new RealConvergenceChecker(1e-8), 100);
        assertFalse(Double.isNaN(simplex[0].getValue()));
        assertEquals(3, simplex.length);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        double[] input = {1.0, 2.0, 3.0, 4.0};
        SimpleOptimizer optimizer = new ThrowingOptimizer();
        RealPointValuePair[] simplex = md.optimize(input, comparator, new RealConvergenceChecker(1e-8), 100, optimizer);
        assertNull(simplex);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        MultiDirectional md = new MultiDirectional();
        double[] input = {1.0, 2.0, 3.0, 4.0};
        RealPointValuePair[] simplex = md.optimize(input, comparator, new RealConvergenceChecker(1e-8), 1);
        assertArrayEquals(new RealPointValuePair[]{}, simplex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };
        MultiDirectional md = new MultiDirectional();
        double[] input = {1.0, 2.0};
        RealPointValuePair[] simplex = md.optimize(input, comparator, new RealConvergenceChecker(1e-8), 100);
    }

}