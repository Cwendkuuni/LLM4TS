package com.ib.client;
import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.m_conId);
        Assert.assertEquals(0, comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertEquals(0, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }
}