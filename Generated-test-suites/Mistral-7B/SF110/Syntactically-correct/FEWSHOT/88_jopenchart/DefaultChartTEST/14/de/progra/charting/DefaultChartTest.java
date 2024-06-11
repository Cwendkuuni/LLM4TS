package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructor() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        assertNotNull(chart.getTitle());
        assertEquals("Title", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertEquals(0, chart.getBounds().getWidth());
        assertEquals(0, chart.getBounds().getHeight());
    }

    @Test
    public void testDefaultChartWithTitleConstructor() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");

        Title title = chart.getTitle();
        assertEquals("Title", title.getText());
        assertNotNull(chart.getLegend());
        assertNull(chart.getCoordSystem());

        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals(0, legend.getBounds().getX());
        assertEquals(0, legend.getBounds().getY());
        assertEquals(0, legend.getBounds().getWidth());
        assertEquals(0, legend.getBounds().getHeight());

        assertEquals(0, chart.getBounds().getWidth());
        assertEquals(0, chart.getBounds().getHeight());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemConstructor() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);

        Title title = chart.getTitle();
        assertEquals("Title", title.getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());

        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals(0, legend.getBounds().getX());
        assertEquals(0, legend.getBounds().getY());
        assertEquals(0, legend.getBounds().getWidth());
        assertEquals(0, legend.getBounds().getHeight());

        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals(0, coordSystem.getBounds().getX());
        assertEquals(0, coordSystem.getBounds().getY());
        assertEquals(0, coordSystem.getBounds().getWidth());
        assertEquals(0, coordSystem.getBounds().getHeight());

        assertEquals(0, chart.getBounds().getWidth());
        assertEquals(0, chart.getBounds().getHeight());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemAndAxisLabelsConstructor() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");

        Title title = chart.getTitle();
        assertEquals("Title", title.getText());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());

        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals(0, legend.getBounds().getX());
        assertEquals(0, legend.getBounds().getY());
        assertEquals(0, legend.getBounds().getWidth());
        assertEquals(0, legend.getBounds().getHeight());

        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertNotNull(coordSystem.getXAxis());
        assertNotNull(coordSystem.getYAxis());
        assertEquals("X Axis", coordSystem.getXAxis().getLabel());
        assertEquals("Y Axis", coordSystem.getYAxis().getLabel());

        assertEquals(0, chart.getBounds().getWidth());
        assertEquals(0, chart.getBounds().getHeight());
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");

        Title title = new Title("Title");
        title.setBounds(new Rectangle(10, 10, 100, 30));

        Legend legend = new Legend(new RowColorModel(model));
        legend.setBounds(new Rectangle(120, 50, 100, 30));

        CoordSystem coordSystem = new CoordSystem(model);
        coordSystem.setXAxis(new Axis("X Axis", new ArrayList<>()) {
            {
                setBounds(new Rectangle(10, 90, 200, 30));
            }
        });
        coordSystem.setYAxis(new Axis("Y Axis", new ArrayList<>()) {
            {
                setBounds(new Rectangle(10, 130, 200, 30));
            }
        });
        coordSystem.setBounds(new Rectangle(10, 10, 400, 300));

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        ChartRenderer chartRenderer = new TestChartRenderer();
        chart.setChartRenderer(chartRenderer);

        Dimension expectedSize = new Dimension(430, 350);
        Dimension actualSize = chart.getPreferredSize();

        assertEquals(expectedSize.getWidth(), actualSize.getWidth());
        assertEquals(expectedSize.getHeight(), actualSize.getHeight());
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");

        Title title = new Title("Title");
        title.setBounds(new Rectangle(10, 10, 100, 30));

        Legend legend = new Legend(new RowColorModel(model));
        legend.setBounds(new Rectangle(120, 50, 100, 30));

        CoordSystem coordSystem = new CoordSystem(model);
        coordSystem.setXAxis(new Axis("X Axis", new ArrayList<>()) {
            {
                setBounds(new Rectangle(10, 90, 200, 30));
            }
        });
        coordSystem.setYAxis(new Axis("Y Axis", new ArrayList<>()) {
            {
                setBounds(new Rectangle(10, 130, 200, 30));
            }
        });
        coordSystem.setBounds(new Rectangle(10, 10, 400, 300));

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);

        ChartRenderer chartRenderer = new TestChartRenderer();
        chart.setChartRenderer(chartRenderer);

        Graphics2D g = mock(Graphics2D.class);
        chart.render(g);

        verify(g).setColor(Color.white);
        verify(g).fillRect(0, 0, 430, 350);
        verify(g).setColor(Color.black);
        verify(title).render(g);
        verify(legend).render(g);
        verify(coordSystem).render(g);
        verifyNoMoreInteractions(g);
    }
}