package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component mockComponent;
 private SimpleKeyListener mockSimpleKeyListener;

 @Before
 public void setUp() {
 mockComponent = mock(Component.class);
 mockSimpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperCreation() {
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testComponentAdded() {
 verify(mockComponent).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testEnterKeyPressed() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(true);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_ENTER, 0, 0));
 verify(mockSimpleKeyListener).enter(mockComponent);
 }

 @Test(expected = IllegalStateException.class)
 public void testEnterKeyPressedOnUnfocusedComponent() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(false);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_ENTER, 0, 0));
 }

 @Test
 public void testInsertKeyPressed() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(true);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_INSERT, 0, 0));
 verify(mockSimpleKeyListener).insert(mockComponent);
 }

 @Test
 public void testDeleteKeyPressed() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(true);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_DELETE, 0, 0));
 verify(mockSimpleKeyListener).delete(mockComponent);
 }

 @Test
 public void testTabKeyPressed() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(true);
 when(mockComponent.isFocusTraversable()).thenReturn(true);
 when(mockComponent.isFocusableWindowState(FocusConstants.FOCUS_FORWARD)).thenReturn(true);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_Tab, 0, 0));
 verify(mockSimpleKeyListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedOtherKeys() {
 when(mockComponent.isFocusable()).thenReturn(true);
 when(mockComponent.isFocusOwner()).thenReturn(true);

 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 0, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_A, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_B, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_C, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_D, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_F, 0, 0));
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_CODE_SPACE, 0, 0));

 for (int i = 0; i < SimpleKeyListenerHelper.KEY_CODES.length; i++) {
 int keyCode = SimpleKeyListenerHelper.KEY_CODES[i];
 int expectedType = getExpectedType(keyCode);
 verifyZeroInteractions(mockSimpleKeyListener);
 verifyNoMoreInteractions(simpleKeyListenerHelper);
 assertFalse(simpleKeyListenerHelper.isKeyPressed(keyCode));
 }
 }

 @Test
 public void testFireKeyEvent() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(mockSimpleKeyListener).enter(mockComponent);
 }

 private int getExpectedType(int keyCode) {
 switch (keyCode) {
 case KeyEvent.KEY_CODE_ENTER:
 return 10;
 case KeyEvent.KEY_CODE_DELETE:
 return 127;
 case KeyEvent.KEY_CODE_INSERT:
 return 155;
 case KeyEvent.KEY_CODE_TAB:
 return 9;
 default:
 return -1;
 }
}
}