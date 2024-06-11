package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.equals;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.points.RealVector;
import org.apache.commons.math3.optim.points.SimplePointValuePair;
import org.apache.commons.math3.optim.points.WolfePoint;
import org.apache.commons.math3.optim.initializer.RandomPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMethod;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.MaxEval.MaxCount;
import org.apache.commons.math3.optim.MinEval.MinCount;
import org.apache.commons.math3.optim.initializer.ValueGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.RegularPoint;
import org.apache.commons.math3.optim.initializer.RandomPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMethod;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.MaxEval.MaxCount;
import org.apache.commons.math3.optim.MinEval.MinCount;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteTolerance() {
        new BrentOptimizer(-0.1, 0.1, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallRelativeTolerance() {
        new BrentOptimizer(0.00001, 0.1, null);
    }

    @Test
    public void testBrentOptimizerWithMinimizationGoal() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x - 1.0, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public double value() {
                return 2.0;
            }
        }, null);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithMaximizationGoal() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x - 1.0, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public double value() {
                return -2.0;
            }
        }, null);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return 1.0 / (1.0 + Math.abs(x));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.0001;
            }
        });
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public double value() {
                return 0.1;
            }
        }, null);
        assertNotNull(result);
        assertTrue(Math.abs(result.getValue() - 1.0) < 0.001);
    }

    @Test
    public void testBrentOptimizerWithObjectiveGradient() {
        ObjectiveFunctionGradient function = new ObjectiveFunctionGradient() {
            public double value(double x) {
                return Math.sin(x);
            }

            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public double value() {
                return 0.0;
            }
        }, null);
        assertNotNull(result);
        assertEquals(Math.PI / 2, result.getPoint(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithInitialPoints() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public RealVector getInitialPoints(int numberOfPoints) {
                return new PointValuePairList(numberOfPoints).add(new SimplePointValuePair(1.0, 1.0)).toRealVector();
            }
        }, null);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithWolfePointInitializer() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public PointValuePair generatePoint() {
                return new WolfePoint(function, 3, new MaxEval(1000), new MinEval(1000));
            }
        }, null);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithRandomPointInitializer() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, null);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public PointValuePair generatePoint() {
                return new SimplePointValuePair(new RandomPointGenerator().nextDouble(), new RandomPointGenerator().nextDouble());
            }
        }, null);
        assertNotNull(result);
        assertTrue(result.getPoint() > 0.0 && result.getPoint() < 1.0);
    }

    @Test
    public void testBrentOptimizerWithBrentMethodInitializer() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentMethod brentMethod = new BrentMethod();
        brentMethod.setTolerance(0.001);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, brentMethod);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public PointValuePair generatePoint() {
                return new RegularPoint(-1.0);
            }
        }, null);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithMaxEvalInitializer() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        MaxEval maxEval = new MaxEval(100);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, maxEval);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public PointValuePair generatePoint() {
                return new RegularPoint(-1.0);
            }
        }, null);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithMinEvalInitializer() {
        ObjectiveFunction function = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        MinEval minEval = new MinEval(100);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, minEval);
        PointValuePair result = optimizer.optimize(function, new InitialGuess() {
            public PointValuePair generatePoint() {
                return new RegularPoint(1.0);
            }
        }, null);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
    }
}