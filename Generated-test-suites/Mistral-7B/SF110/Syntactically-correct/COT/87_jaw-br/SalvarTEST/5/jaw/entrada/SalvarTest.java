package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private ProcessarEntidades form;

 @Test
 public void testSalvarWithValidData() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 assertFalse(salvar.getVel());
 assertNull(salvar.getArquivo());
 assertNotNull(salvar.getEntidade());
Salvar salvar1 = new Salvar(form, Salvar.SALVAR);
 Forma forma = new Forma();
 forma.setNome("Teste");
 forma.setAltura(1.0);
 forma.setComprimento(2.0);
 salvar1.getEntidade().add(forma);
 Forma forma1 = new Forma();
 forma1.setNome("Teste1");
 forma1.setAltura(1.1);
 forma1.setComprimento(2.1);
 salvar1.getEntidade().add(forma1);
 assertTrue(salvar1.getVel());
 assertNotNull(salvar1.getArquivo());
 assertFalse(salvar1.getArquivo().endsWith(".jaw.xml"));
 assertFalse(salvar1.getArquivo().endsWith(".jaw"));
 assertTrue(salvar1.arquivo.contains(".jaw"));
 }

 @Test(expected = Exception.class)
 public void testSalvarWithExceptionWhenNoFileChooserSelectedFile() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.getEntidade().clear();
 salvar.vela = true;
 salvar.arquivo = "";
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 salvar.salvar(salvar.entidade, salvar.arquivo);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithExceptionWhenFileFilterNotFound() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.getEntidade().clear();
 salvar.vela = true;
 salvar.arquivo = new File("teste.txt").getAbsolutePath();
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 salvar.salvar(salvar.entidade, salvar.arquivo);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithExceptionWhenFileAlreadyExists() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 Forma forma = new Forma();
 forma.setNome("Teste");
 forma.setAltura(1.0);
 forma.setComprimento(2.0);
 salvar.getEntidade().clear();
 salvar.getEntidade().add(forma);
 File file = new File("teste.jaw");
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;
 file.createNewFile();
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 salvar.salvar(salvar.entidade, salvar.arquivo);
 }

 @Test
 public void testSalvarWithValidDataXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 Forma forma = new Forma();
 forma.setNome("Teste");
 forma.setAltura(1.0);
 forma.setComprimento(2.0);
 salvar.getEntidade().clear();
 salvar.getEntidade().add(forma);
 salvar.vela = true;
 salvar.arquivo = "";
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<forma>\n<nome>Teste</nome>\n<altura>1.0</altura>\n<comprimento>2.0</comprimento>\n</forma>\n<forma>\n<nome>Teste1</nome>\n<altura>1.1</altura>\n<comprimento>2.1</comprimento>\n</forma>\n</entidades>";
 Forma forma1 = new Forma();
 forma1.setNome("Teste1");
 forma1.setAltura(1.1);
 forma1.setComprimento(2.1);
 salvar.getEntidade().add(forma1);
 String actual = "";
 try {
 actual = salvar.salvar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected, actual);
 }

 @Test(expected = Exception.class)
 public void testSerializarExceptionWhenNoFileChooserSelectedFileXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.getEntidade().clear();
 salvar.vela = true;
 salvar.arquivo = "";
 try {
 salvar.serializar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = Exception.class)
 public void testSerializarExceptionWhenFileFilterNotFoundXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML_COMO);
 salvar.getEntidade().clear();
 salvar.vela = true;
 salvar.arquivo = new File("teste.txt").getAbsolutePath();
 try {
 salvar.serializar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSerializarWithValidDataXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 Forma forma = new Forma();
 forma.setNome("Teste");
 forma.setAltura(1.0);
 forma.setComprimento(2.0);
 salvar.getEntidade().clear();
 salvar.getEntidade().add(forma);
 salvar.vela = true;
 salvar.arquivo = "";
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<forma>\n<nome>Teste</nome>\n<altura>1.0</altura>\n<comprimento>2.0</comprimento>\n</forma>\n<forma>\n<nome>Teste1</nome>\n<altura>1.1</altura>\n<comprimento>2.1</comprimento>\n</forma>\n</entidades>";
 Forma forma1 = new Forma();
 forma1.setNome("Teste1");
 forma1.setAltura(1.1);
 forma1.setComprimento(2.1);
 salvar.getEntidade().add(forma1);
 String actual = "";
 try {
 actual = salvar.serializar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected, actual);
 }

 @Test(expected = FileNotFoundException.class)
 public void testSerializarWithExceptionWhenFileNotFound() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.getEntidade().clear();
 salvar.vela = true;
 File file = new File("teste.jaw");
 salvar.arquivo = file.getAbsolutePath();
 file.delete();
 try {
 salvar.serializar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSerializarWithValidDataWhenFileAlreadyExistsXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 Forma forma = new Forma();
 forma.setNome("Teste");
 forma.setAltura(1.0);
 forma.setComprimento(2.0);
 salvar.getEntidade().clear();
 salvar.getEntidade().add(forma);
 salvar.vela = true;
 salvar.arquivo = "";
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<forma>\n<nome>Teste</nome>\n<altura>1.0</altura>\n<comprimento>2.0</comprimento>\n</forma>\n<forma>\n<nome>Teste1</nome>\n<altura>1.1</altura>\n<comprimento>2.1</comprimento>\n</forma>\n</entidades>";
 Forma forma1 = new Forma();
 forma1.setNome("Teste1");
 forma1.setAltura(1.1);
 forma1.setComprimento(2.1);
 salvar.getEntidade().add(forma1);
 String actual = "";
 try {
 actual = salvar.salvar(salvar.entidade, salvar.arquivo);
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expected, actual);
 }

}