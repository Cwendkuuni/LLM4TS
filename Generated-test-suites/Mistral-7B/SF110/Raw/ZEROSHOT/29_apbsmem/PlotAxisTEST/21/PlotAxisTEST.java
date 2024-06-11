import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Locale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {
 // No-op
 }

 @Override
 public void draw(Graphics graphics, Component component, Rectangle rectangle) {
 // No-op
 }

 @Override
 public void setAxisBounds(PlotRunList plotRunList) {
 // No-op
 }
 };
 }

 @Test
 public void testDefaultValues() {
 assertEquals(1.0, axis.xA, 0.0);
 assertEquals(1.0, axis.xB, 0.0);
 assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
 assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
 assertEquals(1, axis.gridStyle);
 assertNull(axis.label);
 assertNotNull(axis.scale);
 assertInstanceOf(LinearAxisScale.class, axis.scale);
 assertNotNull(axis.nf);
 assertInstanceOf(NumberFormat.class, axis.nf);
 assertEquals(0, axis.manualLB);
 assertEquals(false, axis.manualUB);
 assertEquals(false, axis.manualQuant);
 }

 @Test
 public void testConstructorWithLabelScaleAndNumberFormat() {
 axis = new PlotAxis("TestAxis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 assertEquals("TestAxis", axis.getLabel());
 assertSame(axis.scale, new LinearAxisScale());
 assertSame(axis.nf, new DecimalFormat("#.##"));
 assertEquals(1, axis.gridStyle);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("NewLabel");
 assertEquals("NewLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 final LogAxisScale logScale = new LogAxisScale();
 axis.setScale(logScale);
 assertSame(axis.scale, logScale);
 }

 @Test
 public void testSetNumberFormat() {
 final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
 axis.setNumberFormat(decimalFormat);
 assertSame(axis.nf, decimalFormat);
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertEquals(true, axis.manualLB);
 axis.manualLowerBounds(false);
 assertEquals(false, axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(10.0);
 assertEquals(10.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(2.5);
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 axis.setNumberFormat(decimalFormat);
 assertEquals("2.50", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertEquals(true, axis.manualUB);
 axis.manualUpperBounds(false);
 assertEquals(false, axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(20.0);
 assertEquals(20.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(3.14);
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 axis.setNumberFormat(decimalFormat);
 assertEquals("3.14", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertEquals(true, axis.manualQuant);
 axis.manualTickStepSize(false);
 assertEquals(false, axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegativeValue() {
 axis.setTickStepSize(-0.5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testScaleCoord() {
 axis.setLowerBounds(0.0);
 axis.setUpperBounds(10.0);
 axis.setTickStepSize(2.0);
 assertEquals(4, axis.scaleCoord(2.0));
 }
}