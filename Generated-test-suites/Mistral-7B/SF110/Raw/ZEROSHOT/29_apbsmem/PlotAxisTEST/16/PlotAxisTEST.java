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
	public void testPlotAxisConstructor() {
		PlotAxis axis = new PlotAxis();
		assertNotNull(axis);

		PlotAxis axisWithLabel = new PlotAxis("TestAxis", new LinearAxisScale(), new DecimalFormat("#.##"), 1);
		assertNotNull(axisWithLabel);
		assertEquals("TestAxis", axisWithLabel.getLabel());
	}

	@Test
	public void testSetGetLabel() {
		PlotAxis axis = new PlotAxis();
		assertNull(axis.getLabel());

		axis.setLabel("TestAxis");
		assertEquals("TestAxis", axis.getLabel());
	}

	@Test
	public void testSetGetScale() {
		PlotAxis axis = new PlotAxis();
		assertSame(axis.getScale(), new LinearAxisScale());

		PlotAxisScale scale = new LinearAxisScale();
		axis.setScale(scale);
		assertSame(axis.getScale(), scale);
	}

	@Test
	public void testSetGetNumberFormat() {
		PlotAxis axis = new PlotAxis();
		assertSame(axis.getNumberFormat(), NumberFormat.getInstance());

		DecimalFormat format = new DecimalFormat("#.##");
		axis.setNumberFormat(format);
		assertSame(axis.getNumberFormat(), format);
	}

	@Test
	public void testSetGetGridStyle() {
		PlotAxis axis = new PlotAxis();
		assertEquals(1, axis.getGridStyle());

		axis.setGridStyle(2);
		assertEquals(2, axis.getGridStyle());
	}

	@Test
	public void testManualBounds() {
		PlotAxis axis = new PlotAxis();

		axis.manualLowerBounds(true);
		axis.setLowerBounds(10.5);
		assertEquals(10.5, axis.getLowerBounds(), 0.001);

		axis.manualUpperBounds(true);
		axis.setUpperBounds(20.3);
		assertEquals(20.3, axis.getUpperBounds(), 0.001);

		axis.manualLowerBounds(false);
		axis.setLowerBounds(15.6);
		assertEquals(15.6, axis.getLowerBounds(), 0.001);

		axis.manualUpperBounds(false);
		axis.setUpperBounds(25.7);
		assertEquals(25.7, axis.getUpperBounds(), 0.001);
	}

	@Test
	public void testTickStepSize() {
		PlotAxis axis = new PlotAxis();

		axis.manualTickStepSize(true);
		axis.setTickStepSize(5.0);
		assertEquals(5.0, axis.getTickStepSize(), 0.001);

		axis.manualTickStepSize(false);
		axis.setTickStepSize(10.0);
		assertEquals(10.0, axis.getTickStepSize(), 0.001);
	}

	@Test
	public void testSetAxisBounds() {
		PlotAxis axis = new PlotAxis();
		PlotRunList runList = new PlotRunList();

		axis.setAxisBounds(runList);
	}

	@Test
	public void testSetSize() {
		PlotAxis axis = new PlotAxis();
		Component component = new Component() {
			@Override
			public void paint(Graphics g) {
				// empty
			}
		};
		Rectangle plotFrame = new Rectangle(0, 0, 100, 100);

		axis.setSize(plotFrame);
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis();
		assertEquals(0, axis.scaleCoord(0.0));
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis();
		PlotAxis clonedAxis = (PlotAxis) axis.clone();
		assertNotSame(axis, clonedAxis);
	}

	@Test
	public void testDraw() {
		PlotAxis axis = new PlotAxis();
		Component component = new Component() {
			@Override
			public void paint(Graphics g) {
				// empty
			}
		};
		Rectangle plotFrame = new Rectangle(0, 0, 100, 100);

		axis.setSize(plotFrame);
		axis.draw(new Graphics() {
			@Override
			public void drawLine(int x1, int y1, int x2, int y2) {
				// empty
			}

			@Override
			public void setColor(Color c) {
				// empty
			}
		}, component, plotFrame);
	}

	@Test
	public void testFindGoodLimits() {
		PlotAxisScale scale = new LinearAxisScale();
		PlotAxis axis = new PlotAxis();

		AxisLimitData data = scale.findGoodLimits(1.0, 10.0);
		assertEquals(1.0, data.lb, 0.001);
		assertEquals(5.0, data.ub, 0.001);
		assertEquals(2.0, data.quantum, 0.001);

		data = scale.findGoodLimits(10.0, 1.0);
		assertEquals(5.0, data.lb, 0.001);
		assertEquals(1.0, data.ub, 0.001);
		assertEquals(-2.0, data.quantum, 0.001);
	}

	@Test
	public void testResizeAxis() {
		PlotAxis axis = new PlotAxis() {
			@Override
			protected void resizeAxis(Rectangle p0) {
				// empty
			}
		};
		Rectangle plotFrame = new Rectangle(0, 0, 100, 100);

		axis.setSize(plotFrame);
		axis.resizeAxis(plotFrame);
	}

	@Test(expected = CloneNotSupportedException.class)
	public void testCloneNotSupportedException() {
		PlotAxis axis = new PlotAxis();
		axis.clone();
	}
}