package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;

import org.junit.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;

public class DefaultChartTest {

 private DefaultChart chart;

 @Before
 public void setUp() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 }

 @Test
 public void testDefaultConstructor() {
 assertNull(this.chart.getTitle());
 assertNull(this.chart.getLegend());
 assertNull(this.chart.getCoordSystem());
 assertNotNull(this.chart.getRowColorModel());
 }

 @Test
 public void testChartWithTitleConstructor() {
 Title title = new Title("Test Title");
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 assertSame(title, this.chart.getTitle());
 assertNull(this.chart.getLegend());
 assertNull(this.chart.getCoordSystem());
 assertNotNull(this.chart.getRowColorModel());
 }

 @Test
 public void testChartWithTitleAndCoordinateSystemConstructor() {
 Title title = new Title("Test Title");
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 assertSame(title, this.chart.getTitle());
 assertNotNull(this.chart.getCoordSystem());
 }

 @Test
 public void testGetPreferredSize() {
 assertNull(this.chart.getPreferredSize());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics2D() {
 this.chart.render(null);
 }

 @Test
 public void testRender() {
 Graphics2D g = this.createMockGraphics2D();
 this.chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 }

 @Test
 public void testRenderWithTitleAndLegend() {
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 this.chart.setTitle(title);
 this.chart.setLegend(legend);
 Graphics2D g = this.createMockGraphics2D();
 this.chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(title).render(g);
 verify(legend).render(g);
 }

 @Test
 public void testRenderWithTitleAndLegendAndCoordinateSystem() {
 Title title = new Title("Test Title");
 Legend legend = new Legend(new RowColorModel(new ChartDataModel()));
 CoordSystem coordSystem = new CoordSystem(new ChartDataModel());
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
 this.chart.setTitle(title);
 this.chart.setLegend(legend);
 this.chart.setCoordSystem(coordSystem);
 Graphics2D g = this.createMockGraphics2D();
 this.chart.render(g);
 verify(g).setColor(Color.white);
 verify(g).fillRect(0, 0, anyInt(), anyInt());
 verify(g).setColor(Color.black);
 verify(title).render(g);
 verify(legend).render(g);
 verify(coordSystem).render(g);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetCoordSystemWithInvalidCoordinateSystemType() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 this.chart.setCoordSystem(new CoordSystem(model, -1));
 }

 @Test(expected = NullPointerException.class)
 public void testSetTitleWithNullTitle() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 this.chart.setTitle(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetLegendWithNullLegend() {
 ChartDataModel model = new ChartDataModel();
 this.chart = new DefaultChart(model, "Test Chart");
 this.chart.setLegend(null);
 }

 private Graphics2D createMockGraphics2D() {
 return Mockito.mock(Graphics2D.class);
 }

}