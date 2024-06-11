package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ComboLegTest {
    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
    }
}