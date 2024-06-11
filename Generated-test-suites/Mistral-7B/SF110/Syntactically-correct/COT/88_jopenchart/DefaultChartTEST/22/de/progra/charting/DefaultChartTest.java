package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private ChartDataModel model;
 private Title title;
 private Legend legend;
 private CoordSystem coordSystem;
 private DefaultChart chart;

 @Test
 public void testDefaultChartConstruction() {
 chart = new DefaultChart(new ChartDataModel(), "Default Chart");
 assertNotNull(chart);
 }

 @Test
 public void testChartConstructionWithTitle() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText());
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystem() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y);
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 assertNotNull(chart.getCoordSystem());
 }

 @Test
 public void testChartConstructionWithTitleAndCoordinateSystemAndLabels() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis Label", "Y-Axis Label");
 assertNotNull(chart);
 assertSame(title, chart.getTitle());
 assertNotNull(chart.getCoordSystem());
 assertSame("X-Axis Label", chart.getCoordSystem().getXAxisUnit());
 assertSame("Y-Axis Label", chart.getCoordSystem().getYAxisUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCoordinateSystemOption() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 try {
 chart = new DefaultChart(model, title.getText(), 2);
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetPreferredSize() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText());
 assertNull(chart.getPreferredSize());
 }

 @Test
 public void testRenderMethod() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText());
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 }

 @Test
 public void testRenderMethodWithTitle() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 chart = new DefaultChart(model, title.getText());
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 Title expectedTitle = new Title("Test Chart Title");
 verify(title).setBounds(eq(new Rectangle2D.Double(width / 2 - (double)expectedTitle.getPreferredSize().getWidth() / 2.0, 0, expectedTitle.getPreferredSize().getWidth(), expectedTitle.getPreferredSize().getHeight())));
 verify(title).render(g);
 }

 @Test
 public void testRenderMethodWithTitleAndLegend() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 Legend legend = new Legend(new RowColorModel(model));
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y);
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 Title expectedTitle = new Title("Test Chart Title");
 verify(title).setBounds(eq(new Rectangle2D.Double(width / 2 - (double)expectedTitle.getPreferredSize().getWidth() / 2.0, 0, expectedTitle.getPreferredSize().getWidth(), expectedTitle.getPreferredSize().getHeight())));
 verify(title).render(g);
 Legend expectedLegend = new Legend(new RowColorModel(model));
 verify(legend).setBounds(eq(new Rectangle2D.Double(width - legend.getPreferredSize().getWidth(), height / 2 - legend.getPreferredSize().getHeight() / 2.0, legend.getPreferredSize().getWidth(), legend.getPreferredSize().getHeight())));
 verify(legend).render(g);
 }

 @Test
 public void testRenderMethodWithTitleAndLegendAndCoordinateSystem() {
 model = new ChartDataModel();
 title = new Title("Test Chart Title");
 Legend legend = new Legend(new RowColorModel(model));
 coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X-Axis Label", "Y-Axis Label");
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 Title expectedTitle = new Title("Test Chart Title");
 verify(title).setBounds(eq(new Rectangle2D.Double(width / 2 - (double)expectedTitle.getPreferredSize().getWidth() / 2.0, 0, expectedTitle.getPreferredSize().getWidth(), expectedTitle.getPreferredSize().getHeight())));
 verify(title).render(g);
 Legend expectedLegend = new Legend(new RowColorModel(model));
 verify(legend).setBounds(eq(new Rectangle2D.Double(width - legend.getPreferredSize().getWidth(), height / 2 - legend.getPreferredSize().getHeight() / 2.0, legend.getPreferredSize().getWidth(), legend.getPreferredSize().getHeight())));
 verify(legend).render(g);
 verify(coordSystem).setBounds(eq(new Rectangle2D.Double(0, 0, width - legend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight() - 5)));
 verify(coordSystem).render(g);
 }

 @Test
 public void testStaticInitializer() {
 assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
 assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
 }

 private Graphics2D createMockGraphics2D() {
 return mock(Graphics2D.class);
 }
}