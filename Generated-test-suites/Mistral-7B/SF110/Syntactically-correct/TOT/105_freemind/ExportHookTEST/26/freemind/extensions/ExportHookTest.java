package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageFilter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileChooserBuilder;
import javax.swing.filechooser.FileFilterDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import freemind.controller.ControllerTools;
import freemind.controller.ControllerWrapper;
import freemind.controller.ModeController;
import freemind.controller.MapController;
import freemind.extension.api.ModeControllerHook;
import freemind.extension.api.ModeControllerHookAdapter;
import freemind.extension.api.ModeControllerHookContext;
import freemind.extension.api.ModeControllerHookHandler;
import freemind.extension.api.ModeControllerHookInvoker;
import freemind.extension.api.ModeControllerHookRegistry;
import freemind.main.FreeMind;
import freemind.main.FreeMindResources;
import freemind.modes.MMapMode;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ModeControllerHook adapter;
 private ControllerWrapper controller;
 private MapView view;
 private ModeControllerHookInvoker invoker;
 private ModeControllerHookHandler handler;
 private ModeControllerHookRegistry registry;

 @Before
 public void setUp() {
 controller = Mockito.mock(ControllerWrapper.class, Answers.RETURNS_DEEP_STUBS);
 view = Mockito.mock(MapView.class, Answers.RETURNS_DEEP_STUBS);
 invoker = Mockito.mock(ModeControllerHookInvoker.class);
 handler = Mockito.mock(ModeControllerHookHandler.class);
 registry = Mockito.mock(ModeControllerHookRegistry.class);
 adapter = new ExportHook() {
 {
 setController(controller);
 setInvoker(invoker);
 setHandler(handler);
 setRegistry(registry);
 }
 };
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(adapter.chooseFile("type", "description", null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(adapter.chooseFile("type", null, null));
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(adapter.chooseFile(null, "description", null));
 }

 @Test
 public void testChooseFileValidOutput() {
 final File expectedOutput = new File("path/to/output.ext");
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class, Answers.RETURNS_DEEP_STUBS));
 when(controller.getText("file_already_exists")).thenReturn("file already exists");
 when(controller.getMap()).thenReturn(Mockito.mock(MMapMode.class));
 when(controller.getText(anyString())).thenAnswer(i -> i.getArguments()[0].toString());
 when(controller.getFileChooser(any(ImageFilter.class))).thenAnswer(i -> FileChooserBuilder.newInstance().setApproveButtonText("Save").setSelectedFiles(new File[] { expectedOutput }).createFileChooser());
 when(invoker.invokeModeControllerHook(eq(adapter), any(ModeControllerHookContext.class))).thenReturn(true);
 when(handler.handleModeControllerHook(eq(adapter), any(ModeControllerHookContext.class))).thenReturn(true);
 when(registry.registerModeControllerHook(eq(adapter), any(Class.class), anyInt(), anyBoolean())).thenReturn(1);

 final File result = adapter.chooseFile("type", "description", "ext");
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testChooseFileCancel() {
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class, Answers.RETURNS_DEEP_STUBS));
 when(controller.getText("file_already_exists")).thenReturn("file already exists");
 when(controller.getMap()).thenReturn(Mockito.mock(MMapMode.class));
 when(controller.getText(anyString())).thenAnswer(i -> i.getArguments()[0].toString());
 when(controller.getFileChooser(any(ImageFilter.class))).thenAnswer(i -> FileChooserBuilder.newInstance().setApproveButtonText("Save").createFileChooser());
 when(invoker.invokeModeControllerHook(eq(adapter), any(ModeControllerHookContext.class))).thenReturn(true);
 when(handler.handleModeControllerHook(eq(adapter), any(ModeControllerHookContext.class))).thenReturn(true);
 when(registry.registerModeControllerHook(eq(adapter), any(Class.class), anyInt(), anyBoolean())).thenReturn(1);
 when(controller.getFileChooser(any(ImageFilter.class)).showSaveDialog(any())).thenReturn(JOptionPane.CLOSED_OPTION);

 final File result = adapter.chooseFile("type", "description", null);
 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(adapter.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 final String expectedOutput = "output";
 when(controller.getText(eq("file_already_exists"))).thenReturn("output");

 final String result = adapter.getTranslatableResourceString("file_already_exists");
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(adapter.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidImage() {
 final BufferedImage expectedOutput = ImageIO.read(new File("path/to/image.ext"));
 when(controller.getView()).thenReturn(view);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
 when(view.createImage(anyInt(), anyInt())).thenReturn(ImageIO.createImage(new ByteArrayInputStream(new byte[0]), "ext", null));
 when(view.print(any(Graphics.class))).thenReturn(true);
 when(view.getSubimage(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(expectedOutput);

 final BufferedImage result = adapter.createBufferedImage();
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 adapter.copyFromResource(null, "fileName", "destinationDirectory");
 }

 @Test
 public void testCopyFromResourceNullFileName() {
 adapter.copyFromResource("prefix", null, "destinationDirectory");
 }

 @Test
 public void testCopyFromResourceNullDestinationDirectory() {
 adapter.copyFromResource("prefix", "fileName", null);
 }

 @Test
 public void testCopyFromResourceValidFile() throws IOException {
 final String expectedPath = "path/to/output.ext";
 final byte[] expectedBytes = new byte[10];
 when(controller.getLogger()).thenReturn(Mockito.mock(FreeMind.Logger.class, Answers.RETURNS_DEEP_STUBS));

 final URL resource = new URL("resource:/prefix/fileName.ext");
 when(controller.getResource(eq("prefix/fileName.ext"))).thenReturn(resource);
 when(resource.openStream()).thenReturn(new ByteArrayInputStream(expectedBytes));
 when(controller.getFileOutputStream(eq("destinationDirectory/fileName.ext"))).thenReturn(new FileOutputStream(new File("destinationDirectory/fileName.ext")));
 when(controller.copyStream(any(InputStream.class), any(OutputStream.class), anyBoolean())).thenReturn(true);

 adapter.copyFromResource("prefix", "fileName", "destinationDirectory");
 verify(controller, times(1)).getLogger().finest("searching for prefixfileName");
 verify(controller, times(1)).getLogger().finest("searching for prefixfileName and should go to destinationDirectory");
 verify(controller, times(1)).getLogger().severe("Cannot find resource: prefixfileName");
 verify(controller, times(1)).getLogger().severe("File not found or could not be copied. Was earching for prefixfileName and should go to destinationDirectory");
 }

 @Test
 public void testCopyFromFileValidFile() throws IOException {
 final String expectedPath = "path/to/output.ext";
 final byte[] expectedBytes = new byte[10];
 when(controller.getLogger()).thenReturn(Mockito.mock(FreeMind.Logger.class, Answers.RETURNS_DEEP_STUBS));

 final File resource = new File("path/to/input.ext");
 when(controller.getLogger()).thenReturn(Mockito.mock(FreeMind.Logger.class, Answers.RETURNS_DEEP_STUBS));
 when(controller.getFileInputStream(eq("path/to/input.ext"))).thenReturn(new FileInputStream(resource));
 when(controller.getFileOutputStream(eq("destinationDirectory/fileName.ext"))).thenReturn(new FileOutputStream(new File("destinationDirectory/fileName.ext")));
 when(controller.copyStream(any(InputStream.class), any(OutputStream.class), anyBoolean())).thenReturn(true);

 adapter.copyFromFile("path/to", "fileName", "destinationDirectory");
 verify(controller, times(1)).getLogger().finest("searching for path/tofileName");
 verify(controller, times(1)).getLogger().finest("searching for path/tofileName and should go to destinationDirectory");
 verify(controller, times(1)).getLogger().severe("Cannot find resource: path/tofileName");
 verify(controller, times(1)).getLogger().severe("File not found or could not be copied. Was earching for path/tofileName and should go to destinationDirectory");
 }
}