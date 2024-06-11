package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private CoordSystem coord;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(new DoubleRange(0.0, 10.0), new DoubleRange(-5.0, 5.0), "TestXaxis");
 constraints2 = new ChartDataModelConstraints(new DoubleRange(-5.0, 5.0), new DoubleRange(0.0, 10.0), "TestYaxis");
 coord = new CoordSystem(100, 100, 10, 10, 5, 5, model, constraints, constraints2);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double minX = constraints.getMinimumColumnValue();
 double maxX = constraints.getMaximumColumnValue();
 double minY = constraints.getMinimumValue().doubleValue();
 double maxY = constraints.getMaximumValue().doubleValue();
 TextLayout xAxisLayout = new TextLayout(coord.getYAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 TextLayout yAxisLayout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int expectedMaxXLabelWidth = utilities.computeYAxisLabelWidth() + 6;
 int expectedLabelWidth = (int)(xAxisLayout.getBounds().getWidth() + 6);
 int expectedMargin = Math.max(expectedMaxXLabelWidth, expectedLabelWidth) - (minX / (minX + maxX) * (coord.getBounds().getWidth() - coord.getRightMargin())) + 5;
 int actualMargin = utilities.computeLeftMargin();
 assertEquals("computeLeftMargin: wrong margin calculated", expectedMargin, actualMargin);
 }

 @Test
 public void testComputeRightMargin() {
 TextLayout xAxisLayout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int expectedWidth = (int)(xAxisLayout.getBounds().getWidth() + 15.0 / 3.0);
 int actualMargin = utilities.computeRightMargin();
 assertEquals("computeRightMargin: wrong margin calculated", expectedWidth, actualMargin);
 }

 @Test
 public void testComputeTopMargin() {
 TextLayout yAxisLayout = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int expectedHeight = (int)(yAxisLayout.getBounds().getHeight() + 15.0 / 3.0 + yAxisLayout.getDescent());
 int actualMargin = utilities.computeTopMargin();
 assertEquals("computeTopMargin: wrong margin calculated", expectedHeight, actualMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 double minX2 = constraints2.getMinimumColumnValue();
 double maxX2 = constraints2.getMaximumColumnValue();
 double minY2 = constraints2.getMinimumValue().doubleValue();
 double maxY2 = constraints2.getMaximumValue().doubleValue();
 TextLayout xAxisLayout2 = new TextLayout(coord.getXAxisUnit(), coord.getFont(), coord.getFontRenderContext());
 int expectedMaxYLabelHeight = utilities.computeXAxisLabelHeight() + 6;
 int expectedLabelHeight2 = (int)(xAxisLayout2.getBounds().getHeight());
 int expectedMargin = Math.max(expectedMaxYLabelHeight, expectedLabelHeight2) - (minY2 / (minY2 + maxY2) * (coord.getBounds().getHeight() - coord.getTopMargin())) + 10;
 int actualMargin = utilities.computeBottomMargin();
 assertEquals("computeBottomMargin: wrong margin calculated", expectedMargin, actualMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double minX = constraints.getMinimumColumnValue();
 double maxX = constraints.getMaximumColumnValue();
 double minY = constraints.getMinimumValue().doubleValue();
 double maxY = constraints.getMaximumValue().doubleValue();
 double tickSpacing = ChartUtilities.calculateTickSpacing(minX, maxX);
 double ypt = 0.0;
 int expectedHeight = 0;
 if (minY > 0.0) {
 ypt = minY;
 } else if(maxY < 0.0) {
 ypt = maxY;
 }
 DecimalFormat df = coord.getXDecimalFormat();
 FontRenderContext frc = coord.getFontRenderContext();
 Font f = coord.getFont();
 boolean paint = false;
 for (double d = minX; d <= maxX; d += tickSpacing) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 expectedHeight += r.getHeight();
 }
 paint = !paint;
 }
 int actualHeight = utilities.computeXAxisLabelHeight();
 assertEquals("computeXAxisLabelHeight: wrong height calculated", expectedHeight, actualHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double minX = constraints.getMinimumColumnValue();
 double maxX = constraints.getMaximumColumnValue();
 double minY = constraints.getMinimumValue().doubleValue();
 double maxY = constraints.getMaximumValue().doubleValue();
 double tickSpacing = ChartUtilities.calculateTickSpacing(minY, maxY);
 double xpt = 0.0;
 int expectedWidth = 0;
 if (minX > 0.0) {
 xpt = minX;
 } else if(maxX < 0.0 && coord.getSecondYAxis() != null) {
 xpt = maxX;
 }
 DecimalFormat df = coord.getYDecimalFormat();
 FontRenderContext frc = coord.getFontRenderContext();
 Font f = coord.getFont();
 boolean paint = false;
 for (double d = minY; d <= maxY; d += tickSpacing) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 expectedWidth += (int)r.getWidth();
 }
 paint = !paint;
 }
 int actualWidth = utilities.computeYAxisLabelWidth();
 assertEquals("computeYAxisLabelWidth: wrong width calculated", expectedWidth, actualWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
 utilities.drawNumericalXAxisTicks(g);
 // Testing is not possible in JUnit, as it requires a graphical environment
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
 utilities.drawXAxisTicks(g);
 // Testing is not possible in JUnit, as it requires a graphical environment
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
 utilities.drawYAxisTicks(g);
 // Testing is not possible in JUnit, as it requires a graphical environment
 }

}