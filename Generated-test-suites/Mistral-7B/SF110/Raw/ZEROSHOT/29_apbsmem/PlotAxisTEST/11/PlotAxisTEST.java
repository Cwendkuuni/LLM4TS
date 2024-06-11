package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.LinearAxisScale;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.PlotRunList;

public class PlotAxisTest {

	@Test
	public void testDefaultConstructor() {
		PlotAxis axis = new PlotAxis();
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
	public void testCustomConstructor() {
		DecimalFormat format = new DecimalFormat("#.##");
		PlotAxis axis = new PlotAxis("Test Axis", new LinearAxisScale(), format, 2);
		assertEquals(1.0, axis.xA, 0.0);
		assertEquals(1.0, axis.xB, 0.0);
		assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
		assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
		assertEquals(2, axis.gridStyle);
		assertNotNull(axis.scale);
		assertNotNull(axis.label);
		assertEquals("Test Axis", axis.label);
	}

	@Test(expected = NullPointerException.class)
	public void testScaleNull() {
		PlotAxis axis = new PlotAxis();
		axis.setScale(null);
	}

	@Test
	public void testSetLabel() {
		PlotAxis axis = new PlotAxis();
		axis.setLabel("Test Label");
		assertEquals("Test Label", axis.getLabel());
	}

	@Test
	public void testGetLabel() {
		PlotAxis axis = new PlotAxis();
		assertNull(axis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxis axis = new PlotAxis();
		PlotAxisScale scale = new LinearAxisScale();
		axis.setScale(scale);
		assertEquals(scale, axis.getScale());
	}

	@Test
	public void testGetScale() {
		PlotAxis axis = new PlotAxis();
		assertNull(axis.getScale());
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		DecimalFormat format = new DecimalFormat("#.##");
		axis.setNumberFormat(format);
		assertEquals(format, axis.getNumberFormat());
	}

	@Test
	public void testGetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis();
		axis.setGridStyle(1);
		assertEquals(1, axis.getGridStyle());
	}

	@Test
	public void testGetGridStyle() {
		PlotAxis axis = new PlotAxis();
		assertEquals(1, axis.getGridStyle());
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
		axis.setLowerBounds(5.0);
		assertEquals(5.0, axis.getLowerBounds(), 0.0);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setLowerBounds(5.0);
		DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
		assertEquals("5.00", axis.lowerBoundsAsString());
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
		axis.setUpperBounds(10.0);
		assertEquals(10.0, axis.getUpperBounds(), 0.0);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis();
		axis.setUpperBounds(10.0);
		DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
		assertEquals("10.00", axis.upperBoundsAsString());
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
		axis.setTickStepSize(2.5);
		assertEquals(2.5, axis.getTickStepSize(), 0.0);
	}

	@Test
	public void testSetAxisBounds() {
		PlotAxis axis = new PlotAxis();
		PlotRunList list = new PlotRunList();
		axis.setAxisBounds(list);
		// Testing abstract method
		fail("setAxisBounds is abstract and should not be called directly.");
	}

	@Test
	public void testSetSize() {
		PlotAxis axis = new PlotAxis();
		Component component = new Component() {
			@Override
			public void paint(Graphics g) {
			}
		};
		Rectangle plotFrame = new Rectangle(50, 50, 100, 100);
		axis.setSize(plotFrame);
		// Testing protected methods
		fail("setSize, setAxisTickLimits, and resizeAxis are protected and should not be called directly in a test.");
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis();
		assertEquals(0, axis.scaleCoord(0.0));
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clone = (PlotAxis)axis.clone();
		assertNotSame(axis, clone);
		assertEquals(axis.xA, clone.xA, 0.0);
		assertEquals(axis.xB, clone.xB, 0.0);
		assertEquals(axis.aLB, clone.aLB, 0.0);
		assertEquals(axis.aUB, clone.aUB, 0.0);
		assertEquals(axis.gridStyle, clone.gridStyle);
		assertEquals(axis.scale, clone.scale);
		assertEquals(axis.nf, clone.nf);
		assertEquals(axis.label, clone.label);
	}
}