package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener mockSimpleKeyListener;
 private Component mockComponent;

 @Before
 public void setup() {
 mockSimpleKeyListener = Mockito.mock(SimpleKeyListener.class);
 mockComponent = Mockito.mock(Component.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 }

 @Test
 public void testConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertEquals(mockComponent, simpleKeyListenerHelper.component);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 verify(mockComponent, times(1)).removeKeyListener(simpleKeyListenerHelper);
 assertEquals(null, simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListener() {
 when(mockComponent).getClass().getName().equals("javax.swing.JTextField") // JTextField is an example of Component
 .thenReturn(true);

 when(mockComponent).isEditable().thenReturn(true);

 when(mockSimpleKeyListener.enter(mockComponent)).thenReturn(true);
 when(mockSimpleKeyListener.insert(mockComponent)).thenReturn(true);
 when(mockSimpleKeyListener.delete(mockComponent)).thenReturn(true);
 when(mockSimpleKeyComponent.getFocusTraversalKeys(FocusTraversalPolicy.FORWARD_TRAVERSAL).length > 0).thenReturn(true);

 simpleKeyListenerHelper.attach(); // Need to attach to be able to test notifyListener

 KeyEvent keyEventEnter = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "Enter");
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, KeyEvent.KEY_PRESSED, keyEventEnter);
 verify(mockSimpleKeyListener, times(1)).enter(mockComponent);

 KeyEvent keyEventTab = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, "Tab");
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, KeyEvent.KEY_PRESSED, keyEventTab);
 verify(mockSimpleKeyListener, times(1)).tab(mockComponent);

 KeyEvent keyEventDelete = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, "Delete");
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, KeyEvent.KEY_PRESSED, keyEventDelete);
 verify(mockSimpleKeyListener, times(1)).delete(mockComponent);

 KeyEvent keyEventBackspace = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, "Backspace");
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, KeyEvent.KEY_PRESSED, keyEventBackspace);
 verify(mockSimpleKeyListener, times(1)).delete(mockComponent);

 KeyEvent keyEventInsert = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, "Insert");
 simpleKeyListenerHelper.notifyListener(mockSimpleKeyListener, KeyEvent.KEY_PRESSED, keyEventInsert);
 verify(mockSimpleKeyListener, times(1)).insert(mockComponent);
 }

 @Test
 public void testKeyPressed() {
 when(mockComponent).getClass().getName().equals("javax.swing.JTextField")
 .thenReturn(true);

 when(mockComponent).isEditable().thenReturn(true);

 when(mockSimpleKeyListener.enter(mockComponent)).thenReturn(true);
 when(mockSimpleKeyListener.insert(mockComponent)).thenReturn(true);
 when(mockSimpleKeyListener.delete(mockComponent)).thenReturn(true);
 when(mockSimpleKeyListener.tab(mockComponent)).thenReturn(true);

 simpleKeyListenerHelper.attach();

 KeyEvent keyEventEnter = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "Enter");
 simpleKeyListenerHelper.keyPressed(keyEventEnter);
 verify(mockSimpleKeyListener, times(1)).enter(mockComponent);

 KeyEvent keyEventTab = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, "Tab");
 simpleKeyListenerHelper.keyPressed(keyEventTab);
 verify(mockSimpleKeyListener, times(1)).tab(mockComponent);

 KeyEvent keyEventDelete = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, "Delete");
 simpleKeyListenerHelper.keyPressed(keyEventDelete);
 verify(mockSimpleKeyListener, times(1)).delete(mockComponent);

 KeyEvent keyEventInsert = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, "Insert");
 simpleKeyListenerHelper.keyPressed(keyEventInsert);
 verify(mockSimpleKeyListener, times(1)).insert(mockComponent);

 KeyEvent keyEventBackspace = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, "Backspace");
 simpleKeyListenerHelper.keyPressed(keyEventBackspace);
 verify(mockSimpleKeyListener, times(1)).delete(mockComponent);
 }

 @Test
 public void testKeyReleased() {
 // Not testing keyReleased method, as it is empty in this class
 }

 @Test
 public void testKeyTyped() {
 // Not testing keyTyped method, as it is empty in this class
 }
}