package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.model.TestChartDataModel;
import de.progra.charting.test.render.TestChartRenderer;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertEquals("Test Chart", chart.getTitle().getText());
        assertTrue(chart.getCoordSystem() == null);
    }

    @Test
    public void testLinearXLinearYCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCoordinateSystem() {
        new DefaultChart(new TestChartDataModel(), "Test Chart", 2);
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        Graphics2D g = mock(Graphics2D.class);

        Title title = mock(Title.class);
        when(title.getPreferredSize()).thenReturn(new Dimension(100, 50));
        chart.setTitle(title);

        Legend legend = mock(Legend.class);
        when(legend.getPreferredSize()).thenReturn(new Dimension(150, 50));
        chart.setLegend(legend);

        CoordSystem coordSystem = mock(CoordSystem.class);
        when(coordSystem.getBounds()).thenReturn(new Rectangle(0, 0, 600, 400));
        chart.setCoordSystem(coordSystem);

        Set<AbstractChartRenderer> renderers = new HashSet<>();
        renderers.add(new TestChartRenderer());
        chart.setChartRenderer(new HashMap<Model, AbstractChartRenderer>() {{
            put(new TestChartDataModel(), renderers.iterator().next());
        }});

        chart.render(g);

        // Check title rendering
        verify(title).setBounds(argThat(r -> r.x == 300 && r.width == 100 && r.height == 50));
        verify(title).render(g);

        // Check legend rendering
        verify(legend).setBounds(argThat(r -> r.x == 750 && r.width == 150 && r.height == 50 && r.y == 200));
        verify(legend).render(g);

        // Check coord system rendering
        verify(coordSystem).setBounds(argThat(r -> r.x == 0 && r.width == 600 && r.height == 400 && r.y == 0));
        verify(coordSystem).render(g);

        // Check renderer rendering
        for (AbstractChartRenderer renderer : renderers) {
            verify(renderer).setBounds(argThat(r -> r.x == 0 && r.width == 600 && r.height == 400 && r.y == 0));
            verify(renderer).render(g);
        }

        // Check background filling
        verify(g).fillRect(0, 0, 600, 400);
    }
}