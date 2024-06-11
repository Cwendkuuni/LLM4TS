package de.progra.charting.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.ModelData;
import de.progra.charting.RowColorModel;
import de.progra.charting.Title;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.test.model.TestChartDataModel;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstruction() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertTrue(chart.getChartDataModel() instanceof TestChartDataModel);
        assertTrue(chart.getRowColorModel() instanceof RowColorModel);
        assertTrue(chart.getLegend() instanceof Legend);
        assertTrue(chart.getTitle() instanceof Title);
        assertTrue(chart.getCoordSystem() instanceof CoordSystem);
    }

    @Test
    public void testDefaultChartWithTitle() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertTrue(chart.getChartDataModel() instanceof TestChartDataModel);
        assertTrue(chart.getRowColorModel() instanceof RowColorModel);
        assertTrue(chart.getLegend() == null);
        assertTrue(chart.getTitle() != null);
    }

    @Test
    public void testDefaultChartWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        Title title = chart.getTitle();
        assertEquals("Test Chart", title.getText());
        Legend legend = chart.getLegend();
        assertNull(legend);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertEquals("X Axis", coordSystem.getXAxisUnit());
        assertEquals("Y Axis", coordSystem.getYAxisUnit());
        assertTrue(chart.getChartDataModel() instanceof TestChartDataModel);
        assertTrue(chart.getRowColorModel() instanceof RowColorModel);
        assertTrue(chart.getLegend() == null);
        assertTrue(chart.getTitle() != null);
    }

    @Test
    public void testDefaultChartPreferredSize() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testDefaultChartRender() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart");
        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        g.getFontMetrics().getStringBounds("Test Chart", g).width > 0;
    }

    @Test
    public void testDefaultChartRenderWithTitleAndLegend() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = new Title("Test Chart Title");
        Legend legend = new Legend(new RowColorModel(new TestChartDataModel()));

        chart.setTitle(title);
        chart.setLegend(legend);

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        Rectangle2D titleBounds = g.getFontMetrics().getStringBounds("Test Chart Title", g);
        Rectangle2D legendBounds = g.getFontMetrics().getStringBounds("Test Chart", g);

        title.setBounds(new Rectangle((int)titleBounds.getX(), (int)titleBounds.getY(), (int)titleBounds.getWidth(), (int)titleBounds.getHeight()));
        legend.setBounds(new Rectangle((int)(g.getClipBounds().getWidth() - legendBounds.getWidth()), (int)(g.getClipBounds().getHeight() / 2 - legendBounds.getHeight() / 2), (int)legendBounds.getWidth(), (int)legendBounds.getHeight()));

        title.render(g);
        legend.render(g);

        assertTrue(title.getBounds().intersects(titleBounds));
        assertTrue(legend.getBounds().intersects(legendBounds));
    }

    @Test
    public void testDefaultChartRenderWithTitleLegendAndData() {
        DefaultChart chart = new DefaultChart(new TestChartDataModel(), "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        Title title = new Title("Test Chart Title");
        Legend legend = new Legend(new RowColorModel(new TestChartDataModel()));
        ChartRenderer chartRenderer = new MockChartRenderer();

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.getChartDataModel().setChartRenderer(chartRenderer);

        Graphics2D g = createMockGraphics2D();

        chart.render(g);

        Rectangle2D titleBounds = g.getFontMetrics().getStringBounds("Test Chart Title", g);
        Rectangle2D legendBounds = g.getFontMetrics().getStringBounds("Test Chart", g);
        Rectangle2D chartRendererBounds = g.getClipBounds();

        title.setBounds(new Rectangle((int)titleBounds.getX(), (int)titleBounds.getY(), (int)titleBounds.getWidth(), (int)titleBounds.getHeight()));
        legend.setBounds(new Rectangle((int)(g.getClipBounds().getWidth() - legendBounds.getWidth()), (int)(g.getClipBounds().getHeight() / 2 - legendBounds.getHeight() / 2), (int)legendBounds.getWidth(), (int)legendBounds.getHeight()));
        chartRenderer.setBounds(new Rectangle(0, titleBounds.getHeight(), g.getClipBounds().getWidth() - legendBounds.getWidth(), g.getClipBounds().getHeight() - titleBounds.getHeight() - 5));

        title.render(g);
        legend.render(g);
        chartRenderer.render(g);

        assertTrue(title.getBounds().intersects(titleBounds));
        assertTrue(legend.getBounds().intersects(legendBounds));
        assertTrue(chartRenderer.getBounds().intersects(chartRendererBounds));
    }

    private Graphics2D createMockGraphics2D() {
        GraphicsConfiguration gc = new MockGraphicsConfiguration();
        return (Graphics2D) gc.createGraphics();
    }

    private static class MockChartRenderer implements AbstractChartRenderer {
        @Override
        public void render(Graphics2D g) {
            // Empty implementation
        }

        @Override
        public Rectangle2D getBounds() {
            return new Rectangle2D.Double(0, 0, 100, 100);
        }

        @Override
        public Map<String, AbstractChartRenderer> getChartRenderer() {
            return new HashMap<>();
        }
    }

    private static class TestChartDataModel implements ChartDataModel {
        private Map<String, ModelData> modelData = new HashMap<>();

        public TestChartDataModel() {
            modelData.put("Test Series 1", new ModelData("Test Series 1", Arrays.asList(new double[] {1, 2, 3, 4, 5}, new double[] {1, 2, 3, 4, 5})));
            modelData.put("Test Series 2", new ModelData("Test Series 2", Arrays.asList(new double[] {1, 2, 3, 4, 5}, new double[] {1, 2, 3, 4, 5})));
        }

        @Override
        public Map<String, ModelData> getModelData() {
            return modelData;
        }

        @Override
        public AbstractChartRenderer getChartRenderer() {
            return null;
        }
    }
}