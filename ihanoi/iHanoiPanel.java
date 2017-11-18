/*
 * iHanoi : Problema das Torres de Hano'i na Internet
 * iMática: http://www.matematica.br
 * LInE : "L"aboratory of "I"nformatics i"n" "E"ducation : http://line.ime.usp ; http://www.usp.br/line
 * 
 * @author Leo^nidas de Oliveira Branda~o
 * 
 * @description Main panel with all othe graphical componets
 * 
 * @version
 *  2016/08/01 : new class only to draw towers and discs; new layout
 */


package ihanoi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Panel;
import java.awt.TextField;
import java.util.Vector;

import ihanoi.util.ProcessReadWrite;
import ihanoi.util.FileUtilities;
import ihanoi.util.StaticMethods;


public class iHanoiPanel extends Panel implements ActionListener, ItemListener, MouseListener {

  // Double buffering: sem isso o AWT nao chama "paint(Graphics)" mais de 1 vez dentro de laco
  //                   tambem serve para eliminar "flickering" de AWT
  private static Graphics offgraphics;
  private static Image offscreen = null;

  private static final int // dimensao global do iHanoi
          DimPainelC =  660, // 750,
          DimPainelL =  400; // 450;

  public static final int
     MAXDISCOS = 20,
     Nhastes = 3,       // numero maximo de discos que sao apresentados
     DimX = DimPainelC, // largura do painel principal
     DimY = DimPainelL; // altura do painel principal

  // ATENTION: do not leave this button, it must appear only after the learner try his solution!
  // It allows to perform the automatic movement of the discs
  static boolean AUTOMATICO = false;

  private static final int FT1 = 10, FT2 = 11, FT3 = 12, FT4 = 13, FT5 = 14;
  private static final Font fonteBotoes = new Font("Helvetica",  Font.BOLD, FT2);
  public static final Color
          corEarth = new Color(116, 13, 13),       // TowersPanel.java: color to base ground (earth)
          corF     = new Color(40, 80, 150),       // background color to the text fields
          corFundo = new Color(90, 120, 150),      // background color to the "applet"
          corBaseHastes = new Color(40, 80,150),   // color to the base button of the stick towers
          corFundoHastes = new Color(180,200,220), // background color to the towers panel
          corHaste = new Color(100,150,200);       // color to the stick towers

  private static int MaxDiscos;
  public static int MaxDiscos () { return MaxDiscos; }

  iHanoi ihanoi; //TODO: pode jogar fora 'Frame fHanoi'?

  DialogSobre dialog_sobre = null; // dialog window to presents what is iHanoi, a little bit of history
  Frame fHanoi = null;

  private TowersPanel towersPanel; // central panel: with the towers and discs

  private Label Lde, Lpara, Lmov;
  private Button Bfim, Batualiza, Bsobre, Bdesfaz, Brever, Bautom;
  private Button Bwrite, Bread;

  public void brever_setEnabled (boolean bool) { Brever.setEnabled(bool); }

  public Choice CNdiscos;
  public void setEnabledChoice (boolean bool) { // ihanoi/TowersPanel.java: setMovements(String strContent): if it is exercise => do not allow change
    CNdiscos.setEnabled(bool);
    }

  private TextField
    Tmsg = new TextField(110),
    Tde = new TextField(4),
    Tpara = new TextField(6),
    Tmov = new TextField(6);
  public void setTextTmsg (String txt) { Tmsg.setText(txt); }
  public void setTextFrom (String txt) { Tde.setText(txt); }
  public void setTextTo   (String txt) { Tpara.setText(txt); }
  public void setTextTmov (String txt) { Tmov.setText(txt); }
  private void resetAllTexts () {
    Tde.setText(" ");
    Tpara.setText(" ");
    Tmov.setText("0");
    Tmsg.setText("                                                 ");
    }
  
  private String strContent; // last file read
  public void setContent (String str) { strContent = str; }

  private static final int largMov = 400, altMov = 520; // largura x altura de janela para mostrar movimentos realizados

  public int X, Y, Ndiscos,
         dimX, dimY; 

  public int De=-1, Para=-1, // indica de que haste para que haste deve-se mover disco topo
         Nmovimentos=0;

