package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

	@Test
	public void testPlotAxisConstructor() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis);

		PlotAxis axisWithLabel = new PlotAxis("TestAxis", null, null, 1);
		assertNotNull(axisWithLabel);
		assertEquals("TestAxis", axisWithLabel.getLabel());
	}

	@Test
	public void testSetLabel() {
		PlotAxis axis = new PlotAxis();
		axis.setLabel("TestLabel");
		assertEquals("TestLabel", axis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxis axis = new PlotAxis();
		PlotAxisScale scale = new PlotAxisScale() {
			@Override
			public double func(double x) {
				return x * 2.0;
			}
		};
		axis.setScale(scale);
		assertEquals(scale, axis.getScale());
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		DecimalFormat format = new DecimalFormat("#.##");
		axis.setNumberFormat(format);
		assertEquals(format, axis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis();
		axis.setGridStyle(PlotAxis.kTickMarksOnly);
		assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
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
		axis.setLowerBounds(1.0);
		assertEquals(1.0, axis.getLowerBounds(), 0.001);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(1.23);
		assertEquals("1.23", axis.lowerBoundsAsString());
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
		axis.setUpperBounds(2.5);
		assertEquals(2.5, axis.getUpperBounds(), 0.001);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(3.14);
		assertEquals("3.14", axis.upperBoundsAsString());
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
		axis.setTickStepSize(0.5);
		assertEquals(0.5, axis.getTickStepSize(), 0.001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNegativeTickStepSize() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(-0.1);
	}

	// TODO: Implement setAxisBounds, setSize, resizeAxis, and draw methods tests
	// using mocks or stubs for external dependencies

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = (PlotAxis)axis.clone();
		assertNotSame(axis, clonedAxis);
		assertEquals(axis.getClass(), clonedAxis.getClass());
		assertEquals(axis.getLabel(), clonedAxis.getLabel());
		assertEquals(axis.getScale(), clonedAxis.getScale());
		assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
		assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
		assertEquals(axis.manualLB, clonedAxis.manualLB);
		assertEquals(axis.manualUB, clonedAxis.manualUB);
		assertEquals(axis.manualQuant, clonedAxis.manualQuant);
		assertEquals(axis.getLowerBounds(), clonedAxis.getLowerBounds(), 0.001);
		assertEquals(axis.getUpperBounds(), clonedAxis.getUpperBounds(), 0.001);
		assertEquals(axis.getTickStepSize(), clonedAxis.getTickStepSize(), 0.001);
	}
}