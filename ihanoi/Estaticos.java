/*
 * @author Leônidas de Oliveira Brandão
 * @description Métodos estáticos
 */

package ihanoi;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Estaticos {

  // This method could nota be used when "applet"...
  public static String dadosUsuario () {
    String dados_str = "";
    try {
      dados_str = "[" + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
      //InetAddress inetaddress; // se quiser pegar também a máquina de quem gravou
      // try {
      //     inetaddress = InetAddress.getLocalHost();
      // } catch (java.net.UnknownHostException unknownhostexception) {
      //     inetaddress = null;
      // }
      dados_str +=  "; "+ System.getProperty("user.name") // + "@" + inetaddress);
                +   "]";
    } catch (java.lang.Exception e) {
       dados_str += "]";
       // System.err.println("GL: "+e);
       }
    return dados_str;
    }

  public static String [] pegaItem (String str) {
    StringTokenizer tokens = new StringTokenizer(str,"="); // "n=4" ou "lang=pt"
    String item0=null, item1=null;
    String [] resp = null;
    int cont=0;
    try {
    if (tokens.hasMoreTokens()) {
       item0 = tokens.nextToken();
       cont++;
       }
    if (tokens.hasMoreTokens()) {
       if (cont==0)
          item0 = tokens.nextToken();
       else
          item1 = tokens.nextToken();
       cont++;
       }
    if (cont==0)
       return null;
    resp = new String[cont];
    resp[0] = item0;
    if (cont>1)
       resp[1] = item1;
    } catch (Exception e) {
       System.err.println("Estaticos: erro ao tentar decompor "+str+": "+e);
       }
    return resp;
    } // String [] pegaItem(String str)


  // Pega parametros: java ihanoi.iHanoi n=4 lang=pt
  public static String [] parametros (String strArgs) {
    String [] vetStr = null; // lingua, pais
    String [] vet0=null, vet1=null; // vet0: number of discs; vet1: language
    if (strArgs==null)
       return null;
    StringTokenizer tokens = new StringTokenizer(strArgs,", "); // "n=4 lang=pt" -> "n=4, lang=pt"
    String item=null, strNum = null, strLingua = null;
    int temNum = 0, cont = 0;
    if (tokens.hasMoreTokens()) try { // "n=4 lang=pt"
       item = tokens.nextToken();
       vet0 = pegaItem(item.trim());
       if (vet0!=null) {
          if (vet0[0].toLowerCase().equals("n")) {
             strNum = vet0[1]; // number of discs
             }
          else
          if (vet0[0].toLowerCase().equals("lang")) {
             strLingua = vet0[1]; // lingua || lingua_pais
             }
          }
    } catch (Exception e ) { System.err.println("Estaticos: erro em primeiro argumento "+item+": "+e); }
    if (tokens.hasMoreTokens()) try {
       item = tokens.nextToken();
       vet1 = pegaItem(item.trim());
       if (vet1!=null) {
          if (vet1[0].toLowerCase().equals("n")) {
             strNum = vet1[1]; // number of discs
             }
          else
          if (vet1[0].toLowerCase().equals("lang")) {
             strLingua = vet1[1]; // lingua || lingua_pais
             }
          }
    } catch (Exception e ) { System.err.println("Estaticos: erro em segundo argumento "+item+": "+e); }
    if (vet0!=null && vet1!=null) // vet0.length+vet1.length;
       cont = 2;
    else
    if (vet0!=null || vet1!=null)
       cont = 1;
    if (strNum==null) {
       cont++;
       strNum = iHanoi.NUM; // number of discs: default
       }
    vetStr = new String[cont];
    vetStr[0] = strNum;                  // number of discs
    if (cont>1) {
       vetStr[1] = strLingua;            // language
       }
    return vetStr;
    } // String [] parametros(String strArgs)


  // Split: 'lang=pt_BR' em String("pt","BR")
  private static String [] decompoeLang (String str) {
    String [] vetStr = null; // lingua, pais
    if (str==null)
       return null;
    StringTokenizer tokens = new StringTokenizer(str,"=");
    String item;
    if (tokens.hasMoreTokens()) {
       item = tokens.nextToken();
       if (item!=null && item.toLowerCase().equals("lang") && tokens.hasMoreTokens()) {
          // get 'pt_BR'
          item = tokens.nextToken();
          if (item.length()>2) {
             // It has the form: 'pt_BR'
             vetStr = new String[2];
             vetStr[0] = item.substring(0,2); // language
             vetStr[1] = item.substring(2,2); // country
             return vetStr;
             }
          else {
             // It has the form: 'pt'
             vetStr = new String[1];
             vetStr[0] = item.substring(0,2); // language
             return vetStr;
             }
          }
       else { // problema: veio 'lang='
          return null; // new String[2];
          }
       }
    return null; // new String[2];
    } // boolean decompoeLang(String str)

  public static String listaStrings (String [] str) {
    String strR = "";
    if (str==null)
       return strR;
    int tam = str.length;
    if (tam==0)
       return strR;
    strR = str[0];
    for (int i=1; i<tam; i++)
        strR += ", "+str[i];
    return strR;
    }

  }
