import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testPlotAxisConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 axis.setScale(new LogAxisScale());
 assertNotSame(LinearAxisScale.class, axis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 NumberFormat nf = NumberFormat.getInstance();
 axis.setNumberFormat(nf);
 assertSame(nf, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(PlotAxis.kTickMarksOnly);
 assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLowerBoundsNegativeValue() {
 axis = new PlotAxis();
 axis.setLowerBounds(-1.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.5);
 assertEquals("1.5", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpperBoundsNegativeValue() {
 axis = new PlotAxis();
 axis.setUpperBounds(-1.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(2.5);
 assertEquals("2.5", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegativeValue() {
 axis = new PlotAxis();
 axis.setTickStepSize(-0.5);
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
 public void testSetAxisTickLimits() {
 // Protected method, cannot be tested directly
 }

 @Test
 public void testResizeAxis() {
 // Protected abstract method, cannot be tested directly
 }

 @Test
 public void testDraw() {
 // Abstract method, cannot be tested directly
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 double x = 1.0;
 int expected = (int)(1.0 * 1.0 + 1.0);
 int result = axis.scaleCoord(x);
 assertEquals(expected, result);
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotSame(axis, clonedAxis);
 }
}