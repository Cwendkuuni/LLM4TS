package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelDataSeries;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.mockito.ChartDataModelMock;
import de.progra.charting.test.mockito.CoordSystemMock;
import de.progra.charting.test.mockito.DefaultChartMock;
import de.progra.charting.test.mockito.LegendMock;
import de.progra.charting.test.mockito.TitleMock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitle() {
        Title titleMock = Mockito.mock(Title.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", titleMock);
        Mockito.verify(titleMock).setChart(chart);
        assertEquals("Title", chart.getTitle().getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystem() {
        Title titleMock = Mockito.mock(Title.class);
        CoordSystem coordSystemMock = Mockito.mock(CoordSystem.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", CoordSystem.LINEAR_X_LINEAR_Y, titleMock, coordSystemMock);
        Mockito.verify(titleMock).setChart(chart);
        Mockito.verify(coordSystemMock).setChart(chart);
        assertEquals("Title", chart.getTitle().getText());
        assertSame(titleMock, chart.getTitle());
        assertSame(coordSystemMock, chart.getCoordSystem());
        assertNull(chart.getLegend());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystemAndLegend() {
        Title titleMock = Mockito.mock(Title.class);
        CoordSystem coordSystemMock = Mockito.mock(CoordSystem.class);
        Legend legendMock = Mockito.mock(Legend.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", CoordSystem.LINEAR_X_LINEAR_Y, titleMock, coordSystemMock, legendMock);
        Mockito.verify(titleMock).setChart(chart);
        Mockito.verify(coordSystemMock).setChart(chart);
        Mockito.verify(legendMock).setChart(chart);
        assertEquals("Title", chart.getTitle().getText());
        assertSame(titleMock, chart.getTitle());
        assertSame(coordSystemMock, chart.getCoordSystem());
        assertSame(legendMock, chart.getLegend());
        assertNull(chart.getChartRenderer());
    }

    @Test
    public void testDefaultChartGetPreferredSizeReturnsNull() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRenderWithTitle() {
        Title titleMock = Mockito.mock(Title.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", titleMock);
        Graphics2D mockGraphics2D = Mockito.mock(Graphics2D.class);

        Mockito.when(titleMock.getPreferredSize()).thenReturn(new Dimension(100, 50));
        Mockito.when(titleMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);

        chart.render(mockGraphics2D);

        Mockito.verify(mockGraphics2D).fillRect(0, 0, 1, 1); // Fills the background with white color
        Mockito.verify(mockGraphics2D).setColor(eq(Color.black));
        Mockito.verify(mockGraphics2D).drawString("Title", (float)(100 / 2.0) - 50, 20); // Draws the title
    }

    @Test
    public void testRenderWithTitleAndLegend() {
        Title titleMock = Mockito.mock(Title.class);
        Legend legendMock = Mockito.mock(Legend.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", titleMock, legendMock);
        Graphics2D mockGraphics2D = Mockito.mock(Graphics2D.class);

        Mockito.when(titleMock.getPreferredSize()).thenReturn(new Dimension(100, 50));
        Mockito.when(titleMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);
        Mockito.when(legendMock.getPreferredSize()).thenReturn(new Dimension(150, 50));
        Mockito.when(legendMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);

        chart.render(mockGraphics2D);

        Mockito.verify(mockGraphics2D).fillRect(0, 0, 1, 1); // Fills the background with white color
        Mockito.verify(mockGraphics2D).setColor(eq(Color.black));
        Mockito.verify(mockGraphics2D).drawString("Title", (float)(100 / 2.0) - 50, 20); // Draws the title
        Mockito.verify(mockGraphics2D).draw(any(Shape.class)); // Draws the legend
    }

    @Test
    public void testRenderWithTitleAndLegendAndCoordinateSystem() {
        Title titleMock = Mockito.mock(Title.class);
        Legend legendMock = Mockito.mock(Legend.class);
        CoordSystem coordSystemMock = Mockito.mock(CoordSystem.class);
        DefaultChart chart = new DefaultChart(new ChartDataModel(), "Title", titleMock, coordSystemMock, legendMock);
        Graphics2D mockGraphics2D = Mockito.mock(Graphics2D.class);

        Mockito.when(titleMock.getPreferredSize()).thenReturn(new Dimension(100, 50));
        Mockito.when(titleMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);
        Mockito.when(legendMock.getPreferredSize()).thenReturn(new Dimension(150, 50));
        Mockito.when(legendMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);
        Mockito.when(coordSystemMock.getPreferredSize()).thenReturn(new Dimension(200, 50));
        Mockito.when(coordSystemMock.render(Mockito.any(Graphics2D.class))).thenReturn(true);

        chart.render(mockGraphics2D);

        Mockito.verify(mockGraphics2D).fillRect(0, 0, 1, 1); // Fills the background with white color
        Mockito.verify(mockGraphics2D).setColor(eq(Color.black));
        Mockito.verify(mockGraphics2D).drawString("Title", (float)(100 / 2.0) - 50, 20); // Draws the title
        Mockito.verify(mockGraphics2D).draw(any(Shape.class)); // Draws the legend
        Mockito.verify(mockGraphics2D).draw(any(Shape.class)); // Draws the coordinate system
    }

    @Test
    public void testRenderWithData() {
        ChartDataModel chartDataModel = new ChartDataModel();
        List<ModelDataSeries> series1 = Arrays.asList(new ModelDataSeries(new String[]{"A1", "A2", "A3"}, new Number[]{1, 2, 3}));
        List<ModelDataSeries> series2 = Arrays.asList(new ModelDataSeries(new String[]{"B1", "B2", "B3"}, new Number[]{4, 5, 6}));
        Map<String, AbstractChartRenderer> chartRenderer = new HashMap<>();
        AbstractChartRenderer renderer1 = Mockito.mock(AbstractChartRenderer.class);
        AbstractChartRenderer renderer2 = Mockito.mock(AbstractChartRenderer.class);
        chartRenderer.put("Series1", renderer1);
        chartRenderer.put("Series2", renderer2);

        DefaultChart chart = new DefaultChart(chartDataModel, "Title", null, null, null, null, chartRenderer);
        Graphics2D mockGraphics2D = Mockito.mock(Graphics2D.class);

        for (AbstractChartRenderer renderer : chartRenderer.values()) {
            Mockito.when(renderer.getPreferredSize()).thenReturn(new Dimension(150, 50));
            Mockito.when(renderer.render(Mockito.any(Graphics2D.class))).thenReturn(true);
        }

        chart.render(mockGraphics2D);

        for (AbstractChartRenderer renderer : chartRenderer.values()) {
            Mockito.verify(renderer).render(mockGraphics2D);
        }
    }
}