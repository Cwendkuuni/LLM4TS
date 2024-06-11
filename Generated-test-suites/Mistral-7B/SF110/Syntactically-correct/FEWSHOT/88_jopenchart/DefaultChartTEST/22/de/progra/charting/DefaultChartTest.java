package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.Test;

import de.progra.charting.model.*;
import java.awt.*;
import java.util.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartCreation() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNotNull(chart);
    }

    @Test
    public void testDefaultChartCreationWithCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart);
        assertTrue(chart.getCoordSystem() != null);
    }

    @Test
    public void testDefaultChartCreationWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", DefaultChart.LINEAR_X_LINEAR_Y, "XAxis", "YAxis");
        assertNotNull(chart);
        assertTrue(chart.getCoordSystem() != null);
        assertEquals("XAxis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("YAxis", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderMethod() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        ArrayList<AbstractChartRenderer> rendererList = new ArrayList<>();
        AbstractChartRenderer renderer1 = Mockito.mock(AbstractChartRenderer.class);
        AbstractChartRenderer renderer2 = Mockito.mock(AbstractChartRenderer.class);
        rendererList.add(renderer1);
        rendererList.add(renderer2);

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);
        chart.setChartRenderer(new HashMap<ChartType, AbstractChartRenderer>() {{
            put(ChartType.LINE, renderer1);
            put(ChartType.BAR, renderer2);
        }});

        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        int width = 600;
        int height = 400;

        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        when(legend.getPreferredSize()).thenReturn(new Dimension(150, 50));
        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 50, width - 150, height - 50 - 50));

        chart.render(graphics);

        verify(graphics).setColor(Color.white);
        verify(graphics).fillRect(0, 0, width, height);
        verify(graphics).setColor(Color.black);
        verify(title).render(graphics);
        verify(legend).render(graphics);
        verify(coordSystem).render(graphics);
        verify(renderer1, times(1)).render(graphics);
        verify(renderer2, times(1)).render(graphics);
    }

    @Test
    public void testRenderMethodWithNullTitleAndLegend() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        Graphics2D graphics = Mockito.mock(Graphics2D.class);
        int width = 600;
        int height = 400;

        chart.render(graphics);

        verifyNoMoreInteractions(graphics);
    }
}