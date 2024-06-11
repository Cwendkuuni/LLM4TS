import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.initializer.AverageValueInitializer;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.point.PointValue;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.AbstractFunctionValues;
import org.apache.commons.math3.optim.nonlinear.grad.FiniteDifferenceGradientProducer;
import org.apache.commons.math3.optim.initializer.SimpleStartPointInitializer;
import org.apache.commons.math3.optim.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerEmpty() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            optimizer.optimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGaussNewtonOptimizerDefault() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setFunction(new LinearFunction(new double[]{1.0, 2.0}));
        optimizer.initialize(new AverageValueInitializer(2), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.0, 0.0}, point.toVector().toArray(), 0.0);
        assertEquals(0.0, cost, 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerCustomChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer() {
            @Override
            protected ConvergenceChecker<PointVectorValuePair> getConvergenceChecker() {
                return new ConvergenceChecker<PointVectorValuePair>() {
                    @Override
                    public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                        return current.getValue() < 0.01;
                    }
                };
            }
        };

        optimizer.setFunction(new LinearFunction(new double[]{1.0, 2.0}));
        optimizer.initialize(new AverageValueInitializer(2), null);

        try {
            optimizer.optimize();
            fail();
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testGaussNewtonOptimizerCustomStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer() {
            @Override
            protected ConvergenceChecker<PointVectorValuePair> getConvergenceChecker() {
                return new SimpleLineSearch();
            }
        };

        optimizer.setFunction(new LinearFunction(new double[]{1.0, 2.0}));
        optimizer.initialize(new SimpleStartPointInitializer(new double[]{3.0, 4.0}), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{1.5, 0.5}, point.toVector().toArray(), 0.01);
        assertEquals(-2.5, cost, 0.01);
    }

    @Test
    public void testGaussNewtonOptimizerCustomConstraints() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer() {
            @Override
            protected ConvergenceChecker<PointVectorValuePair> getConvergenceChecker() {
                return new SimpleLineSearch();
            }
        };

        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                new double[]{1.0, 1.0},
                new double[]{-1.0, 1.0}}, new double[]{2.0, 3.0});
        optimizer.setFunction(f);

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, RelativeConstraintTolerance.DEFAULT));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 2.0, RelativeConstraintTolerance.DEFAULT));

        optimizer.initialize(new AverageValueInitializer(2), constraints);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.5, 1.5}, point.toVector().toArray(), 0.01);
        assertEquals(1.5, cost, 0.01);
    }

    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(new LinearFunction(new double[]{1.0, 2.0}));
        optimizer.initialize(new AverageValueInitializer(2), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.0, 0.0}, point.toVector().toArray(), 0.0);
        assertEquals(0.0, cost, 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setFunction(new LinearFunction(new double[]{1.0, 2.0}));
        optimizer.initialize(new AverageValueInitializer(2), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.0, 0.0}, point.toVector().toArray(), 0.0);
        assertEquals(0.0, cost, 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerFunctionGradient() {
        LinearFunction f = new LinearFunction(new double[]{1.0, 2.0});

        AbstractFunctionValues funValues = new AbstractFunctionValues() {
            @Override
            public double value(RealVector v) {
                return f.value(v.toArray());
            }

            @Override
            public RealVector gradient(RealVector v) {
                double[] vArray = v.toArray();
                return new ArrayRealVector(new double[]{f.gradient(vArray)[0], f.gradient(vArray)[1]});
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(funValues);

        optimizer.initialize(new AverageValueInitializer(2), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.0, 0.0}, point.toVector().toArray(), 0.0);
        assertEquals(0.0, cost, 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerFunctionGradientFiniteDifference() {
        LinearFunction f = new LinearFunction(new double[]{1.0, 2.0});

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(f);

        optimizer.initialize(new AverageValueInitializer(2), null);

        PointValuePair result = optimizer.optimize();
        PointValue point = result.getPoint();
        double cost = result.getValue();

        assertArrayEquals(new double[]{0.0, 0.0}, point.toVector().toArray(), 0.0);
        assertEquals(0.0, cost, 0.0);

        FiniteDifferenceGradientProducer finiteDifferenceGradientProducer = new FiniteDifferenceGradientProducer();
        optimizer.setFunctionGradientProducer(finiteDifferenceGradientProducer);

        PointValuePair result2 = optimizer.optimize();
        PointValue point2 = result2.getPoint();
        double cost2 = result2.getValue();

        RealVector gradient = finiteDifferenceGradientProducer.getGradient(point2.toVector(), 1.0E-6);
        double[] gradientArray = gradient.toArray();
        assertArrayEquals(new double[]{f.gradient(point2.toVector().toArray())[0], f.gradient(point2.toVector().toArray())[1]}, gradientArray, 0.01);
    }
}