/*
 * iHanoi : Problema das Torres de Hano'i na Internet
 * iMath: http://www.matematica.br
 * LInE : "L"aboratory of "I"nformatics i"n" "E"ducation : http://line.ime.usp ; http://www.usp.br/line
 * 
 * @author Leo^nidas de Oliveira Branda~o
 * 
 * @description Programa interativo Web para o Problema das Torres de Hano'i - apresentado por Edouard Lucas, em 1883.
 * 
 * @version
 *  2017/10/01 : 3.1.0
 *  2017/09/29 : 3.0.3 : fixed write file ("Discs: 0" alwary!) - ihanoi/TowersPanel.java : getMovements()
 *  2016/09/13 : 3.0.1 : fixed some missing messages
 *  2016/08/01 : 3.0   : new features to read/write file, including iHanoi use as iLM; fixed the message inverted ("n discs with m movements" - "m" and "n" were inverted)
 *  fevereiro 2011 / setembro 2002 / maio 2008 (internacionalizacao) / junho 2002 / setembro 2001 / outubro 2001 
 */

// ---------------------------------------------------------------------------------------------------- //
// iLM features : parameters and methods to allow to integrate iHanoi as an interactive Learning Module 
//                to be used with LMS using JavaScript
// JavaScript:
//  var strAnswer = document.applets[0].getAnswer();
//      var value = document.applets[0].getEvaluation();
//
// iHanoi integrated with LMS (iHanoi as an iLM)
// - public float getEvaluation(): get the number of movements NM evaluating it (0 if the learner do not moved all discs to B or C), NM/(2^n-1) if moved to B, and NM/(2^n-1) moved to C
// - public String getAnswer()   : get the movements performed by the learner using a simple format (iHanoi format), if the learner did nothing, returns -1
// - public void sendAnswer(java.net.URL codebase): send to the URL the learner movements


package ihanoi;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color; //
import java.awt.Dimension;
import java.awt.Font; //
import java.awt.Frame; //
import java.awt.Graphics;
import java.awt.GridLayout; // java/awt/GridLayout
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel; //
import java.awt.TextField;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Vector;

import ihanoi.util.StaticMethods;
import ihanoi.util.FileUtilities;

/*
 *  Main class principal: it works as application and "applet"
 */
public class iHanoi extends Applet {

  public static final String VERSION  =  "3.1.0"; //
    // 3.1.0 [2017/10/01] several changes, fixed HTML reading file e about
    // 3.0.3 [2017/09/29] fixed write file ("Discs: 0" alwary!) - ihanoi/TowersPanel.java : getMovements()
    // 3.0.2 [2016/09/19] fixed treatment to extra char over the Web (eol in file ends with ASCII 10, eol over Web ends with 13 and 10)
    // 3.0.1 [2016/09/13] fixed some missing messages (like 'msgMenuAbrir' changed to the new 'wfdMsgOpen')
    // 3.0 : [2016/08/01] new version with read and write option, besides iHanoi now work as an iLM (interactive Learning Module)
    // 2.5 : [2016/07/24] fixed: movements messages and discs in succes case; initial presentation of "Choice" for discs number
    // 2.4 : [2011/03/23] pequenos acertos (gerenciar corretamente atualizacao de discos; move automatica; lancar janela Sobre)
    // 2.3 : [2011/03/10] permitir 'choice' p/ num. discos mesmo em applet com automatico; parar durante mov. automatica
    // 2.2 : [2011/03/04] acerto de dimensao e posicionamento para aparece corretamente o 'choice' p/ num. discos
    // 2.1 : [2008/10/19 - 11/08] implementacao de recursos para "mov. automaticos" (faltam "n=20", "2 cores")
    // 2.0 : [2008/05/18] implementacao de "desfazer ultimo movimento" e "rever todos movimentos" (inclusive anota em
    //                   janela todos os movimentos efetuados)
    // ?.? : [2008/05/01-05] arrumado fechamento de janelas (sobre e frame); intenacionalizacao; mensagens
    // 1.6 : [2002/09/10] arrumado o erro que impedia rodar em alguns navegadores (problema de acesso a classes)
    // 1.5 : [2002/09/09] correcoes de atribuicoes com "null", acrescimo de frame p/ "sobre"

  private static final int FT1 = 10, FT2 = 11, FT3 = 12, FT4 = 13, FT5 = 14;

  public final static Color corEarth = new Color(116, 13, 13), // TowersPanel.java: color to base ground (earth)
                            corH = new Color(60, 90, 150),     // ihanoi/JanelaTexto.java: color to the dialog window background
                            corF = new Color(90, 120, 150),    //
                            corFP = new Color(40, 90, 200);    // color to the primary menu background
  public final static Color fundo_azul_claro = new Color( 86, 139, 175), // WarningDialog
                            fundoAzulEscuro1 = new Color( 20,  90, 150), // WindowFileDialog.java
                            fundoTopo        = Color.white;              // WindowFileDialog.java
  public final static Font FONTE1 = new Font("Helvetica",  Font.PLAIN, FT2),
                           ftPlain8  = new Font("Arial", Font.PLAIN, FT1), // do not use font size 8 or 10...
                           ftPlain10 = new Font("Arial", Font.PLAIN, FT2),
                           ftPlain11 = new Font("Helvetica", Font.PLAIN, FT3),
                           ftPlain12 = new Font("Helvetica", Font.PLAIN, FT4);

