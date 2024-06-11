import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.LinearAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle p0) {}
 @Override
 public void draw(Graphics p0, Component p1, Rectangle p2) {}
 };
 }

 @Test
 public void testSetAndGetLabel() {
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testSetAndGetScale() {
 PlotAxisScale scale = new LinearAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetAndGetNumberFormat() {
 NumberFormat format = NumberFormat.getInstance();
 axis.setNumberFormat(format);
 assertEquals(format, axis.getNumberFormat());
 }

 @Test
 public void testSetAndGetGridStyle() {
 axis.setGridStyle(PlotAxis.kMajorGrid);
 assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 axis.setLowerBounds(-1.0);
 assertEquals(-1.0, axis.getLowerBounds(), 0.01);
 axis.manualLowerBounds(false);
 axis.setLowerBounds(0.0);
 assertEquals(0.0, axis.getLowerBounds(), 0.01);
 }

 @Test
 public void testSetAndGetUpperBounds() {
 axis.manualUpperBounds(true);
 axis.setUpperBounds(1.0);
 assertEquals(1.0, axis.getUpperBounds(), 0.01);
 axis.manualUpperBounds(false);
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.01);
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 axis.setTickStepSize(1.5);
 assertEquals(1.5, axis.getTickStepSize(), 0.01);
 axis.manualTickStepSize(false);
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.01);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullLabel() {
 axis.setLabel(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullScale() {
 axis.setScale(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullNumberFormat() {
 axis.setNumberFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLowerBoundsBelowUpperBounds() {
 axis.setLowerBounds(2.0);
 axis.setUpperBounds(1.0);
 }
}