package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

 private PlotAxis xAxis;

 @Before
 public void setup() {
 xAxis = new PlotAxis("X-axis", null, null, 1);
 xAxis.setLowerBounds(0.0);
 xAxis.setUpperBounds(10.0);
 xAxis.setGridStyle(PlotAxis.kTickMarksOnly);
 xAxis.setTickStepSize(1.0);
 }

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 assertEquals("", axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testGetLabel() {
 assertEquals("X-axis", xAxis.getLabel());
 }

 @Test
 public void testSetLabel() {
 xAxis.setLabel("New label");
 assertEquals("New label", xAxis.getLabel());
 }

 @Test
 public void testGetScale() {
 assertEquals(LinearAxisScale.class, xAxis.getScale().getClass());
 }

 @Test
 public void testSetScale() {
 NumberFormat format = new DecimalFormat("#.##");
 PlotAxisScale customScale = new CustomAxisScale();
 xAxis.setScale(customScale);
 assertEquals(customScale, xAxis.getScale());
 }

 @Test(expected = NullPointerException.class)
 public void testSetScaleWithNullArgument() {
 xAxis.setScale(null);
 }

 @Test
 public void testGetNumberFormat() {
 assertEquals(NumberFormat.getInstance().getClass(), xAxis.getNumberFormat().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 NumberFormat customFormat = new DecimalFormat("#,##0.00");
 xAxis.setNumberFormat(customFormat);
 assertEquals(customFormat, xAxis.getNumberFormat());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatWithNullArgument() {
 xAxis.setNumberFormat(null);
 }

 @Test
 public void testGetGridStyle() {
 assertEquals(1, xAxis.getGridStyle());
 }

 @Test
 public void testSetGridStyle() {
 xAxis.setGridStyle(PlotAxis.kMajorGrid);
 assertEquals(2, xAxis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleWithInvalidArgument() {
 xAxis.setGridStyle(3);
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
 xAxis.setLowerBounds(-5.0);
 assertEquals(-5.0, xAxis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 xAxis.setLowerBounds(0.5);
 assertEquals("0.5", xAxis.lowerBoundsAsString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLowerBoundsWithNegativeValue() {
 xAxis.setLowerBounds(-1.0);
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
 xAxis.setUpperBounds(15.5);
 assertEquals(15.5, xAxis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 xAxis.setUpperBounds(7.2);
 assertEquals("7.2", xAxis.upperBoundsAsString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpperBoundsWithNegativeValue() {
 xAxis.setUpperBounds(-3.0);
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
 xAxis.setTickStepSize(2.5);
 assertEquals(2.5, xAxis.getTickStepSize(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeWithNegativeValue() {
 xAxis.setTickStepSize(-1.0);
 }

 @Test
 public void testSetAxisBounds() {
 // Mocked implementation for testing as the method is abstract
 xAxis.setAxisBounds(null);
 }

 @Test
 public void testSetSize() {
 // Mocked implementation for testing as the method is abstract
 xAxis.setSize(null);
 }

 @Test
 public void testScaleCoord() {
 assertEquals(0, xAxis.scaleCoord(0.0));
 assertEquals(11, xAxis.scaleCoord(10.0));
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = xAxis.clone();
 assertEquals(xAxis, clonedAxis);
 }
}