package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

 @Test
 public void testPlotAxisCreation() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);

 // Test default values
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 assertNull(axis.getLabel());
 assertNull(axis.getScale());
 assertInstanceOf(LinearAxisScale.class, axis.getScale());
 assertInstanceOf(NumberFormat.class, axis.getNumberFormat());
 assertInstanceOf(DecimalFormat.class, axis.getNumberFormat());
 }

 @Test
 public void testPlotAxisCreationWithCustomValues() {
 PlotAxis axis = new PlotAxis("Test Label", new LogAxisScale(), new DecimalFormat("#.##"), 3);
 assertNotNull(axis);

 // Test custom values
 assertEquals("Test Label", axis.getLabel());
 assertInstanceOf(LogAxisScale.class, axis.getScale());
 assertInstanceOf(DecimalFormat.class, axis.getNumberFormat());
 assertEquals(3, axis.getGridStyle());
 }

 @Test
 public void testSetGetLabel() {
 PlotAxis axis = new PlotAxis();
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullLabel() {
 PlotAxis axis = new PlotAxis();
 axis.setLabel(null);
 }

 @Test
 public void testSetGetScale() {
 PlotAxis axis = new PlotAxis();
 PlotAxisScale scale = new LinearAxisScale();
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullScale() {
 PlotAxis axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test
 public void testSetGetNumberFormat() {
 PlotAxis axis = new PlotAxis();
 NumberFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertSame(format, axis.getNumberFormat());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullNumberFormat() {
 PlotAxis axis = new PlotAxis();
 axis.setNumberFormat(null);
 }

 @Test
 public void testSetGridStyle() {
 PlotAxis axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeGridStyle() {
 PlotAxis axis = new PlotAxis();
 axis.setGridStyle(-1);
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 axis.manualLowerBounds(false);
 assertFalse(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 PlotAxis axis = new PlotAxis();
 double lowerBound = 1.0;
 axis.setLowerBounds(lowerBound);
 assertEquals(lowerBound, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testSetInvalidLowerBounds() {
 PlotAxis axis = new PlotAxis();
 double lowerBound = Double.NaN;
 axis.setLowerBounds(lowerBound);
 assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
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
 assertTrue(axis.isManualUB());
 axis.manualUpperBounds(false);
 assertFalse(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 PlotAxis axis = new PlotAxis();
 double upperBound = 1.0;
 axis.setUpperBounds(upperBound);
 assertEquals(upperBound, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testSetInvalidUpperBounds() {
 PlotAxis axis = new PlotAxis();
 double upperBound = Double.NaN;
 axis.setUpperBounds(upperBound);
 assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals("1.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 axis.manualTickStepSize(false);
 assertFalse(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 PlotAxis axis = new PlotAxis();
 double tickStepSize = 1.0;
 axis.setTickStepSize(tickStepSize);
 assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testSetInvalidTickStepSize() {
 PlotAxis axis = new PlotAxis();
 double tickStepSize = Double.NaN;
 axis.setTickStepSize(tickStepSize);
 assertEquals(0.0, axis.getTickStepSize(), 0.0);
 }
}