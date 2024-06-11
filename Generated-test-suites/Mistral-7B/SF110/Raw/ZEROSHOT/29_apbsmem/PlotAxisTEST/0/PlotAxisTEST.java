package jahuwaldt.plot.test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.LinearAxisScale;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

 @Test
 public void testDefaultConstructor() {
 PlotAxis axis = new PlotAxis();
 assertEquals(1.0, axis.xA, 0.0);
 assertEquals(1.0, axis.xB, 0.0);
 assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
 assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
 assertEquals(1, axis.gridStyle);
 assertNull(axis.label);
 assertInstanceOf(LinearAxisScale.class, axis.getScale());
 assertInstanceOf(NumberFormat.class, axis.getNumberFormat());
 }

 @Test
 public void testConstructorWithLabelScaleFormatAndGridStyle() {
 PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
 assertEquals("TestAxis", axis.getLabel());
 assertInstanceOf(LinearAxisScale.class, axis.getScale());
 assertInstanceOf(NumberFormat.class, axis.getNumberFormat());
 assertEquals(1, axis.gridStyle);
 }

 @Test
 public void testSetLabel() {
 PlotAxis axis = new PlotAxis();
 axis.setLabel("TestLabel");
 assertEquals("TestLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxis axis = new PlotAxis();
 PlotAxisScale scale = new LinearAxisScale();
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 PlotAxis axis = new PlotAxis();
 NumberFormat format = NumberFormat.getNumberInstance();
 axis.setNumberFormat(format);
 assertSame(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 PlotAxis axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = NullPointerException.class)
 public void testSetScaleNull() {
 PlotAxis axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatNull() {
 PlotAxis axis = new PlotAxis();
 axis.setNumberFormat(null);
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB);
 axis.manualLowerBounds(false);
 assertFalse(axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals("1.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB);
 axis.manualUpperBounds(false);
 assertFalse(axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 assertEquals("2.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.manualQuant);
 axis.manualTickStepSize(false);
 assertFalse(axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test // Assume setAxisBounds is abstract and cannot be tested directly
 public void testSetAxisBounds() {
 // Provide a mock PlotRunList for testing
 PlotRunList mockPlotRunList = mock(PlotRunList.class);
 PlotAxis axis = new PlotAxis() {
 @Override
 public void setAxisBounds(PlotRunList p0) {
 super.setAxisBounds(p0);
 // Add your assertions or tests here
 }
 };
 axis.setAxisBounds(mockPlotRunList);
 // Add your assertions or tests here
 }

 @Test // Assume resizeAxis is abstract and cannot be tested directly
 public void testResizeAxis() {
 // Provide a mock Graphics, Component, and Rectangle for testing
 Graphics mockGraphics = mock(Graphics.class);
 Component mockComponent = mock(Component.class);
 Rectangle mockRectangle = mock(Rectangle.class);
 PlotAxis axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle p0) {
 super.resizeAxis(p0);
 // Add your assertions or tests here
 }
 };
 axis.resizeAxis(mockRectangle);
 // Add your assertions or tests here
 }

 @Test // Assume draw is abstract and cannot be tested directly
 public void testDraw() {
 // Provide a mock Graphics, Component, and Rectangle for testing
 Graphics mockGraphics = mock(Graphics.class);
 Component mockComponent = mock(Component.class);
 Rectangle mockRectangle = mock(Rectangle.class);
 PlotAxis axis = new PlotAxis() {
 @Override
 public void draw(Graphics p0, Component p1, Rectangle p2) {
 super.draw(mockGraphics, mockComponent, mockRectangle);
 // Add your assertions or tests here
 }
 };
 // Add your assertions or tests here
 }
}