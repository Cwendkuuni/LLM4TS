import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.progra.charting.model.*;

import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CoordSystemUtilitiesTest {

    @Mock
    private CoordSystem coordSystem;

    @Mock
    private ChartDataModel chartDataModel;

    @Mock
    private ChartDataModelConstraints constraints;

    @Mock
    private ChartDataModelConstraints constraints2;

    @InjectMocks
    private CoordSystemUtilities coordSystemUtilities;

    @Test
    public void testComputeLeftMargin() {
        when(constraints.getMinimumColumnValue()).thenReturn(-10.0);
        when(constraints.getMaximumColumnValue()).thenReturn(10.0);
        when(constraints.getMinimumValue()).thenReturn(-5.0);
        when(constraints.getMaximumValue()).thenReturn(5.0);
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);
        when(coordSystem.getYAxisUnit()).thenReturn("Unit Y");

        double expectedLeftMargin = 26;

        assertEquals(expectedLeftMargin, coordSystemUtilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);

        double expectedRightMargin = 15;

        assertEquals(expectedRightMargin, coordSystemUtilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        when(constraints.getMinimumValue()).thenReturn(-5.0);
        when(constraints.getMaximumValue()).thenReturn(5.0);
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);

        double expectedTopMargin = 22;

        assertEquals(expectedTopMargin, coordSystemUtilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        when(constraints.getMinimumValue()).thenReturn(-5.0);
        when(constraints.getMaximumValue()).thenReturn(5.0);
        when(constraints2.getMinimumColumnValue()).thenReturn(10.0);
        when(constraints2.getMaximumColumnValue()).thenReturn(15.0);
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);

        double expectedBottomMargin = 54;

        assertEquals(expectedBottomMargin, coordSystemUtilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        when(constraints.getMinimumColumnValue()).thenReturn(-5.0);
        when(constraints.getMaximumColumnValue()).thenReturn(5.0);
        when(constraints.getMinimumValue()).thenReturn(-1.0);
        when(constraints.getMaximumValue()).thenReturn(1.0);
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);

        int expectedLabelHeight = 17;

        assertEquals(expectedLabelHeight, coordSystemUtilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        when(constraints.getMinimumValue()).thenReturn(-5.0);
        when(constraints.getMaximumValue()).thenReturn(5.0);
        when(coordSystem.getFont()).thenReturn(Mockito.mock(Font.class));
        when(coordSystem.getFontRenderContext()).thenReturn(Mockito.mock(GraphicsEnvironment.class).getFontRenderContexts()[0]);

        int expectedLabelWidth = 48;

        assertEquals(expectedLabelWidth, coordSystemUtilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        given(coordSystem.isPaintLabels()).willReturn(true);
        given(coordSystem.getTransform(0)).willReturn(Mockito.mock(AffineTransform.class));
        given(chartDataModel.getColumnCount()).willReturn(10);

        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        coordSystemUtilities.drawNumericalXAxisTicks(graphics2D);

        verify(graphics2D, times(10)).draw(Mockito.any(Line2D.class));
    }

    @Test
    public void testDrawXAxisTicks() {
        given(coordSystem.isPaintLabels()).willReturn(true);
        given(coordSystem.getTransform(0)).willReturn(Mockito.mock(AffineTransform.class));

        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        coordSystemUtilities.drawXAxisTicks(graphics2D);

        verify(graphics2D, times(10)).draw(Mockito.any(Line2D.class));
    }

    @Test
    public void testDrawYAxisTicks() {
        given(coordSystem.isPaintLabels()).willReturn(true);
        given(coordSystem.getTransform(0)).willReturn(Mockito.mock(AffineTransform.class));
        given(coordSystem.getXDecimalFormat()).willReturn(Mockito.mock(DecimalFormat.class));

        Graphics2D graphics2D = Mockito.mock(Graphics2D.class);
        coordSystemUtilities.drawYAxisTicks(graphics2D);

        verify(graphics2D, times(10)).draw(Mockito.any(Line2D.class));
    }
}