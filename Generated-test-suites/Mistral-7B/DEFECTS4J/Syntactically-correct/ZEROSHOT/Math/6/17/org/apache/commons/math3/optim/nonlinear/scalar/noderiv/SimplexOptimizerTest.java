import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimplexOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSimpleValueCheckerConstructor() {
        double rel = 0.0001;
        double abs = 0.01;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
        assertEquals(new SimpleValueChecker(rel, abs), optimizer.getConvergenceChecker());
    }

    @Test
    public void testSimpleValueCheckerConstructorWithChecker() {
        double rel = 0.0001;
        double abs = 0.01;
        SimpleValueChecker checker = new SimpleValueChecker(rel, abs);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testValidSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new SimpleSimplex(2);
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    @Test
    public void testNoOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = (x -> x[0] * x[0] + x[1] * x[1]);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertNotNull(result);
    }

    @Test
    public void testOptimizationDataWithAbstractSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = (x -> x[0] * x[0] + x[1] * x[1]);
        AbstractSimplex simplex = new SimpleSimplex(2);
        OptimizationData data = new OptimizationData();
        data.setParameter("simplex", simplex);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, data);
        assertNotNull(result);
    }

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = (x -> x[0] * x[0] + x[1] * x[1]);
        AbstractSimplex simplex = new SimpleSimplex(2);
        OptimizationData data = new OptimizationData();
        data.setParameter("simplex", simplex);
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, data);
        assertEquals(new PointValuePair(new double[] {-Math.sqrt(2)/2, -Math.sqrt(2)/2}, 0), result);
    }
}

class SimpleSimplex implements AbstractSimplex {
    private int size;

    public SimpleSimplex(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void build(double[] startPoint) {
        // Empty implementation
    }

    @Override
    public void evaluate(MultivariateFunction func, Comparator<PointValuePair> comparator) {
        // Empty implementation
    }

    @Override
    public void iterate(MultivariateFunction func, Comparator<PointValuePair> comparator) {
        // Empty implementation
    }

    @Override
    public PointValuePair getPoint(int index) {
        // Empty implementation
        return null;
    }
}