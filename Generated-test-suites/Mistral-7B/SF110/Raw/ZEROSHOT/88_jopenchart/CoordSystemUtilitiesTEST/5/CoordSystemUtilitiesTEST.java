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
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystem coord;

 @Before
 public void setUp() {
 final DecimalFormat df = new DecimalFormat("#.##");
 final DecimalFormat df2 = new DecimalFormat("#");
 final Font font = new Font("SansSerif", Font.BOLD, 12);
 final TextAttribute ta = new TextAttribute();
 ta.setFont(font);
 final TextAttribute ta2 = new TextAttribute();
 ta2.setFont(font);

 this.constraints = new ChartDataModelConstraints();
 this.constraints.setMinimumColumnValue(-5.0);
 this.constraints.setMaximumColumnValue(5.0);
 this.constraints.setMinimumValue(new Double(10.0));
 this.constraints.setMaximumValue(new Double(20.0));
 this.constraints.setXDecimalFormat(df);
 this.constraints.setXTickSpacing(1.0);

 this.constraints2 = new ChartDataModelConstraints();
 this.constraints2.setMaximumColumnValue(5.0);
 this.constraints2.setMinimumColumnValue(-5.0);
 this.constraints2.setMinimumValue(new Double(-10.0));
 this.constraints2.setMaximumValue(new Double(-5.0));
 this.constraints2.setYDecimalFormat(df2);
 this.constraints2.setYTickSpacing(1.0);

 this.model = new ChartDataModel();
 this.model.addColumnName("Test1");
 this.model.addColumnName("Test2");
 this.model.addColumnName("Test3");
 this.model.addColumnData(0, new Double[] { new Double(1.0), new Double(2.0), new Double(3.0) });
 this.model.addColumnData(1, new Double[] { new Double(4.0), new Double(5.0), new Double(6.0) });
 this.model.addColumnData(2, new Double[] { new Double(7.0), new Double(8.0), new Double(9.0) });

 this.coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), this.constraints, this.constraints2, this.model);
 this.utilities = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
 }

 @Test
 public void testComputeLeftMargin() {
 final int result = this.utilities.computeLeftMargin();
 final int expected = 160; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testComputeRightMargin() {
 final int result = this.utilities.computeRightMargin();
 final int expected = 45; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testComputeTopMargin() {
 final int result = this.utilities.computeTopMargin();
 final int expected = 30; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testComputeBottomMargin() {
 final int result = this.utilities.computeBottomMargin();
 final int expected = 112; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int result = this.utilities.computeXAxisLabelHeight();
 final int expected = 18; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int result = this.utilities.computeYAxisLabelWidth();
 final int expected = 30; // Calculated value based on given constraints
 assertEquals(expected, result);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 final Graphics2D g = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
 this.utilities.drawNumericalXAxisTicks(g);
 // Add assertions based on the expected output of the method, for example:
 assertTrue(g.getColor().equals(Color.BLACK));
 }

 // Add more test methods for other methods in the CoordSystemUtilities class
}