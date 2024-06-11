import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;

    @Before
    public void setup() {
        coordSystem = new CoordSystem();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        NumberFormat yAxisFormat = new DecimalFormat("#.##");
        DecimalFormat xDecimalFormat = new DecimalFormat("#");
        coordSystem.setXAxisUnit(yAxisFormat.format(1));
        coordSystem.setYAxisUnit(xDecimalFormat.format(1));
        coordSystem.setFont(new Font("Arial", Font.BOLD, 12));
        coordSystem.setFontRenderContext(new FontRenderContext());

        int leftMargin = utilities.computeLeftMargin();
        int expectedLeftMargin = 38; // Calculated based on the given code
        assertEquals(expectedLeftMargin, leftMargin);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        constraints.setMinimumColumnValue(Double.NaN);
        constraints.setMaximumColumnValue(-1.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        utilities.computeLeftMargin();
    }

    // Test other methods with similar test cases and edge cases
    // ...
}