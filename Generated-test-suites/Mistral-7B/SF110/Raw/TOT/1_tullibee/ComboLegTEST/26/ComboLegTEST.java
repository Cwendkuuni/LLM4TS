package com.ib.client;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ComboLegTest {

    @Test
    public void testEmptyConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertNull(comboLeg.m_conId);
        Assert.assertNull(comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertNull(comboLeg.m_openClose);
        Assert.assertNull(comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithConId() {
        ComboLeg comboLeg = new ComboLeg(123);
        Assert.assertEquals(123, comboLeg.m_conId);
        Assert.assertNull(comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertNull(comboLeg.m_openClose);
        Assert.assertNull(comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithAllArguments() {
        String exchange = "IBKR";
        String action = "BUY";
        int shortSaleSlot = 1;
        String designatedLocation = "NY";

        ComboLeg comboLeg = new ComboLeg(1, 2, action, exchange, 1, shortSaleSlot, designatedLocation);

        Assert.assertEquals(1, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals(action, comboLeg.m_action);
        Assert.assertEquals(exchange, comboLeg.m_exchange);
        Assert.assertEquals(1, comboLeg.m_openClose);
        Assert.assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        Assert.assertEquals(designatedLocation, comboLeg.m_designatedLocation);

        verify(mock(Util.class), times(2)).StringCompareIgnCase(anyString(), anyString());
    }

}