package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.Title;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.test.util.ChartDataModelTestUtil;
import de.progra.charting.test.util.ChartRendererTestUtil;

public class DefaultChartTest {

    @Test
    public void testEmptyDefaultChart() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertTrue(chart instanceof AbstractChart);
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("Title", title.getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystem() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertTrue(chart instanceof AbstractChart);
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("Title", title.getText());
        CoordSystem cs = chart.getCoordSystem();
        assertNotNull(cs);
        assertNull(chart.getLegend());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemAndLegend() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        assertTrue(chart instanceof AbstractChart);
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals("Title", title.getText());
        CoordSystem cs = chart.getCoordSystem();
        assertNotNull(cs);
        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals("X-Axis", cs.getXAxisUnit());
        assertEquals("Y-Axis", cs.getYAxisUnit());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testRenderTitle() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        chart.render(g);
        verify(g).drawString("Test Title", (float)(chart.getBounds().getWidth() / 2 - (title.getPreferredSize().getWidth() / 2.0)), 0);
    }

    @Test
    public void testRenderLegend() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        ChartRenderer renderer = ChartRendererTestUtil.getTestRenderer();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Legend legend = new Legend(new RowColorModel(model));
        chart.setLegend(legend);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        chart.render(g);
        verify(g).drawString("Test Title", (float)(chart.getBounds().getWidth() / 2 - (title.getPreferredSize().getWidth() / 2.0)), 0);
        verify(g).draw(Mockito.any(Shape.class));
        verify(g).setColor(Color.BLACK);
    }

    @Test
    public void testRenderChartRenderer() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        ChartRenderer renderer = ChartRendererTestUtil.getTestRenderer();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Legend legend = new Legend(new RowColorModel(model));
        chart.setLegend(legend);
        ChartRenderer chartRenderer = Collections.singletonMap("TestRenderer", renderer);
        chart.setChartRenderer(chartRenderer);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        chart.render(g);
        verify(g).drawString("Test Title", (float)(chart.getBounds().getWidth() / 2 - (title.getPreferredSize().getWidth() / 2.0)), 0);
        verify(g).draw(Mockito.any(Shape.class));
        verify(renderer).render(g);
    }

    @Test
    public void testRenderCoordinateSystem() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        CoordSystem cs = new CoordSystem(model);
        chart.setCoordSystem(cs);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        chart.render(g);
        verify(g).setColor(Color.WHITE);
        verify(g).fillRect(0, 0, (float)chart.getBounds().getWidth(), (float)chart.getBounds().getHeight());
        verify(g).setColor(Color.BLACK);
        verify(cs).render(g);
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = ChartDataModelTestUtil.getTestDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis", "Y-Axis");
        Title title = new Title("Test Title");
        chart.setTitle(title);
        Legend legend = new Legend(new RowColorModel(model));
        chart.setLegend(legend);
        ChartRenderer renderer = ChartRendererTestUtil.getTestRenderer();
        ChartRenderer chartRenderer = Collections.singletonMap("TestRenderer", renderer);
        chart.setChartRenderer(chartRenderer);
        CoordSystem cs = new CoordSystem(model);
        chart.setCoordSystem(cs);
        Dimension expectedSize = new Dimension(500, 500);
        chart.setBounds(new Rectangle(0, 0, 500, 500));
        Dimension actualSize = chart.getPreferredSize();
        assertEquals(expectedSize, actualSize);
    }
}