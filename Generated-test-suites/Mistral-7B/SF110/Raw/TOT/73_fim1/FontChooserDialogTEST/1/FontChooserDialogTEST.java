package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component parentComponent;
 private JFontChooser chooserPaneMock;
 private ActionListener okListenerMock, cancelListenerMock;

 @Before
 public void setUp() {
 frame = Mockito.spy(new JFrame());
 parentComponent = Mockito.spy(new JButton());
 chooserPaneMock = Mockito.mock(JFontChooser.class);
 okListenerMock = Mockito.mock(ActionListener.class);
 cancelListenerMock = Mockito.mock(ActionListener.class);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFontChooserDialogCreation() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 assertEquals("Test Dialog", fontChookerDialog.getTitle());
 assertNotNull(fontChooserDialog.getOkButton());
 assertNotNull(fontChooserDialog.getCancelButton());
 }

 @Test
 public void testSetVisible_initialFontIsSet() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 fontChooserDialog.setVisible(true);
 verify(chooserPaneMock, times(1)).setFont(initialFont);
 }

 @Test
 public void testSetVisible_cancelButtonClosesDialog() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 fontChooserDialog.setVisible(true);
 verify(frame, times(1)).setVisible(false);
 }

 @Test
 public void testSetVisible_okButtonClosesDialog() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 fontChooserDialog.setVisible(true);
 verify(frame, times(1)).setVisible(false);
 verify(okListenerMock, times(1)).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testReset_fontIsResetToInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 verify(chooserPaneMock, times(1)).setFont(initialFont);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFontChooserDialogCreation_nullParentComponent() {
 new FontChooserDialog(null, "Test Dialog", true, chooserPaneMock, okListenerMock, cancelListenerMock);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogCreation_nullTitle() {
 new FontChooserDialog(parentComponent, null, true, chooserPaneMock, okListenerMock, cancelListenerMock);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogCreation_nullChooserPane() {
 new FontChooserDialog(parentComponent, "Test Dialog", true, null, okListenerMock, cancelListenerMock);
 }
}