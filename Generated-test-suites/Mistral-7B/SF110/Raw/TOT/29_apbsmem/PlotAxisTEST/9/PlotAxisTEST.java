package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {
 // empty implementation
 }

 @Override
 protected void draw(Graphics graphics, Component component, Rectangle rectangle) {
 // empty implementation
 }

 @Override
 public void setAxisBounds(PlotRunList plotRunList) {
 // empty implementation
 }
 };
 }

 @Test
 public void testDefaultConstruction() {
 // Test default constructor
 axis.setLabel("Default Axis");
 axis.setScale(new LinearAxisScale());
 axis.setNumberFormat(DecimalFormat.getInstance());
 axis.setGridStyle(kTickMarksOnly);
 assertEquals("Default Axis", axis.getLabel());
 assertEquals(LinearAxisScale.class, axis.getScale().getClass());
 assertEquals(DecimalFormat.class, axis.getNumberFormat().getClass());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testCustomConstruction() {
 // Test constructor with custom label, scale, number format, and grid style
 axis = new PlotAxis("Custom Axis", new LogAxisScale(), new DecimalFormat("#.##########"), 2);
 axis.setLabel("Custom Axis");
 axis.setScale(new LogAxisScale());
 axis.setNumberFormat(new DecimalFormat("#.##########"));
 axis.setGridStyle(2);
 assertEquals("Custom Axis", axis.getLabel());
 assertEquals(LogAxisScale.class, axis.getScale().getClass());
 assertEquals(DecimalFormat.class, axis.getNumberFormat().getClass());
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 // Test setting and getting label
 axis.setLabel("New Label");
 assertEquals("New Label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 // Test setting and getting scale
 axis.setScale(new ReversedAxisScale());
 assertEquals(ReversedAxisScale.class, axis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 // Test setting and getting number format
 axis.setNumberFormat(NumberFormat.getNumberInstance());
 assertEquals(NumberFormat.class, axis.getNumberFormat().getClass());
 }

 @Test
 public void testSetGridStyle() {
 // Test setting and getting grid style
 axis.setGridStyle(kMajorGrid);
 assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = NullPointerException.class)
 public void testNullScale() {
 // Test setting scale to null
 axis.setScale(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNumberFormat() {
 // Test setting number format to null
 axis.setNumberFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGridStyleLessThanZero() {
 // Test setting grid style less than zero
 axis.setGridStyle(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGridStyleGreaterThanThree() {
 // Test setting grid style greater than three
 axis.setGridStyle(4);
 }

 @Test
 public void testGetLowerBounds() {
 // Test getting lower bounds in default state
 assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testSetLowerBounds() {
 // Test setting and getting lower bounds
 axis.setLowerBounds(0.0);
 assertEquals(0.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 // Test converting lower bounds to string
 axis.setLowerBounds(10.5);
 assertEquals("10.5", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualLowerBounds() {
 // Test manual lower bounds
 axis.manualLowerBounds(true);
 assertFalse(axis.manualLB());
 }

 @Test
 public void testSetManualLowerBounds() {
 // Test setting and getting manual lower bounds
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB());
 }

 @Test
 public void testGetUpperBounds() {
 // Test getting upper bounds in default state
 assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testSetUpperBounds() {
 // Test setting and getting upper bounds
 axis.setUpperBounds(0.0);
 assertEquals(0.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 // Test converting upper bounds to string
 axis.setUpperBounds(10.5);
 assertEquals("10.5", axis.upperBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 // Test manual upper bounds
 axis.manualUpperBounds(true);
 assertFalse(axis.manualUB());
 }

 @Test
 public void testSetManualUpperBounds() {
 // Test setting and getting manual upper bounds
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB());
 }

 @Test
 public void testGetTickStepSize() {
 // Test getting tick step size in default state
 assertEquals(0.0, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testSetTickStepSize() {
 // Test setting and getting tick step size
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }
}