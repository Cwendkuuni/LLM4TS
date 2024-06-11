package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import org.junit.*;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.parent = mock(MainFrame.class);
 this.chatFrame = new InternalChatFrame(this.parent);
 }

 @Test
 public void testAddText_NormalMessage() {
 String message = "User1: Hello World";
 this.chatFrame.addText(message);
 String expectedText = "User1: Hello World\n";
 assertSame(expectedText, this.chatFrame.getChatlog());
 }

 @Test
 public void testAddText_WhisperMessage() {
 String message = "/w User1: Hello World";
 this.chatFrame.addText(message);
 String expectedText = "User1 (whisper): Hello World\n";
 assertSame(expectedText, this.chatFrame.getChatlog());
 }

 @Test
 public void testAddText_ErrorMessage() {
 String message = "InvalidMessage";
 this.chatFrame.addText(message);
 String expectedText = "InvalidMessage\n";
 assertSame(expectedText, this.chatFrame.getChatlog());
 }

 @Test
 public void testAddStylesToDocument_StylesAdded() {
 StyledDocument doc = mock(StyledDocument.class);
 this.chatFrame.addStylesToDocument(doc);
 int numStyles = 7;
 verify(doc, times(numStyles)).addStyle(anyString(), eq(StyleContext.getDefaultStyleContext().getStyle("default")));
 }

 @Test
 public void testCreateGUI_ComponentsAdded() {
 GridBagConstraints c = mock(GridBagConstraints.class);
 JSplitPane split = mock(JSplitPane.class);
 JList list = mock(JList.class);
 JScrollPane scrList = mock(JScrollPane.class);
 JPanel right = mock(JPanel.class);
 JTextPane showTxt = mock(JTextPane.class);
 JScrollPane scrTxt = mock(JScrollPane.class);
 JTextField txtInput = mock(JTextField.class);
 JPopupMenu listPop = mock(JPopupMenu.class);
 JMenuItem listMenuItem = mock(JMenuItem.class);
 JPopupMenu txtPop = mock(JPopupMenu.class);
 JMenuItem txtMenuItem = mock(JMenuItem.class);

 when(split.setLeftComponent(scrList)).thenReturn(split);
 when(split.setRightComponent(right)).thenReturn(split);
 when(right.add(scrTxt)).thenReturn(right);
 when(scrTxt.add(showTxt)).thenReturn(scrTxt);
 when(showTxt.addMouseListener(any())).thenReturn(showTxt);
 when(list.addMouseListener(any())).thenReturn(list);
 when(parent.getSize()).thenReturn(new Dimension(800, 600));

 this.chatFrame = new InternalChatFrame(this.parent);

 verify(list, times(1)).setVisible(true);
 verify(list, times(1)).setSelectionMode(0);
 verify(list, times(1)).addMouseListener(this.chatFrame);
 verify(scrList, times(1)).setViewportView(this.chatFrame.list);
 verify(split, times(1)).setLeftComponent(this.chatFrame.scrList);
 verify(split, times(1)).setDividerLocation(100);
 verify(this.parent, times(1)).add(this.chatFrame);
 }

 @Test
 public void testActionPerformed_ChooseColor() {
 Color newColor = Color.RED;
 this.chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(this.chatFrame.listPop, times(1)).isVisible();
 verifyZeroInteractions(this.chatFrame.list);
 verifyZeroInteractions(this.chatFrame.txtPop);

 when(this.chatFrame.listPop.getComponent(0)).thenReturn(this.chatFrame.listMenuItem);
 when(this.chatFrame.listMenuItem.getActionCommand()).thenReturn("chooseColor");
 this.chatFrame.listPop.show(this.chatFrame, 0, 0);
 this.chatFrame.listMenuItem.actionPerformed(mock(ActionEvent.class));

 this.chatFrame.changeStyle(12, newColor, false, false, "User1");
 verify(this.chatFrame.doc, times(1)).getStyle("User1");
 verify(this.chatFrame.doc, times(1)).getStyle("User1").getBackground();
 verify(this.chatFrame.doc, times(1)).getStyle("User1").setForeground(newColor);
 }

 @Test
 public void testActionPerformed_BgColor() {
 Color newColor = Color.RED;
 this.chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(this.chatFrame.txtPop, times(1)).isVisible();
 verifyZeroInteractions(this.chatFrame.listPop);
 verifyZeroInteractions(this.chatFrame.list);

 when(this.chatFrame.txtPop.getComponent(0)).thenReturn(this.chatFrame.txtMenuItem);
 when(this.chatFrame.txtMenuItem.getActionCommand()).thenReturn("bgColor");
 this.chatFrame.txtPop.show(this.chatFrame, 0, 0);
 this.chatFrame.txtMenuItem.actionPerformed(mock(ActionEvent.class));

 verify(this.chatFrame.showTxt, times(1)).setBackground(newColor);
 }
}