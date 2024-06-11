package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

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
 private File testFile;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 testFile = new File("test.jaw.xml");
 try {
 testFile.createNewFile();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testAbrirXML() {
 // Teste de abertura de arquivo XML com sucesso
 abrir = new Abrir(form, testFile);
 assertTrue(abrir.xml.length() > 0);
 try {
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 form.setEntidades(serializacaoXML.xml2Entidades(new FileReader(testFile)));
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 String expected = "Teste XML"; // Valor esperado para testar se o arquivo foi carregado corretamente
 String actual = abrir.xml;
 assertEquals(expected, actual);
 } catch (Exception e) {
 fail("Erro na abertura do arquivo XML: " + e.getMessage());
 }

 // Teste de abertura de arquivo XML inválido
 abrir = new Abrir(form, new File("test.invalido.xml"));
 String actual = abrir.xml;
 assertEquals("", actual);
 JOptionPane.showMessageDialog(null, "Teste de mensagem de erro ao abrir arquivo XML inválido", "Teste de erro", JOptionPane.INFORMATION_MESSAGE);

 // Teste de limpeza do arquivo XML
 deleteFile(testFile);
}

 @Test
 public void testAbrirSerializado() {
 // Teste de abertura de arquivo serializado com sucesso
 try {
 File arquivoSerializado = new File("test.jaw");
 Serializacao serializacao = new Serializacao();
 serializacao.grava(form.getEntidades(), arquivoSerializado);
 abrir = new Abrir(form, Abrir.ABRIR);
 String expected = "Teste Serializado"; // Valor esperado para testar se o arquivo foi carregado corretamente
 String actual = abrir.xml;
 assertEquals(expected, actual);
 } catch (Exception e) {
 fail("Erro na abertura do arquivo serializado: " + e.getMessage());
 } finally {
 deleteFile(new File("test.jaw"));
 }

 // Teste de abertura de arquivo serializado inválido
 abrir = new Abrir(form, Abrir.ABRIR);
 String actual = abrir.xml;
 assertEquals("", actual);
 JOptionPane.showMessageDialog(null, "Teste de mensagem de erro ao abrir arquivo serializado inválido", "Teste de erro", JOptionPane.INFORMATION_MESSAGE);
}

 @Test
 public void testAbrirArquivoNaoEncontrado() {
 // Teste de abertura de arquivo não encontrado
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 FileFilter filtro = new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory();
 }

 @Override
 public String getDescription() {
 return "Teste Filtro";
 }
 };
 escolhedor.setFileFilter(filtro);
 escolhedor.showOpenDialog(null);
 String actual = abrir.xml;
 assertEquals("", actual);
 JOptionPane.showMessageDialog(null, "Teste de mensagem de erro ao abrir arquivo não encontrado", "Teste de erro", JOptionPane.INFORMATION_MESSAGE);
}

 @Test
 public void testAbrirCancelado() {
 // Teste de usuário cancelando abertura de arquivo
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showOpenDialog(null);
 String actual = abrir.xml;
 assertEquals("", actual);
}

 private void deleteFile(File file) {
 if (file.exists()) {
 file.delete();
 }
}
}