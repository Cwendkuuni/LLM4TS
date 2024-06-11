import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertEquals("Default label is null", null, axis.getLabel());
 assertEquals("Default scale is LinearAxisScale", PlotAxisScale.class, axis.getScale().getClass());
 assertEquals("Default number format is NumberFormat.getInstance()", NumberFormat.class, axis.getNumberFormat().getClass());
 assertEquals("Default grid style is 1", 1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithLabelScaleFormatGridStyle() {
 PlotAxis axis = new PlotAxis("TestLabel", new PlotAxisScale() {}, NumberFormat.getInstance(), 2);
 assertEquals("Label is TestLabel", "TestLabel", axis.getLabel());
 assertNull("Scale is null by default when not provided", axis.getScale());
 assertNull("NumberFormat is NumberFormat.getInstance() when not provided", axis.getNumberFormat());
 assertEquals("Grid style is 2", 2, axis.getGridStyle());
 }

 @Test
 public void testSetterGetters() {
 PlotAxis axis = new PlotAxis();
 axis.setLabel("TestLabel");
 assertEquals("Label is TestLabel", "TestLabel", axis.getLabel());

 axis.setScale(new PlotAxisScale());
 assertEquals("Scale is an instance of PlotAxisScale", PlotAxisScale.class, axis.getScale().getClass());

 axis.setNumberFormat(NumberFormat.getNumberInstance());
 assertEquals("NumberFormat is an instance of NumberFormat", NumberFormat.class, axis.getNumberFormat().getClass());

 axis.setGridStyle(3);
 assertEquals("Grid style is 3", 3, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.manualLowerBounds(true);
 axis.setLowerBounds(1.0);
 assertEquals("Lower bounds is 1.0", 1.0, axis.getLowerBounds(), 0.001);
 axis.manualLowerBounds(false);
 axis.setLowerBounds(Double.NaN);
 assertEquals("Lower bounds is Double.NaN", Double.NaN, axis.getLowerBounds());
 }

 @Test
 public void testSetterGetterUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.manualUpperBounds(true);
 axis.setUpperBounds(2.0);
 assertEquals("Upper bounds is 2.0", 2.0, axis.getUpperBounds(), 0.001);
 axis.manualUpperBounds(false);
 axis.setUpperBounds(Double.POSITIVE_INFINITY);
 assertEquals("Upper bounds is Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, axis.getUpperBounds());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.manualTickStepSize(true);
 axis.setTickStepSize(0.5);
 assertEquals("Tick step size is 0.5", 0.5, axis.getTickStepSize(), 0.001);
 axis.manualTickStepSize(false);
 axis.setTickStepSize(-1.0);
 assertEquals("Tick step size is -1.0", -1.0, axis.getTickStepSize(), 0.001);
 }

 @Test
 public void testScaleCoord() {
 PlotAxis axis = new PlotAxis();
 axis.setAxisBounds(new PlotRunList());
 axis.setAxisTickLimits();
 assertEquals("Scale coord for 0.0 is 0", 0, axis.scaleCoord(0.0), 0);
 assertEquals("Scale coord for 1.0 is 100", 100, axis.scaleCoord(1.0), 0);
 }

 @Test
 public void testSetAxisBounds() {
 // To be implemented in subclasses
 }

 @Test
 public void testDraw() {
 // To be implemented in subclasses
 }

 @Test
 public void testClone() {
 PlotAxis original = new PlotAxis();
 PlotAxis cloned = original.clone();
 assertNotSame("Cloned instance should not be the same as the original", original, cloned);
 }
}