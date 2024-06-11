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
 private PlotAxisScale axisScale;

 @Before
 public void setUp() {
 xAxis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle p0) {
 // empty implementation
 }

 @Override
 protected void draw(Graphics p0, Component p1, Rectangle p2) {
 // empty implementation
 }

 @Override
 public void setAxisBounds(PlotRunList p0) {
 // empty implementation
 }
 };
 yAxis = new PlotAxis("yAxis", new PlotAxisScale(), null, 1) {
 @Override
 protected void resizeAxis(Rectangle p0) {
 // empty implementation
 }

 @Override
 protected void draw(Graphics p0, Component p1, Rectangle p2) {
 // empty implementation
 }

 @Override
 public void setAxisBounds(PlotRunList p0) {
 // empty implementation
 }
 };
 axisScale = new PlotAxisScale() {
 @Override
 public AxisLimitData findGoodLimits(double lb, double ub) {
 return new AxisLimitData(lb, ub, 0.1);
 }
 };
 xAxis.setScale(axisScale);
 yAxis.setScale(axisScale);
 }

 @Test
 public void testConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertNotNull(plotAxis);
 }

 @Test
 public void testSetLabel() {
 xAxis.setLabel("xAxis");
 assertEquals("xAxis", xAxis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxisScale scale = new PlotAxisScale();
 xAxis.setScale(scale);
 assertEquals(scale, xAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 xAxis.setNumberFormat(decimalFormat);
 assertEquals(decimalFormat, xAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 xAxis.setGridStyle(PlotAxis.kTickMarksOnly);
 assertEquals(PlotAxis.kTickMarksOnly, xAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 xAxis.manualLowerBounds(true);
 assertTrue(xAxis.isManualLB());
 xAxis.manualLowerBounds(false);
 assertFalse(xAxis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 xAxis.setLowerBounds(1.0);
 assertEquals(1.0, xAxis.getLowerBounds(), 0.01);
 }

 @Test
 public void testLowerBoundsAsString() {
 xAxis.setLowerBounds(1.0);
 DecimalFormat decimalFormat = (DecimalFormat)xAxis.getNumberFormat();
 assertEquals("1.00", xAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 xAxis.manualUpperBounds(true);
 assertTrue(xAxis.isManualUB());
 xAxis.manualUpperBounds(false);
 assertFalse(xAxis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 xAxis.setUpperBounds(2.0);
 assertEquals(2.0, xAxis.getUpperBounds(), 0.01);
 }

 @Test
 public void testUpperBoundsAsString() {
 xAxis.setUpperBounds(2.0);
 DecimalFormat decimalFormat = (DecimalFormat)xAxis.getNumberFormat();
 assertEquals("2.00", xAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 xAxis.manualTickStepSize(true);
 assertTrue(xAxis.isManualQuant());
 xAxis.manualTickStepSize(false);
 assertFalse(xAxis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 xAxis.setTickStepSize(0.5);
 assertEquals(0.5, xAxis.getTickStepSize(), 0.01);
 }

 @Test
 public void testScaleCoord() {
 xAxis.setLowerBounds(0.0);
 xAxis.setUpperBounds(10.0);
 xAxis.setTickStepSize(1.0);
 assertEquals(5, xAxis.scaleCoord(5.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCloneUnsupported() {
 xAxis.clone();
 fail("Clone should not be supported for PlotAxis");
 }

 @Test
 public void testCloneSupported() {
 PlotAxis cloneAxis = (PlotAxis)xAxis.clone();
 assertNotNull(cloneAxis);
 assertNotSame(xAxis, cloneAxis);
 }

 @Test
 public void testSetAxisBounds() {
 // Empty test, as implementation is abstract and not testable in isolation
 }

 @Test
 public void testSetSize() {
 // Empty test, as implementation is abstract and not testable in isolation
 }

 @Test
 public void testDraw() {
 // Empty test, as implementation is abstract and not testable in isolation
 }
}