/*
 * iHanoi - Interactive/Internet Tangram: http://www.matematica.br/ihanoi
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br
 *
 * @author Leo^nidas O. Branda~o
 * 
 * @description Utilities to manage files (from hard disc and from Web)
 * 
 * @see 
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


import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader; //
import java.io.InputStream;
import java.io.InputStreamReader; //

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

// import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;

// ihanoi/util/FileUtilities.java:303: warning: save(java.io.OutputStream,java.lang.String) in java.util.Properties has been deprecated
//        manifestFile.save(new FileOutputStream(strFileName), header);


public class FileUtilities {

  // "Applet" use 'this.getParameter("iLM_PARAM_encoding");' to change UTF-8 x ISO-8859-1 ?
  public static String INPUT_ENC = "ISO8859-1"; // "UTF-8" "ISO8859-1"
  public static String OUTPUT_ENC = "ISO8859-1"; // "UTF-8" "ISO8859-1"

  public static final String FIRSTLINE = "# ihanoi: http://www.matematica.br!";

  // Store the iHanoi movements in a file named 'fName'
  public static void storeFile (String fName, String strContent, String msgTracking) {
    try {
      // para nao dar problema de acento ISO -> UTF...
      // java -Dfile.encoding=iso-8859-1 igeom.IGeom s2.geo
      java.io.FileOutputStream fos = new java.io.FileOutputStream(fName);
      java.io.Writer out = new java.io.OutputStreamWriter(fos, "ISO8859_1"); // OutputStreamWriter(fos, "UTF8");
      out.write(strContent);
      out.close();
    } catch (Exception e) {
      System.err.println("Error: trying to write the file " + fName + " (" + msgTracking + "): " + e);
      e.printStackTrace();
      }
    }


  // ----------------------------------------
  // Gravar arquivo

  // Called by:
  public static void gravar (String nome, String arquivo_str, String msg_org) {
      try { // para não dar problema de acento ISO -> UTF...
          // java -Dfile.encoding=iso-8859-1 igeom.IGeom s2.geo
          FileOutputStream fos = new FileOutputStream(nome);
          Writer out = new OutputStreamWriter(fos, OUTPUT_ENC); //
          out.write(arquivo_str);
          out.close();
       } catch (Exception e) {
          System.err.println("Erro: ao tentar escrever arquivo "+nome+" ("+msg_org+"): "+e); // e.printStackTrace();
          }
       }
  // ----------------------------------------

  // ----------------------------------------
  // iLM: allow to read file through URL
  //DEBUG: iLM: public static String staticDebugURL = "";
  public static String readFileThroughURL (java.applet.Applet applet, String strURL) {
   java.io.InputStream inputStream = null;
   java.net.URL endURL = null;
   java.lang.StringBuffer stringbuffer = null;
   try { //

      if (strURL==null)
         return "";
      int sizeOf = strURL.length();
      if (sizeOf>FIRSTLINE.length())
         sizeOf = FIRSTLINE.length();

      if (strURL.substring(0,sizeOf).equals(FileUtilities.FIRSTLINE)) { // first iGeom protocol: text inside tag 'igeom'
         System.out.println("This is a tag with file content! (" + strURL.substring(0,sizeOf) + "...)");
         return strURL;
         }

     endURL = new java.net.URL(strURL); // se for URL

     //DEBUG: iLM: staticDebugURL = "\n1: " + endURL;
   } catch (java.net.MalformedURLException e) {
     try { // se falhou, tente completar com endereço base do applet e completar com nome de arquivo
       // applet.getDocumentBase().toString() = "http://localhost/igeom/exemplo.html"
       strURL = applet.getCodeBase().toString() + strURL.trim(); //
       // Nao esta funcionando para 'navegador' em sistema de arquivo
       // (para 'exemplo-exercicio-professor.html' => entra 'file:/home/.../exemplo-exercicio-professor.geo')
       // if (strURL!=null && strURL.length()>4 && !strURL.trim().substring(0,4).equals("http")) // estas 3 linhas nao resolvem...
       // if (strURL.charAt(0)=='/')
       //   strURL = "file://" + strURL.trim(); // para caso de aplicativo
       System.out.println("Is it a valid URL? ("+strURL+")");
       endURL = new java.net.URL(strURL); // se for URL
       //DEBUG: iLM: staticDebugURL += "2: " + endURL;
     } catch (java.net.MalformedURLException ue) {
       System.err.println("Error: in utilities: reading URL 1: applet="+applet+", "+strURL+": "+ue);
       //DEBUG: iLM: staticDebugURL += "\n2: ERROR " + ue;
       return "";
       }
     }
   try {
     // System.out.println("Is it a valid URL = "+endURL + "?");
     inputStream = endURL.openStream();
     //DEBUG: iLM: System.out.println("Sistema.java: readFileThroughURL: endURL="+endURL+", inputStream="+inputStream);
     java.io.InputStreamReader inputstreamreader = new java.io.InputStreamReader(inputStream, FileUtilities.INPUT_ENC);
     //DEBUG: iLM: System.out.println("Sistema.java: readFileThroughURL: inputstreamreader="+inputstreamreader);
     java.io.StringWriter stringwriter = new java.io.StringWriter();
     int i = 8192;
     char [] cs = new char[i];
     int i_11_, count = 0;
     try {
       for (;;) {
         count++; //Debug
         i_11_ = inputstreamreader.read(cs, 0, i);
         if (i_11_ == -1)
           break;
         stringwriter.write(cs, 0, i_11_);
         }
      stringwriter.close();
      inputStream.close();
     } catch (java.io.IOException ioexception) {
       System.err.println("Erro: leitura URL 2: "+strURL+": " + ioexception);
       //DEBUG: iLM: staticDebugURL += "\n3: ERROR " + ioexception;
       }
     String strResponse = stringwriter.toString();
     //DEBUG: iLM: staticDebugURL += "\n4: count="+count+":\nSize: " + strResponse.length();

     int sizeOf = (strResponse==null ? 0 :  strResponse.length());
     System.out.println("System utilities: read from URL: bytes read = " + sizeOf + " (" + count + ")");
     //T if (strResponse==null || strResponse=="" || strResponse.length()==0) {
     //T    applet.getAppletContext().showDocument(endURL);
     //T    System.out.println("Sistema.java: readFileThroughURL: showDocument("+endURL+"): "+strResponse);
     //T    }
     //T else System.out.println("Sistema.java: readFileThroughURL: #+strResponse="+strResponse.length());

     return strResponse;

   } catch (java.io.IOException ioe) {
     System.out.println("Erro: leitura URL: "+strURL+": "+ioe.toString());
     //DEBUG: iLM: ioe.printStackTrace();
     //DEBUG: iLM: staticDebugURL += "\n4: ERROR " + ioe;
     }
   return "";
   } // public static String readFileThroughURL(java.applet.Applet applet, String strURL)


  // Read
  public static String [] readLines (String nome, String msg_org) {
    return generalFileRead(nome,msg_org,true);
    }

  public static String fileRead (String nome, String msg_org) {
    String [] str = generalFileRead(nome,msg_org,false);
    return str[0];
    }

  public static String []  generalFileRead (String nome, String msg_org, boolean devolve_linhas) {
    String str_arq = "", linha;
    Vector vet_linhas = null;
    String [] str_linhas = null;
    int cont_linhas = 0;
    if (devolve_linhas) vet_linhas = new Vector();
    try {
      BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(nome), INPUT_ENC));
      while (( (linha = buffer.readLine())!=null)) { //
        cont_linhas++;
        if (str_arq!="") str_arq += "\n";
        if (devolve_linhas)
           vet_linhas.addElement(linha);
        else
          str_arq += linha;
        // System.out.println(" - "+linha); //
        }
      buffer.close();
      }
    catch (Exception e) {
      System.err.println("Erro: na leitura do arquivo " + nome + " " + msg_org + ": " + e.toString());
      return null;
      }
    if (devolve_linhas) try {
       str_linhas = new String[cont_linhas];
       for (int i=0; i<cont_linhas; i++)
           str_linhas[i] = (String)vet_linhas.elementAt(i);
        return str_linhas;
       } catch (Exception e) { e.printStackTrace(); return str_linhas; }
    str_linhas = new String[1];
    str_linhas[0] = str_arq;
    return str_linhas;
    }
  // ----------------------------------------



  // ----------------------------------------
  // User defined Properties: begin
  public static Properties getPropertiesFromFile (String strFileName) {
    // File file = getFile(strFileName);
    // if (file==null) {
    //   // System.err.println("Error: couldn't find the file '" + strFileName + "'!");
    //   return null;
    //   }

    Properties manifestFile = new Properties();
    try {
      manifestFile.load(new FileInputStream(strFileName));
    } catch (java.io.IOException fnfe) {
      System.err.println("Error: couldn't find the file '" + strFileName + "': " + fnfe.toString());
      return null;
      }

    //DEBUG: System.out.println("Sistema.getPropertiesFromFile: " + strFileName);

    // Get a list of (key,value) from the manifest.
    // Use an Enumeration to put them into a Vector?
    //V Vector filelistNames = new Vector();
    //V Vector filelistValues = new Vector();
    Enumeration names = manifestFile.propertyNames();
    int count = 0;
    while (names.hasMoreElements()) {
       String strItem = (String) names.nextElement();
       if (!strItem.startsWith("#")) {
          String strValue = manifestFile.getProperty(strItem);
          //V filelistNames.addElement(strItem);
          //V filelistValues.addElement(strValue);
          //DEBUG: System.out.println(" - " + count + ": " + strItem + " = " + strValue);
          count++;
          }
       else
       if (strItem.startsWith("# created:")) {
          String strDate = strItem.substring(10, strItem.length()).trim();
          manifestFile.put("created", strDate);
          //DEBUG: System.out.println(" + " + count + ": " + strItem + " = " + strDate);
          count++;
          }
       else {
	  String strValue = manifestFile.getProperty(strItem);
          //DEBUG: System.out.println(" * " + count + ": " + strItem + " = " + strValue);
          count++;
          }
       }
    return manifestFile;
    }

  public static void setPropertiesFromFile (String strFileName, Properties manifestFile, String header) {
    try {
      manifestFile.save(new FileOutputStream(strFileName), header);
    } catch (java.io.IOException fnfe) {
      System.err.println("Error: couldn't save the file '" + strFileName + "': " + fnfe.toString());
      return;
      }
    // System.out.println("Sistema.setPropertiesFromFile: " + strFileName);
    }


  } // public class FileUtilities
