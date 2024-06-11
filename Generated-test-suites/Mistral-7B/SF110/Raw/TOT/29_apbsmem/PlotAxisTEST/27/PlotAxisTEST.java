package jahuwaldt.plot.test;

import jahuwaldt.plot.*;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlotAxisTest {
    @Test
    public void testEmptyPlotAxis() {
        // Test instantiation and default values
        PlotAxis plotAxis = new PlotAxis();

        // Test methods
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());

        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertSame(scale, plotAxis.getScale());

        NumberFormat numberFormat = NumberFormat.getInstance();
        plotAxis.setNumberFormat(numberFormat);
        assertSame(numberFormat, plotAxis.getNumberFormat());

        plotAxis.setGridStyle(PlotAxis.kTickMarksOnly);
        assertEquals(1, plotAxis.getGridStyle());

        plotAxis.manualLowerBounds(true);
        plotAxis.setLowerBounds(5.0);
        assertEquals(5.0, plotAxis.getLowerBounds(), 0.01);

        plotAxis.manualUpperBounds(true);
        plotAxis.setUpperBounds(10.0);
        assertEquals(10.0, plotAxis.getUpperBounds(), 0.01);

        plotAxis.manualTickStepSize(true);
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.01);

        // Test setAxisBounds
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        assertNotNull(plotAxis.getAxisBounds());
        assertEquals(plotRunList, plotAxis.getAxisBounds());

        // Test draw method (mock Graphics, Component, and Rectangle)
        Graphics graphics = mock(Graphics.class);
        Component component = mock(Component.class);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        plotAxis.draw(graphics, component, rectangle);
        verify(graphics, atLeastOnce()).drawLine(argThat(line -> line.x1 == 0 && line.y1 == 0 && line.x2 == 10 && line.y2 == 0), any());
        verifyNoMoreInteractions(graphics);
    }

    @Test
    public void testPlotAxisWithArguments() {
        // Test instantiation with arguments and default values overridden
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat numberFormat = new DecimalFormat("#.##");
        int gridStyle = PlotAxis.kMajorGrid;
        PlotAxis plotAxis = new PlotAxis(label, scale, numberFormat, gridStyle);

        // Test methods
        plotAxis.setLabel("New Test Label");
        assertEquals("New Test Label", plotAxis.getLabel());

        PlotAxisScale newScale = new LogAxisScale();
        plotAxis.setScale(newScale);
        assertSame(newScale, plotAxis.getScale());

        NumberFormat newNumberFormat = new NumberFormat() {
            @Override
            public String format(Object obj) {
                return "Formatted: " + obj.toString();
            }
        };
        plotAxis.setNumberFormat(newNumberFormat);
        assertSame(newNumberFormat, plotAxis.getNumberFormat());

        plotAxis.setGridStyle(PlotAxis.kTick);
        assertEquals(3, plotAxis.getGridStyle());

        plotAxis.manualLowerBounds(true);
        plotAxis.setLowerBounds(3.0);
        assertEquals(3.0, plotAxis.getLowerBounds(), 0.01);

        plotAxis.manualUpperBounds(true);
        plotAxis.setUpperBounds(7.0);
        assertEquals(7.0, plotAxis.getUpperBounds(), 0.01);

        plotAxis.manualTickStepSize(true);
        plotAxis.setTickStepSize(1.0);
        assertEquals(1.0, plotAxis.getTickStepSize(), 0.01);

        // Test setAxisBounds
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        assertNotNull(plotAxis.getAxisBounds());
        assertEquals(plotRunList, plotAxis.getAxisBounds());

        // Test draw method (mock Graphics, Component, and Rectangle)
        Graphics graphics = mock(Graphics.class);
        Component component = mock(Component.class);
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        plotAxis.draw(graphics, component, rectangle);
        verify(graphics, atLeastOnce()).drawLine(argThat(line -> line.x1 == 0 && line.y1 == 0 && line.x2 == 10 && line.y2 == 0), any());
        verifyNoMoreInteractions(graphics);
    }
}