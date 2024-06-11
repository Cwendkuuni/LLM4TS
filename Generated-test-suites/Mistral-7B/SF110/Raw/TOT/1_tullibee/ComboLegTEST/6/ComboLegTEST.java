import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ib.client.ComboLeg;

public class ComboLegTest {
    
    @Test
    public void testConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NYSE");
        assertEquals(1, comboLeg.getConId());
        assertEquals(2, comboLeg.getRatio());
        assertEquals("BUY", comboLeg.getAction());
        assertEquals("IBKR", comboLeg.getExchange());
        assertEquals(0, comboLeg.getOpenClose());
        assertEquals(1, comboLeg.getShortSaleSlot());
        assertEquals("NYSE", comboLeg.getDesignatedLocation());
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NYSE");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NYSE");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 2, "NYSE");
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 0, 1, "NYSE");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertNotEquals(comboLeg1, comboLeg3);
        assertNotEquals(comboLeg1, comboLeg4);
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NYSE");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NYSE");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 2, "NYSE");
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 0, 1, "NYSE");

        assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
        assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
        assertNotEquals(comboLeg1.hashCode(), comboLeg4.hashCode());
    }

    @Test
    public void testNull() {
        assertNull(new ComboLeg(0, 0, null, null, 0, 0, null));
    }

    @Test
    public void testEmptyString() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "", "IBKR", 0, 1, "");
        assertEquals(1, comboLeg.getConId());
        assertEquals(2, comboLeg.getRatio());
        assertNull(comboLeg.getAction());
        assertEquals("IBKR", comboLeg.getExchange());
        assertEquals(0, comboLeg.getOpenClose());
        assertEquals(1, comboLeg.getShortSaleSlot());
        assertNull(comboLeg.getDesignatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRatio() {
        new ComboLeg(1, -1, "BUY", "IBKR", 0, 1, "NYSE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullExchange() {
        new ComboLeg(1, 2, "BUY", null, 0, 1, "NYSE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDesignatedLocation() {
        new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, null);
    }
}