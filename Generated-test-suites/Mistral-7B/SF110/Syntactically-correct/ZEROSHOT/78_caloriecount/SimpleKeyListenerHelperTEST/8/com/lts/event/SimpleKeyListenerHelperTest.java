package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener mockSimpleKeyListener;

 @Before
 public void setup() {
 component = Mockito.mock(Component.class);
 mockSimpleKeyListener = Mockito.mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 assertEquals(component, simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyPressedEnter() {
 KeyEvent keyEvent = new KeyEvent(component, 10, true, KeyEvent.CHAR_UNDEFINED, 0, "Enter");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 Mockito.verify(mockSimpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 KeyEvent keyEvent = new KeyEvent(component, 9, true, KeyEvent.CHAR_UNDEFINED, 0, "Tab");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 Mockito.verify(mockSimpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 KeyEvent keyEvent = new KeyEvent(component, 127, true, KeyEvent.CHAR_UNDEFINED, 0, "Delete");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 Mockito.verify(mockSimpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressedInsert() {
 KeyEvent keyEvent = new KeyEvent(component, 155, true, KeyEvent.CHAR_UNDEFINED, 0, "Insert");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 Mockito.verify(mockSimpleKeyListener).insert(component);
 }

 @Test
 public void testKeyPressedOtherKeycodes() {
 KeyEvent keyEvent = new KeyEvent(component, 1, true, KeyEvent.CHAR_UNDEFINED, 0, "OtherKey");
 simpleKeyListenerHelper.keyPressed(keyEvent);
 Mockito.verifyNoMoreInteractions(mockSimpleKeyListener);
 }

 @Test
 public void testFireEvent() {
 simpleKeyListenerHelper.fire(10);
 Mockito.verify(mockSimpleKeyListener).enter(component);
 }

 @Test
 public void testFireEventNullComponent() {
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper.fire(10);
 Mockito.verifyNoInteractions(mockSimpleKeyListener);
 }
}