import jahuwaldt.plot.PlotAxis;
import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String newLabel = "TestLabel";
        axis.setLabel(newLabel);
        String expectedLabel = "TestLabel";
        String actualLabel = axis.getLabel();
        assert(expectedLabel.equals(actualLabel));
    }

    @Test(expected = NullPointerException.class)
    public void testSetLabelNull() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel(null);
    }

    @Test
    public void testSetLabelEmptyString() {
        PlotAxis axis = new PlotAxis();
        String newLabel = "";
        axis.setLabel(newLabel);
        String expectedLabel = "";
        String actualLabel = axis.getLabel();
        assert(expectedLabel.equals(actualLabel));
    }

    @Test
    public void testSetLabelLongString() {
        PlotAxis axis = new PlotAxis();
        String newLabel = "This is a very long string that tests the limit of the label length.";
        axis.setLabel(newLabel);
        String expectedLabel = "This is a very long string that tests the limit of the label length.";
        String actualLabel = axis.getLabel();
        assert(expectedLabel.equals(actualLabel));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGridStyleNegative() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGridStyleNull() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetGridStyleUnsupportedStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(10);
    }
}