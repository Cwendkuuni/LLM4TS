package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utils;

    @Before
    public void setUp() {
        CoordSystem coord = new CoordSystem(new JFreeChart(), new ChartDataModel(), new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool()), new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool()));
        this.utils = new CoordSystemUtilities(coord, new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool()), new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool()), new ChartDataModel(new DataKeyPool()));
    }

    @Test
    public void testComputeLeftMargin() {
        double[] values = {-5.0, 0.0, 5.0};
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool());
        constraints.setMinimumColumnValue(values[0]);
        constraints.setMaximumColumnValue(values[2]);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new JFreeChart(), new ChartDataModel(), constraints, constraints), constraints, constraints, new ChartDataModel(new DataKeyPool()));
        int leftMargin = utils.computeLeftMargin();
        assertEquals(20, leftMargin);

        double minColumnValue = -10.0;
        double maxColumnValue = 10.0;
        constraints.setMinimumColumnValue(minColumnValue);
        constraints.setMaximumColumnValue(maxColumnValue);
        utils = new CoordSystemUtilities(new CoordSystem(new JFreeChart(), new ChartDataModel(), constraints, constraints), constraints, constraints, new ChartDataModel(new DataKeyPool()));
        leftMargin = utils.computeLeftMargin();
        double xmin = Math.abs(minColumnValue);
        double xmax = Math.abs(maxColumnValue);
        TextLayout layout = new TextLayout(utils.c.getYAxisUnit(), utils.c.getFont(), utils.c.getFontRenderContext());
        int maxlmargin = utils.computeYAxisLabelWidth() + 6;
        maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
        int margin = (int)(maxlmargin - xmin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin()));
        margin += 5;
        int expectedMargin = (int)(maxlmargin - xmin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin())) + 5;
        assertEquals(expectedMargin, leftMargin);

        DecimalFormat df = new DecimalFormat("#.##");
        TextLayout labelLayout = new TextLayout(df.format(xmin), utils.c.getFont(), utils.c.getFontRenderContext());
        margin = (int)(maxlmargin - xmin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin())) + labelLayout.getBounds().getWidth() + 5;
        expectedMargin = margin;
        if (margin < 20) {
            expectedMargin = 20;
        }
        assertEquals(expectedMargin, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utils = this.utils;
        int rightMargin = utils.computeRightMargin();
        assertEquals(15, rightMargin);

        TextLayout layout = new TextLayout(utils.c.getXAxisUnit(), utils.c.getFont(), utils.c.getFontRenderContext());
        double width = layout.getBounds().getWidth();
        int n = (int)(width + 15.0 / 3.0);
        int expectedRightMargin = Math.max(n, 15);
        assertEquals(expectedRightMargin, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utils = this.utils;
        int topMargin = utils.computeTopMargin();
        assertEquals(15, topMargin);

        TextLayout layout = new TextLayout(utils.c.getYAxisUnit(), utils.c.getFont(), utils.c.getFontRenderContext());
        double height = layout.getBounds().getHeight();
        int n = (int)(height + 15.0 / 3.0 + layout.getDescent());
        int expectedTopMargin = Math.max(n, 15);
        assertEquals(expectedTopMargin, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        double[] values = {-5.0, 0.0, 5.0};
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new DataKeyPool(), new DataKeyPool());
        constraints.setMinimumValue(new Double(-5.0));
        constraints.setMaximumValue(new Double(5.0));
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new JFreeChart(), new ChartDataModel(), constraints, constraints), constraints, constraints, new ChartDataModel(new DataKeyPool()));
        int bottomMargin = utils.computeBottomMargin();
        assertEquals(20, bottomMargin);

        double minValue = -10.0;
        double maxValue = 10.0;
        constraints.setMinimumValue(new Double(minValue));
        constraints.setMaximumValue(new Double(maxValue));
        utils = new CoordSystemUtilities(new CoordSystem(new JFreeChart(), new ChartDataModel(), constraints, constraints), constraints, constraints, new ChartDataModel(new DataKeyPool()));
        bottomMargin = utils.computeBottomMargin();
        double ymin = Math.abs(minValue);
        double ymax = Math.abs(maxValue);
        layout = new TextLayout(utils.c.getXAxisUnit(), utils.c.getFont(), utils.c.getFontRenderContext());
        int maxbmargin = utils.computeXAxisLabelHeight() + 6;
        maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
        int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (utils.c.getBounds().getHeight() - utils.c.getTopMargin()));
        margin += 10;
        int expectedMargin = margin;
        if (margin < 20) {
            expectedMargin = 20;
        }
        assertEquals(expectedMargin, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities utils = this.utils;
        int height = utils.computeXAxisLabelHeight();
        fail("Method should not be called directly");
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities utils = this.utils;
        int width = utils.computeYAxisLabelWidth();
        fail("Method should not be called directly");
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemUtilities utils = this.utils;
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utils.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystemUtilities utils = this.utils;
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utils.drawXAxisTicks(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystemUtilities utils = this.utils;
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utils.drawYAxisTicks(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities utils = this.utils;
        Line2D line = utils.getXAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities utils = this.utils;
        Line2D line = utils.getYAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities utils = this.utils;
        Line2D line = utils.getSecondYAxisLine2D();
        assertNull(line);
    }
}