package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXML() {
 String arquivoXml = "teste.jaw.xml";
 abrir = new Abrir(form, arquivoXml);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidFormat() {
 String arquivoInvalido = "teste.txt";
 abrir = new Abrir(form, arquivoInvalido);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializado() {
 String arquivoSerializado = "teste.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(arquivoSerializado);
 try {
 file.createNewFile();
 } catch (IOException e) {
 e.printStackTrace();
 }
 abrir = new Abrir(form, arquivoSerializado);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidFormat() {
 String arquivoInvalido = "teste.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir = new Abrir(form, arquivoInvalido);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testOpenFileDialogXMLFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 FileFilter filtro = new Abrir().new Filtro2();
 escolhedor.setFileFilter(filtro);
 assertTrue(escolhedor.accept(new File("teste.jaw.xml")));
 }

 @Test
 public void testOpenFileDialogSerializadoFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 FileFilter filtro = new Abrir().new Filtro();
 escolhedor.setFileFilter(filtro);
 assertTrue(escolhedor.accept(new File("teste.jaw")));
 }

 @Test
 public void testOpenFileDialogInvalidFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 FileFilter filtro = new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.getName().toLowerCase().endsWith(".teste");
 }

 @Override
 public String getDescription() {
 return "teste files";
 }
 };
 escolhedor.setFileFilter(filtro);
 assertFalse(escolhedor.accept(new File("teste.jaw.xml")));
 }
}