  private boolean ehApplet;

  public Color [] cor = null; //SA
  public Color corH; 
  public Dimension dim;

  private String strFileNameRead = null;     // name of the last file read - ihanoi.util.ProcessReadWrite.readFromFile(iHanoiPanel,String,String)
  private String strLastFileRecorded = null; // name of the last file recorded - ihanoi.util.ProcessReadWrite.processReadFile(iHanoi,iHanoiPanel,Frame)
  public String getFileNameRead () {
    return strFileNameRead;
    }
  public void setFileNameRead (String strName) { // called by: ihanoi.util.ProcessReadWrite.readFromFile(iHanoiPanel,String,String)
    this.strFileNameRead = strName;
    }
  public String getLastFileRecorded () { return strLastFileRecorded; } // iHanoi.getLastFileRecorded()
  public String getFileNameWrite () { // called by: ihanoi.util.ProcessReadWrite.processWriteFile(iHanoiPanel,String,String)
    return this.strLastFileRecorded;
    }
  public void setFileNameWrite (String strName) { // called by: ihanoi.util.ProcessReadWrite.processWriteFile(iHanoiPanel,String,String)
    this.strLastFileRecorded = strName;
    }


  protected iHanoiPanel (Frame frame) {
    fHanoi = frame;
    }

  public void criaDiscosCores (int num, TowersPanel towersPanel) {
    this.towersPanel = towersPanel;
    towersPanel.buildDiscColors(num);
    if (num>MAXDISCOS) { //SA
      String msg = ResourceReader.read("limiteEstourado");
      System.out.println(msg);
      num = MAXDISCOS;
      }
   MaxDiscos = num; //SA
   }


  // Update the discs in towers
  // true => do not clear the "Vector vectorOfMovements" (that registers movements)
  // Called by: this.atualizaDiscos(), this.actionPerformed(ActionEvent), this.itemStateChanged(ItemEvent); TowersPanel.reverTodosOsMovimentos()
  public void updateDiscs (boolean rever, boolean chanceNumOfDiscs) {
    int i, x, y;
    Nmovimentos=0;

    this.resetAllTexts(); // Tde, Tpara, Tmov, Tmsg <- "0", "...<blanks>", " " , " "

    De=-1; Para=-1;
    if (chanceNumOfDiscs)
      towersPanel.updateDiscs(this.Ndiscos);
    else
      towersPanel.updateDiscs();

    Tmsg.setText(ResourceReader.read("moveMsgInicial"));
    if (!rever)
       towersPanel.setVectorOfMovements(new Vector()); // movements vector
    Brever.setEnabled(false); // allow to review movements (after finished the job)
    }


  // Restart! Put all discs back to the stick tower 0
  // The biggest disc will return to the position 0 in vector 0, the others will be set as -1
  // There are 3 vectors (stack), one for each tower. In each stack, the position is porpotional of the disc radius: radius(disc at i) > radius(disc i-1)
  public void atualizaDiscos () {
    int i, x,y;
    this.setTextTmov("0"); // Tmov
    towersPanel.updateDiscs();
    }


  // Called by: this.buildComponents(...)
  private Button createButtonWithListeners (iHanoiPanel painel1, Panel painel2, String str_bt) {
    Button bt = new Button(str_bt);
    painel2.add(bt); // grid with options
    bt.setFont(fonteBotoes);
    bt.setBackground(corF);
    bt.setForeground(Color.white);
    bt.addMouseListener(painel1);
    bt.addActionListener(painel1);
    return bt;
    } // private Button createButtonWithListeners(iHanoiPanel painel1, Panel painel2, String str_bt)


