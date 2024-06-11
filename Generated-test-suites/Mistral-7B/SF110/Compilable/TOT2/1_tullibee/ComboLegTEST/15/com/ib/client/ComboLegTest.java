package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "ICE_MARKETS", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "BUY", "ICE_MARKETS", 1);
        ComboLeg comboLeg3 = new ComboLeg(1, 1, "SELL", "ICE_MARKETS", 1);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(null));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", "ICE_MARKETS", 1);
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "BUY", "ICE_MARKETS", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "SELL", "ICE_MARKETS", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", "ICE_MARKETS", 1);
        assertTrue(comboLeg.equals(comboLeg));
    }
}