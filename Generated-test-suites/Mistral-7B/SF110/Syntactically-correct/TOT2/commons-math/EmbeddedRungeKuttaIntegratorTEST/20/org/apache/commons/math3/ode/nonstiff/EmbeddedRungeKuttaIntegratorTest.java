import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.*;
import org.apache.commons.math3.ode.nonstiff.MyEmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.MyODEEquations;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        int order = integrator.getOrder();
        assertEquals(5, order);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        double safety = integrator.getSafety();
        assertEquals(0.9, safety, 0.01);
    }

    @Test
    public void testIntegrate() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        integrator.integrate(equations, t);
        double[] expectedY = {1.0, 2.0};
        assertArrayEquals(expectedY, equations.getCompleteState(), 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void testIntegrateWithNullODE() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = null;
        double t = 1.0;
        integrator.integrate(equations, t);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidStepSize() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        integrator.setMinStep(-1.0);
        integrator.integrate(equations, t);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidMaxStep() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        integrator.setMaxStep(0.0);
        integrator.integrate(equations, t);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullVector() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        integrator.integrate(equations, t, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidVectorSize() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        double[] y = new double[3];
        integrator.integrate(equations, t, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullVecTolerance() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        double[] vecTolerance = null;
        integrator.integrate(equations, t, vecTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidVecToleranceSize() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        double[] vecTolerance = new double[2];
        integrator.integrate(equations, t, vecTolerance);
    }

    @Test
    public void testIntegrateWithValidParameters() throws Exception {
        MyEmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new MyODEEquations();
        double t = 1.0;
        double[] y = {1.0, 2.0};
        double[] vecTolerance = {1e-6, 1e-6};
        integrator.integrate(equations, t, y, vecTolerance);
    }
}