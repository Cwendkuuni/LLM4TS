package com.ib.client;
import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose, int p_shortSaleSlot, String p_designatedLocation) {
 return new ComboLeg(p_conId, p_ratio, p_action, p_exchange, p_openClose, p_shortSaleSlot, p_designatedLocation);
 }

 @Test
 public void testConstructorWithDefaultValues() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.m_conId);
 Assert.assertEquals(0, comboLeg.m_ratio);
 Assert.assertNull(comboLeg.m_action);
 Assert.assertNull(comboLeg.m_exchange);
 Assert.assertEquals(0, comboLeg.m_openClose);
 Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
 Assert.assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testConstructorWithValues() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 3;
 String designatedLocation = "New York";

 ComboLeg comboLeg = createComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

 Assert.assertEquals(conId, comboLeg.m_conId);
 Assert.assertEquals(ratio, comboLeg.m_ratio);
 Assert.assertEquals(action, comboLeg.m_action);
 Assert.assertEquals(exchange, comboLeg.m_exchange);
 Assert.assertEquals(openClose, comboLeg.m_openClose);
 Assert.assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
 Assert.assertEquals(designatedLocation, comboLeg.m_designatedLocation);
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = createComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "New York");
 ComboLeg comboLeg2 = createComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "New York");
 ComboLeg comboLeg3 = createComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "London");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullExchange() {
 ComboLeg comboLeg = new ComboLeg(1, 2, null, null, 0, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeRatio() {
 ComboLeg comboLeg = new ComboLeg(1, -2, "BUY", "IBKR", 0, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidOpenCloseValue() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 4, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullDesignatedLocation() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 0, 0, null);
 }

}