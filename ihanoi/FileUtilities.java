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

package ihanoi;

public class FileUtilities {
   
   
  // Store the iHanoi movements in a file named 'fName'
  public static void storeFile (String fName, String strContent, String msgTracking) {
    try {
      // para nao dar problema de acento ISO -> UTF...
      // java -Dfile.encoding=iso-8859-1 ihanoi.iHanoi
      java.io.FileOutputStream fos = new java.io.FileOutputStream(fName);
      java.io.Writer out = new java.io.OutputStreamWriter(fos, "ISO8859_1"); // OutputStreamWriter(fos, "UTF8");
      out.write(strContent);
      out.close();
    } catch (Exception e) {
      System.err.println("Error: trying to write the file " + fName + " (" + msgTracking + "): " + e);
      e.printStackTrace();
      }
    }

  }
