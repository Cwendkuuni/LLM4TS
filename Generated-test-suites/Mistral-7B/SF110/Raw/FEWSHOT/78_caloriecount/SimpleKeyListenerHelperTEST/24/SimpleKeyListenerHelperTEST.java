package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener mockSimpleKeyListener;

 @Before
 public void setUp() {
 component = Mockito.mock(Component.class);
 mockSimpleKeyListener = Mockito.mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertEquals(component, simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testAddKeyListener() {
 assertTrue(component.addKeyListener(simpleKeyListenerHelper));
 assertEquals(simpleKeyListenerHelper, component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)));
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 verify(component, times(1)).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListenerEnter() {
 when(component.isFocused()).thenReturn(true);
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 10, null);
 verify(mockSimpleKeyListener, times(1)).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 when(component.isFocused()).thenReturn(true);
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 155, null);
 verify(mockSimpleKeyListener, times(1)).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 when(component.isFocused()).thenReturn(true);
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 127, null);
 verify(mockSimpleKeyListener, times(1)).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 when(component.isFocused()).thenReturn(true);
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, 9, null);
 verify(mockSimpleKeyListener, times(1)).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
 verify(mockSimpleKeyListener, times(1)).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ""));
 verify(mockSimpleKeyListener, times(1)).tab(component);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ""));
 verify(mockSimpleKeyListener, times(1)).delete(component);
 }

 @Test
 public void testKeyPressedInsert() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ""));
 verify(mockSimpleKeyListener, times(1)).insert(component);
 }

 @Test
 public void testKeyReleased() {
 simpleKeyListenerHelper.keyReleased(new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
 }

 @Test
 public void testKeyTyped() {
 simpleKeyListenerHelper.keyTyped(new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
 }
}