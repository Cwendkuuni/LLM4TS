package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener mockSimpleKeyListener;
 private Component mockComponent;

 @Before
 public void setUp() {
 mockSimpleKeyListener = mock(SimpleKeyListener.class);
 mockComponent = mock(Component.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 }

 @After
 public void tearDown() {
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testAddKeyListener() {
 verify(mockComponent).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 verify(mockComponent).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListenerEnterKeyCode() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
 verify(mockSimpleKeyListener).enter(mockComponent);
 }

 @Test
 public void testNotifyListenerInsertKeyCode() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
 verify(mockSimpleKeyListener).insert(mockComponent);
 }

 @Test
 public void testNotifyListenerDeleteKeyCode() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
 verify(mockSimpleKeyListener).delete(mockComponent);
 }

 @Test
 public void testNotifyListenerTabKeyCode() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 verify(mockSimpleKeyListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedEnterKeyCode() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 0, 0, KeyEvent.VK_ENTER));
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
 verify(mockSimpleKeyListener).enter(mockComponent);
 }

 @Test
 public void testKeyPressedInsertKeyCode() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 0, 0, KeyEvent.VK_INSERT));
 simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
 verify(mockSimpleKeyListener).insert(mockComponent);
 }

 @Test
 public void testKeyPressedDeleteKeyCode() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 0, 0, KeyEvent.VK_DELETE));
 simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
 verify(mockSimpleKeyListener).delete(mockComponent);
 }

 @Test
 public void testKeyPressedTabKeyCode() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 0, 0, KeyEvent.VK_TAB));
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 verify(mockSimpleKeyListener).tab(mockComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFireWithNullKeyEvent() {
 simpleKeyListenerHelper.fire(null);
 }
}