  public static final int LARG_SOBRE=590, ALT_SOBRE=415; // width and height of the window About
  public static final int SOBRE_LNH=26, SOBRE_COL=72;    // ihanoi/DialogSobre.java : numero de linha e colunas em jan. Sobre
  public static final Font FONTE_SOBRE = new Font("Helvetica", Font.PLAIN, FT2);

  static final int LARGURA =  750;
  static final int ALTURA  =  475;

  public static final String NUM = "4"; // default number of discs (used in "ihanoi/util/StaticMethods.getiHanoiParameters(String)")

  static String SOBRE; // = montaSobre();

  private static Frame fHanoi = null;
  public Frame getFrame () { return fHanoi; } // ihanoi/WarningDialog.java: constructors

  // Window "About"
  Frame JanelaSobre = null;
  DialogSobre dialogSobre = null;

  public iHanoiPanel ihanoiPanel; // main panel: it includes all other graphical components

  private TowersPanel towersPanel = new TowersPanel(); //__
  public  TowersPanel getTowersPanel () { return towersPanel; } // ihanoi.iHanoiPanel.actionPerformed(ActionEvent event)

  private boolean ehApplet;
  // Color corFundo = new Color(90, 120, 150);


  /// iLM methods --- initial
  //
  // iLM_PARAM_Assignment : activity Web address
  //  @see ihanoi/util/FileUtilities.java : public static String readFileThroughURL(java.applet.Applet applet, String strURL)
  // iLM_PARAM_Feedback: "false" => do not present to the learner the message indicating the evaluation result
  // iLM_PARAM_Authoring: "true" => teacher authoring
  // iLM_PARAM_ServerToGetAnswerURL - http://www.matematica.br/descricao-ima.txt
  // iLM_PARAM_encoding: UTF-8 x ISO-8859-1 ?

  private float evaluation = -1;
  private int numberOfDiscs = -1; // defined by 'TowersPanel.java: public String getMovements()'
  private int numberOfMovements = -1; // defined by 'TowersPanel.java: public String getMovements()'
  public void setEvaluation (int nod, int nom, float eval) { // Called by: 'TowersPanel.java: public String getMovements()'
    this.evaluation = eval;
    this.numberOfDiscs = nod;
    this.numberOfMovements = nom;
    }

  // Get the movements performed by the learner using a simple format (iHanoi format), if the learner did nothing, returns -1
  public String getAnswer () {
    String strContents = towersPanel.getMovements();
    System.out.println("getAnswer(): numberOfDiscs=" + numberOfDiscs + ", numberOfMovements=" + numberOfMovements + ", evaluation=" + evaluation);
    if (strContents==null || strContents=="") {
      evaluation = -1; // just in case...
      return "-1"; // no answer!
      }
    return strContents;
    }

  // Get the number of movements NM evaluating it (0 if the learner do not moved all discs to B or C), NM/(2^n-1) if moved to B, and NM/(2^n-1) moved to C
  // It must be first called method 'getAnswer()'!
  public float getEvaluation () {
    // if (valorExercicio==-111) getAnswer(); return valorExercicio;
    return evaluation; // do not movements
    }
  /// iLM methods --- final

  public String getFileNameRead () { // called by: ihanoi.util.ProcessReadWrite.readFromFile(iHanoiPanel,String,String)
    return ihanoiPanel.getFileNameRead(); // calls: iHanoiPanel.getFileNameRead(): strFileNameRead
    }
  public String getLastFileRecorded () { // called by: ihanoi.util.ProcessReadWrite.
    return ihanoiPanel.getLastFileRecorded(); // calls: iHanoiPanel.getLastFileRecordedd(): strLastFileRecorded
    }   


  // Used to present information about iHanoi and the legend of Towers of Hanoi
  private String montaSobre () { // define "SOBRE"
    String Sfim =""; // new String("");
    String strB = "         ";
    // Sfim += strB + ResourceReader.read("sobre[0]") + "\n";
    // Sfim += strB + ResourceReader.read("sobre[1]") + "\n";
    for (int i=0; i<19; i++ )
        Sfim += ResourceReader.read("sobre[" + i + "]") + "\n";
    Sfim += "" + strB + "\n" + ResourceReader.read("autor") + "\n";
    Sfim += "" + strB + ResourceReader.read("imatica") + "\n";
    Sfim += "" + strB + ResourceReader.read("LInE");
    return Sfim;
    }

  // Change properties of TowersPanel
  private void setTowerPanel (int numberD, iHanoi ihanoi, iHanoiPanel ihanoiPanel) {
    this.towersPanel.setTowerPanel(numberD, ihanoi, ihanoiPanel);
    // define: this.towersPanel.numberOfDiscs = numberD; this.towersPanel.ihanoiPanel = ihanoiPanel; this.towersPanel.ihanoi = ihanoi
    }

