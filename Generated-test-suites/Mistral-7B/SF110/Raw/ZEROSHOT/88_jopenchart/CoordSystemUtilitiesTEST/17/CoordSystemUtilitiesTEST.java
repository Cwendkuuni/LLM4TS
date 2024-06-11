package de.progra.charting.test;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 final DecimalFormat xFormat = new DecimalFormat("#.##");
 final DecimalFormat yFormat = new DecimalFormat("#.##");
 final Font font = new Font("SansSerif", Font.BOLD, 12);
 final ChartDataModelConstraints xConstraints = new ChartDataModelConstraints(xFormat, null, null, 0.0, 10.0, 1.0, 1.0, 10, 0, null);
 final ChartDataModelConstraints yConstraints = new ChartDataModelConstraints(yFormat, null, null, 0.0, 10.0, 1.0, 1.0, 10, 0, null);
 final ChartDataModelConstraints xConstraints2 = new ChartDataModelConstraints(yFormat, null, null, 0.0, 10.0, 1.0, 1.0, 10, 0, null);
 final ChartDataModel dataModel = new ChartDataModel();
 final CoordSystem coordSystem = new CoordSystem(new Dimension(300, 300), font, new java.awt.FontRenderContext(), new java.text.AttributedCharacterIterator.Factory() {
 public AttributedCharacterIterator getIterator(String s) {
 return new SimpleAttributeIterator(s, new SimpleAttributeSet());
 }
 });
 this.constraints = xConstraints;
 this.constraints2 = xConstraints2;
 this.model = dataModel;
 this.utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 final int leftMargin = utilities.computeLeftMargin();
 assertEquals(20, leftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 final int rightMargin = utilities.computeRightMargin();
 assertEquals(15, rightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 final int topMargin = utilities.computeTopMargin();
 assertEquals(15, topMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 final int bottomMargin = utilities.computeBottomMargin();
 assertEquals(20, bottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int xAxisLabelHeight = utilities.computeXAxisLabelHeight();
 assertEquals(21, xAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int yAxisLabelWidth = utilities.computeYAxisLabelWidth();
 assertEquals(21, yAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 final Graphics2D g = new Graphics2D(new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB));
 utilities.drawNumericalXAxisTicks(g);
 }

 @Test
 public void testDrawXAxisTicks() {
 final Graphics2D g = new Graphics2D(new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB));
 utilities.drawXAxisTicks(g);
 }

 @Test
 public void testDrawYAxisTicks() {
 final Graphics2D g = new Graphics2D(new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB));
 utilities.drawYAxisTicks(g);
 }

 @Test
 public void testGetXAxisLine2D() {
 final Line2D xAxisLine = utilities.getXAxisLine2D();
 assertEquals(0.0, xAxisLine.getX1(), 0.01);
 assertEquals(0.0, xAxisLine.getY1(), 0.01);
 assertEquals(10.0, xAxisLine.getX2(), 0.01);
 assertEquals(0.0, xAxisLine.getY2(), 0.01);
 }

 @Test
 public void testGetYAxisLine2D() {
 final Line2D yAxisLine = utilities.getYAxisLine2D();
 assertEquals(0.0, yAxisLine.getX1(), 0.01);
 assertEquals(0.0, yAxisLine.getY1(), 0.01);
 assertEquals(0.0, yAxisLine.getX2(), 0.01);
 assertEquals(10.0, yAxisLine.getY2(), 0.01);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 final Line2D secondYAxisLine = utilities.getSecondYAxisLine2D();
 assertEquals(10.0, secondYAxisLine.getX1(), 0.01);
 assertEquals(0.0, secondYAxisLine.getY1(), 0.01);
 assertEquals(10.0, secondYAxisLine.getX2(), 0.01);
 assertEquals(10.0, secondYAxisLine.getY2(), 0.01);
 }
}