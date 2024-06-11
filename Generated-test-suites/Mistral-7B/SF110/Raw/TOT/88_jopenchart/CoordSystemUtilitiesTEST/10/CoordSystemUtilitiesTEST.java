package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

import org.junit.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @Before
 public void setUp() {
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_positiveXminAndXmax() {
 double xmin = 10.0;
 double xmax = 20.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedLeftMargin = 20;
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeLeftMargin_negativeXminAndPositiveXmax() {
 double xmin = -5.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedLeftMargin = 58; // calculated based on provided code
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_nullConstraints() {
 utilities = new CoordSystemUtilities(new CoordSystem(), null, constraints2, model);
 int actualLeftMargin = utilities.computeLeftMargin();
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_nullModel() {
 utilities = new CoordSystemUtilities(new CoordSystem(), constraints, null, model);
 int actualLeftMargin = utilities.computeLeftMargin();
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = 0.0;
 double xmax = 20.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedRightMargin = 21; // calculated based on provided code
 int actualRightMargin = utilities.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 double xmin = 0.0;
 double xmax = 20.0;
 double ymin = -5.0;
 double ymax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedTopMargin = 15; // calculated based on provided code
 int actualTopMargin = utilities.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin_positiveYminAndYmax() {
 double xmin = 0.0;
 double xmax = 20.0;
 double ymin = 5.0;
 double ymax = 15.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedBottomMargin = 20; // calculated based on provided code
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeBottomMargin_negativeYminAndPositiveYmax() {
 double xmin = 0.0;
 double xmax = 20.0;
 double ymin = -5.0;
 double ymax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedBottomMargin = 41; // calculated based on provided code
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = -10.0;
 double max = 10.0;
 double tick = 1.0;
 int height = 0;
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 constraints.setMinimumValue(new DoubleValue(min));
 constraints.setMaximumValue(new DoubleValue(max));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedHeight = 4; // calculated based on provided code
 int actualHeight = utilities.computeXAxisLabelHeight();
 assertEquals(expectedHeight, actualHeight);
 }

 @Test
 public void testComputeXAxisLabelWidth() {
 double min = -10.0;
 double max = 10.0;
 double tick = 1.0;
 int width = 0;
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 constraints.setMinimumValue(new DoubleValue(min));
 constraints.setMaximumValue(new DoubleValue(max));
 model.setColumns(new String[]{"Column1", "Column2"});
 model.setRowData(new Object[][]{new Double[]{1.0, 2.0}, new Double[]{3.0, 4.0}});

 int expectedWidth = 4; // calculated based on provided code
 int actualWidth = utilities.computeXAxisLabelWidth();
 assertEquals(expectedWidth, actualWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utilities.drawNumericalXAxisTicks(g);

 // Add verification code here based on your mocked Graphics2D object
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utilities.drawXAxisTicks(g);

 // Add verification code here based on your mocked Graphics2D object
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 utilities.drawYAxisTicks(g);

 // Add verification code here based on your mocked Graphics2D object
 }
}