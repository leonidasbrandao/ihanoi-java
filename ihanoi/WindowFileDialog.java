/**
 * @author Leo^nidas de Oliveira Branda~o
 * @see    igeom/IGeom: accept (File dir, String name): chama WindowFileDialog
 * @see    igeom/ig/AreaDeDesenho: gravarScript(): chama WindowFileDialog
 * Extende "java.awt.FileDialog" para colocar cor de fundo e mudar letras "defaults"
 * Data: 2013/03/07-12 acresc. FileNameFilter; alterada indentacao e comentarios
 *       2011-02-20 
 *       19/Setembro/2010
 *       2373 2009-04-22 17:19 igeom/ig/WindowFileDialog.java
 *       1522 Aug 29  2005     igeom/ig/WindowFileDialog.java
 * 
 * Janela para ler/gravar arquivo.
 * Processamento, apos clique no botao OK, é feito em "ihanoi.iHanoi.abra_arquivo(String,String)
 * IGeom.processaMenu(Object,String)
 * 
 */

package ihanoi;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Color;
import java.awt.Frame;
import java.io.FilenameFilter;
import java.io.File;


public class WindowFileDialog extends FileDialog {

  public static final String defaultFileNameFilter = "*.ihn,*.txt";
  public static String defaultFileName = "example.ihn";

  public WindowFileExists jan_arq = null; // janela que eventualmente sinalizara que arq. ja existe

  private static final Color corFundo = iHanoi.fundoAzulEscuro1;

  public synchronized void cria_jan_arq (ihanoi.iHanoi janelaMae, String msg, String nome_arquivo, int n) {
    jan_arq = new WindowFileExists(janelaMae, msg, 1);
    }


  private void inicia (final String defaultFNF) {
    //try{String str=""; System.out.println(str.charAt(3));}catch(Exception e) {System.out.println("WindowFileDialog.java"); e.printStackTrace();}
    if (defaultFNF!=null) {
       FilenameFilter fnf = new FilenameFilter() { // 'final' to avoid the error 'is accessed from within inner class; needs to be declared final'
          public boolean accept(File dir, String name) { // this method is tested against any file in the current directory
            return name.endsWith(defaultFNF) ? true : false; }
            };
       setFilenameFilter(fnf);
       }

    setBackground(corFundo);
    setForeground(iHanoi.fundoTopo); //
    setFont(new Font("Arial", Font.BOLD, 10) );
    setFile(defaultFileName);
    setTitle(ResourceReader.read("wfdMsgOpen")); // Open file...
    }

  // Open a new file (under 'menu file')
  // From: igeom/IGeom.processaMenu(Object obj, String s)
  public WindowFileDialog (Frame f) {
    super(f);
    this.inicia(null);
    }

  // Open a new file (under 'menu file')
  // From: igeom/IGeom.processaMenu(Object obj, String s)
  //       igeom/ig/AreaDeDesenho.executarScript(AreaDeDesenho.java:6953)
  public WindowFileDialog (Frame f, String s, String strFilterName) {
    super(f,s);
    this.defaultFileName = s;
    this.inicia(strFilterName); //
    }

  // Open a new file (under 'menu file')
  // From: igeom/IGeom.processaMenu(Object obj, String s)
  //       igeom/ig/AreaDeDesenho.java:6852: cannot resolve symbol
  public WindowFileDialog (Frame f, String s, String fileName, int i) {
    super(f,s,i);
    this.defaultFileName = fileName;
    this.inicia(null); //
    }

  // Open a new file (under 'menu file')
  // From: igeom/IGeom.processaMenu(Object obj, String s)
  public WindowFileDialog (Frame f, String s, String fileName, String strFilterName, int i) {
    super(f,s,i);
    this.defaultFileName = fileName;
    this.inicia(strFilterName); //
    }

  }
