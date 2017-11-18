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
 * @description Static utilities methods
 * 
 * @see
 *  
 * @version 2008/03/18 First version
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

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

public class StaticMethods {

  // To register information about the user
  public static String dadosUsuario () {
    // do not use with applets: results in error similar to "sun.applet.AppletSecurityException: checkpropsaccess.key"
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
       System.err.println("StaticMethods: erro ao tentar decompor "+str+": "+e);
       }
    return resp;
    } // String [] pegaItem(String str)


  // Get the parameters from command line: java ihanoi.iHanoi n=4 lang=pt
  public static String [] getiHanoiParameters (String strArgs) {
    String [] vetStr = null; // lingua, pais
    String [] vet0=null, vet1=null; // vet0: número de disco; vet1: lingua
    if (strArgs==null)
       return null;
    StringTokenizer tokens = new StringTokenizer(strArgs,", "); // "n=4 lang=pt" -> "n=4, lang=pt"
    String item=null, strNum = null, strLingua = null;
    int temNum = 0, cont = 0;
    //- System.out.println("[StaticMethods] #tokens="+tokens.countTokens());
    if (tokens.hasMoreTokens()) try { // "n=4 lang=pt"
       item = tokens.nextToken();
       vet0 = pegaItem(item.trim());
       if (vet0!=null) {
          if (vet0[0].toLowerCase().equals("n")) {
             strNum = vet0[1]; // número de discos
             //- System.out.println(" 0: n="+strNum);
             }
          else
          if (vet0[0].toLowerCase().equals("lang")) {
             strLingua = vet0[1]; // lingua || lingua_pais
             //- System.out.println(" 0: lang="+strLingua);
             }
          }
    } catch (Exception e ) { System.err.println("StaticMethods: erro em primeiro argumento "+item+": "+e); }
    if (tokens.hasMoreTokens()) try {
       item = tokens.nextToken();
       vet1 = pegaItem(item.trim());
       if (vet1!=null) {
          if (vet1[0].toLowerCase().equals("n")) {
             strNum = vet1[1]; // número de discos
             //- System.out.println(" 1: n="+strNum);
             }
          else
          if (vet1[0].toLowerCase().equals("lang")) {
             strLingua = vet1[1]; // lingua || lingua_pais
             //- System.out.println(" 1: lang="+strLingua);
             }
          }
    } catch (Exception e ) { System.err.println("StaticMethods: erro em segundo argumento "+item+": "+e); }
    if (vet0!=null && vet1!=null) // vet0.length+vet1.length;
       cont = 2;
    else
    if (vet0!=null || vet1!=null)
       cont = 1;
    if (strNum==null) {
       //- System.out.println(" - alterar strNum="+strNum+" -> "+strNum+" cont="+cont);
       cont++;
       strNum = ihanoi.iHanoi.NUM; // default number of discs
       }
    vetStr = new String[cont];
    vetStr[0] = strNum;            // number of discs
    if (cont>1) {
       vetStr[1] = strLingua;      // language
       //- System.out.println(" - devolve 0: cont="+cont+": strNum="+vetStr[0]+" strLingua="+vetStr[1]+" ");
       }
    return vetStr;
    } // String [] getiHanoiParameters(String strArgs)


  // Split parameters: 'lang=pt_BR' in String("pt","BR")
  private static String [] decompoeLang (String str) {
    // System.out.println("[B.decompoeLang] inicio: str="+str);
    String [] vetStr = null; // language, coutry
    if (str==null)
       return null;
    StringTokenizer tokens = new StringTokenizer(str,"=");
    String item;
    // System.out.println("[B.decompoeLang] #tokens="+tokens.countTokens());
    if (tokens.hasMoreTokens()) {
       item = tokens.nextToken();
       // System.out.println("[B.decompoeLang] item="+item);
       if (item!=null && item.toLowerCase().equals("lang") && tokens.hasMoreTokens()) {
          // pegou 'pt_BR'
          item = tokens.nextToken();
          // System.out.println("[B.decompoeLang] item="+item);
          if (item.length()>2) {
             // its form is: 'pt_BR'
             vetStr = new String[2];
             vetStr[0] = item.substring(0,2); // lingua
             vetStr[1] = item.substring(2,2); // pais
             // System.out.println("[B.decompoeLang] lingua="+lingua+" pais="+pais);
             return vetStr;
             }
          else {
             // its form is: 'pt'
             vetStr = new String[1];
             vetStr[0] = item.substring(0,2); // lingua
             // System.out.println("[B.decompoeLang] lingua="+lingua+" [pais="+pais+"]");
             return vetStr;
             }
          }
       else { // problem: came empty 'lang='
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


  // Decode iHanoi file (ihn)
  // Called by : ihanoi.iHanoiPanel.actionPerformed(ActionEvent event): result = StaticMethods.decodeIHN(this.strContent, this.ihanoi.getTowersPanel());
  // Called by : ihanoi.iHanoi.init(): StaticMethods.decodeIHN(strContent, this.towersPanel)
  public static int decodeIHN (String strContent, ihanoi.TowersPanel towersPanel) {
    Object obj;
    String strNumber = "", strExerc; // Content = FIRSTLINE;   // "# ihanoi: http://www.matematica.br!"     
    int ndiscs, nmovements;
    int i, de, para, tam = strContent.length();
    char ch;
    if (tam < 34) {
      System.err.println("Error: this is not an iHanoi content! (first line with " + tam + ")\n" + strContent);
      return -1;
      }
    //D System.out.println("TowersPanel.setMovements: strContent= " + strContent);

    // Get the number of discs
    i = strContent.indexOf("Discs: ", 34); // skip iHanoi identification ".: iHanoi: http://www.matematica.br :."
    if (i<0) {
       System.err.println("Error: this is not an iHanoi content! Expected 'Discs:'");
       //D System.err.println("TP.setMovements: i=" + i + ": " + strContent);
       return -2;
       }
    i += 7; // skip label "Discs:"
    while (strContent.charAt(i)==' ' || strContent.charAt(i)=='\n') i++; //

    while (strContent.charAt(i)>='0' && strContent.charAt(i)<='9') { // get number of discs
       strNumber += strContent.charAt(i); i++;
       }
    ndiscs = java.lang.Integer.parseInt(strNumber); // number of discs
    //D System.err.println("TP.setMovements: 'Discs: " + ndiscs + "'");

    // Verify if it is an exercise, in this case make the minimum number of movements (=2^n-1, n=ndiscs)
    i = strContent.indexOf("Exercise", i);
    if (i<0)
      strExerc = null;
    else
      strExerc = strContent.substring(i, i+8); // "Exercise"
    if (i<0) i = 0;
    if (strExerc!=null && strExerc.toLowerCase().equals("exercise")) { // it is an exercise
      towersPanel.setIsExercise(true); // it is exercise
      //D System.err.println("SM.decodeIHN: it is exercise"); //D
      }
    else { // if (strExerc!=null && strExerc.toLowerCase().equals("exercise"))
      towersPanel.setIsExercise(false); // it is NOT exercise       

      //D System.err.println("SM.decodeIHN: it is not exercise"); //D

      // Get the number of movements in the file or, case it is an exercise, the minimum number of movements (=2^n-1, n=ndiscs)
      i = strContent.indexOf("Size: ", i);
      if (i<0) {
         System.err.println("Error: this is not an iHanoi content! Expected 'Size:'");
         return -3;
         }
      i += 6; // jump "Size:"
      strNumber = "";
      while (strContent.charAt(i)==' ' || strContent.charAt(i)=='\n') i++; //
      while (strContent.charAt(i)>='0' && strContent.charAt(i)<='9') {
         strNumber += strContent.charAt(i); i++;
         }
      nmovements = java.lang.Integer.parseInt(strNumber); // number of movements

      i = strContent.indexOf("Movements:", i); // Attention: this string could not have ' ' after ':'
      if (i<0) i = 0;
      if (i<0) {
         System.err.println("Error: this is not an iHanoi content! Expected 'Movements:' (" + i + ")");
         // return -4;
         }

      i += 10; // jump "Movements:"
      // ATTENTION: there are difference between read a file content with file and under the Web, even using "appletviewer"
      //            locally (with "appletviewer answer_ihanoi3.html") and via Web (with "appletviewer http://localhost/ilm/answer_ihanoi3.html")
      // The difference is that locally each line ends with '\n' (ASCII=10) and under the Web, ends with ASCII=13 and after with '\n'.
      // To test:
      //  int ascii = 10; char c = (char) ascii; System.out.println(" ascii 10: " + c);
      //  ascii = 13; c = (char) ascii; System.out.println(" ascii 13: " + c);
      // Next line is to get the additional character under the Web: i,i+1 = 13,10
      if ((int)strContent.charAt(i)==13) // under Web there additional character!
        i++; // jump ASCII 13, get ASCII 10 ('\n')
      //D //System.out.println(" i=" + i + ": " + strContent.charAt(i) + " (apos 'Movements:')");

      strNumber = "";
      while (strContent.charAt(i)==' ' || strContent.charAt(i)=='\n') i++; //
      //D System.out.println(" i=" + i + ": " + strContent.charAt(i) + " (apos 'Movements:')");

      // Now read the learner movements
      Vector inverseVectorOfMovements = new Vector();
      int j = 0;
      char c;
      while (true) {

        // The last is to take care of extra character via Web - over Web jump ASCII 13, get ASCII 10 ('\n')
        while ((c=strContent.charAt(i))==' ' || c=='\n' || c==';' || (int)c==13) i++;

        //D System.out.println(" j=" + j + ": i=" + i + ": " + strContent.charAt(i) + "");
        ch = strContent.charAt(i); i++;
        de = (ch - 'A'); // 'A' -> 0; 'B' -> 1; 'C' -> 2;
        while (strContent.charAt(i)==' ' || strContent.charAt(i)=='\n' || strContent.charAt(i)==';') i++; //
        if ((int)strContent.charAt(i)==13) // under Web there additional character!
          i++; // jump ASCII 13, get ASCII 10 ('\n')
        //D System.out.println(" j=" + j + ": i=" + i + ": " + strContent.charAt(i) + "");
        ch = strContent.charAt(i); i++;
        para = (ch - 'A'); // 'A' -> 0; 'B' -> 1; 'C' -> 2;

        towersPanel.addElementVectorOfMovements(inverseVectorOfMovements, de, para); // add new movement: new Move(de, para)
        //D System.out.println(" " + j + ": " + de + " -> " + para + "\n---"); //

        j++;
        if (j == nmovements) break;
        } // while (true)

      // Invert order of movements to simulate that the learner performed by himelf the movements
      Vector vecMov = new Vector();
      towersPanel.setVectorOfMovements(vecMov);
      for (i=0; i<j; i++) {
        obj = inverseVectorOfMovements.elementAt(i);
        //D System.out.println(" - " + ((ihanoi.TowersPanel.Move)obj).de + " -> " + ((ihanoi.TowersPanel.Move)obj).para + "");
        //D System.out.println(" - " + ihanoi.TowersPanel.fromMove(obj) + " -> " + ihanoi.TowersPanel.toMove(obj) + "");
        towersPanel.addElementVectorOfMovements(obj); // add new movement (TowersPanel.java: class Move)
        }

      } // else if (strExerc!=null && strExerc.toLowerCase().equals("exercise"))

    towersPanel.Ndiscos = ndiscs;
    //__ towersPanel.setDiscs(ndiscs); // set "towersPanel.numberOfDiscs", "disco=new Disco[ndiscs]" and "cor=new Color[MaxDiscos]"
    //D System.err.println("StaticMethods.decodeIHN: 'Discs: " + ndiscs + "' => " + ndiscs);
    //__ towersPanel.ihanoiPanel.updateDiscs(true, false); // true => do not clear the "Vector vectorOfMovements"

    return 1;
    } // public static String decodeIHN(String strContent, String [] str, TowersPanel towersPanel)


  }
