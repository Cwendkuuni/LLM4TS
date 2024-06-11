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
	public void testDefaultConstructor() {
		PlotAxis axis = new PlotAxis();
		assertEquals(1.0, axis.getxA(), 0.0);
		assertEquals(1.0, axis.getxB(), 0.0);
		assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
		assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
		assertEquals(1, axis.getGridStyle());
		assertNull(axis.getScale());
		assertNull(axis.getNumberFormat());
		assertEquals("", axis.getLabel());
	}

	@Test
	public void testCustomConstructor() {
		PlotAxis axis = new PlotAxis("TestAxis", null, null, 1);
		assertEquals(1.0, axis.getxA(), 0.0);
		assertEquals(1.0, axis.getxB(), 0.0);
		assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
		assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
		assertEquals(1, axis.getGridStyle());
		assertNull(axis.getScale());
		assertNull(axis.getNumberFormat());
		assertEquals("TestAxis", axis.getLabel());
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
		PlotAxisScale scale = new LogAxisScale();
		axis.setScale(scale);
		assertEquals(scale, axis.getScale());
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		DecimalFormat df = new DecimalFormat("#0.00");
		axis.setNumberFormat(df);
		assertEquals(df, axis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis();
		axis.setGridStyle(PlotAxis.kMajorGrid);
		assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
	}

	@Test
	public void testManualLowerBounds() {
		PlotAxis axis = new PlotAxis();
		axis.manualLowerBounds(true);
		assertTrue(axis.isManualLB());
	}

	@Test
	public void testSetLowerBounds() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(5.0);
		assertEquals(5.0, axis.getLowerBounds(), 0.0);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(5.0);
		DecimalFormat df = new DecimalFormat("#0.0");
		axis.setNumberFormat(df);
		assertEquals("5.0", axis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBounds() {
		PlotAxis axis = new PlotAxis();
		axis.manualUpperBounds(true);
		assertTrue(axis.isManualUB());
	}

	@Test
	public void testSetUpperBounds() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(10.0);
		assertEquals(10.0, axis.getUpperBounds(), 0.0);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(10.0);
		DecimalFormat df = new DecimalFormat("#0.0");
		axis.setNumberFormat(df);
		assertEquals("10.0", axis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSize() {
		PlotAxis axis = new PlotAxis();
		axis.manualTickStepSize(true);
		assertTrue(axis.isManualQuant());
	}

	@Test
	public void testSetTickStepSize() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(2.5);
		assertEquals(2.5, axis.getTickStepSize(), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeTickStepSize() {
		PlotAxis axis = new PlotAxis();
		axis.setTickStepSize(-1.5);
	}

	@Test
	public void testDraw() {
		PlotAxis axis = new PlotAxis();
		Graphics g = new Graphics() {
			@Override
			public void drawString(String str, float x, float y) {}
			@Override
			public void drawLine(int x1, int y1, int x2, int y2) {}
		};
		Rectangle rect = new Rectangle(0, 0, 100, 100);
		axis.draw(g, null, rect);
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis();
		assertEquals(0, axis.scaleCoord(0.0));
		assertEquals(101, axis.scaleCoord(1.0));
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = (PlotAxis)axis.clone();
		assertNotSame(axis, clonedAxis);
		assertEquals(axis.getLabel(), clonedAxis.getLabel());
		assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
		assertEquals(axis.getScale(), clonedAxis.getScale());
		assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
	}
}