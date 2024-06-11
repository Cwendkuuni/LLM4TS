package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

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
 CoordSystem coord = new CoordSystem(500, 500, new StandardAxis(0.0, 10.0), new StandardAxis(0.0, 10.0), model);
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = -5.0;
 double xmax = 5.0;
 double expected = 122;
 int actual = utils.computeLeftMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeLeftMarginWithZeroXValues() {
 double xmin = 0.0;
 double xmax = 5.0;
 double expected = 122;
 int actual = utils.computeLeftMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeLeftMarginWithNegativeXValues() {
 double xmin = -5.0;
 double xmax = 0.0;
 double expected = 151;
 int actual = utils.computeLeftMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 double expected = 30;
 int actual = utils.computeRightMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double height = 10.0;
 double expected = 30;
 int actual = utils.computeTopMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = -5.0;
 double ymax = 5.0;
 double expected = 122;
 int actual = utils.computeBottomMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeBottomMarginWithZeroYValues() {
 double ymin = 0.0;
 double ymax = 5.0;
 double expected = 122;
 int actual = utils.computeBottomMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeBottomMarginWithNegativeYValues() {
 double ymin = -5.0;
 double ymax = 0.0;
 double expected = 151;
 int actual = utils.computeBottomMargin();
 assertEquals((int)expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = -5.0;
 double max = 5.0;
 double tick = 1.0;
 double ypt = 0.0;
 int expected = 15;
 int actual = utils.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = -5.0;
 double max = 5.0;
 double tick = 1.0;
 double xpt = 0.0;
 int expected = 30;
 int actual = utils.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utils.drawNumericalXAxisTicks(g);
 verify(g).draw(Mockito.any(Line2D.class));
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utils.drawXAxisTicks(g);
 verify(g).draw(Mockito.any(Line2D.class));
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utils.drawYAxisTicks(g);
 verify(g).draw(Mockito.any(Line2D.class));
 }

}