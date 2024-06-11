package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ComboLegTest {
    @Test
    public void testEmptyConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
    }
}