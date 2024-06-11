package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @Before
 public void setup() {
 CoordSystem coord = new CoordSystem(new Dimension(500, 500), new Dimension(50, 50), new Dimension(50, 50), null, null);
 ChartDataModel model = new ChartDataModel();
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with valid data
 double xmin = -5.0;
 double xmax = 5.0;
 DecimalFormat df = new DecimalFormat("0.00");
 TextLayout layout = new TextLayout("Unit", new Font("Arial", Font.BOLD, 12), utilities.c.getFontRenderContext());
 int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 int width = utilities.c.getBounds().getWidth() - utilities.c.getRightMargin();
 double x = xmin / (xmin + xmax) * (width - utilities.marginOffset);
 int margin = (int)(maxlmargin - x / (xmin + xmax) * (width - utilities.marginOffset)) + 5;
 assertEquals(21, margin);

 // Test with xmin <= 0.0 and xmax > 0.0
 xmin = -2.5;
 xmax = 2.5;
 maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 width = utilities.c.getBounds().getWidth() - utilities.c.getRightMargin();
 x = xmin / (xmin + xmax) * (width - utilities.marginOffset);
 margin = (int)(maxlmargin - x / (xmin + xmax) * (width - utilities.marginOffset)) + 5;
 assertEquals(21, margin);

 // Test with xmin <= 0.0 and xmax <= 0.0
 xmin = -5.0;
 xmax = -3.0;
 maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 width = utilities.c.getBounds().getWidth() - utilities.c.getRightMargin();
 x = xmin / (xmin + xmax) * (width - utilities.marginOffset);
 margin = (int)(maxlmargin - x / (xmin + xmax) * (width - utilities.marginOffset)) + 5;
 assertEquals(21, margin);

 // Test with xmin > 0.0 and xmax <= 0.0
 xmin = 3.0;
 xmax = -2.5;
 maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 width = utilities.c.getBounds().getWidth() - utilities.c.getRightMargin();
 x = xmin / (xmin + xmax) * (width - utilities.marginOffset);
 margin = (int)(maxlmargin - x / (xmin + xmax) * (width - utilities.marginOffset)) + 5;
 assertEquals(21, margin);

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.computeLeftMargin();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeRightMargin() {
 // Test with valid data
 TextLayout layout = new TextLayout("Unit", new Font("Arial", Font.BOLD, 12), utilities.c.getFontRenderContext());
 int width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
 assertEquals(32, utilities.computeRightMargin());

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.computeRightMargin();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeTopMargin() {
 // Test with valid data
 TextLayout layout = new TextLayout("Unit", new Font("Arial", Font.BOLD, 12), utilities.c.getFontRenderContext());
 int height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
 assertEquals(32, utilities.computeTopMargin());

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.computeTopMargin();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with valid data
 double ymin = -5.0;
 double ymax = 5.0;
 TextLayout layout = new TextLayout("Unit", new Font("Arial", Font.BOLD, 12), utilities.c.getFontRenderContext());
 int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 int height = utilities.c.getBounds().getHeight() - utilities.c.getTopMargin();
 double y = ymin / (ymin + ymax) * (height - utilities.marginOffset);
 int margin = (int)(maxbmargin - y / (ymin + ymax) * (height - utilities.marginOffset)) + 10;
 assertEquals(32, margin);

 // Test with ymin <= 0.0 and ymax > 0.0
 ymin = -2.5;
 ymax = 2.5;
 maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 height = utilities.c.getBounds().getHeight() - utilities.c.getTopMargin();
 y = ymin / (ymin + ymax) * (height - utilities.marginOffset);
 margin = (int)(maxbmargin - y / (ymin + ymax) * (height - utilities.marginOffset)) + 10;
 assertEquals(32, margin);

 // Test with ymin <= 0.0 and ymax <= 0.0
 ymin = -5.0;
 ymax = -3.0;
 maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 height = utilities.c.getBounds().getHeight() - utilities.c.getTopMargin();
 y = ymin / (ymin + ymax) * (height - utilities.marginOffset);
 margin = (int)(maxbmargin - y / (ymin + ymax) * (height - utilities.marginOffset)) + 10;
 assertEquals(32, margin);

 // Test with ymin > 0.0 and ymax <= 0.0
 ymin = 3.0;
 ymax = -2.5;
 maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 height = utilities.c.getBounds().getHeight() - utilities.c.getTopMargin();
 y = ymin / (ymin + ymax) * (height - utilities.marginOffset);
 margin = (int)(maxbmargin - y / (ymin + ymax) * (height - utilities.marginOffset)) + 10;
 assertEquals(32, margin);

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.computeBottomMargin();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with valid data
 DecimalFormat df = new DecimalFormat("0.00");
 FontRenderContext frc = utilities.c.getFontRenderContext();
 Font f = utilities.c.getFont();
 double min = -5.0;
 double max = 5.0;
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double ypt = 0.0;
 if (utilities.constraints.getMinimumValue().doubleValue() > 0.0) {
 ypt = utilities.constraints.getMinimumValue().doubleValue();
 } else if (utilities.constraints.getMaximumValue().doubleValue() < 0.0) {
 ypt = utilities.constraints.getMaximumValue().doubleValue();
 }
 boolean paint = false;
 int height = 0;
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 height = Math.max(height, (int)r.getHeight());
 }
 paint = !paint;
 }
 assertEquals(59, height);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with valid data
 DecimalFormat df = new DecimalFormat("0.00");
 FontRenderContext frc = utilities.c.getFontRenderContext();
 Font f = utilities.c.getFont();
 double min = -5.0;
 double max = 5.0;
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double xpt = 0.0;
 if (utilities.constraints.getMinimumColumnValue() > 0.0) {
 xpt = utilities.constraints.getMinimumColumnValue();
 } else if (utilities.constraints.getMaximumColumnValue() < 0.0 && utilities.c.getSecondYAxis() != null) {
 xpt = utilities.constraints.getMaximumColumnValue();
 }
 boolean paint = false;
 int width = 0;
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 width = Math.max((int)r.getWidth(), width);
 }
 paint = !paint;
 }
 assertEquals(117, width);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with valid data
 Graphics2D g = null;
 try {
 utilities.drawNumericalXAxisTicks(g);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 g = utilities.c.createGraphics();
 utilities.drawNumericalXAxisTicks(g);
 g.dispose();
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with valid data
 Graphics2D g = null;
 try {
 utilities.drawXAxisTicks(g);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 g = utilities.c.createGraphics();
 utilities.drawXAxisTicks(g);
 g.dispose();

 // Test with empty data
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(utilities.c, utilities.constraints, utilities.constraints2, model);
 g = utilities.c.createGraphics();
 utilities.drawXAxisTicks(g);
 g.dispose();

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.drawXAxisTicks(g);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with valid data
 Graphics2D g = null;
 try {
 utilities.drawYAxisTicks(g);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 g = utilities.c.createGraphics();
 utilities.drawYAxisTicks(g);
 g.dispose();

 // Test with empty data
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(utilities.c, utilities.constraints, utilities.constraints2, model);
 g = utilities.c.createGraphics();
 utilities.drawYAxisTicks(g);
 g.dispose();

 // Test with invalid data
 utilities.c = null;
 try {
 utilities.drawYAxisTicks(g);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}