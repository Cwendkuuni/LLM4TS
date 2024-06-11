package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

 private PlotAxis xAxis;
 private PlotAxis yAxis;

 @Before
 public void setUp() {
 xAxis = new LinearAxis("X-Axis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 yAxis = new LinearAxis("Y-Axis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 }

 @Test
 public void testDefaultConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertEquals(1.0, plotAxis.xA, 0.01);
 assertEquals(1.0, plotAxis.xB, 0.01);
 assertEquals(Double.MAX_VALUE, plotAxis.aLB, 0.01);
 assertEquals(-1.7976931348623157E308, plotAxis.aUB, 0.01);
 assertEquals(1, plotAxis.gridStyle);
 assertNull(plotAxis.label);
 assertNull(plotAxis.scale);
 assertNull(plotAxis.nf);
 }

 @Test
 public void testConstructorWithLabelScaleNumberFormatAndGridStyle() {
 PlotAxis plotAxis = new PlotAxis("TestAxis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 assertEquals("TestAxis", plotAxis.getLabel());
 assertSame(new LinearAxisScale(), plotAxis.getScale());
 assertSame(new DecimalFormat("#.##"), plotAxis.getNumberFormat());
 assertEquals(1, plotAxis.getGridStyle());
 }

 @Test
 public void testSetLabelMethod() {
 xAxis.setLabel("Test X-Axis");
 assertEquals("Test X-Axis", xAxis.getLabel());
 }

 @Test
 public void testSetScaleMethod() {
 PlotAxisScale axisScale = new LogAxisScale();
 xAxis.setScale(axisScale);
 assertSame(axisScale, xAxis.getScale());
 }

 @Test
 public void testSetNumberFormatMethod() {
 DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
 xAxis.setNumberFormat(numberFormat);
 assertSame(numberFormat, xAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyleMethod() {
 xAxis.setGridStyle(2);
 assertEquals(2, xAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBoundsMethod() {
 xAxis.manualLowerBounds(true);
 assertTrue(xAxis.manualLB);
 xAxis.manualLowerBounds(false);
 assertFalse(xAxis.manualLB);
 }

 @Test
 public void testSetLowerBoundsMethod() {
 xAxis.setLowerBounds(1.0);
 assertEquals(1.0, xAxis.getLowerBounds(), 0.01);
 }

 @Test
 public void testLowerBoundsAsStringMethod() {
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 xAxis.setLowerBounds(1.5);
 assertEquals("1.50", xAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBoundsMethod() {
 xAxis.manualUpperBounds(true);
 assertTrue(xAxis.manualUB);
 xAxis.manualUpperBounds(false);
 assertFalse(xAxis.manualUB);
 }

 @Test
 public void testSetUpperBoundsMethod() {
 xAxis.setUpperBounds(2.0);
 assertEquals(2.0, xAxis.getUpperBounds(), 0.01);
 }

 @Test
 public void testUpperBoundsAsStringMethod() {
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 xAxis.setUpperBounds(2.5);
 assertEquals("2.50", xAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSizeMethod() {
 xAxis.manualTickStepSize(true);
 assertTrue(xAxis.manualQuant);
 xAxis.manualTickStepSize(false);
 assertFalse(xAxis.manualQuant);
 }

 @Test
 public void testSetTickStepSizeMethod() {
 xAxis.setTickStepSize(0.5);
 assertEquals(0.5, xAxis.getTickStepSize(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeWithNegativeValue() {
 xAxis.setTickStepSize(-0.5);
 }

 @Test
 public void testGetTickStepSizeMethod() {
 assertEquals(1.0, xAxis.getTickStepSize(), 0.01);
 }

 @Test
 public void testSetAxisBoundsMethod() {
 // As this method is abstract, cannot test directly
 // Need to mock or extend the TestCase with the specific PlotAxis subclass
 }

 @Test
 public void testSetSizeMethod() {
 Rectangle rectangle = new Rectangle(0, 0, 100, 100);
 xAxis.setSize(rectangle);
 // As this method calls abstract methods setAxisTickLimits() and resizeAxis(),
 // Cannot test directly without mocking or extending the TestCase with the specific PlotAxis subclass
 }

 @Test
 public void testDrawMethodWithGraphicsComponentAndRectangle() {
 Graphics graphics = new Graphics() {};
 Component component = new Object() {};
 Rectangle rectangle = new Rectangle(0, 0, 100, 100);
 xAxis.draw(graphics, component, rectangle);
 // Cannot test directly as this method is abstract
 }

 @Test
 public void testScaleCoordMethod() {
 double x = 1.5;
 int result = xAxis.scaleCoord(x);
 assertEquals(3, result);
 }

 @Test
 public void testCloneMethod() {
 PlotAxis cloneAxis = (PlotAxis) xAxis.clone();
 assertNotSame(xAxis, cloneAxis);
 assertEquals(xAxis.getLabel(), cloneAxis.getLabel());
 assertEquals(xAxis.getScale(), cloneAxis.getScale());
 assertEquals(xAxis.getNumberFormat(), cloneAxis.getNumberFormat());
 assertEquals(xAxis.getGridStyle(), cloneAxis.getGridStyle());
 }
}