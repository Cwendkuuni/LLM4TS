import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RobotRendererTest {

    private Graphics2D g2d;
    private RobotSnapshot robotSnapshot;
    private RobotRenderer renderer;

    @Before
    public void setUp() {
        g2d = Mockito.mock(Graphics2D.class);
        robotSnapshot = Mockito.mock(RobotSnapshot.class);
        renderer = new RobotRenderer();
    }

    @Test
    public void testRenderWithDeadRobot() {
        Mockito.when(robotSnapshot.isDead()).thenReturn(true);
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Mockito.verify(g2d, Mockito.never()).fill(Mockito.any(Shape.class));
    }

    @Test
    public void testRenderWithoutShowStatusBars() {
        renderer.setShowStatusBars(false);
        Mockito.when(robotSnapshot.isDead()).thenReturn(false);
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Mockito.verify(g2d, Mockito.never()).paintArmor(g2d, robotSnapshot);
        Mockito.verify(g2d, Mockito.never()).paintHeat(g2d, robotSnapshot);
    }

    @Test
    public void testRenderWithShowStatusBars() {
        renderer.setShowStatusBars(true);
        Mockito.when(robotSnapshot.isDead()).thenReturn(false);
        renderer.render(g2d, robotSnapshot, new HashSet<>());
        Mockito.verify(g2d, Mockito.atLeastOnce()).paintArmor(g2d, robotSnapshot);
        Mockito.verify(g2d, Mockito.atLeastOnce()).paintHeat(g2d, robotSnapshot);
    }

    @Test
    public void testRenderWithSelectedRobot() {
        Mockito.when(robotSnapshot.isDead()).thenReturn(false);
        Set<Integer> selectedRobotIds = new HashSet<>(Arrays.asList(robotSnapshot.getId()));
        renderer.render(g2d, robotSnapshot, selectedRobotIds);
        Mockito.verify(g2d, Mockito.atLeastOnce()).paintSelection(g2d, robotSnapshot);
    }

    @Test
    public void testIsRenderDead() {
        assertTrue(renderer.isRenderDead());
        renderer.setRenderDead(false);
        assertFalse(renderer.isRenderDead());
    }

    @Test
    public void testIsShowStatusBars() {
        assertTrue(renderer.isShowStatusBars());
        renderer.setShowStatusBars(false);
        assertFalse(renderer.isShowStatusBars());
    }

    @Test
    public void testIsShowName() {
        assertTrue(renderer.isShowName());
        renderer.setShowName(false);
        assertFalse(renderer.isShowName());
    }

    @Test
    public void testIsFillShield() {
        assertTrue(renderer.isFillShield());
        renderer.setFillShield(false);
        assertFalse(renderer.isFillShield());
    }
}