  // Create buttons to the options panel, with listeners, to be used on a top panel
  // Buttons: Bfim, Batualiza, Bsobre, Brever, Bdesfaz, Bread, Bwrite, Bautom
  // Label:   Lde, Lpara, Lmov
  // Called by: this.montaPainel(...)
  private void buildComponents (Panel buttonsPanel) {
    Lde = new Label(ResourceReader.read("de"));
    Lpara = new Label(ResourceReader.read("para"));
    Lmov = new Label(ResourceReader.read("numMov")); // Number of movements

    // Grid with options
    if (!ehApplet) {
      Bfim    = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("fim"));
      }
    Batualiza = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("atualiza"));
    Bsobre    = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("sobre"));
    Brever    = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("rever"));
    Bdesfaz   = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("desfaz"));
    if (!ehApplet) {
      Bread   = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("read"));
      Bwrite  = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("write"));
      }
     
    if (AUTOMATICO) {
      Bautom  = createButtonWithListeners(this, buttonsPanel, ResourceReader.read("automatico"));
      Bautom.setEnabled(true); // permite rever movimentos: fica desabilitado ate finalizar movimentos
      }
    }

  private void montaPainelTitulo (Panel titlePanel, Label Ltitulo, Label LtituloVersao, int lenTitle, int lenVers) {
    int sizeT = 10*lenTitle, sizeV = 10*lenVers, sizeTV = sizeT + sizeV;
    int DimL = 27;
    titlePanel.setLayout(null); //rnul
    titlePanel.setSize(iHanoi.LARGURA, 27);
    titlePanel.setLocation(0,0);
    titlePanel.add(Ltitulo);
    titlePanel.add(LtituloVersao);
    Ltitulo.setSize(sizeT, DimL);
     Ltitulo.setLocation(iHanoi.LARGURA - sizeTV, 0);
    LtituloVersao.setSize(sizeV, DimL); // 130 x 27
     LtituloVersao.setLocation(iHanoi.LARGURA - 100, 0);
    }

  private void montaPainelBotoes (Panel optionsPanel, Choice CNdiscos, Label LNdiscos) {
    int DimL=27, DimD=30, posY = 0, delY = 0; // largura x altura de botoes
    optionsPanel.setLayout(null); //layout
    optionsPanel.setSize(iHanoi.LARGURA, DimL);
    optionsPanel.setLocation(0,27);
    if (!ehApplet) {
      Bfim.setSize(40,27);
      Bfim.setLocation(10,0);
      }
    Batualiza.setSize(70,27);
     Batualiza.setLocation(60,0);
    Bsobre.setSize(60,27);
     Bsobre.setLocation(130,0);
    Bdesfaz.setSize(60,27);
     Bdesfaz.setLocation(200,0);
    Brever.setSize(60,27);
     Brever.setLocation(260,0);
     Brever.setEnabled(false); // allow to review the movements: stay disable until the target is reached (all discs in rod B or C)
    if (!ehApplet) {
      Bread.setSize(50,27);
      Bread.setLocation(320,0); // 260 + 60
      Bwrite.setSize(50,27);
      Bwrite.setLocation(370,0); // 320 + 50
      }
    if (AUTOMATICO) {
      Bautom.setSize(60,27);
      Bautom.setLocation(420,0); // 370 + 50
      Bautom.setEnabled(true); // allow iHanoi to present the minimum movements: is disable if exercise or if defined by parameters when "applet"
      // Bdesfaz.setEnabled(false); // avoid to undo movements
      }

    CNdiscos.setSize(40, 27);
     CNdiscos.setLocation(510,0); // antes de LNdiscos
    LNdiscos.setSize(230,DimL);
     LNdiscos.setLocation(551,0); // DimX-190 = 480
    } // private void montaPainelBotoes(Panel optionsPanel, Label Lime_usp, Choice CNdiscos, Label LNdiscos)

  private void montaPainelDePara (Panel fromToPanel) { // , Panel pDe, Panel pPara, Panel pMov, Label Lime_usp) {
    int dimY = 10;
    Label Lime_usp = new Label("LInE - IME - USP");
     Lime_usp.setForeground(Color.white);
     Lime_usp.setFont(new Font("Helvetica", Font.PLAIN, FT2));

    fromToPanel.setLayout(null);
    fromToPanel.add(Lde); fromToPanel.add(Tde); fromToPanel.add(Lpara); fromToPanel.add(Tpara);
    fromToPanel.add(Lmov); fromToPanel.add(Tmov); fromToPanel.add(Lime_usp);
    Lde.setSize(30, 29);
     Lde.setLocation(20,0);
    Tde.setSize(25, 29);
     Tde.setLocation(55,0);
     Tde.setEditable(false);   Tde.setBackground(corF); Tde.setForeground(Color.white);
    Lpara.setSize(40, 29);
     Lpara.setLocation(110,0);
    Tpara.setSize(25, 29);
     Tpara.setLocation(150,0);
     Tpara.setEditable(false); Tpara.setBackground(corF); Tpara.setForeground(Color.white);
    Lmov.setSize(139, 29);
     Lmov.setLocation(210,0);
    Tmov.setSize(40, 29);
     Tmov.setLocation(355,0);
     Tmov.setEditable(false);
     Tmov.setBackground(corF);
     Tmov.setForeground(Color.white);
    Lime_usp.setSize(125,27);
     Lime_usp.setLocation(595,0);
    }


  // Build the main panel of iHanoi (panel with all other graphical components)
  // Called by: iHanoi.montaJanela(...)
  public void montaPainel (int Nd, DialogSobre ds, iHanoi ihanoi, Frame fhanoi, boolean ehApplet) {
    int DimL=27, DimD=30; // largura x altura de botoes

    this.ihanoi = ihanoi; // pode ser 'Applet iHanoi'?

    this.dialog_sobre = ds;
    this.fHanoi = fhanoi; // pode ser 'Applet iHanoi'?
    this.ehApplet = ehApplet;
    this.Ndiscos = Nd;
    this.X = this.DimX; this.Y = this.DimY; 

    this.setSize(iHanoi.LARGURA, iHanoi.ALTURA);  // this [                                    ] - LARGURA x ALTURA
    this.setLayout(null);                         //      [ northPanel  [                    ] ] - LARGURA x 80
    this.setBackground(corFundo);                 //      [ towersPanel [                    ] ] -
    this.setForeground(Color.white);              //      [ southPanel  [                    ] ] -

    this.setFont(iHanoi.FONTE1);

    Panel northPanel = new Panel();   // Panel with: titlePanel , optionsPanel
    Panel titlePanel = new Panel();
    Panel optionsPanel = new Panel(); // | [End] [Restart] [About] [Review] [Undo] [Read] [Write] [Autom] [N] "Select number of discs" |
    Panel southPanel = new Panel();   // Panel with: fromToPanel, messagesPanel
    Panel fromToPanel = new Panel();  // FlowLayout
    Panel messagesPanel = new Panel();

    int countButtons = 7;
    if (!ehApplet) {
      countButtons++; // Bwrite
      countButtons++; // Bread
      }
    if (AUTOMATICO)
      countButtons++; // Bautom

    northPanel.setLayout(null); //layout

    southPanel.setLayout(null); // Panel with: fromToPanel, messagesPanel //layout
    messagesPanel.setLayout(null);

    Label Ltitulo, LtituloVersao, LNdiscos;
    Button Ha, Hb, Hc;
    String s; 
    int x, y;

    String [] strVet = { ""+iHanoi.VERSION };
    String Stitulo = ResourceReader.read("torresTit") + " - " + ResourceReader.read("torresEnd");
    String StitVer = "[" + ResourceReader.read("iHanoiVersao") + ": " + iHanoi.VERSION + "]"; // iHanoiVersao=Versao
    // Torres de Hano'i - iMatica - http://www.matematica.br [versao]

    //--- Options Panel -------------------------------------------------------------------------------------------//

    // Build labels and buttons, and add buttons:
    // Buttons: Bfim, Batualiza, Bsobre, Brever, Bdesfaz, Bread, Bwrite, Bautom
    // Label:   Lde, Lpara, Lmov
    buildComponents(optionsPanel); // build components with messages and buttons (like "End" and "Review")

    Brever.setEnabled(false); // permite rever movimentos: fica desabilitado ate finalizar movimentos

    // Choice: escolhe numero de discos
    CNdiscos = new Choice();
    CNdiscos.setBackground(corF);

    for (int i=1; i<=MaxDiscos; i++) //SA
        CNdiscos.addItem("" + i);
    CNdiscos.setFont(new Font("Helvetica", Font.PLAIN, FT2));
    CNdiscos.setForeground(Color.white);

    CNdiscos.select(Ndiscos-1); // start with the maximum number of discs (Ndiscos)
    CNdiscos.addItemListener(this);

    // Position
    optionsPanel.add(CNdiscos, java.awt.BorderLayout.WEST); // grid with options

    // Label: msg sobre escolha de num. de discos
    x = (int) DimX/4; y = DimY - 70;
    LNdiscos = new Label(ResourceReader.read("cfgSelecioneNum")); // "Selecione numero de discos"
    LNdiscos.setSize(230, DimL);
    LNdiscos.setFont(new Font("Helvetica", Font.PLAIN, FT2));
    LNdiscos.setForeground(Color.white); // LNdiscos.setBackground(corF);
    optionsPanel.add(LNdiscos, java.awt.BorderLayout.CENTER); // grid with options
    //-------------------------------------------------------------------------------------------------------------//

    //--- Title Panel ---------------------------------------------------------------------------------------------//
    Ltitulo = new Label(Stitulo);
    Ltitulo.setFont(new Font("Helvetica", Font.BOLD, 11));

    LtituloVersao = new Label(StitVer); // iHanoiVersao=Versao
    LtituloVersao.setFont(new Font("Helvetica", Font.PLAIN, FT2));

    titlePanel.setBackground(corFundo);

    montaPainelTitulo(titlePanel, Ltitulo, LtituloVersao, Stitulo.length(), StitVer.length()); // positions...
    montaPainelBotoes(optionsPanel, CNdiscos, LNdiscos); // positions...

    northPanel.add(titlePanel); northPanel.add(optionsPanel);
    northPanel.setSize(iHanoi.LARGURA, 60);
    northPanel.setLocation(0,0);

    this.add(northPanel); //layout
    //-------------------------------------------------------------------------------------------------------------//

    //--- Towers Panel --------------------------------------------------------------------------------------------//
    towersPanel.setSize(DimPainelC, 325);
    towersPanel.setLocation(35, 30);
    this.add(towersPanel); //layout
    //-------------------------------------------------------------------------------------------------------------//

    //--- South Panel: From/to and Message Panel ------------------------------------------------------------------//
    Tde.setEditable(false);   Tde.setBackground(corF);   Tde.setForeground(Color.white);
    Tpara.setEditable(false); Tpara.setBackground(corF); Tpara.setForeground(Color.white);
    Tmov.setEditable(false);  Tmov.setBackground(corF);  Tmov.setForeground(Color.white);

    montaPainelDePara(fromToPanel); // positions...

    Tmsg.setForeground(Color.white);
    // moveMsgInicial = Clique nas bases das hastes para mover discos (A,B ou C), primeira e origem
    Tmsg.setText(ResourceReader.read("moveMsgInicial"));
    Tmsg.setEditable(false); Tmsg.setBackground(corF);
    Tmsg.setSize(iHanoi.LARGURA-10, 30); Tmsg.setLocation(2,0);
    messagesPanel.add(Tmsg);

    southPanel.add(fromToPanel);    southPanel.add(messagesPanel);
    southPanel.setSize(iHanoi.LARGURA, 65); southPanel.setLocation(2,375);
    fromToPanel.setSize(iHanoi.LARGURA-4, 33);
    messagesPanel.setSize(iHanoi.LARGURA-4, 33);
    fromToPanel.setLocation(2,0);
    messagesPanel.setLocation(0,35);

    this.add(southPanel);

    //-------------------------------------------------------------------------------------------------------------//

    towersPanel.redrawDiscs();
    this.atualizaDiscos();
    } // public void montaPainel(int Nd, DialogSobre ds, Frame fHanoi, boolean ehApplet)


  // Called by: TowersPanel.java: setMovements(String strContent)
  public void setEnabledNotExercise () { //TODO: usar?
    Bdesfaz.setEnabled(true); // avoid undo movements?
    Brever.setEnabled(true); // allow to review the movements: stay disable until the target is reached (all discs in rod B or C)
    // Bautom.setEnabled(true); // allow iHanoi to present the minimum movements: is disable if exercise or if defined by parameters when "applet"
    CNdiscos.setEnabled(true); // isn't exercise: allow the learner to change the number of discs
    }

  // Called by: TowersPanel.java: setMovements(String strContent)
  public void setEnabledExercise () { //TODO: usar?
    Bdesfaz.setEnabled(true); // avoid undo movements?
    Brever.setEnabled(false); // allow to review the movements: stay disable until the target is reached (all discs in rod B or C)
    CNdiscos.setEnabled(false); // is exercise: do not allow the learner to change the number of discs
    // Bautom.setEnabled(true); // allow iHanoi to present the minimum movements: is disable if exercise or if defined by parameters when "applet"
    }
  // this.setEnabledUndo(false); // disable 'review' button (new movements)

  public void mouseClicked (java.awt.event.MouseEvent me) {}
  public void mouseReleased (java.awt.event.MouseEvent me) {}
  public void mousePressed (java.awt.event.MouseEvent me) {}
  public void mouseExited (java.awt.event.MouseEvent me) {
    Tmsg.setText(ResourceReader.read("msgHastes"));
    }
  public void mouseEntered (java.awt.event.MouseEvent evt) {
    Object obj = evt.getSource();
    Button bt = null;
    String s;
    if (obj instanceof Button) try {
      bt = ((Button)obj);
      s = bt.getLabel();
      if (bt==Bfim)
         Tmsg.setText(ResourceReader.read("msgFim"));
      else
      if (bt==Batualiza)
         Tmsg.setText(ResourceReader.read("msgAtualiza"));
      else
      if (bt==Bsobre)
         Tmsg.setText(ResourceReader.read("msgSobre"));
      else
      if (bt==Brever)
         Tmsg.setText(ResourceReader.read("msgRever"));
      else
      if (bt==Bdesfaz)
         Tmsg.setText(ResourceReader.read("msgDesfaz"));
      else
      if (bt==Bautom)
         Tmsg.setText(ResourceReader.read("msgAutom"));
      else
      if (bt==Bread)
         Tmsg.setText(ResourceReader.read("msgRead"));
      else
      if (bt==Bwrite)
         Tmsg.setText(ResourceReader.read("msgWrite"));
      } catch (Exception exp) { System.err.println("Mouse over button " + bt + ", is it empty?"); }
    } // void mouseEntered(java.awt.event.MouseEvent evt)


  // Use when learner click over any option button (only excluding the rod basis)
  public void actionPerformed (ActionEvent event) {
    String strArg = event.getActionCommand();
    Object object = event.getSource();

    // System.out.println("\n-----------------------\niHanoiPanel.actionPerformed: getActionCommand=" + strArg + ", src=" + object);
    int d; // auxiliar para movimentos de/para
    try {
      // Atualiza mensagem: objetivo agora e "haste origem" ou "haste destino"?
      if (De==-1) { // origem: ja clicou na primeira haste
         // movePara = Ja escolhida haste de onde saira disco do topo, escolha haste destino
         Tmsg.setText(ResourceReader.read("movePara"));
         }
      else
         // moveDe = Escolhida nova haste que tera disco do topo removido
         Tmsg.setText(ResourceReader.read("moveDe"));
    } catch (Exception e) {
      System.err.println("iHanoiPanel.actionPerformed('move'): error in message 'movePara' or 'moveDe' (missing?):" + e.toString());
      }

    //T System.out.println("actionPerformed: " + strArg + ": " + object);

    try { // trata evento
       if (strArg.equals(ResourceReader.read("desfaz"))) { // undo the last movement
          towersPanel.desfazUltimoMovimento();
          return;
          }
       else
       if (strArg.equals(ResourceReader.read("rever"))) { // redo the last movement undone
          towersPanel.reverTodosOsMovimentos();
          return;
          } //
       else
       if (strArg.equals(ResourceReader.read("fim"))) { System.exit (0); return; } //
       else
       if (strArg.equals("Sobre")) { 
          // Frame JanelaSobre = new Frame(ResourceReader.read("ihanoi"));
          // JanelaSobreP.setVisible(true);
          if (dialog_sobre==null) {
             dialog_sobre = new DialogSobre(fHanoi, iHanoi.SOBRE);
             dialog_sobre.showDialog(); //
             }
          else {
             dialog_sobre.showDialog();
             }
          // "Torres de Hano'i - Leo^nidas O. Branda~o (IME - USP) - http://www.matematica.br"
          // Tmsg.setText();
          return; 
          }
       else
       if (strArg.equals(ResourceReader.read("atualiza"))) { 
          this.updateDiscs(false, false); // do not change number of discs
          return;
          }
       else
       if (strArg.equals(ResourceReader.read("automatico"))) {
          if (towersPanel.topo(0)==-1) {
             // nao existe mais disco na origem "de"
             Tmsg.setText(ResourceReader.read("moveAutoVazia")); // Nao existe mais disco na haste de origem
             return;
             }

          // towersPanel.debugLista();
          towersPanel.moveAutomatico(Ndiscos, 0, 2, 1); // automatic movement: Ndiscos discs from haste[0] to haste[2]
          Brever.setEnabled(true); // allow to review the moviments: remain disabled until the target is achived
          return;
          }
       else
       if (object == CNdiscos) {
          int ndiscsN = Integer.valueOf(CNdiscos.getSelectedItem()).intValue();
          if (this.Ndiscos != ndiscsN) {
            this.Ndiscos = ndiscsN;
            this.updateDiscs(false, true); // change the number of discs
            }
          else 
            this.updateDiscs(false, false); // do not change number of discs
          return;
          }
       else

       if (object == Bread) {
         this.strContent = null; // will be defined by 'ProcessReadWrite.processReadFile(...)' -> 'ProcessReadWrite.processReadFile(...)'
         int answer = ProcessReadWrite.processReadFile(this.ihanoi, this, this.fHanoi); // ihanoi.util.ProcessReadWrite: read to 'this.strContent'
	 int result;
         if (answer==1 && strContent!=null) {
           result = StaticMethods.decodeIHN(this.strContent, this.ihanoi.getTowersPanel()); // ihanoi/util/StaticMethods.java
           result = this.towersPanel.setMovements(this.strContent);
	   String strMsg = "";
	   if (result<0)
             strMsg = ResourceReader.read("tpErrNotiHanoi"); // The file content didn't appears to be an iHanoi file!
           else {
             strMsg = ResourceReader.read("tpiHanoiRead"); // The file content has been correctly interpreted!
             }
	   this.setTextTmsg(strMsg);
           System.out.println("iHanoiPanel.actionPerformed('button read)': " + strMsg);
           }

         return;
         }
       else
       if (object == Bwrite) {
         String strContent = this.towersPanel.getMovements();
         System.out.println("iHanoiPanel.actionPerformed('button write'): content =\n" + strContent);
         if (strContent!=null && strContent.length()>5) {
           // ihanoi/util/ProcessReadWrite.java : processWriteFile(...)
           // FileUtilities.storeFile("ihanoi.ihn", strContent, "actionPerformed"); // ihanoi.util.FileUtilities
           ProcessReadWrite.processWriteFile(this.ihanoi, this, this.ihanoi.getFrame(), strContent); // 'ihanoi.util.ProcessReadWrite' uses 'FileUtilities.storeFile(...)'
           System.out.println("iHanoiPanel.java: actionPerformed: strContent=" + strContent);
           // ihanoi/util/ProcessReadWrite.java :
           // + processWriteFile(...): FileUtilities.storeFile(strFileNameW, strContent, "ProcessReadWrite.processWriteFile(...)");
           }
         else {
           String strE = (ResourceReader.read("ihp_err_no_movements")); //
           System.err.println(strE); // "Error: there is no movements to be registered!"
           Tmsg.setText(strE);
           }
         }

    } catch (Exception e) { // trata evento
       System.err.println("iHanoi: erro em tratamento de evento "+e);
       e.printStackTrace();
       }
    } // void actionPerformed(ActionEvent event)


  public void itemStateChanged (ItemEvent event) {
    int ndiscsN = Integer.valueOf(CNdiscos.getSelectedItem()).intValue();
    if (this.Ndiscos != ndiscsN) {
      this.Ndiscos = ndiscsN;
      this.updateDiscs(false, true); // change the number of discs
      }
    else 
      this.updateDiscs(false, false); // do not change number of discs
    }

  }
