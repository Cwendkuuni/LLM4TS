package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RobotRendererTest {

    private static final double DELTA = 0.001;
    private static final double EPSILON = 1e-6;
    private static final Color HEAT_COLOR1 = new Color(1.0f, 0.0f, 0.0f, 0.1f);
    private static final Color HEAT_COLOR2 = Color.yellow;
    private static final Point2D.Double POINT_ZERO = new Point2D.Double(0.0, 0.0);
    private RobotRenderer robotRenderer;
    private Graphics2D graphics2D;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        robotRenderer = new RobotRenderer();
        graphics2D = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public double getX() {
                return 100.0;
            }

            @Override
            public double getY() {
                return 100.0;
            }

            @Override
            public double getHeading() {
                return new Angle(Math.PI / 2.0);
            }

            @Override
            public double getTurretHeading() {
                return new Angle(Math.PI / 4.0);
            }

            @Override
            public double getArmor() {
                return 50.0;
            }

            @Override
            public double getTemperature() {
                return new Temperature(50.0);
            }

            @Override
            public boolean isDead() {
                return false;
            }

            @Override
            public boolean isActiveShield() {
                return true;
            }

            @Override
            public String getName() {
                return "Robot 1";
            }

            @Override
            public Vector positionVector() {
                return new Vector(new Point2D.Double(100.0, 100.0));
            }
        };
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithDefaultSettings() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        verify(graphics2D, times(1)).setPaint(eq(HEAT_COLOR1));
        verify(graphics2D, times(1)).fill(any(Shape.class));
        verify(graphics2D, times(1)).setPaint(Color.RED);
        verify(graphics2D, times(1)).fill(any(Path2D.class));
        verify(graphics2D, times(1)).setStroke(any(Stroke.class));
        verify(graphics2D, times(1)).draw(any(Line2D.class));
        verify(graphics2D, times(1)).setPaint(Color.YELLOW);
        verify(graphics2D, times(1)).drawString(eq("Robot 1"), argThat(isCloseTo(new Point2D.Double(95.0, 96.0), DELTA)), argThat(isCloseTo(new Point2D.Double(95.0, 95.0), DELTA)));
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testRenderWithDisabledRenderDeadOption() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.setRenderDead(false);
        robotRenderer.render(graphics2D, new RobotSnapshot() {
            @Override
            public int getId() {
                return 2;
            }

            @Override
            public double getX() {
                return 150.0;
            }

            @Override
            public double getY() {
                return 150.0;
            }

            @Override
            public double getHeading() {
                return new Angle(Math.PI / 2.0);
            }

            @Override
            public double getTurretHeading() {
                return new Angle(Math.PI / 4.0);
            }

            @Override
            public double getArmor() {
                return 50.0;
            }

            @Override
            public double getTemperature() {
                return new Temperature(50.0);
            }

            @Override
            public boolean isDead() {
                return true;
            }

            @Override
            public boolean isActiveShield() {
                return false;
            }

            @Override
            public String getName() {
                return "Robot 2";
            }

            @Override
            public Vector positionVector() {
                return new Vector(new Point2D.Double(150.0, 150.0));
            }
        }, selectedRobotIds);
        verifyZeroInteractions(graphics2D);
    }

    @Test
    public void testRenderWithDisabledShowStatusBarsOption() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.setShowStatusBars(false);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        verifyZeroInteractions(graphics2D);
    }

    @Test
    public void testRenderWithDisabledShowNameOption() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.setShowName(false);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        verifyZeroInteractions(graphics2D);
    }

    @Test
    public void testRenderWithDisabledFillShieldOption() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.setFillShield(false);
        robotRenderer.render(graphics2D, robotSnapshot, selectedRobotIds);
        verify(graphics2D, times(1)).setPaint(eq(Color.LIGHT_GRAY));
        verify(graphics2D, times(1)).draw(any(Shape.class));
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testRenderWithRobotSelected() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>(Arrays.asList(1)));
        verify(graphics2D, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2D, times(1)).fill(any(Shape.class));
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testRenderWithMultipleRobotsSelected() {
        when(graphics2D.getColor()).thenReturn(Color.BLACK);
        robotRenderer.render(graphics2D, robotSnapshot, new HashSet<>(Arrays.asList(1, 2)));
        verify(graphics2D, times(1)).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(graphics2D, times(2)).fill(any(Shape.class));
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testIsRenderDeadReturnsCorrectValue() {
        assertTrue(robotRenderer.isRenderDead());
        robotRenderer.setRenderDead(false);
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBarsReturnsCorrectValue() {
        assertTrue(robotRenderer.isShowStatusBars());
        robotRenderer.setShowStatusBars(false);
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void testIsShowNameReturnsCorrectValue() {
        assertTrue(robotRenderer.isShowName());
        robotRenderer.setShowName(false);
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void testIsFillShieldReturnsCorrectValue() {
        assertTrue(robotRenderer.isFillShield());
        robotRenderer.setFillShield(false);
        assertFalse(robotRenderer.isFillShield());
    }
}