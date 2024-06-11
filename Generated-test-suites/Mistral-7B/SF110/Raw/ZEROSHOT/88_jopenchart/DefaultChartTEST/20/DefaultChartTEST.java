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

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;
 private ChartDataModel model;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 model.setDataPoints(Arrays.asList(new DataPoint("A", 1.0, 1.0), new DataPoint("B", 2.0, 2.0), new DataPoint("C", 3.0, 3.0)));
 chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testEmptyConstructor() {
 assertNotNull(chart);
 assertTrue(chart.getTitle() == null);
 assertTrue(chart.getLegend() == null);
 assertTrue(chart.getCoordSystem() == null);
 assertTrue(chart.getChartRenderer().isEmpty());
 }

 @Test
 public void testTitleConstructor() {
 Title title = new Title("Test Title");
 chart = new DefaultChart(model, title.getText());
 assertTrue(title.equals(chart.getTitle()));
 }

 @Test
 public void testTitleAndCoordConstructor() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "", "");
 assertTrue(title.equals(chart.getTitle()));
 assertTrue(coordSystem.equals(chart.getCoordSystem()));
 }

 @Test
 public void testTitleAndCoordAndAxisConstructor() {
 Title title = new Title("Test Title");
 CoordSystem coordSystem = new CoordSystem(model);
 chart = new DefaultChart(model, title.getText(), DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
 assertTrue(title.equals(chart.getTitle()));
 assertTrue(coordSystem.equals(chart.getCoordSystem()));
 assertTrue(chart.getCoordSystem().getXAxisUnit().equals("X Axis"));
 assertTrue(chart.getCoordSystem().getYAxisUnit().equals("Y Axis"));
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(chart.getPreferredSize());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderNullGraphics() {
 chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = createMockGraphics2D();
 chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 Title title = chart.getTitle();
 Legend legend = chart.getLegend();
 CoordSystem coordSystem = chart.getCoordSystem();
 Collection<AbstractChartRenderer> renderer = chart.getChartRenderer().values();

 verify(g).setColor(Color.white);
 verify(g).fillRect(0, anyInt(), anyInt(), anyInt());
 if (title != null) {
 Title expectedTitle = new Title(title.getText());
 expectedTitle.setBounds(eq(new Rectangle2D.Double(width / 2 - (expectedTitle.getPreferredSize().getWidth() / 2.0), 0, expectedTitle.getPreferredSize().getWidth(), expectedTitle.getPreferredSize().getHeight())));
 title.render(g);
 verify(g).setColor(Color.black);
 verify(g).fillRect(expectedTitle.getBounds().getX(), expectedTitle.getBounds().getY(), expectedTitle.getBounds().getWidth(), expectedTitle.getBounds().getHeight());
 }
 if (legend != null) {
 Legend expectedLegend = new Legend(chart.getRowColorModel());
 expectedLegend.setBounds(eq(new Rectangle2D.Double((int)(width - expectedLegend.getPreferredSize().getWidth()), (int)(height / 2 - expectedLegend.getPreferredSize().getHeight() / 2.0 + title.getPreferredSize().getHeight()), expectedLegend.getPreferredSize().getWidth(), expectedLegend.getPreferredSize().getHeight())));
 legend.render(g);
 verify(g).setColor(Color.black);
 verify(g).fillRect(expectedLegend.getBounds().getX(), expectedLegend.getBounds().getY(), expectedLegend.getBounds().getWidth(), expectedLegend.getBounds().getHeight());
 }
 if (coordSystem != null) {
 coordSystem.setBounds(eq(new Rectangle2D.Double(0, title.getPreferredSize().getHeight(), (width - legend.getPreferredSize().getWidth()), (height - title.getPreferredSize().getHeight() - 5))));
 for (AbstractChartRenderer rendererInstance : renderer) {
 AbstractChartRenderer expectedRenderer = mock(AbstractChartRenderer.class);
 rendererInstance.setBounds(eq(new Rectangle2D.Double(0, title.getPreferredSize().getHeight(), width - legend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight() - 5)));
 rendererInstance.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, title.getPreferredSize().getHeight(), width - legend.getPreferredSize().getWidth(), height - title.getPreferredSize().getHeight() - 5);
 verify(g).setColor(rendererInstance.getLineColor());
 // Add more verifications based on the AbstractChartRenderer implementations
 }
 coordSystem.render(g);
 // Add more verifications based on the CoordSystem implementation
 }
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = mock(Graphics2D.class);
 return g;
 }

}