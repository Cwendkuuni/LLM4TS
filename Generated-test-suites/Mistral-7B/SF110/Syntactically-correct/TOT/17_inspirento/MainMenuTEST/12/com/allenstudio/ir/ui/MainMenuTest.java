package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private static final String FILE_NAME = "testFile.txt";
 private static final String MENU_0 = "File";
 private static final String MENU_1_0 = "File->Open";
 private static final String MENU_1_1 = "File->Open->Browse";
 private static final String MENU_2 = "Edit";
 private static final String MENU_2_0 = "Edit->Cut";
 private static final String MENU_3 = "Help";
 private static final String MENU_3_0 = "Help->About";

 @Mock
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMainMenuConstruction() {
 MainMenu mm = new MainMenu(mediator);
 assertNotNull(mm);
 // Verify number of menus created
 assertEquals(3, mm.getComponentCount());
 // Verify each menu created
 List<JMenu> menus = Arrays.asList(mm.getComponents());
 assertEquals(MENU_0, menus.get(0).getText());
 assertEquals(MENU_1_0, menus.get(1).getText());
 assertEquals(MENU_2, menus.get(2).getText());
 assertEquals(MENU_3, menus.get(3).getText());
 }

 @Test
 public void testCreateMenu() {
 String tempLabel = "Test File*";
 String menu = MENU_1_0;
 JMenu jMenu = new MainMenu(mediator).createMenu(menu);
 assertNotNull(jMenu);
 assertEquals(tempLabel.substring(0, tempLabel.length() - 2), jMenu.getText());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = MENU_1_0;
 JMenuItem item = mock(JMenuItem.class);
 when(item.getText()).thenReturn(MENU_1_1);

 MainMenu.addShortcutAndIcon(item, key);
 ArgumentCaptor<InputEvent> inputEventCaptor = ArgumentCaptor.forClass(InputEvent.class);
 verify(item).setAccelerator(any(KeyStroke.class));
 // Verify no icon was set since there's no icon defined for this key
 verifyNoMoreInteractions(item);
 }

 @Test
 public void testAddShortcutAndIconWithIcon() {
 String key = MENU_3_0;
 JMenuItem item = mock(JMenuItem.class);
 when(item.getText()).thenReturn(MENU_3);

 UIManager uiManager = UIManager.getInstances();
 url = new File("src/test/resources/icon.png").toURI().toURL();
 uiManager.put("Help.aboutIcon", url);

 MainMenu.addShortcutAndIcon(item, key);
 ArgumentCaptor<Image> imageCaptor = ArgumentCaptor.forClass(Image.class);
 verify(item).setIcon(imageCaptor.capture());
 assertEquals("icon.png", imageCaptor.getValue().getFile().getPath());
 }

 @Test
 public void testAddMenuItemWithShortcut() {
 String key = MENU_1_1;
 JMenuItem item = mock(JMenuItem.class);
 when(item.getText()).thenReturn(MENU_1_0);

 MainMenu.addShortcutAndIcon(item, key);
 assertEquals(MENU_1_1, item.getText());
 ArgumentCaptor<InputEvent> inputEventCaptor = ArgumentCaptor.forClass(InputEvent.class);
 verify(item).setAccelerator(any(KeyStroke.class));
 }

 @Test
 public void testAddMenuItemWithShortcutAndIcon() {
 String key = MENU_3_0;
 JMenuItem item = mock(JMenuItem.class);
 when(item.getText()).thenReturn(MENU_3);

 UIManager uiManager = UIManager.getInstances();
 uiManager.put("Help.aboutIcon", new File("src/test/resources/icon.png").toURI().toURL());

 MainMenu.addShortcutAndIcon(item, key);
 assertEquals(MENU_3, item.getText());
 ArgumentCaptor<Image> imageCaptor = ArgumentCaptor.forClass(Image.class);
 verify(item).setIcon(imageCaptor.capture());
 assertEquals("icon.png", imageCaptor.getValue().getFile().getPath());
 }

 @Test
 public void testOpenFileDialog() {
 String key = MENU_1_1;
 JMenuItem openItem = mock(JMenuItem.class);
 when(openItem.getText()).thenReturn(MENU_1_1);
 JFileChooser fileChooser = mock(JFileChooser.class);
 JOptionPane.showInternalFrameDialog(mock(Frame.class), fileChooser, "Open", JOptionPane.DEFAULT_OPTION);
 when(fileChooser.showOpenDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getCurrentDirectory()).thenReturn(new File("src/test/resources"));
 when(fileChooser.getSelectedFile()).thenReturn(new File(FILE_NAME));

 MainMenu.addShortcutAndIcon(openItem, key);
 MainMenu menu = new MainMenu(mediator);
 JPopupMenu popupMenu = new JPopupMenu();
 popupMenu.add(openItem);
 menu.add(popupMenu);

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(popupMenu);
 openItem.doClick();
 }
 });

 verify(fileChooser).setDialogTitle("Open");
 verify(fileChooser).setFileFilter(any(FileFilter.class));
 verify(fileChooser).setApproveButtonText("Open");
 verify(fileChooser).setApproveButtonMnemonic('O');
 verify(fileChooser).setLayout(isA(BorderLayout.class));
 verify(fileChooser).setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
 verify(fileChooser).setFileSelectionMode(JFileChooser.FILES_ONLY);
 verify(fileChooser).setAcceptAllFileFilterUsed(false);
 verify(fileChooser).setMultiSelectionEnabled(false);
 verify(fileChooser).setFileView(isA(JFileChooser.FileView[].class));
 verify(fileChooser).setAccessory(any(JComponent.class));
 verify(fileChooser).setControlButtonsAreShown(true);
 // Verify the file was opened
 verify(fileChooser).setSelectedFile(new File(FILE_NAME));
 }
}