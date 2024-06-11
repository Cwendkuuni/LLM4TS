package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testConstructorWithString() {
 String arquivo = "teste.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertEquals("teste.jaw.xml", form.getNomeArquivoSalvo());
 }

 @Test
 public void testConstructorWithFileChooserAndXML() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("C:/Tests"));
 escolhedor.setFileFilter(new AbrirTest.Filtro2());
 File arquivo = escolhedor.getSelectedFile();
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 escolhedor.showOpenDialog(null);
 assertEquals(arquivo.getAbsolutePath(), form.getNomeArquivoSalvo());
 }

 @Test
 public void testConstructorWithFileChooserAndSerializado() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("C:/Tests"));
 escolhedor.setFileFilter(new AbrirTest.Filtro());
 File arquivo = escolhedor.getSelectedFile();
 abrir = new Abrir(form, Abrir.ABRIR);
 escolhedor.showOpenDialog(null);
 assertEquals(arquivo.getAbsolutePath(), form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullForm() {
 abrir = new Abrir(null, "teste.jaw.xml");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArquivo() {
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirSerializadoWithInvalidFileType() {
 abrir = new Abrir(form, new File("teste.txt"));
 assertFalse(form.getEntidades() != null);
 }

 @Test
 public void testAbrirSerializadoWithValidFile() throws IOException {
 File arquivo = new File("teste.jaw");
 Serializacao serializacao = new Serializacao();
 serializacao.gravar(new Object(), arquivo);
 abrir = new Abrir(form, arquivo);
 assertTrue(form.getEntidades() != null);
 }

 @Test
 public void testAbrirXMLWithInvalidFileType() {
 abrir = new Abrir(form, new File("teste.xml"));
 assertFalse(form.getEntidades() != null);
 }

 @Test
 public void testAbrirXMLWithValidFile() throws IOException {
 File arquivo = new File("teste.jaw.xml");
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n<entidade>\n<id>1</id>\n<nome>Teste</nome>\n</entidade>\n</entidades>";
 FileWriter writer = new FileWriter(arquivo);
 writer.write(xml);
 writer.close();
 abrir = new Abrir(form, arquivo);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithMissingFile() {
 abrir = new Abrir(form, new File("teste.jaw"));
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithMissingFile() throws FileNotFoundException {
 abrir = new Abrir(form, new File("teste.jaw.xml"));
 }

 @Test
 public void testShowMessageDialogWhenInvalidSerializadoFile() {
 abrir = new Abrir(form, new File("teste.txt"));
 String message = JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!");
 assertEquals("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", message);
 }

 @Test
 public void testShowMessageDialogWhenInvalidXMLFile() {
 abrir = new Abrir(form, new File("teste.txt"));
 abrir.abrirXML(new File("teste.xml"));
 String message = JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!");
 assertEquals("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", message);
 }

 private static class Filtro2 extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 }

 private static class Filtro extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
}

}