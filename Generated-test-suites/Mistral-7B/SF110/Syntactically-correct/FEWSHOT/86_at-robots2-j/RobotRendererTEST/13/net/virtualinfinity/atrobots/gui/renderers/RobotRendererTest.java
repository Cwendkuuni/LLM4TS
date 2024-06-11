package net.virtualinfinity.atrobots.gui.renderers;

import static net.virtualinfinity.atrobots.measures.Length.millimeters;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import org.junit.Before;
import org.junit.Test;

public class RobotRendererTest {
    private static final double DELTA = 0.001;

    private RobotRenderer renderer;
    private Graphics2D graphics2DMock;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        this.renderer = new RobotRenderer();
        this.graphics2DMock = mock(Graphics2D.class);
        this.robotSnapshot = new RobotSnapshot() {
            @Override
            public double getArmor() {
                return 100.0;
            }

            @Override
            public double getHeading() {
                return new AbsoluteAngle(Math.PI / 2.0);
            }

            @Override
            public double getPositionVector() {
                return new PositionVector(100.0, 100.0);
            }

            @Override
            public double getTemperature() {
                return new Temperature(50.0, Length.millimeters);
            }

            @Override
            public double getTurretHeading() {
                return new AbsoluteAngle(Math.PI / 4.0);
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
            public int getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "TestBot";
            }

            @Override
            public boolean isActiveShield() {
                return true;
            }

            @Override
            public boolean isDead() {
                return false;
            }
        };
        this.selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithDeadRobot() {
        when(this.graphics2DMock.getFontMetrics()).thenReturn(new FontMetrics() {
            @Override
            public Rectangle2D getStringBounds(final String text, final Font font) {
                return new Rectangle2D.Double(0.0, 0.0, 50.0, 10.0);
            }
        });
        this.renderer.render(this.graphics2DMock, this.robotSnapshot, this.selectedRobotIds);

        verifyNoMoreInteractions(this.graphics2DMock);
    }

    @Test
    public void testRenderWithNonDeadRobot() {
        this.renderer.render(this.graphics2DMock, this.robotSnapshot, this.selectedRobotIds);

        verify(this.graphics2DMock, times(7)).fill(any(Shape.class));
        verify(this.graphics2DMock, times(7)).draw(any(Shape.class));
    }

    @Test
    public void testRenderWithSelectedRobot() {
        this.selectedRobotIds.add(this.robotSnapshot.getId());
        this.renderer.render(this.graphics2DMock, this.robotSnapshot, this.selectedRobotIds);

        verify(this.graphics2DMock).fill(argThat(shape -> shape instanceof Ellipse2D && shape.getBounds2D().contains(this.robotSnapshot.getX(), this.robotSnapshot.getY())));
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(this.renderer.isRenderDead());
        this.renderer.setRenderDead(false);
        assertFalse(this.renderer.isRenderDead());
    }

    @Test
    public void testSetRenderDead() {
        this.renderer.setRenderDead(false);
        assertTrue(this.renderer.isRenderDead() == false);
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(this.renderer.isShowStatusBars());
        this.renderer.setShowStatusBars(false);
        assertFalse(this.renderer.isShowStatusBars());
    }

    @Test
    public void testSetShowStatusBars() {
        this.renderer.setShowStatusBars(false);
        assertTrue(this.renderer.isShowStatusBars() == false);
    }

    @Test
    public void testIsShowName() {
        assertTrue(this.renderer.isShowName());
        this.renderer.setShowName(false);
        assertFalse(this.renderer.isShowName());
    }

    @Test
    public void testSetShowName() {
        this.renderer.setShowName(false);
        assertTrue(this.renderer.isShowName() == false);
    }

    @Test
    public void testIsFillShield() {
        assertTrue(this.renderer.isFillShield());
        this.renderer.setFillShield(false);
        assertFalse(this.renderer.isFillShield());
    }

    @Test
    public void testSetFillShield() {
        this.renderer.setFillShield(false);
        assertTrue(this.renderer.isFillShield() == false);
    }
}