import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.ib.client.ComboLeg;

public class ComboLegTest {

    ComboLeg comboLeg1;
    ComboLeg comboLeg2;

    @Before
    public void setUp() {
        comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", 0);
        comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", 0);
    }

    @Test
    public void testComboLegConstructor() {
        // Test default constructor
        ComboLeg comboLegDefault = new ComboLeg();
        assertNull(comboLegDefault.m_conId);
        assertNull(comboLegDefault.m_ratio);
        assertNull(comboLegDefault.m_action);
        assertNull(comboLegDefault.m_exchange);
        assertNull(comboLegDefault.m_openClose);
        assertNull(comboLegDefault.m_shortSaleSlot);
        assertNull(comboLegDefault.m_designatedLocation);

        // Test constructor with given parameters
        assertEquals(1, comboLeg1.m_conId);
        assertEquals(2, comboLeg1.m_ratio);
        assertEquals("BUY", comboLeg1.m_action);
        assertEquals("SMART", comboLeg1.m_exchange);
        assertEquals(0, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertEquals(null, comboLeg1.m_designatedLocation);
    }

    @Test
    public void testEqualsMethod() {
        // Test when two objects are equal
        assertTrue(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different conId)
        comboLeg2.m_conId = 2;
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different ratio)
        comboLeg2.m_ratio = 3;
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different action)
        comboLeg2.m_action = "SELL";
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different exchange)
        comboLeg2.m_exchange = "NEW_EXCHANGE";
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different openClose)
        comboLeg2.m_openClose = 1;
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different shortSaleSlot)
        comboLeg2.m_shortSaleSlot = 1;
        assertFalse(comboLeg1.equals(comboLeg2));

        // Test when two objects are not equal (different designatedLocation)
        comboLeg2.m_designatedLocation = "NEW_DESIGNATED_LOCATION";
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}