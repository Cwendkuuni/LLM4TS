import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis);
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
 PlotAxisScale scale = new PlotAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 NumberFormat format = NumberFormat.getInstance();
 axis.setNumberFormat(format);
 assertEquals(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLowerBounds());
 }

 @Test
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 axis.setLowerBounds(1.0);
 assertEquals("1.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUpperBounds());
 }

 @Test
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals(1.0, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 axis.setUpperBounds(1.0);
 assertEquals("1.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualTickStepSize());
 }

 @Test
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testSetAxisBoundsWithNullPlotRunList() {
 axis = new PlotAxis();
 axis.setAxisBounds(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAxisBoundsWithNegativeQuantum() {
 axis = new PlotAxis();
 axis.setTickStepSize(-1.0);
 axis.setAxisBounds(new PlotRunList());
 }

 @Test
 public void testSetAxisBounds() {
 axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 axis.setAxisBounds(new PlotRunList());
 }

 @Test
 public void testClone() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 axis.setUpperBounds(1.0);
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertEquals("Test Label", clonedAxis.getLabel());
 assertEquals(1.0, clonedAxis.getUpperBounds(), 0.001);
 }
}