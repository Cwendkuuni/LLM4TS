package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystem coord;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystemUtilities utilities;

 @Before
 public void setUp() {
 coord = new CoordSystem(500, 500);
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_PositiveXMinXMax() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int leftMargin = utilities.computeLeftMargin();
 assertEquals(20, leftMargin);
 }

 @Test
 public void testComputeLeftMargin_NegativeXMinXMax() {
 double xmin = -5.0;
 double xmax = 5.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int leftMargin = utilities.computeLeftMargin();
 double xminAbs = Math.abs(xmin);
 double xmaxAbs = Math.abs(xmax);
 TextLayout layout = new TextLayout(coord.getYAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
 int margin = (int)(maxlmargin - xminAbs / (xminAbs + xmaxAbs) * (coord.getBounds().getWidth() - coord.getRightMargin()));
 margin += 5;
 assertEquals(margin, leftMargin);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_NullCoord() {
 utilities = new CoordSystemUtilities(null, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_NullConstraints() {
 utilities = new CoordSystemUtilities(coord, null, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_NullConstraints2() {
 utilities = new CoordSystemUtilities(coord, constraints, null, model);
 }

 @Test
 public void testComputeLeftMargin_NullModel() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, null);
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int rightMargin = utilities.computeRightMargin();
 TextLayout layout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
 assertEquals(Math.max(width, 15), rightMargin);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeRightMargin_NullCoord() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeRightMargin();
 }

 @Test
 public void testComputeTopMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = -5.0;
 double ymax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int topMargin = utilities.computeTopMargin();
 TextLayout layout = new TextLayout(coord.getYAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 double height = layout.getBounds().getHeight();
 int n = (int)(height + 15.0 / 3.0 + layout.getDescent());
 assertEquals(Math.max(n, 15), topMargin);
 }

 @Test
 public void testComputeTopMargin_YMinGreaterThanZero() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 5.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int topMargin = utilities.computeTopMargin();
 assertEquals(20, topMargin);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeTopMargin_NullCoord() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeTopMargin();
 }

 @Test
 public void testComputeBottomMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = -5.0;
 double ymax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int bottomMargin = utilities.computeBottomMargin();
 TextLayout layout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
 int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (coord.getBounds().getHeight() - coord.getTopMargin()));
 margin += 10;
 assertEquals(margin, bottomMargin);
 }

 @Test
 public void testComputeBottomMargin_YMinLessThanZero() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = -5.0;
 double ymax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int bottomMargin = utilities.computeBottomMargin();
 double yminAbs = Math.abs(ymin);
 double ymaxAbs = Math.abs(ymax);
 TextLayout layout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
 int margin = (int)(maxbmargin - yminAbs / (yminAbs + ymaxAbs) * (coord.getBounds().getHeight() - coord.getTopMargin()));
 margin += 10;
 assertEquals(margin, bottomMargin);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeBottomMargin_NullCoord() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeBottomMargin();
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int height = utilities.computeXAxisLabelHeight();
 double min = constraints.getMinimumColumnValue();
 double max = constraints.getMaximumColumnValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double ypt = 0.0;
 if (constraints.getMinimumValue().doubleValue() > 0.0) {
 ypt = constraints.getMinimumValue().doubleValue();
 }
 else if (constraints.getMaximumValue().doubleValue() < 0.0) {
 ypt = constraints.getMaximumValue().doubleValue();
 }
 boolean paint = false;
 DecimalFormat df = coord.getXDecimalFormat();
 FontRenderContext frc = coord.getFontRenderContext();
 Font f = coord.getFont();
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 height = Math.max(height, (int)r.getHeight());
 }
 paint = !paint;
 }
 assertEquals(height, utilities.computeXAxisLabelHeight());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeXAxisLabelHeight_NullCoord() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeXAxisLabelHeight();
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.addColumn("Test", new String[]{"Test1", "Test2", "Test3", "Test4", "Test5"});

 int width = utilities.computeYAxisLabelWidth();
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double xpt = 0.0;
 if (constraints.getMinimumColumnValue() > 0.0) {
 xpt = constraints.getMinimumColumnValue();
 }
 else if (constraints.getMaximumColumnValue() < 0.0 && coord.getSecondYAxis() != null) {
 xpt = constraints.getMaximumColumnValue();
 }
 boolean paint = false;
 DecimalFormat df = coord.getYDecimalFormat();
 FontRenderContext frc = coord.getFontRenderContext();
 Font f = coord.getFont();
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 width = Math.max((int)r.getWidth(), width);
 }
 paint = !paint;
 }
 assertEquals(width, utilities.computeYAxisLabelWidth());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeYAxisLabelWidth_NullCoord() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeYAxisLabelWidth();
 }

}