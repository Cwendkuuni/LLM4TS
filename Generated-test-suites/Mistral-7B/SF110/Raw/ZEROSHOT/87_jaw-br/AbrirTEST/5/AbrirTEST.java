package jaw.entrada.test;

import static org.junit.Assert.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private String arquivoXml;
 private String arquivoJaw;
 private String arquivoInvalido;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 arquivoXml = "teste.jaw.xml";
 arquivoJaw = "teste.jaw";
 arquivoInvalido = "teste.txt";
 }

 @Test
 public void testAbrirXml() {
 Abrir abrir = new Abrir(form, arquivoXml);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXmlInvalidArquivo() {
 Abrir abrir = new Abrir(form, arquivoInvalido);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoInvalidArquivo() {
 Abrir abrir = new Abrir(form, arquivoInvalido);
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testAbrirSerializado() {
 File file = new File(arquivoJaw);
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 chooser.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 chooser.showOpenDialog(null);
 File selectedFile = chooser.getSelectedFile();
 if (selectedFile != null) {
 file = selectedFile;
 }
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(file.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = JOptionPane.ShowMessageDialogException.class)
 public void testAbrirSerializadoInvalidFileType() {
 File file = new File(arquivoXml);
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 chooser.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.getName().toLowerCase().endsWith(".xml");
 }

 @Override
 public String getDescription() {
 return "XML files";
 }
 });
 chooser.showOpenDialog(null);
 File selectedFile = chooser.getSelectedFile();
 if (selectedFile != null) {
 file = selectedFile;
 }
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirSerializado(file.getAbsolutePath());
 fail("Expected JOptionPane.ShowMessageDialogException");
 }

 @Test
 public void testAbrirXmlFileFilter() {
 FileDialog dialog = new FileDialog(new Frame(), "XML files", FileDialog.OPEN);
 dialog.setFile(".", "*.xml");
 dialog.show();
 String arquivo = dialog.getFile();
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(arquivo);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = JOptionPane.ShowMessageDialogException.class)
 public void testAbrirXmlInvalidFileType() {
 FileDialog dialog = new FileDialog(new Frame(), "JAW files", FileDialog.OPEN);
 dialog.setFile(".", "*.xml");
 dialog.show();
 String arquivo = dialog.getFile();
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(arquivo);
 fail("Expected JOptionPane.ShowMessageDialogException");
 }
}