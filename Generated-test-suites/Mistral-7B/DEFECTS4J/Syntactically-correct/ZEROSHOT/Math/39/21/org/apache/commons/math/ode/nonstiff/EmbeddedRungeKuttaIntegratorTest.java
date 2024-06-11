import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Test constructor with no arguments
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaMethod", false, null, null, null, null, 0.0, 0.0, null, null);
        Assert.assertEquals("EmbeddedRungeKuttaMethod", integrator.getName());
        Assert.assertFalse(integrator.isFSAL());
        Assert.assertNull(integrator.getC());
        Assert.assertNull(integrator.getA());
        Assert.assertNull(integrator.getB());
        Assert.assertNull(integrator.getPrototype());
        Assert.assertEquals(0.0, integrator.getMinStep(), 1e-10);
        Assert.assertEquals(0.0, integrator.getMaxStep(), 1e-10);
        Assert.assertNull(integrator.vecAbsoluteTolerance);
        Assert.assertNull(integrator.vecRelativeTolerance);

        // Test constructor with arguments
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.0}, {0.23562277660168379, 0.678723362825083, 0.23562277660168379, 1.0},
                {0.32211382338131525, 0.82741012526921642, 0.32211382338131525, 1.0},
                {0.66666666666666667, 1.3320409363171294, 0.66666666666666667, 1.0}};
        double[] b = {0.25, 0.6, 0.75, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaMethod", false, c, a, b, prototype, 0.1, 1.0, null, null);
        Assert.assertEquals("EmbeddedRungeKuttaMethod", integrator.getName());
        Assert.assertFalse(integrator.isFSAL());
        Assert.assertArrayEquals(c, integrator.getC(), 1e-10);
        Assert.assertArrayEquals(a, integrator.getA(), 1e-10);
        Assert.assertArrayEquals(b, integrator.getB(), 1e-10);
        Assert.assertNotNull(integrator.getPrototype());
        Assert.assertEquals(0.1, integrator.getMinStep(), 1e-10);
        Assert.assertEquals(1.0, integrator.getMaxStep(), 1e-10);
        Assert.assertNull(integrator.vecAbsoluteTolerance);
        Assert.assertNull(integrator.vecRelativeTolerance);

        // Test getOrder method
        integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaMethod", false, c, a, b, prototype, 0.1, 1.0, null, null);
        int order = integrator.getOrder();
        Assert.assertEquals(4, order);

        // Test getSafety method
        integrator.setSafety(0.5);
        double safety = integrator.getSafety();
        Assert.assertEquals(0.5, safety, 1e-10);

        // Test setSafety method
        integrator.setSafety(0.8);
        integrator.setSafety(0.5);
        safety = integrator.getSafety();
        Assert.assertEquals(0.5, safety, 1e-10);

        // Test getMinReduction method
        integrator.setMinReduction(0.3);
        double minReduction = integrator.getMinReduction();
        Assert.assertEquals(0.3, minReduction, 1e-10);

        // Test setMinReduction method
        integrator.setMinReduction(0.1);
        integrator.setMinReduction(0.3);
        minReduction = integrator.getMinReduction();
        Assert.assertEquals(0.3, minReduction, 1e-10);

        // Test getMaxGrowth method
        integrator.setMaxGrowth(2.0);
        double maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals(2.0, maxGrowth, 1e-10);

        // Test setMaxGrowth method
        integrator.setMaxGrowth(1.5);
        integrator.setMaxGrowth(2.0);
        maxGrowth = integrator.getMaxGrowth();
        Assert.assertEquals(2.0, maxGrowth, 1e-10);

    }

}