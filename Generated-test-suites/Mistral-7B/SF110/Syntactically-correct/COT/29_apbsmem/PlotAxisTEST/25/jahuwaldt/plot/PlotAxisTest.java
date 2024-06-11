package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis xAxis;

 @Before
 public void setUp() {
 xAxis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rect) {
 // no implementation needed for testing
 }

 @Override
 public void draw(Graphics g, Component c, Rectangle rect) {
 // no implementation needed for testing
 }
 };
 }

 @Test
 public void testConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertNull(plotAxis.getLabel());
 assertNotNull(plotAxis.getScale());
 assertInstanceOf(LinearAxisScale.class, plotAxis.getScale());
 assertNull(plotAxis.getNumberFormat());
 assertEquals(1, plotAxis.getGridStyle());
 }

 @Test
 public void testConstructorWithLabelScaleAndFormat() {
 PlotAxis plotAxis = new PlotAxis("Test Label", new PlotAxisScale(), null, 1);
 assertEquals("Test Label", plotAxis.getLabel());
 assertNotNull(plotAxis.getScale());
 assertInstanceOf(PlotAxisScale.class, plotAxis.getScale());
 assertNull(plotAxis.getNumberFormat());
 assertEquals(1, plotAxis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 xAxis.setLabel("Test Label");
 assertEquals("Test Label", xAxis.getLabel());
 }

 @Test
 public void testSetScale() {
 final PlotAxisScale testScale = new PlotAxisScale();
 xAxis.setScale(testScale);
 assertSame(testScale, xAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 final NumberFormat testFormat = NumberFormat.getInstance();
 xAxis.setNumberFormat(testFormat);
 assertSame(testFormat, xAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 xAxis.setGridStyle(12);
 assertEquals(12, xAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 xAxis.manualLowerBounds(true);
 assertTrue(xAxis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 xAxis.setLowerBounds(3.14);
 assertEquals(3.14, xAxis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 xAxis.setLabel(new DecimalFormat("#.###").format(3.14));
 xAxis.setLowerBounds(3.14);
 assertEquals("3.140", xAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 xAxis.manualUpperBounds(true);
 assertTrue(xAxis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 xAxis.setUpperBounds(6.28);
 assertEquals(6.28, xAxis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 xAxis.setLabel(new DecimalFormat("#.###").format(6.28));
 xAxis.setUpperBounds(6.28);
 assertEquals("6.280", xAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 xAxis.manualTickStepSize(true);
 assertTrue(xAxis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 xAxis.setTickStepSize(0.25);
 assertEquals(0.25, xAxis.getTickStepSize(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeTickStepSize() {
 xAxis.setTickStepSize(-0.25);
 }

 @Test
 public void testSetAxisBounds() {
 // Abstract method, cannot be tested directly
 }

 @Test
 public void testSetSize() {
 // Abstract method, cannot be tested directly
 }

 @Test
 public void testScaleCoord() {
 xAxis.setAxisBounds(new PlotRunList());
 xAxis.setSize(new Rectangle(0, 0, 100, 100));
 xAxis.setAxisTickLimits();
 assertTrue(xAxis.scaleCoord(0.0) >= 0 && xAxis.scaleCoord(0.0) <= 100);
 assertTrue(xAxis.scaleCoord(1.0) >= 0 && xAxis.scaleCoord(1.0) <= 100);
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)xAxis.clone();
 assertNotSame(xAxis, clonedAxis);
 assertEquals(xAxis.getLabel(), clonedAxis.getLabel());
 assertSame(xAxis.getScale(), clonedAxis.getScale());
 assertSame(xAxis.getNumberFormat(), clonedAxis.getNumberFormat());
 assertSame(xAxis.getGridStyle(), clonedAxis.getGridStyle());
 assertArrayEquals(xAxis.getManualBoundsFlags(), clonedAxis.getManualBoundsFlags());
 assertArrayEquals(xAxis.getManualTickStepSize(), clonedAxis.getManualTickStepSize());
 }
}