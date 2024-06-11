package de.progra.charting.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.progra.charting.AbstractChart;
import de.progra.charting.CoordSystem;
import de.progra.charting.Legend;
import de.progra.charting.Model.ChartDataModel;
import de.progra.charting.Render.AbstractChartRenderer;
import de.progra.charting.model.Title;

public class DefaultChartTest {

    @Test
    public void testConstructor() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(Collections.emptyList()), "Test Title");
        assertNull(chart.getCoordSystem());
        assertNull(chart.getTitle());
        assertNull(chart.getLegend());
    }

    @Test
    public void testConstructorWithTitle() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(Collections.emptyList()), "Test Title");
        Title title = chart.getTitle();
        assertEquals("Test Title", title.getText());
    }

    @Test
    public void testConstructorWithTitleAndCoordinateSystem() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(Collections.emptyList()), "Test Title", DefaultChart.LINEAR_X_LINEAR_Y);
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
    }

    @Test
    public void testConstructorWithTitleAndCoordinateSystemAndAxes() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(Collections.emptyList()), "Test Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals("X Axis", coordSystem.getXAxisUnit());
        assertEquals("Y Axis", coordSystem.getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart(new ChartDataModel(Collections.emptyList()), "Test Title");
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel(Arrays.asList(new DataPoint[0], new DataPoint[0]));
        DefaultChart chart = new DefaultChart(model, "Test Title");
        Title title = new Title("Test Title");
        Legend legend = new Legend(new RowColorModel(model));
        CoordSystem coordSystem = new CoordSystem(model);
        List<AbstractChartRenderer> rendererList = Arrays.asList(new AbstractChartRenderer[0]);

        chart.setTitle(title);
        chart.setLegend(legend);
        chart.setCoordSystem(coordSystem);
        chart.setChartRenderer(new HashMap<String, AbstractChartRenderer>() {{
            put("Renderer1", new AbstractChartRenderer() {
                @Override
                public void render(Graphics2D g) {
                    g.setColor(Color.RED);
                    g.drawString("Renderer1", 10, 10);
                }
            });
        }});

        Graphics2D graphics = null;
        Rectangle2D boundingBox = new Rectangle2D.Float(0, 0, 500, 500);

        // Test rendering with no title and legend
        chart.render(graphics = createGraphics2D(boundingBox));
        assertNull(graphics);

        // Test rendering with title only
        title.setBounds(new Rectangle(250, 0, 100, 50));
        chart.setTitle(title);
        chart.render(graphics = createGraphics2D(boundingBox));
        assertEquals(250, title.getX());
        assertEquals(0, title.getY());

        // Test rendering with title and legend
        legend.setBounds(new Rectangle(350, 250, 100, 50));
        chart.setLegend(legend);
        chart.render(graphics = createGraphics2D(boundingBox));
        assertEquals(350, legend.getX());
        assertEquals(250, legend.getY());

        // Test rendering with title, legend and renderers
        AbstractChartRenderer renderer1 = chart.getChartRenderer().get("Renderer1");
        renderer1.setBounds(new Rectangle(50, 50, 100, 100));
        chart.getCoordSystem().setBounds(new Rectangle(150, 200, 200, 300));
        chart.render(graphics = createGraphics2D(boundingBox));
        assertEquals(50, renderer1.getX());
        assertEquals(50, renderer1.getY());
        assertEquals(100, renderer1.getWidth());
        assertEquals(100, renderer1.getHeight());

        // Test rendering with title, legend and empty coordinate system
        chart.setCoordSystem(null);
        chart.render(graphics = createGraphics2D(boundingBox));
        assertNull(graphics);
    }

    private Graphics2D createGraphics2D(Rectangle2D boundingBox) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D graphics = gc.createCompatibleGraphics(boundingBox.width, boundingBox.height);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics;
    }
}