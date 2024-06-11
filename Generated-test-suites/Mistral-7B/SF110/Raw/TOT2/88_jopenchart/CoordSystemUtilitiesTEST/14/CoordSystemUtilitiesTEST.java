package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;

 @Before
 public void setUp() {
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 this.utils = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Positive X values
 double xmin = 1.0;
 double xmax = 10.0;
 assertEquals(28, this.utils.computeLeftMargin());

 // Negative X values
 xmin = -1.0;
 xmax = 1.0;
 assertEquals(28, this.utils.computeLeftMargin());

 // Zero X values
 xmin = 0.0;
 xmax = 0.0;
 assertEquals(20, this.utils.computeLeftMargin());

 // Minimum X values are negative
 xmin = -1.0;
 xmax = 1.0;
 assertEquals(43, this.utils.computeLeftMargin());

 // Maximum X values are positive
 xmin = -1.0;
 xmax = 10.0;
 assertEquals(32, this.utils.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // X axis label width is less than 15 pixels
 final TextLayout layout = new TextLayout("X Axis Label", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
 final double width = layout.getBounds().getWidth();
 assertEquals(15, this.utils.computeRightMargin());

 // X axis label width is greater than 15 pixels
 final TextLayout layout2 = new TextLayout("Very long X Axis Label", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
 final double width2 = layout2.getBounds().getWidth();
 assertEquals(Math.max((int)width2, 30), this.utils.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 // Positive Y values
 double ymin = 1.0;
 double ymax = 10.0;
 assertEquals(30, this.utils.computeTopMargin());

 // Negative Y values
 ymin = -1.0;
 ymax = 0.0;
 assertEquals(30, this.utils.computeTopMargin());

 // Zero Y values
 ymin = 0.0;
 ymax = 0.0;
 assertEquals(20, this.utils.computeTopMargin());

 // Minimum Y values are negative
 ymin = -1.0;
 ymax = 0.0;
 assertEquals(45, this.utils.computeTopMargin());

 // Maximum Y values are positive
 ymin = -1.0;
 ymax = 10.0;
 assertEquals(33, this.utils.computeTopMargin());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeTopMarginWithNullFontContext() {
 this.utils.c = null;
 this.utils.computeTopMargin();
 }

 @Test
 public void testComputeBottomMargin() {
 // Positive Y values
 double ymin = 1.0;
 double ymax = 10.0;
 assertEquals(40, this.utils.computeBottomMargin());

 // Negative Y values
 ymin = -1.0;
 ymax = 0.0;
 assertEquals(40, this.utils.computeBottomMargin());

 // Zero Y values
 ymin = 0.0;
 ymax = 0.0;
 assertEquals(20, this.utils.computeBottomMargin());

 // Minimum Y values are negative
 ymin = -1.0;
 ymax = 0.0;
 assertEquals(61, this.utils.computeBottomMargin());

 // Maximum Y values are positive
 ymin = -1.0;
 ymax = 10.0;
 assertEquals(45, this.utils.computeBottomMargin());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeBottomMarginWithNullFontContext() {
 this.utils.c = null;
 this.utils.computeBottomMargin();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeXAxisLabelHeightWithNullModel() {
 this.utils.model = null;
 this.utils.computeXAxisLabelHeight();
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // No minimum value or maximum value
 assertEquals(0, this.utils.computeXAxisLabelHeight());

 // One minimum value and one maximum value
 double min = 1.0;
 double max = 10.0;
 assertEquals(14, this.utils.computeXAxisLabelHeight());

 // Minimum value is zero
 min = 0.0;
 assertEquals(14, this.utils.computeXAxisLabelHeight());

 // Maximum value is zero
 max = 0.0;
 assertEquals(14, this.utils.computeXAxisLabelHeight());
}

 @Test
 public void testComputeYAxisLabelWidth() {
 // No minimum value or maximum value
 assertEquals(0, this.utils.computeYAxisLabelWidth());

 // One minimum value and one maximum value
 double min = 1.0;
 double max = 10.0;
 assertEquals(7, this.utils.computeYAxisLabelWidth());

 // Minimum value is zero
 min = 0.0;
 assertEquals(7, this.utils.computeYAxisLabelWidth());

 // Maximum value is zero
 max = 0.0;
 assertEquals(7, this.utils.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with no labels
 this.utils.c.setPaintLabels(false);
 Graphics2D g = null;
 this.utils.drawNumericalXAxisTicks(g);

 // Test with labels
 this.utils.c.setPaintLabels(true);
 Graphics2D g2 = null;
 this.utils.drawNumericalXAxisTicks(g2);
 }

 @Test(expected = NullPointerException.class)
 public void testDrawNumericalXAxisTicksWithNullGraphicsContext() {
 this.utils.c.setPaintLabels(true);
 this.utils.drawNumericalXAxisTicks(null);
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with no labels
 this.utils.c.setPaintLabels(false);
 Graphics2D g = null;
 this.utils.drawXAxisTicks(g);

 // Test with labels
 this.utils.c.setPaintLabels(true);
 Graphics2D g2 = null;
 this.utils.drawXAxisTicks(g2);
 }

 @Test(expected = NullPointerException.class)
 public void testDrawXAxisTicksWithNullGraphicsContext() {
 this.utils.c.setPaintLabels(true);
 this.utils.drawXAxisTicks(null);
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with no labels
 this.utils.c.setPaintLabels(false);
 Graphics2D g = null;
 this.utils.drawYAxisTicks(g);

 // Test with labels
 this.utils.c.setPaintLabels(true);
 Graphics2D g2 = null;
 this.utils.drawYAxisTicks(g2);
 }

 @Test(expected = NullPointerException.class)
 public void testDrawYAxisTicksWithNullGraphicsContext() {
 this.utils.c.setPaintLabels(true);
 this.utils.drawYAxisTicks(null);
 }
}