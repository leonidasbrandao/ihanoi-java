/*
 * iHanoi - Interactive/Internet Tangram: http://www.matematica.br/ihanoi
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br
 *
 * @author Leo^nidas de Oliveira Branda~o
 * 
 * @description Utilities to manage files (from hard disc and from Web)
 * ihanoi.util.ProcessReadWrite
 * 
 * @see ihanoi/iHanoiPanel.java
 *      - String strFileNameRead    : name of the last file read
 *      - String strLastFileRecorded: name of the last file read
 * @see ihanoi/util/FileUtilities.java
 *      - public static void storeFile (String fName, String strContent, String msgTracking)
 *      - public static void gravar (String nome, String arquivo_str, String msg_org)
 *      - public static String []  ler_geral (String nome, String msg_org, boolean devolve_linhas)
 *  
 * @credits
 * This source is free and provided by iMath Project (University of Sao Paulo - Brazil). In order to contribute, please
 * contact the iMath coordinator Leo^nidas O. Branda~o.
 *
 * O codigo fonte deste programa e' livre e desenvolvido pelo projeto iMatica (Universidade de Sao Paulo). Para contribuir,
 * por favor contate o coordenador do projeto iMatica, professor Leo^nidas O. Branda~o.
 * 
 */

package ihanoi.util;


import java.awt.FileDialog;
import java.awt.Frame;

import java.io.BufferedReader; //
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.Vector;

import ihanoi.iHanoi;
import ihanoi.iHanoiPanel;
import ihanoi.ResourceReader;
import ihanoi.WarningDialog;
import ihanoi.WindowFileDialog;
import ihanoi.WindowFileExists;


// Allow filter name
class classFileNameFilter implements java.io.FilenameFilter {
  // Is it an accepted file extension?
  // To use a class as parameter of 'java.awt.FileDialog fd': fd.setFilenameFilter(ihanoi) 
  public boolean accept (java.io.File file, java.lang.String strName) { // implement for java.io.FilenameFilter
    strName = strName.toLowerCase();
    if (strName.endsWith(".ihn")) return true;
    return false;
    }
  }

public class ProcessReadWrite {

  // ultimoArquivoSalvo -> ihanoi.iHanoiPanel.strLastFileRecorded
  // ihanoi.iHanoiPanel.getLastFileRecorded

  public static String sep = "/"; // Linux: "/" ; win => "\", c.c. "/"

  private static final String MARCAIGEOM = "# ihanoi: http://www.matematica.br!";

  private static String versionInFile; // iHanoi.VERSION

  private static final boolean lista = true;

  public static final String INPUT_ENC  = "ISO-8859-1"; // "UTF-8";
  public static final String OUTPUT_ENC = "ISO-8859-1"; // "UTF-8";

  // Verify if the file has the inital iHanoi mark "# ihanoi: http://www.matematica.br!"
  public static boolean hasFirstLine (String linha) {
    if ( linha==null || linha.length()<MARCAIGEOM.length() || ! linha.substring(0,MARCAIGEOM.length()).equals(MARCAIGEOM) ) {
      return false;
      }
   return true;
   }

  // Return only the last name of the file (considering path)
  // If used "java ihanoi.iHanoi ../temp/exerc6-2b.ihn", getOnlyNameFile("../temp/exerc6-2b.ihn") devolve "exerc6-2b.ihn"
  public static String getOnlyNameFile (String nome) {
    String token = "";
    boolean lista = false;

    sep = java.io.File.separator; // it is a public variable!

    if (lista) System.out.println("ProcessReadWrite.getOnlyNameFile(String nome) nome=" + nome + ", sep=<" + sep + ">");
    StringTokenizer st = new StringTokenizer(nome, sep);
    while (st.hasMoreTokens()) {
       if (lista) System.out.println(" " + token);
       token = st.nextToken();
       }
    return token;
    }


