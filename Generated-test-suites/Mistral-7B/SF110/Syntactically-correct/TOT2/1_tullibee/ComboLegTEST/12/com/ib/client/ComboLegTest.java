import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(null, comboLeg.m_action);
        assertEquals(null, comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 3);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertEquals(null, comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", 3, 4, "DesignatedLocation1");

        assertEquals(comboLeg1, comboLeg2);
        assertNotEquals(comboLeg1, comboLeg3);
    }
}