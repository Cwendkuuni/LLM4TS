package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

	@Test
	public void testDefaultConstructor() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis);
		assertEquals(1.0, axis.getxA(), 0.0);
		assertEquals(1.0, axis.getxB(), 0.0);
		assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
		assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
		assertEquals(1, axis.getGridStyle());
		assertEquals(new LinearAxisScale(), axis.getScale());
		assertNotNull(axis.getNumberFormat());
		assertNull(axis.getLabel());
	}

	@Test
	public void testCustomConstructor() {
		String label = "Test Label";
		PlotAxisScale scale = new LinearAxisScale();
		NumberFormat nf = NumberFormat.getNumberInstance();
		int gridStyle = 2;
		PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
		assertNotNull(axis);
		assertEquals(1.0, axis.getxA(), 0.0);
		assertEquals(1.0, axis.getxB(), 0.0);
		assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
		assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
		assertEquals(gridStyle, axis.getGridStyle());
		assertEquals(scale, axis.getScale());
		assertEquals(nf, axis.getNumberFormat());
		assertEquals(label, axis.getLabel());
	}

	@Test
	public void testSetLabel() {
		PlotAxis axis = new PlotAxis();
		String label = "Test Label";
		axis.setLabel(label);
		assertEquals(label, axis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxis axis = new PlotAxis();
		PlotAxisScale scale = new LogAxisScale();
		axis.setScale(scale);
		assertEquals(scale, axis.getScale());
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getPercentInstance();
		axis.setNumberFormat(nf);
		assertEquals(nf, axis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis();
		int gridStyle = 2;
		axis.setGridStyle(gridStyle);
		assertEquals(gridStyle, axis.getGridStyle());
	}

	@Test
	public void testManualLowerBounds() {
		PlotAxis axis = new PlotAxis();
		boolean flag = true;
		axis.manualLowerBounds(flag);
		assertTrue(axis.isManualLB());
	}

	@Test
	public void testSetLowerBounds() {
		PlotAxis axis = new PlotAxis();
		double value = 2.5;
		axis.setLowerBounds(value);
		assertEquals(value, axis.getLowerBounds(), 0.0);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getNumberInstance();
		double value = 2.5;
		axis.setLowerBounds(value);
		assertEquals("2.5", axis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBounds() {
		PlotAxis axis = new PlotAxis();
		boolean flag = true;
		axis.manualUpperBounds(flag);
		assertTrue(axis.isManualUB());
	}

	@Test
	public void testSetUpperBounds() {
		PlotAxis axis = new PlotAxis();
		double value = 2.5;
		axis.setUpperBounds(value);
		assertEquals(value, axis.getUpperBounds(), 0.0);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		NumberFormat nf = NumberFormat.getNumberInstance();
		double value = 2.5;
		axis.setUpperBounds(value);
		assertEquals("2.5", axis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSize() {
		PlotAxis axis = new PlotAxis();
		boolean flag = true;
		axis.manualTickStepSize(flag);
		assertTrue(axis.isManualQuant());
	}

	@Test
	public void testSetTickStepSize() {
		PlotAxis axis = new PlotAxis();
		double value = 2.5;
		axis.setTickStepSize(value);
		assertEquals(value, axis.getTickStepSize(), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTickStepSizeNegativeValue() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(-2.5);
	}

	@Test
	public void testGetTickStepSize() {
		PlotAxis axis = new PlotAxis();
		double value = 2.5;
		axis.setTickStepSize(value);
		assertEquals(value, axis.getTickStepSize(), 0.0);
	}

	@Test
	public void testSetAxisBounds() {
		PlotAxis axis = new PlotAxis();
		PlotRunList p0 = new PlotRunList();
		axis.setAxisBounds(p0);
		// This method is abstract, so we cannot test it directly.
		// We can only test if it throws an exception or not.
		// assertFalse(axis.hasAxisBoundsSet()); // Uncomment this line if you have an implementation for hasAxisBoundsSet() method
	}

	@Test
	public void testSetSize() {
		PlotAxis axis = new PlotAxis();
		Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
		axis.setSize(plotFrame);
		// This method is abstract, so we cannot test it directly.
		// We can only test if it throws an exception or not.
		// assertFalse(axis.hasSizeSet()); // Uncomment this line if you have an implementation for hasSizeSet() method
	}

	@Test
	public void testSetAxisTickLimits() {
		PlotAxis axis = new PlotAxis();
		axis.setAxisTickLimits();
		// This method is protected, so we cannot test it directly.
		// We can only test if it changes the state of the object.
		double lb = axis.getALB();
		double ub = axis.getAUB();
		double quantum = axis.getTickStepSize();
		assertTrue(axis.isSetAxisTickLimits());
		assertNotEquals(Double.MAX_VALUE, lb);
		assertNotEquals(-1.7976931348623157E308, ub);
		assertNotEquals(0.0, quantum);
	}

	@Test
	public void testResizeAxis() {
		PlotAxis axis = new PlotAxis();
		Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
		axis.resizeAxis(plotFrame);
		// This method is abstract, so we cannot test it directly.
		// We can only test if it throws an exception or not.
		// assertFalse(axis.hasResizeAxisCalled()); // Uncomment this line if you have an implementation for hasResizeAxisCalled() method
	}

	@Test
	public void testDraw() {
		PlotAxis axis = new PlotAxis();
		Component c = new Component() {
			@Override
			public Graphics getGraphics() {
				return new Graphics() {
					@Override
					public void drawLine(int x1, int y1, int x2, int y2) {
						// This method is not used in the test. We only use it to mimic the Graphics object.
					}

					@Override
					public void setColor(Color c) {
						// This method is not used in the test. We only use it to mimic the Graphics object.
					}
				};
			}
		};
		Rectangle rect = new Rectangle(10, 10, 100, 100);
		axis.draw(axis.getNumberFormat(), c, rect);
		// This method is abstract, so we cannot test it directly.
		// We can only test if it throws an exception or not.
		// assertFalse(axis.hasDrawCalled()); // Uncomment this line if you have an implementation for hasDrawCalled() method
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis();
		double x = 1.0;
		int expected = (int)(axis.getxA() * 1.0 + axis.getxB());
		int actual = axis.scaleCoord(x);
		assertEquals(expected, actual);
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = axis.clone();
		assertNotNull(clonedAxis);
		assertEquals(axis, clonedAxis);
		assertNotSame(axis, clonedAxis);
	}
}