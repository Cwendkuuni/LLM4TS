package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

import org.junit.Test;

public class PlotAxisTest {

	@Test
	public void testPlotAxisConstructor() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis);
		assertEquals(1.0, axis.xA);
		assertEquals(1.0, axis.xB);
		assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
		assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
		assertEquals(1, axis.gridStyle);
		assertNull(axis.label);
		assertNull(axis.scale);
		assertNull(axis.nf);
	}

	@Test
	public void testPlotAxisWithLabelConstructor() {
		PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
		assertNotNull(axis);
		assertEquals("TestAxis", axis.label);
		assertEquals(1.0, axis.xA);
		assertEquals(1.0, axis.xB);
		assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
		assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
		assertEquals(1, axis.gridStyle);
		assertNull(axis.manualLB);
		assertNull(axis.manualUB);
		assertNull(axis.manualQuant);
		assertEquals(1.0, axis.quantum, 0.0);
	}

	@Test(expected = NullPointerException.class)
	public void testPlotAxisWithNullLabelConstructor() {
		PlotAxis axis = new PlotAxis(null, new LinearAxisScale(), new DecimalFormat("#.##"), 1);
	}

	@Test
	public void testSetLabelMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setLabel("TestAxis");
		assertEquals("TestAxis", axis.getLabel());
	}

	@Test
	public void testSetScaleMethod() {
		PlotAxis axis = new PlotAxis();
		PlotAxisScale scale = new LinearAxisScale();
		axis.setScale(scale);
		assertEquals(scale, axis.getScale());
	}

	@Test(expected = NullPointerException.class)
	public void testSetScaleWithNullArgument() {
		PlotAxis axis = new PlotAxis();
		axis.setScale(null);
	}

	@Test
	public void testSetNumberFormatMethod() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = new DecimalFormat("#.##");
		axis.setNumberFormat(nf);
		assertEquals(nf, axis.getNumberFormat());
	}

	@Test(expected = NullPointerException.class)
	public void testSetNumberFormatWithNullArgument() {
		PlotAxis axis = new PlotAxis();
		axis.setNumberFormat(null);
	}

	@Test
	public void testSetGridStyleMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setGridStyle(2);
		assertEquals(2, axis.getGridStyle());
	}

	@Test
	public void testManualLowerBoundsMethod() {
		PlotAxis axis = new PlotAxis();
		axis.manualLowerBounds(true);
		assertTrue(axis.manualLB);
		axis.manualLowerBounds(false);
		assertFalse(axis.manualLB);
	}

	@Test
	public void testSetLowerBoundsMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(10.5);
		assertEquals(10.5, axis.getLowerBounds(), 0.0);
	}

	@Test
	public void testLowerBoundsAsStringMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(10.5);
		assertEquals("10.5", axis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBoundsMethod() {
		PlotAxis axis = new PlotAxis();
		axis.manualUpperBounds(true);
		assertTrue(axis.manualUB);
		axis.manualUpperBounds(false);
		assertFalse(axis.manualUB);
	}

	@Test
	public void testSetUpperBoundsMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(20.5);
		assertEquals(20.5, axis.getUpperBounds(), 0.0);
	}

	@Test
	public void testUpperBoundsAsStringMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(20.5);
		assertEquals("20.5", axis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSizeMethod() {
		PlotAxis axis = new PlotAxis();
		axis.manualTickStepSize(true);
		assertTrue(axis.manualQuant);
		axis.manualTickStepSize(false);
		assertFalse(axis.manualQuant);
	}

	@Test
	public void testSetTickStepSizeMethod() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(2.5);
		assertEquals(2.5, axis.getTickStepSize(), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTickStepSizeWithNegativeValue() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(-1.5);
	}

}