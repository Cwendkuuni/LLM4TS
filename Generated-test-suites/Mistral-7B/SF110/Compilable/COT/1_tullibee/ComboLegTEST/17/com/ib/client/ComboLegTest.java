package com.ib.client;
import org.junit.Assert;import org.junit.Test;import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testConstructor0() {
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
 public void testConstructor1() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
 Assert.assertEquals(1, comboLeg.m_conId);
 Assert.assertEquals(2, comboLeg.m_ratio);
 Assert.assertEquals("action1", comboLeg.m_action);
 Assert.assertEquals("exchange1", comboLeg.m_exchange);
 Assert.assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
 Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
 Assert.assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testConstructor2() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");
 Assert.assertEquals(1, comboLeg.m_conId);
 Assert.assertEquals(2, comboLeg.m_ratio);
 Assert.assertEquals("action1", comboLeg.m_action);
 Assert.assertEquals("exchange1", comboLeg.m_exchange);
 Assert.assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
 Assert.assertEquals(1, comboLeg.m_shortSaleSlot);
 Assert.assertEquals("designatedLocation1", comboLeg.m_designatedLocation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor3InvalidRatio() {
 new ComboLeg(-1, 2, "action1", "exchange1", ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor4NullAction() {
 new ComboLeg(1, 2, null, "exchange1", ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor5NullExchange() {
 new ComboLeg(1, 2, "action1", null, ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor6NegativeOpenClose() {
 new ComboLeg(1, 2, "action1", "exchange1", -1, 0, "designatedLocation1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor7NegativeShortSaleSlot() {
 new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, -1, "designatedLocation1");
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "action2", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

}