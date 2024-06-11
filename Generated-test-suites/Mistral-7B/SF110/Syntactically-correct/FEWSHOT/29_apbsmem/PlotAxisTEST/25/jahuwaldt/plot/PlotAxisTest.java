package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructors() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);

        PlotAxis axisWithLabel = new PlotAxis("Test Label", null, null, 1);
        assertNotNull(axisWithLabel);
        assertEquals("Test Label", axisWithLabel.getLabel());
    }

    @Test
    public void testSettersAndGetters() {
        PlotAxis axis = new PlotAxis();

        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());

        axis.setScale(new LogAxisScale());
        assertNotNull(axis.getScale());

        axis.setNumberFormat(NumberFormat.getPercentInstance());
        assertNotNull(axis.getNumberFormat());

        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());

        axis.manualLowerBounds(true);
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds());

        axis.manualUpperBounds(true);
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds());

        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList list = new PlotRunList();

        axis.setAxisBounds(list);

        // To be implemented in subclasses
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);

        axis.setSize(plotFrame);

        // To be implemented in subclasses
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Graphics graphics = new MockGraphics();
        Component component = new MockComponent();
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        axis.draw(graphics, component, rectangle);

        // To be implemented in subclasses
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();

        axis.xA = 1.0;
        axis.xB = 10.0;
        double x = 5.0;

        int result = axis.scaleCoord(x);

        assertEquals(6, result); // 1.0 * 5.0 + 1.0 * 10.0 = 6.0
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();

        PlotAxis clonedAxis = axis.clone();

        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getClass(), clonedAxis.getClass());
        assertEquals(axis.getLabel(), clonedAxis.getLabel());
        assertEquals(axis.getScale(), clonedAxis.getScale());
        assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
    }
}

class MockGraphics implements Graphics {
    // Empty implementation
}

class MockComponent implements Component {
    // Empty implementation
}