package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg defaultInstance = new ComboLeg();
        ComboLeg instance = new ComboLeg();
        assertEquals(defaultInstance, instance);
    }

    @Test
    public void testComboLegConstructorIntIntStringStringIntIntIntString() {
        int conId = 1;
        int ratio = 2;
        String action = "ACTION1";
        String exchange = "EXCHANGE1";
        int openClose = 3;
        int shortSaleSlot = 4;
        String designatedLocation = "LOCATION1";

        ComboLeg instance = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, instance.m_conId);
        assertEquals(ratio, instance.m_ratio);
        assertEquals(action, instance.m_action);
        assertEquals(exchange, instance.m_exchange);
        assertEquals(openClose, instance.m_openClose);
        assertEquals(shortSaleSlot, instance.m_shortSaleSlot);
        assertEquals(designatedLocation, instance.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorIntIntStringStringIntIntIntNull() {
        int conId = 1;
        int ratio = 2;
        String action = "ACTION1";
        String exchange = "EXCHANGE1";
        int openClose = 3;
        int shortSaleSlot = 4;

        ComboLeg instance = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, null);

        assertNull(instance.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorIntIntStringStringIntIntIntStringWithSameValues() {
        int conId = 1;
        int ratio = 2;
        String action = "ACTION1";
        String exchange = "EXCHANGE1";
        int openClose = 3;
        int shortSaleSlot = 4;
        String designatedLocation = "LOCATION1";

        ComboLeg firstInstance = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        ComboLeg secondInstance = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testComboLegConstructorWithDifferentValues() {
        int conId1 = 1;
        int ratio1 = 2;
        String action1 = "ACTION1";
        String exchange1 = "EXCHANGE1";
        int openClose1 = 3;
        int shortSaleSlot1 = 4;
        String designatedLocation1 = "LOCATION1";

        int conId2 = 2;
        int ratio2 = 3;
        String action2 = "ACTION2";
        String exchange2 = "EXCHANGE2";
        int openClose2 = 4;
        int shortSaleSlot2 = 5;
        String designatedLocation2 = "LOCATION2";

        ComboLeg firstInstance = new ComboLeg(conId1, ratio1, action1, exchange1, openClose1, shortSaleSlot1, designatedLocation1);
        ComboLeg secondInstance = new ComboLeg(conId2, ratio2, action2, exchange2, openClose2, shortSaleSlot2, designatedLocation2);

        assertFalse(firstInstance.equals(secondInstance));
    }

    @Test
    public void testComboLegConstructorWithSameValuesButDifferentObjects() {
        int conId1 = 1;
        int ratio1 = 2;
        String action1 = new String("ACTION1");
        String exchange1 = new String("EXCHANGE1");
        int openClose1 = 3;
        int shortSaleSlot1 = 4;
        String designatedLocation1 = new String("LOCATION1");

        int conId2 = 1;
        int ratio2 = 2;
        String action2 = new String("ACTION1");
        String exchange2 = new String("EXCHANGE1");
        int openClose2 = 3;
        int shortSaleSlot2 = 4;
        String designatedLocation2 = new String("LOCATION1");

        ComboLeg firstInstance = new ComboLeg(conId1, ratio1, action1, exchange1, openClose1, shortSaleSlot1, designatedLocation1);
        ComboLeg secondInstance = new ComboLeg(conId2, ratio2, action2, exchange2, openClose2, shortSaleSlot2, designatedLocation2);

        assertFalse(firstInstance.equals(secondInstance));
    }

    @Test
    public void testComboLegStringCompareIgnCase() {
        String action1 = "Action";
        String action2 = "action";

        int comparisonResult = Util.StringCompareIgnCase(action1, action2);

        assertEquals(0, comparisonResult);
    }

    @Test
    public void testComboLegStringIsNull() {
        String action = null;

        String otherAction = new String("ACTION");

        int comparisonResult = Util.StringCompareIgnCase(action, otherAction);

        assertNotEquals(0, comparisonResult);
    }
}