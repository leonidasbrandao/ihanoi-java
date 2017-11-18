/*
 * LInE - IME - USP
 * @author Leo^nidas de Oliveira Branda~o
 * @description Internationalization using 'iHanoi.properties'
 */

package ihanoi;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class ResourceReader {
  
  private static ResourceBundle rb;
  

  /// The default constructor uses "locale" standard: pt
  public ResourceReader (String lang) {
    Locale loc=null;
    String lingua="", pais="";
    try {
      lingua = lang;
      if (lang!=null && lang.length()>3) {
         lingua = lang.substring(0,2);
         pais   = lang.substring(2,2);
         }
      loc = new Locale(lingua,pais);
      rb = ResourceBundle.getBundle("ihanoi/iHanoi",loc);
    } catch (Exception e) {
      // e.printStackTrace();
      try {
        loc = new Locale("pt","");
        rb = ResourceBundle.getBundle("ihanoi/iHanoi",loc);
      } catch (Exception e1) {
        System.err.println("ResourceReader: erro em lang default \"pt\": "+e1);
        e1.printStackTrace();
        loc = new Locale("","");
        rb = ResourceBundle.getBundle("ihanoi/iHanoi",loc);
        }
      }
    }
  

  /// Alternative constructor used to test internationalization tool
  // @param i
  public ResourceReader (int i) {
    Locale l = new Locale("en", "US");
    rb = ResourceBundle.getBundle("ihanoi.resource/iHanoi", l);
    }
  
  // Read items from "iHanoi*.properties"
  public static String read (String value) {
    try {
      return rb.getString(value);
    } catch (Exception e) {
      System.err.println("ResourceReader: erro \"read\" "+value+""+rb+": "+e);
      // e.printStackTrace();
      return value;
      }
    }


  // Read items from "iHanoi*.properties"
  // Process message values in properties like this: "File $ARQ$ successfully registered"
  public static String readVar (String strTexto, String strVar, String [] strValor) {
    try {
      // get the string with "$ARQ" returned by "Bundle.msg()"
      StringTokenizer strToken = new StringTokenizer(ResourceReader.read(strTexto), "$");
      String strFinal = "";
      int prox = 0;
      while (strToken.hasMoreTokens()) {
          String strAux = strToken.nextToken();
          if (strAux.equals(strVar)) {
              // if token has the same value in "strVar", change it by the content in "strValor[]"
              String str = strValor != null ? strValor[prox++] : "";
              strFinal = strFinal+str; // concatenate with variable value (exchange "strVal" by "strValor")
              }
          else {
              strFinal = strFinal+strAux; // concatenate with the text
              }
          }

      return strFinal;

    } catch (Exception e) {
      System.err.println("ResourceReader: erro \"readVar\" "+strVar+""+rb+": "+e);
      // e.printStackTrace();
      return strVar;
      }

    } //  static String readVar(String strTexto, String strVar, String [] strValor)

  }
