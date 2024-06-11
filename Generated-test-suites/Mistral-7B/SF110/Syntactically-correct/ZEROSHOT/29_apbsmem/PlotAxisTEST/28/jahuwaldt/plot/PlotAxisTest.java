package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis xAxis;
 private PlotAxis yAxis;

 @Before
 public void setUp() {
 xAxis = new LinearPlotAxis("X-Axis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 yAxis = new LinearPlotAxis("Y-Axis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 }

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 }

 @Test
 public void testSetLabel() {
 xAxis.setLabel("X-Axis Label");
 assertEquals("X-Axis Label", xAxis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxisScale scale = new LogAxisScale();
 xAxis.setScale(scale);
 assertEquals(scale, xAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 DecimalFormat format = new DecimalFormat("#,##0.00");
 xAxis.setNumberFormat(format);
 assertEquals(format, xAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 xAxis.setGridStyle(PlotAxis.kTickMarksOnly);
 assertEquals(PlotAxis.kTickMarksOnly, xAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 xAxis.manualLowerBounds(true);
 xAxis.setLowerBounds(5.5);
 assertEquals(5.5, xAxis.getLowerBounds(), 0.001);
 xAxis.manualLowerBounds(false);
 xAxis.setLowerBounds(-2.3);
 assertEquals(-2.3, xAxis.getLowerBounds(), 0.001);
 }

 @Test
 public void testManualUpperBounds() {
 xAxis.manualUpperBounds(true);
 xAxis.setUpperBounds(10.6);
 assertEquals(10.6, xAxis.getUpperBounds(), 0.001);
 xAxis.manualUpperBounds(false);
 xAxis.setUpperBounds(3.9);
 assertEquals(3.9, xAxis.getUpperBounds(), 0.001);
 }

 @Test
 public void testManualTickStepSize() {
 xAxis.manualTickStepSize(true);
 xAxis.setTickStepSize(1.5);
 assertEquals(1.5, xAxis.getTickStepSize(), 0.001);
 xAxis.manualTickStepSize(false);
 xAxis.setTickStepSize(0.8);
 assertEquals(0.8, xAxis.getTickStepSize(), 0.001);
 }

 @Test
 public void testSetAxisBounds() {
 // TODO: Implement this test
 }

 @Test
 public void testSetSize() {
 Rectangle rectangle = new Rectangle(10, 10, 200, 200);
 xAxis.setSize(rectangle);
 assertNotNull(xAxis.getSize());
 }

 @Test
 public void testDraw() {
 Graphics graphics = createMock(Graphics.class);
 Component component = createMock(Component.class);
 Rectangle rectangle = new Rectangle(10, 10, 200, 200);
 xAxis.draw(graphics, component, rectangle);
 // TODO: Implement this test
 }

 @Test
 public void testScaleCoord() {
 double x = 0.5;
 int expected = (int)(0.5 * xAxis.getAxisLength() + xAxis.getAxisStart());
 int actual = xAxis.scaleCoord(x);
 assertEquals(expected, actual);
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)xAxis.clone();
 assertNotSame(xAxis, clonedAxis);
 assertEquals(xAxis, clonedAxis);
 }

 @Test
 public void testSetAxisTickLimits() {
 xAxis.setAxisTickLimits();
 // TODO: Implement this test
 }

 @Test
 public void testResizeAxis() {
 Rectangle rectangle = new Rectangle(10, 10, 200, 200);
 xAxis.resizeAxis(rectangle);
 // TODO: Implement this test
 }

}