package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

import org.junit.Test;

public class PlotAxisTest {

	@Test
	public void testPlotAxisConstructor1() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis);
		assertEquals(1.0, axis.xA, 0.0);
		assertEquals(1.0, axis.xB, 0.0);
		assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
		assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
		assertEquals(1, axis.gridStyle);
		assertNull(axis.scale);
		assertNotNull(axis.nf);
		assertNull(axis.label);
	}

	@Test
	public void testPlotAxisConstructor2() {
		PlotAxisScale scale = new PlotAxisScale();
		NumberFormat nf = NumberFormat.getInstance();
		PlotAxis axis = new PlotAxis("TestAxis", scale, nf, 1);
		assertNotNull(axis);
		assertEquals(1.0, axis.xA, 0.0);
		assertEquals(1.0, axis.xB, 0.0);
		assertNull(axis.manualLB);
		assertNull(axis.manualUB);
		assertEquals("TestAxis", axis.label);
		assertEquals(scale, axis.scale);
		assertEquals(nf, axis.nf);
		assertEquals(1, axis.gridStyle);
	}

	@Test
	public void testSetLabel() {
		PlotAxis axis = new PlotAxis();
		String label = "TestLabel";
		axis.setLabel(label);
		assertEquals(label, axis.getLabel());
	}

	@Test(expected = NullPointerException.class)
	public void testSetScaleNull() {
		PlotAxis axis = new PlotAxis();
		PlotAxisScale scale = null;
		axis.setScale(scale);
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getInstance();
		axis.setNumberFormat(nf);
		assertEquals(nf, axis.getNumberFormat());
	}

	@Test(expected = NullPointerException.class)
	public void testSetNumberFormatNull() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = null;
		axis.setNumberFormat(nf);
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis();
		axis.setGridStyle(4);
		assertEquals(4, axis.getGridStyle());
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
		double lowerBounds = 5.0;
		axis.setLowerBounds(lowerBounds);
		assertEquals(lowerBounds, axis.getLowerBounds());
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getInstance();
		axis.setLowerBounds(5.0);
		assertEquals("5.0", axis.lowerBoundsAsString());
		axis.setNumberFormat(nf);
		axis.setLowerBounds(5.0);
		assertEquals("5.0", axis.lowerBoundsAsString());
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
		double upperBounds = 10.0;
		axis.setUpperBounds(upperBounds);
		assertEquals(upperBounds, axis.getUpperBounds());
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getInstance();
		axis.setUpperBounds(10.0);
		assertEquals("10.0", axis.upperBoundsAsString());
		axis.setNumberFormat(nf);
		axis.setUpperBounds(10.0);
		assertEquals("10.0", axis.upperBoundsAsString());
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
		double tickStepSize = 2.5;
		axis.setTickStepSize(tickStepSize);
		assertEquals(tickStepSize, axis.getTickStepSize());
	}

	@Test(expected = NegativeArraySizeException.class)
	public void testSetAxisBoundsWithNullPlotRunList() {
		PlotAxis axis = new PlotAxis();
		axis.setAxisBounds(null);
	}

	@Test
	public void testSetAxisBounds() {
		PlotAxis axis = new PlotAxis();
		PlotRunList plotRunList = new PlotRunList();
		axis.setAxisBounds(plotRunList);
		// test implementation in PlotAxis
	}

	@Test
	public void testSetSize() {
		PlotAxis axis = new PlotAxis();
		Rectangle plotFrame = new Rectangle(0, 0, 10, 10);
		axis.setSize(plotFrame);
		// test implementation in PlotAxis
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis();
		double x = 5.0;
		int expected = (int)(5.0 * axis.xA);
		int actual = axis.scaleCoord(x);
		assertEquals(expected, actual);
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = axis.clone();
		assertNotSame(axis, clonedAxis);
		assertEquals(axis.getClass(), clonedAxis.getClass());
		assertEquals(axis.XA, clonedAxis.xA, 0.0);
		assertEquals(axis.xB, clonedAxis.xB, 0.0);
		assertEquals(axis.aLB, clonedAxis.aLB, 0.0);
		assertEquals(axis.aUB, clonedAxis.aUB, 0.0);
		assertEquals(axis.gridStyle, clonedAxis.gridStyle);
		assertEquals(axis.scale, clonedAxis.scale);
		assertEquals(axis.nf, clonedAxis.nf);
		assertEquals(axis.label, clonedAxis.label);
	}

	@Test(expected = CloneNotSupportedException.class)
	public void testCloneNotSupportedException() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = (PlotAxis)axis.clone();
	}

	@Test
	public void testDrawNullGraphics() {
		PlotAxis axis = new PlotAxis();
		assertNull(axis.draw(null, null, null));
	}

	@Test
	public void testDrawNullComponent() {
		PlotAxis axis = new PlotAxis();
		Component component = null;
		assertNull(axis.draw(null, component, null));
	}

	@Test
	public void testDrawNullRectangle() {
		PlotAxis axis = new PlotAxis();
		Rectangle rectangle = null;
		assertNull(axis.draw(null, null, rectangle));
	}

	@Test
	public void testDraw() {
		PlotAxis axis = new PlotAxis();
		Graphics graphics = new MockGraphics();
		Component component = new MockComponent();
		Rectangle rectangle = new Rectangle(0, 0, 10, 10);
		axis.draw(graphics, component, rectangle);
		// test implementation in PlotAxis
	}
}

class MockGraphics implements Graphics {
	// empty implementation for testing
}

class MockComponent implements Component {
	// empty implementation for testing
}