  private void montaJanela (String strDisco, String strLang, Frame fHanoiMJ) {
    int num;

    ResourceReader rr = new ResourceReader(strLang);
    try {
      if (strDisco == null)
         num = Integer.parseInt(NUM);
      else
         num = Integer.parseInt(strDisco); 
    } catch (Exception e) {
       System.out.println("Erro: " + e);
       num = 4;
       }

    ihanoiPanel = new iHanoiPanel(fHanoiMJ);

    this.towersPanel.setTowerPanel(num, this, this.ihanoiPanel); //___

    ihanoiPanel.criaDiscosCores(num, this.towersPanel); //SA
  
    SOBRE = montaSobre(); // build the String with the text "About"

    // Default Layout: BorderLayout
    ihanoiPanel.montaPainel(num, dialogSobre, this, fHanoiMJ, ehApplet); // add graphical components, including 'this.towersPanel'
    this.setLayout(new java.awt.BorderLayout()); //layout
    this.add(ihanoiPanel, java.awt.BorderLayout.CENTER);
    } // void montaJanela(String strDisco, String strLang, Frame fHanoiMJ)


  // Starting point when iHanoi is used under Web browser ("applet") 
  public void init () {
    String strURL, strContent;
    String strNumD;
    String strLangD = getParameter("lang"); // language
    String strAutom = getParameter("auto"); // allow or not the button to start automatic movements

    System.out.println(".: iHanoi: http://www.matematica.br :."); //
    System.out.println("Version: " + VERSION);

    strNumD = getParameter("discs"); // number of discs
    if (strNumD==null)
      strNumD = getParameter("disco"); // number of discs

    if (strAutom!=null && strAutom.equals("true"))
      iHanoiPanel.AUTOMATICO = true;

    fHanoi = new Frame(".: iHanoi: http://www.matematica.br :.");
    ehApplet = true;
    //__ montaJanela(strNumD,strLangD,fHanoi);

    strURL = getParameter("iLM_PARAM_Assignment"); // URL file contents
    if (strURL==null || strURL.length()<3)
      strURL = getParameter("iLM_PARAM_AssignmentURL"); // URL file contents

    if (strURL!=null) try { // read content from URL
      strContent = FileUtilities.readFileThroughURL(this, strURL); // iLM protocol 2.0
      StaticMethods.decodeIHN(strContent, this.towersPanel);

      if (this.towersPanel!=null && this.towersPanel.Ndiscos>0) {
        montaJanela(this.towersPanel.Ndiscos + "", strLangD, fHanoi);
        }
      else
        montaJanela(strNumD,strLangD,fHanoi);

      if (strContent!=null && strContent.length()>5) {
        this.towersPanel.setMovements(strContent); // load movements
        }
      } catch (Exception expt) {
        System.err.println("Error: when reading iHanoi content from 'iLM_PARAM_AssignmentURL': " + strURL);
        expt.printStackTrace();
        }
    else
      montaJanela(strNumD,strLangD,fHanoi);

    } // public void init()


  public void destroy () {
    ihanoiPanel = null; //
    }
  
  public void start () {
    this.setVisible(true);
    }
  
  public void stop () { // usuario visita outra pagina
    this.setVisible(false);
    }
  
  public static void main (String args[ ]) {
    System.out.println(".: iHanoi: http://www.matematica.br :."); //
    System.out.println("Version: " + VERSION);
    String strParam = StaticMethods.listaStrings(args);
    fHanoi = new Frame(".: iHanoi: http://www.matematica.br :.");
    fHanoi.addWindowListener(new WindowAdapter() { 
        public void windowClosing(WindowEvent e) {
           System.exit(0); }
        } );

    iHanoi hanoi = new iHanoi();
    hanoi.setSize(LARGURA, ALTURA); //

    hanoi.ehApplet = false;

    fHanoi.setSize(LARGURA, ALTURA); //
    fHanoi.setLayout(new java.awt.BorderLayout()); //layout
    fHanoi.add(hanoi, java.awt.BorderLayout.CENTER);

    fHanoi.setVisible(true); // Java 1.1: hanoi.show();

    // hanoi.init();
    String [] vetStr = null;
    String strNum, strLang=null;
    if (args!=null)
       vetStr = StaticMethods.getiHanoiParameters(strParam);
    if (vetStr==null)
       strNum = iHanoi.NUM;
    else {
       strNum = vetStr[0]; // numero de discos
       if (vetStr.length>1)
          strLang = vetStr[1]; // lingua
       }
    // "#args=" + args.length + ": strNum=" + strNum + " - args=\"" + strParam + "\"");
    hanoi.montaJanela(strNum, strLang, fHanoi);
    System.out.println(ResourceReader.read("msgMain"));
    // msgMain=Chamada: java ihanoi.iHanoi n=N lang=LN (N inteiro, LN=pt => Portugues ou LN=en => Ingles)
    } // public static void main(String args[])

  }