  // Return: -1 => it is not iHanoi file (do not presents first line); 1 => success in reading; -2 => some error...
  // Called by: ProcessReadWrite.processReadFile(iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame)
  public static int readFromFile (iHanoiPanel ihanoiPanel, String fileName, String fileDirectory) {
    // "Aguarde leitura do arquivo...");
    ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsgRWwaitReading")); // Please, wait for the file reading...

    // Indicate that is under reading process...
    boolean lendoGravando = true;

    FileReader arquivo = null;
    String dir = null;
    StringBuffer strContent = new StringBuffer();
    Vector vecObjects;
    int countLines = 0;

    fileName = ProcessReadWrite.getOnlyNameFile(fileName); // ihanoi.util.ProcessReadWrite.getOnlyNameFile(fileName)
    if (lista) System.out.println("ProcessReadWrite.readFromFile(...): fileName=" + fileName);

    if (fileDirectory!="" && !fileDirectory.equals(ProcessReadWrite.sep) && !fileDirectory.equals(".")) {
       dir  = fileDirectory;
       // If it the same, nothing has changed, let this way. This condition avoids to put "null" when is file under Web (it would results "/home/leo/.../igeom/novo/nullAleo18-1.ihn")
       if (!dir.equals(fileDirectory))
         fileName = dir + ProcessReadWrite.sep + fileName;
       else
         fileName = fileDirectory + ProcessReadWrite.sep + fileName; // necessary the complete name, otherwise do not find files in others directories
       }
    else
      fileName = "." + ProcessReadWrite.sep + fileName;

    ihanoiPanel.setFileNameRead(fileName); // sets 'String iHanoiPanel.strFileNameRead'
    if (lista) System.out.println("ProcessReadWrite.readFromFile(...): dir=" + dir + ", ihanoiPanel.getFileNameRead()=" + fileName);

    try {
      //--- If used 'new BufferedReader(new FileReader(fileName))' => will loose accents under ISO-8859-1 or UTF-8
      //--- BufferedReader buffer = new BufferedReadernew FileReader(fileName)); // if lost accents when compiling under Java 1.1 and runs with >= Java 1.5
      BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), INPUT_ENC));

      // final String INPUT_ENC = "ISO-8859-1"; // "UTF-8";
      // final String OUTPUT_ENC = "ISO-8859-1"; // "UTF-8";

      String linha;
      vecObjects = new Vector();
      versionInFile = "";

      if (lista) System.out.println("ProcessReadWrite.readFromFile(...): file content:");

      linha = buffer.readLine();
      strContent.append(linha);
       countLines++;
      if (lista) System.out.println("" + linha);

      if (!hasFirstLine(linha)) { // verify if the file has the fisrt iHanoi line ("# ihanoi: http://www.matematica.br!")
        ihanoiPanel.setTextTmsg(ResourceReader.read("iuRWerr_notihanoi"));
        System.err.println(ResourceReader.read("iuRWerr_notihanoi") + "\n" + linha); // The file content didn't seems to be generated by iHanoi!
        return -1;
        }

      // Read the file content
      vecObjects.addElement(linha);
      while ( (linha = buffer.readLine())!=null ) {
        countLines++;
        if (lista) System.out.println("" + linha);
        if (linha!=null && linha!="")
          linha = linha.trim();
        else
          continue;
        if (linha.charAt(0) == '[')
          ; // process comments...
        else
          vecObjects.addElement(linha);
        strContent.append(linha + "\n");
        }

      // if (linha.length()>0 && linha.charAt(linha.length()-1)=='!') linha = linha.substring(0,linha.length()-1);
      //D System.err.println("GravadorLeitor.readFromFile(BarraDeDesenho,String,String): eh_exercicio="+eh_exercicio); //+", linha="+linha);

      // Indicate that is under reading process...
      lendoGravando = false;

      ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsg_successReadFile")); // The file content was successfully read!
      ihanoiPanel.setContent(strContent.toString());
      // After returns to 'processReadFile(...)', 'iHanoiPanel' will use 'TowersPanel.setMovements(String)' to internalize the code as movements

      if (lista) System.out.println("ProcessReadWrite.readFromFile(...): " + countLines + " lines read");

      // here: readFromFile(iHanoiPanel ihanoiPanel, String fileName, String fileDirectory)
      return 1;

    } catch (IOException e) {
      String msg = ResourceReader.read("iuRWerr_readFile"); // Error when trying to read file! 
      ihanoiPanel.setTextTmsg(msg + ": " + fileName);
      System.out.println("ProcessReadWrite.readFromFile: Error when trying to read file " + fileName + " " + e.toString());
      System.out.println(" - File name = " + arquivo);
      // e.printStackTrace();
      }
    return -2;
    } // public static int readFromFile(iHanoiPanel ihanoiPanel, String fileName, String fileDirectory)


  // Read a file from hard disc (not used when "applet")
  // Called by: ihanoi/iHanoiPanel.java: public void actionPerformed(ActionEvent event) - it uses 'ihanoi/WindowFileDialog.java'
  public static int processReadFile (iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame) {
    String strLastFileRecorded = ihanoi.getLastFileRecorded();
    WindowFileDialog fd = new WindowFileDialog(frame); // bellow is set filter name: "*.ihn" or "*.txt"
    if (strLastFileRecorded == null) {
      fd.setFile("*.ihn");
      }
    else
      fd.setFile(strLastFileRecorded); //System.out.println("IGeom 1: "+strLastFileRecorded);

    // Any class (here extending Applet) that implements FilenameFilter - it must provides 'boolean accept(java.io.File,java.lang.String)'
    // fd.setFilenameFilter(ihanoi);
    fd.setFilenameFilter(new classFileNameFilter()); // filter name: only files ending with '*.ihn' are listed

    fd.setVisible(true);
    if (fd.getFile() != null) {
      String fileName = fd.getFile();
      // readFromFile(iHanoiPanel ihanoiPanel, String fileName, String fileDirectory)
      return readFromFile(ihanoiPanel, fileName, fd.getDirectory()); // if the file is not local, then use directory to get it
      }
    return -1;
    } // public static int processReadFile(Frame frame, iHanoiPanel ihanoiPanel, String strLastFileRecorded)

  // Return: 1 => OK; 2 => canceled; -1 => error
  public static int processWriteFile (iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame, String strContent) {
    String strLastFileRecorded = ihanoi.getLastFileRecorded();
    String strFileNameW = ihanoiPanel.getFileNameWrite();
    if (strFileNameW==null || strFileNameW=="" || strFileNameW.length()<1)
      strFileNameW = "ihanoi.ihn"; // default name...
    //try{String str=""; System.out.println(str.charAt(3));}catch(Exception e){e.printStackTrace();}

    WindowFileDialog fd = new WindowFileDialog(frame, ResourceReader.read("iuMsgRWrecAs"), strFileNameW, "ihn", FileDialog.SAVE); //"Gravar como..."
    fd.setFile(strFileNameW); // nome "inicial" do arquivo
    fd.setDirectory(".");

    // fd.setFilenameFilter(ihanoi); // list only files with extensions '*.ihn' and...
    fd.setFilenameFilter(new classFileNameFilter()); // list only files with extensions '*.ihn' and...

    fd.setVisible(true);
    if (fd.getFile() == null || fd.getFile().equals("") // clicou cancela => cancelar
        || fd.getDirectory().equals(".")) {             // fechou janela  => cancelar
       ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsgRWrecCanceled")); // The file register was canceled!
       System.out.println(ResourceReader.read("iuMsgRWrecCanceled"));
       return 2;
       }

    // After the file name chossen, the 'WindowFileDialog' reach this point
    strFileNameW = fd.getDirectory() + fd.getFile();
    WindowFileExists.setNome(strFileNameW); // Important: this set the file name (to be registered)
    File arq = new File(strFileNameW);
    boolean gravou = true;
    if (arq.exists()) {
       // Last parameter of WindowFileExists: 0 -> IHN; 1 -> TXT; 2 -> HTML; 3 -> PS; 4 -> GIF
       WindowFileExists jan_arq = new WindowFileExists(ihanoi, "\"" + arq.getName() + "\" " + ResourceReader.read("iuMsgRWfileExists"), 0);
       jan_arq.focusOK(); // coloquei no WindowFileExists o "grave(strFileNameW)"
       if (jan_arq.eh_cancela()) { // cancel: clicked button "cancel"
          gravou = false;
          ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsgRWcanceld") + " (" + strFileNameW + ")"); // "Canceled the file registration!"
          }
       else  {
         ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsgRWregistering") + " " + strFileNameW); // "Gravando o arquivo "
         FileUtilities.storeFile(strFileNameW, strContent, "ProcessReadWrite.processWriteFile(...)");
         }
       }
    else {
       ihanoiPanel.setTextTmsg(ResourceReader.read("iuMsgRWregistering") + " " + strFileNameW); // "Gravando o arquivo "
       FileUtilities.storeFile(strFileNameW, strContent, "ProcessReadWrite.processWriteFile(...)");
       }

    if (gravou) { // foi clicado no "cancelar"
       ihanoiPanel.setFileNameWrite(strFileNameW);
       return 1;
       // alguma mensagem adicional apos gravar o arquivo...
       // String msg1 = arq.getName() + ResourceReader.read("msgiGeomExpWebNaoEdita");
       // WarningDialog aviso = new WarningDialog(msg1); //
       }

    return -1;
    } // public static int processWriteFile(iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame, String strContent)


  } // public class ProcessReadWrite