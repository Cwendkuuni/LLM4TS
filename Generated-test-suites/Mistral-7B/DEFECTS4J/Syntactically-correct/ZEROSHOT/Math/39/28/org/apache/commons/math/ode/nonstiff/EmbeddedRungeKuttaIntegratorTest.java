import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Test constructor with default arguments
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method", false,
                new double[]{0.0, 0.3, 0.382, 0.556, 0.831, 1.0}, new double[][]{
                        {0.0, 0.25, 0.455, 0.622, 0.788, 1.0},
                        {0.0, 0.138, 0.468, 0.613, 0.775, 0.900}}, new double[]{0.0, 0.3, 0.347, 0.679, 0.873, 1.0},
                new RungeKuttaStepInterpolator());
        assertEquals("Embedded Runge-Kutta method", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(new double[]{0.0, 0.3, 0.382, 0.556, 0.831, 1.0}, integrator.getC());
        assertArrayEquals(new double[][]{
                        {0.0, 0.25, 0.455, 0.622, 0.788, 1.0},
                        {0.0, 0.138, 0.468, 0.613, 0.775, 0.900}}, integrator.getA());
        assertArrayEquals(new double[]{0.0, 0.3, 0.347, 0.679, 0.873, 1.0}, integrator.getB());
        assertNull(integrator.getPrototype());
        assertEquals(-1.0 / 6.0, integrator.getExp(), 1e-10);
        assertEquals(0.9, integrator.getSafety(), 1e-10);
        assertEquals(0.2, integrator.getMinReduction(), 1e-10);
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-10);

        // Test constructor with arguments with vectors
        EmbeddedRungeKuttaIntegrator integratorVect = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method", false,
                new double[]{0.0, 0.3, 0.382, 0.556, 0.831, 1.0}, new double[][]{
                        {0.0, 0.25, 0.455, 0.622, 0.788, 1.0},
                        {0.0, 0.138, 0.468, 0.613, 0.775, 0.900}}, new double[]{0.0, 0.3, 0.347, 0.679, 0.873, 1.0},
                new double[]{1.0E-10, 1.0E-8}, new double[]{1.0E-10, 1.0E-8},
                new RungeKuttaStepInterpolator());
        assertEquals("Embedded Runge-Kutta method", integratorVect.getName());
        assertFalse(integratorVect.isFsal());
        assertArrayEquals(new double[]{0.0, 0.3, 0.382, 0.556, 0.831, 1.0}, integratorVect.getC());
        assertArrayEquals(new double[][]{
                        {0.0, 0.25, 0.455, 0.622, 0.788, 1.0},
                        {0.0, 0.138, 0.468, 0.613, 0.775, 0.900}}, integratorVect.getA());
        assertArrayEquals(new double[]{0.0, 0.3, 0.347, 0.679, 0.873, 1.0}, integratorVect.getB());
        assertNotNull(integratorVect.getPrototype());
        assertEquals(-1.0 / 6.0, integratorVect.getExp(), 1e-10);
        assertEquals(0.9, integratorVect.getSafety(), 1e-10);
        assertEquals(0.2, integratorVect.getMinReduction(), 1e-10);
        assertEquals(10.0, integratorVect.getMaxGrowth(), 1e-10);
        assertArrayEquals(new double[]{1.0E-10, 1.0E-8}, integratorVect.getScalAbsoluteTolerance());
        assertArrayEquals(new double[]{1.0E-10, 1.0E-8}, integratorVect.getScalRelativeTolerance());

        // Test getOrder
        assertEquals(6, integrator.getOrder());
        assertEquals(6, integratorVect.getOrder());

        // Test getSafety
        assertEquals(0.9, integrator.getSafety(), 1e-10);
        assertEquals(0.9, integratorVect.getSafety(), 1e-10);

        // Test setSafety
        integrator.setSafety(0.7);
        assertEquals(0.7, integrator.getSafety(), 1e-10);
        integratorVect.setSafety(0.7);
        assertEquals(0.7, integratorVect.getSafety(), 1e-10);

        // Test getMinReduction
        assertEquals(0.2, integrator.getMinReduction(), 1e-10);
        assertEquals(0.2, integratorVect.getMinReduction(), 1e-10);

        // Test setMinReduction
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 1e-10);
        integratorVect.setMinReduction(0.5);
        assertEquals(0.5, integratorVect.getMinReduction(), 1e-10);

        // Test getMaxGrowth
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-10);
        assertEquals(10.0, integratorVect.getMaxGrowth(), 1e-10);

        // Test setMaxGrowth
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 1e-10);
        integratorVect.setMaxGrowth(5.0);
        assertEquals(5.0, integratorVect.getMaxGrowth(), 1e-10);

    }

}