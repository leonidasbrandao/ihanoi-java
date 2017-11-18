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
 * @description This class provides the graphical panel to presents the 3 stick towers and all the iHanoi discs
 * 
 * @see ihanoi/iHanoi.java
 * @see ihanoi/iHanoiPanel.java
 * @see ihanoi/util/ProcessReadWrite.java
 * 
 * @version
 *  2016/08/01 : new class only to draw towers and discs; new layout and features (like read and write movements in a file)
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.util.Vector;

import ihanoi.util.StaticMethods;

public class TowersPanel extends Panel implements ActionListener {

  private static final int FT1 = 10, FT2 = 11, FT3 = 12, FT4 = 13, FT5 = 14;

  private static final Color
          corEarth = iHanoiPanel.corEarth,             // color to base ground (earth)
          corBaseHastes = iHanoiPanel.corBaseHastes,   // color to the towers base (clickable rectangle)
          corFundoHastes = iHanoiPanel.corFundoHastes, // color to the background panel
          corHaste = iHanoiPanel.corHaste;             // color to the stick towers
  private static final  int DimL=27, DimD=30; // largura x altura de botoes

  private static double TIME = 5000000;    // retardo para rever movimentos

  private static final int
     MAXDISCOS = 20,
     Nhastes = 3,    // number of stick towers
     DimX = iHanoiPanel.DimX, // width of the main panel = 660
     DimY = iHanoiPanel.DimY; // height of the main panel = 400

  private static final int largMov = 400, altMov = 520; // width x height to the window used to presents the text represents the movements performed by the learner

  public static final String FIRSTLINE = "# ihanoi: http://www.matematica.br!";

  private Graphics offgraphics;
  private Image offscreen = null;

  private static int MaxDiscos;

  public Color [] cor = null; //SA

  private Button Ha, Hb, Hc;

  private iHanoi ihanoi;

  private iHanoiPanel ihanoiPanel;

  public int numberOfDiscs; // number of discs under operation

  private float evaluation = -1; // to exercises
  private boolean isExercise = false; // is exercise?
  public void setIsExercise(boolean bool) { isExercise = bool; } // used on 'ihanoi.util.StaticMethods.decodeIHN(...)'

  // iHanoi.setMovements(String strContent): towersPanel.setDiscs(ndiscs);
  public void setDiscs (int ndiscs) {
    this.numberOfDiscs = ndiscs;
    this.buildDiscColors(ndiscs); // discos = new Disco[ndiscs];
    }

  public void buildDiscColors (int num) {
    if (num>MAXDISCOS) {
      String msg = ResourceReader.read("limiteEstourado");
      System.out.println(msg);
      num = MAXDISCOS;
      }
    MaxDiscos = num;
    discos = new Disco[MaxDiscos];
    cor = new Color[MaxDiscos];
    }

   
  /// Class for towers data
  protected class StickTower {
    protected  int X, Y, N, topo;
    protected  int [] d; // vetor de discos na haste
    protected  StickTower (int x, int y, int n, int M) {
      int i; d = new int[M];
      topo = -1; 
      for (i=0; i<M; i++) d[i]=-1;
      this.X = x; this.Y = y; this.N = n;
      }
    }


  /// Class for discs data
  protected class Disco { //
    public int X, Y,
           dimX, dimY,
           N;
    public Color C;
    public Disco (int n, int x, int y, int dx, int dy, Color c) {
      this.N = n; this.X = x;  this.Y = y; this.dimX = dx;  this.dimY = dy;
      this.C = c;
      }
    }



  /// Class for discs movements under the towers
  protected class Move { //
    public int de, para;
    public int from () { return this.de; }
    public int to () { return this.para; }
    public Move (int de, int para) {
      this.de = de; this.para = para;
      }
    }

  public static int fromMove (Object obj) {
    if (obj instanceof Move)
      return ((Move)obj).de;
    return -1;
    }
  public static int toMove (Object obj) {
    if (obj instanceof Move)
      return ((Move)obj).para;
    return -1;
    }

  // public Move getMovement(int from, int to) { new Move(from, to); } // Called by: iHanoiPanel.setMovements(String strContent)
  private static int staticNumTowers = 0;
  private int numTower = staticNumTowers++;
  //D static { System.out.println("\n-------------------------------------\nTowersPanel.java: #TowersPanel = " + staticNumTowers); }

  private StickTower [] hastes = new StickTower[Nhastes]; // classe com dados de hastes
  private Disco [] discos; //2017/09/29 = new Disco[MaxDiscos];
  public void setTower (int i, int x, int y) { System.out.println("TowersPanel.setTower(" + i + "," + x + "," + y + "): #hastes=" + hastes.length);
  this.hastes[i].X = x;
  this.hastes[i].Y = y; }
  public void setDisc_X (int i, int x) { this.discos[i].X = x; }
  public void setDisc_Y (int i, int y) { this.discos[i].Y = y; }

  // Debug method: list the discs in any stick towers
  // Vetor haste[i].d = [ disco maior, segundo maior, ..., menor, -1, ..., -1 ] (maior disco da haste[i] na posicao 0)
  // Called by: iHanoiPanel.moveDisco(String strPara, int de, int para, int t1, int t2); iHanoiPanel.actionPerformed(ActionEvent event)
  public void debugLista () {
    StickTower [] hastes = this.hastes;
    int numStickTower = hastes.length;
    System.out.println("TowersPanel.java: debugLista(): numStickTower=" + numStickTower);
    for (int i=0; i<numStickTower; i++) {
      StickTower haste = hastes[i];
      int numDiscos = haste.d.length; // discos existentes
      System.out.print(" #disco " + i + "=" + numDiscos + ": ");
      for (int j=0; j<numDiscos; j++) {
        System.out.print(" " + haste.d[j] + ",");
        }
      System.out.println();
      }
    }


  // Redraw all discs to tower 0
  // The biggest disc is on position 0 of tower A and other towers are empty (with -1 in first position)
  // The integer number represents any disc, with big number for big disc
  public void redrawDiscs () {
    int i, x,y; // ihanoiPanel.setTextTmov("0");
    for (i=0; i<Nhastes; i++)
      hastes[i] = new StickTower((i+1)*(int)DimX/4, DimY-130,i, numberOfDiscs); // numberOfDiscs <= MaxDiscos
    x = hastes[0].X-3; y = hastes[0].Y-20*(numberOfDiscs-1)+40; hastes[0].topo = numberOfDiscs-1;
    for (i=0; i<numberOfDiscs; i++) {
      float aux = ((float)i/numberOfDiscs);

      // Set the i-th disc in tower 0
      hastes[0].d[i] = numberOfDiscs - (i+1);

      // Set the position of disc i (disco[i]) in its correspondent tower
      //D if (discos==null) { discos = new Disco[MaxDiscos]; } // 2017/09/29: add by security?
      try {
        if (discos[i]==null) {
          cor[i] = new Color(aux, aux, aux);
          discos[i] = new Disco(i, x-i*10, y+20*i, (2*i+1)*10+10, 20, cor[i]);
          }
        else {
          discos[i].X = x-i*10;
          discos[i].Y = y+20*i; 
          discos[i].dimX = (2*i+1)*10+10;
          discos[i].dimY = 20;
          discos[i].C = new Color(aux,aux,aux);
          }
      } catch (Exception exp) {
        String strErr = (discos != null) ? " #discos=" + discos.length : " #discos=0";
        System.err.println("TowersPanel.java: redrawDiscs: i=" + i + ", discos:" + strErr);
        exp.printStackTrace();
        }
      }
    } // public void redrawDiscs()


  // Update the discs in towers
  // true => do not clear the "Vector vectorOfMovements" (that registers movements)
  // Called by: iHanoiPanel.updateDiscs(boolean): towersPanel.updateDiscs();
  public void updateDiscs (int newNumOfDiscs) {
    this.numberOfDiscs = newNumOfDiscs; // number of discs to be drawn
    this.Ndiscos = newNumOfDiscs;       // number of discs to count //TODO usar apenas 1???
    this.updateDiscs();
    }

  // Update the discs in towers
  // Restart! Put all discs back to the stick tower 0
  // The biggest disc will return to the position 0 in vector 0, the others will be set as -1
  // There are 3 vectors (stack), one for each tower. In each stack, the position is porpotional of the disc radius: radius(disc at i) > radius(disc i-1)
  // Called by: iHanoiPanel.updateDiscs(boolean): towersPanel.updateDiscs();
  public void updateDiscs () {
    int i, x, y;
    float aux;
    this.Nmovimentos = 0;
    redrawDiscs();
    x = hastes[0].X-3; y = hastes[0].Y-20*(numberOfDiscs-1)+40;
    for (i=0; i<numberOfDiscs; i++) {
      aux = ((float)i/numberOfDiscs);
      hastes[0].d[i] = numberOfDiscs - (i+1);
      discos[i].X = x-i*10;
      discos[i].Y = y+20*i;
      }
    hastes[0].topo = numberOfDiscs-1; hastes[1].topo = -1; hastes[2].topo = -1;
    // System.out.println(" - atualiza: hastes["+0+"].topo="+hastes[0].topo+", hastes["+1+"].topo="+hastes[1].topo+", hastes["+2+"].topo="+hastes[2].topo);
    this.repaint();
    } // public void updateDiscs()


  // Double buffering technique
  public void pinta () {
    Graphics g = null;
    // double buffering
    if (offscreen != null) { // primeiro "paint" entra antes de construir primeira "offscreen"
      g = offscreen.getGraphics();
      if (g!=null)
        offgraphics = g;
        update(g);
        }
      else {
        update(offgraphics);
        }
    }

  public final void paint (Graphics g) {
    pinta();
    }

  public void update (Graphics g) {
    int Dx=5, dx=15, dy=100; // dist=(int)(this.X)/4;
    int i;
    //System.err.println("TowersPanel.update(Graphics): this.X="+this.X+",  this.Y="+this.Y);

    // double buffering
    if (offscreen == null) { // primeiro "paint" entra antes de construir primeira "offscreen"
       offscreen = this.createImage(DimX, DimY); // precisa ser construido dentro do "paint"
       // iHanoi.update: offscreen null: DimX=660, DimY=400, 2*dy/3=66, DimY-dy-10=290
       offgraphics = offscreen.getGraphics();
       }
    g = offscreen.getGraphics(); // pega ultimo buffer "grafico"
    g.setColor(corFundoHastes); // color to the background panel
    g.fill3DRect(Dx, dy/4, DimX-Dx, DimY-dy, true);

    g.setColor(corEarth); // color to the base ground for the towers
    g.fill3DRect(Dx, DimY-97, DimX-Dx, 20, true);

    g.setColor(corHaste);
    boolean error = false;

    // Stick towers
    for (i=0; i<Nhastes; i++) {
    try {
      g.fill3DRect(hastes[i].X, 30, dx, hastes[i].Y, true);
      } catch (Exception e) { //D e.printStackTrace();
        String str=(hastes!=null ? ""+hastes.length : "");
        System.err.println("TowersPanel.update(Graphics): i=" + i + ", #TowersPanel=" + numTower + ", hastes=" + hastes + ", #hastes=" + str + ": " + e);
        //D try{ if (!error) { error = true; System.err.println("TowersPanel.update(Graphics): i="+i+", hastes[" + i + "].X=" + hastes[i].X); } } catch (Exception e2) { System.err.println("TowersPanel.update(Graphics): i="+i+", hastes[" + i + "].X VAZIO!: " + e2); }
        }
      } // for (i=0; i<Nhastes; i++)
    // Discs
    for (i=0; i<numberOfDiscs; i++) {
    try {
      g.setColor(discos[i].C);
      g.fill3DRect(discos[i].X, discos[i].Y-40, discos[i].dimX, discos[i].dimY, true);
      } catch (Exception e) {
        String str = (discos!=null ? ""+discos.length : "");
        System.err.println("TowersPanel.update(Graphics): i="+i+", #discos=" + str + ": " + e); if (!error) { e.printStackTrace(); error = true; }
        }
      }

    // double buffering
    g = this.getGraphics();
    g.drawImage(offscreen,0,0,this);
    } // public void update(Graphics g)


  public int De=-1, Para=-1, // indicate, respectively, the origin and the destination rod index (0=>A, 1=>B, 2=>C)
         Nmovimentos=0,      // number of movements
         Ndiscos;            // number of discs (from 1 to MAXDISCOS)

  private Vector vectorOfMovements = new Vector();
  public void setVectorOfMovements (Vector vect) { this.vectorOfMovements = vect; } // vetor que anota movimentos
  public int getSizeVectorOfMovements () { return this.vectorOfMovements.size(); } // vetor que anota movimentos

  public void addElementVectorOfMovements (Vector vMov, int from, int to) { Move move = new Move(from, to); vMov.addElement(move); } // vetor que anota movimentos
  public void addElementVectorOfMovements (Object obj) {
    if (obj instanceof Move) {
      Move move = (Move) obj;
      this.vectorOfMovements.addElement(move);
      }
    } // vetor que anota movimentos


  // Constructor
  // Called by: ihanoi.iHanoi.montaJanela(...)
  public TowersPanel () {
    int x, y;
    x = (int) DimX/4; y = DimY - 70;

    this.setLayout(null);

    this.discos = new Disco[MaxDiscos];
    this.cor = new Color[MaxDiscos];

    // Towers clickable basis
    Ha = new Button("A"); 
     Ha.setSize(5*DimD,DimL);
     Ha.setLocation(x-2*DimD-10,y-40);
     Ha.setBackground(corBaseHastes);
    Hb = new Button("B"); 
     Hb.setSize(5*DimD,DimL);
     Hb.setLocation(2*x-2*DimD-10,y-40);
     Hb.setBackground(corBaseHastes);
    Hc = new Button("C");
     Hc.setSize(5*DimD,DimL);
     Hc.setLocation(3*x-2*DimD-10,y-40);
     Hc.setBackground(corBaseHastes);

    Ha.setFont(new Font("Helvetica",  Font.BOLD, FT3));
    Hb.setFont(new Font("Helvetica",  Font.BOLD, FT3));
    Hc.setFont(new Font("Helvetica",  Font.BOLD, FT3));

    Ha.addActionListener(this);
    Hb.addActionListener(this);
    Hc.addActionListener(this);

    this.add(Ha);
    this.add(Hb);
    this.add(Hc);

    // iHanoi Frame: LARGURA x ALTURA = 750 x 475
    this.setSize(DimX, DimY);

    } // public TowersPanel(int numberD, iHanoiPanel ihanoiPanel)


  // Power of b^e: help to compute the minimal number of movemnts
  public static long pot (int b, int e) {
    long p=1;
    int i;
    for (i=0; i<e; i++) { p *= b; }
    return p;
    }


  /// Movimentacao automatica: num=num. de discos, de=haste original, para=haste destino, usa=haste apoio
  // Botton of recursion: there is no more disc to be moved
  public void moveAutomatico (int num, int de, int para, int usa) {
    if (num==0 || de==-1 || para==-1)
       return;
    int t1, t2;

    moveAutomatico(num-1, de, usa, para);
    //T if (erro) return;

    acertaOrigemDestino(-1, -1, " ", " "); //
    moveDisco("" + de , de, para, topo(para), topo(de)); //
    retardo(); // atrasa movimentacao para dar tempo ao usuario visualizar
    moveAutomatico(num-1, usa, para, de);
    //T if (erro) return;
    }

  public void reverTodosOsMovimentos () {
    Vector vectorOfMovementsAux = new Vector();
    Object obj;
    Move mv1, mv2;
    String strToPresentMovements = "", strDe, strPr;
    int Nmov_aux, tam, de=-2, para=-2, t1=-2, t2=-2, i;

    if (Nmovimentos==0) { // it came from read a new iHanoi file
       Nmovimentos = this.vectorOfMovements.size();
       }

    // movTitulo     = Movimentos realizados
    // movSubTitulo  = (N. X -> Y :: movimento N: mover disco no topo da hasta X para o topo da Y)
    // movMovimentos = Total de movimentos realizados:
    strToPresentMovements = // ResourceReader.read("ihanoi") + "\n" + ResourceReader.read("torresTit") + "\n" +
          ResourceReader.read("movTitulo") + "\n" +
          ResourceReader.read("movSubTitulo") + "\n" +
          ResourceReader.read("movMovimentos") + ": " + Nmovimentos + "\n" +
          StaticMethods.dadosUsuario() + "\n";

    Ha.setEnabled(false); Hb.setEnabled(false); Hc.setEnabled(false); // avoid problems...
    try {
      // Copia movimentos
      tam = vectorOfMovements.size();
      for (i=0; i<tam; i++)
          vectorOfMovementsAux.addElement(vectorOfMovements.elementAt(i));
      // Reinicia
      this.ihanoiPanel.updateDiscs(false, false);
      pinta(); // double buffering

      // Copia movimentos
      vectorOfMovements = new Vector(); // zera vetor de movimentos

      System.out.println("Review: number of movements=" + tam);
      // Efetua movimentos
      for (i=0; i<tam; i++) {
          retardo(); // retardo para dar tempo de visualizar movimentacao automatica (proporcional ao TIME)
          obj = vectorOfMovementsAux.elementAt(i); // vetor que anota movimentos: inverte
          mv1 = (Move)obj;
          de   = mv1.de;
          para = mv1.para;
          //D System.out.println(i + ": " + de + " -> " + para);
          strDe = haste(de);
          strPr = haste(para);
          strToPresentMovements += i + ": " + strDe + " -> " + strPr + "\n"; // anote movimento
          //D System.out.println(i + ": " + strDe + " -> " + strPr + "");
          t1 = hastes[para].topo; t2 = hastes[de].topo; 
          moveDisco(strDe , de, para, t1, t2); //
          pinta(); // double buffering
          }
      } catch (Exception e) {
        System.err.println("iHanoi: error during the review of used movements:\n " + 
                           "(from,to)=(" + de + "," + para + ") (t1,t2)=(" + t1 + "," + t2 + "): " + e);
        e.printStackTrace();
        }
    JanelaTexto jan_texto = new JanelaTexto(ihanoiPanel, largMov, altMov, strToPresentMovements); // def. alt. e larg. da 
    jan_texto.setVisible(true);
    Ha.setEnabled(true); Hb.setEnabled(true); Hc.setEnabled(true);

    } // private void reverTodosOsMovimentos()


  // Undo the last movement
  public void desfazUltimoMovimento () {
    Object obj;
    Move mv;
    int i=-1, pos, tam, de, para, d, t1, t2;
    int [] vetMov;
    int tamH = hastes.length, Nmov_aux;
    try {
      tam = vectorOfMovements.size();
      if (tam<1) {
         // moveErrDesfazVazio  = Nao existe movimento a ser desfeito!
         ihanoiPanel.setTextTmsg(ResourceReader.read("moveErrDesfazVazio"));
         return;
         }
      obj = vectorOfMovements.elementAt(tam-1); // vetor que anota movimentos: inverte
      vectorOfMovements.removeElementAt(tam-1); // 
      Nmovimentos--;
      ihanoiPanel.setTextTmov(String.valueOf(Nmovimentos));
      acertaOrigemDestino(-1, -1, " ", " "); // De = Para = -1;
      mv = (Move)obj;
      para   = mv.de;          de = mv.para; // inverte movimento
      t1 = hastes[para].topo; t2 = hastes[de].topo; 
      try {
        t1++;
        hastes[para].d[t1] = hastes[de].d[t2];
        hastes[de].d[t2] = -1;
      } catch (Exception e) {
        System.err.println("iHanoi: erro ao tentar listar movimentos (" + i + "):\n "  +  "(de,para)=(" + de + "," + para + ") (t1,t2)=(" + t1 + "," + t2 + "): " + e);
        e.printStackTrace();
        }
      hastes[para].topo++;
      hastes[de].topo--; 
      d = hastes[para].d[t1]; // numero do disco inserido no topo de A
      discos[ d ].X = hastes[para].X-discos[d].N*10-2;
      discos[ d ].Y = hastes[para].Y-20*t1+40;
      repaint();
    } catch (Exception e) {
      System.err.println("iHanoi: erro ao tentar listar movimentos (" + i + "): " + e);
      e.printStackTrace();
      }
    }

 
  // Get the "touer" from its index (0, 1 or 2)
  private static char getTower (int index) {
    switch (index) {
      case 0 : return 'A';
      case 1 : return 'B';
      case 2 : return 'C';
      default: return ' '; // error!
      }
    }


  // Get the movements as string in iHanoi format
  // Called by: iHanoi.java: public String getAnswer() - iLM protocol
  // Called by: iHanoiPanel.actionPerformed(...) from button "Write"
  public String getMovements () {
    Object obj;
    Move mv;
    String strContent = FIRSTLINE; // "# ihanoi: http://www.matematica.br!"
    int de, para, tam = vectorOfMovements.size();
    if (tam == 0) {
      System.err.println("Error: there is no disc movements!"); // ihpErrNoMovements
      return null;
      }

    // Sets on 'ihanoi/iHanoi.java': "public int numberOfDiscs; private float evaluation = -1;" // number of discs under operation end the exercise evaluation
    String str = ""; //D
    if (isExercise) {
      long minimumMov = pot(2, this.numberOfDiscs) - 1; // n discs => minimum is 2^n - 1
      //this.ihanoi.setEvaluation(this.numberOfDiscs, this.Nmovimentos);
      //this.ihanoi.setEvaluation(Ndiscos, tam);
      if (hastes[0].topo == -1) {
        if (hastes[1].topo == -1) { // all discs in rod C => correct
          evaluation = (float)minimumMov / Nmovimentos;
          str = "1: 0==-1 e 1==-1"; //D
          }
        else
        if (hastes[2].topo == -1) { // all discs in rod B => 1/2 correct
          evaluation = (float)minimumMov / (2*Nmovimentos); // 50% of discount...
          str = "2: 0==-1 e 2==-1"; //D
          }
        else { // discs in rod B and in C => incorrect
          evaluation = 0;
          str = "3: 0==-1 e 2!=-1"; //D
          }
        }
      else {
        evaluation = 0;
        str = "4: 0!=-1"; //D
        }
      str += ", evaluation=" + evaluation + ", minimumMov=" + minimumMov + "=" + pot(2, this.numberOfDiscs) + "-" + 1; //D
      this.ihanoi.setEvaluation(this.numberOfDiscs, tam, evaluation);
      }

    if (Ndiscos==0) {
      Ndiscos = this.numberOfDiscs; //Ndiscos; //this.Ndiscos = newNumOfDiscs;
      }
    // System.out.println("TP.getMovements(): Ndiscos=" + Ndiscos + str + ", Nmovimentos=" + Nmovimentos);

    strContent += "\nDiscs: " + Ndiscos + "\nSize: " + tam + "\nMovements:";

    for (int i=0; i<tam; i++) {
      mv = (Move) vectorOfMovements.elementAt(i); // vetor que anota movimentos: inverte
      de = mv.de;
      para = mv.para;
      strContent += "\n" + this.getTower(de) + " " + this.getTower(para) + " ;";
      }
    return strContent;
    }


  // boolean isExercise = false; // is exercise?

  // From: iHanoi.montaJanela(...)
  public void setTowerPanel (int numberD, iHanoi ihanoi, iHanoiPanel ihanoiPanel) {
    this.numberOfDiscs = numberD;
    this.ihanoiPanel = ihanoiPanel;
    this.ihanoi = ihanoi; // to be used to set 'iHanoi.numberOfDiscs' and 'iHanoi.numberOfMovements' (iLM protocol)
    if (MaxDiscos==0)
      MaxDiscos = numberD;
    }


  // Decode iHanoi sessions file (IHN)
  // Set the movements from a string in iHanoi format
  // Called by: ihanoi/iHanoi.java: void init(): this.towersPanel.setMovements(strContent);
  // Return: -1 => file with less than 34 characters; -2 => didn't find 'Discs: '; -3 => didn't find 'Size:'; 1 => OK
  public int setMovements (String strContent) {
    Object obj;
    Vector vectorOfMovementAux = new Vector(); //                           01234567890123456789012345678901234
    String strNumber = "", strExerc, strLine; // Content = FIRSTLINE;   // "# ihanoi: http://www.matematica.br!"
    int ndiscs, nmovements;

    this.setDiscs(this.Ndiscos); // set "towersPanel.numberOfDiscs", "disco=new Disco[ndiscs]" and "cor=new Color[MaxDiscos]"
    if (this.isExercise)
      ihanoiPanel.setEnabledChoice(true); // do not allow to change the number of discs!
    else
      ihanoiPanel.setEnabledChoice(false);

    // Was set on 'StaticMethods.decodeIHN': this.isExercise, this.vectorOfMovements, this.Ndiscos

    this.ihanoiPanel.updateDiscs(true, false); // true => do not clear the "Vector vectorOfMovements"

    if (!isExercise) {
      this.ihanoiPanel.brever_setEnabled(true); // allow to review the moviments: remain disabled until the target is achived
      this.ihanoiPanel.setEnabledNotExercise(); // change iHanoiPanel buttons/options: Brever; Bdesfaz; CNdiscos
      System.out.println("Loaded: #movements=" + this.getSizeVectorOfMovements());
      }
    else {
      this.ihanoiPanel.setEnabledExercise(); // change iHanoiPanel buttons/options: Brever; Bdesfaz; CNdiscos
      System.out.println("Loaded: it is exercise");
      }
     
    return 1;

    } // private void setMovements(String strContent) 


  // Called by: TowersPanel.actionPerformed(ActionEvent)
  public void acertaOrigemDestino (int de, int para, String strDe, String strPara) {
    De   = de;
    Para = para;
    if (de>=0) {
      if (para<0 && hastes[de].topo<0) {
        De = Para = -1;
        ihanoiPanel.setTextFrom(" ");
        ihanoiPanel.setTextTo(" "); // moveHasteVazia = Nao ha disco nesta haste, por favor, escolha outra
        ihanoiPanel.setTextTmsg(ResourceReader.read("moveHasteVazia"));
        return;
        }
      }
    else
    if (de==para) {
      De = Para = -1;
      ihanoiPanel.setTextFrom(" ");
      ihanoiPanel.setTextTo(" "); // moveMesmaHaste=Foi escolhida a haste de destino igual a de origeom,por favor, escolha outra
      if (de!=-1)
        ihanoiPanel.setTextTmsg(ResourceReader.read("moveMesmaHaste"));
      return;
      }
    if (strDe!=null)
      ihanoiPanel.setTextFrom(strDe);
    if (strPara!=null)
      ihanoiPanel.setTextTo(strPara);
    }

  // Return the disc index in the stick top: 'ind_haste'
  public int topo (int ind_haste) {
    int tam, topo;
    tam = hastes==null ? -1 : hastes.length;
    topo = tam>ind_haste ? hastes[ind_haste].topo : -1;
    if (tam<0) System.err.println("Erro: haste " + ind_haste + " com erro: " + tam + " e " + topo);
    return topo;
    }


  // File format "# ihanoi: http://www.matematica.br!";

  private String verifyTopTowers (int towerFrom, int towerTo, int topFrom, int topTo) {
    String strErr = null; // ResourceReader.read("")
    if (towerFrom==towerTo) {
      // ihpErrSameTower   = Invalid movement, the origin and the destination tower are the same!      
      strErr = ResourceReader.read("ihpErrSameTower");
      }
    else
    if (hastes[towerFrom].d[topFrom]==-1) {
      strErr = ResourceReader.read("ihpErrEmptyTower"); // ihpErrEmptyTower = Invalid movement, the origin tower has no discs!
      }
    else
    if (topTo>-1 && hastes[towerFrom].d[topFrom] > hastes[towerTo].d[topTo]) { // has disc in tower 'towerTo' and it is grater than that on top o tower 'towerFrom'
      strErr = ResourceReader.read("ihpErrGreaterOver"); // ihpErrGreaterOver = Invalid movement, any bigger disc could not be moved over any smaller one!
      }
    return strErr;
    }

  // Move disco: De -> Para
  // Called by: ihanoi.TowersPanel.moveAutomatico(int num, int de, int para, int usa)
  // Called by: ihanoi.TowersPanel.reverTodosOsMovimentos()
  // Called by: ihanoi.TowersPanel.actionPerformed(ActionEvent)
  public void moveDisco (String strPara, int de, int para, int topTo, int topFrom) {
    Move mv;
    int d;
    String strVerify = null;
    try {
      ihanoiPanel.setTextTo(strPara);
      if (topFrom<0) // there is no disc in this tower!
        strVerify = "Invalid negative: topTo=" + topTo + ", topFrom=" + topFrom;
      else
        strVerify = this.verifyTopTowers(de, para, topFrom, topTo); // para topTo; de topFrom
      if (strVerify==null) { // this is a valid movemnt
        hastes[para].d[++topTo] = hastes[de].d[topFrom];
        hastes[de].d[topFrom] = -1;
        hastes[para].topo++;
        hastes[de].topo--; 
        d = hastes[para].d[topTo]; // numero do disco inserido no topo de A
        discos[ d ].X = hastes[para].X-discos[d].N*10-2;
        discos[ d ].Y = hastes[para].Y-20*topTo+40;

        Nmovimentos++;
        ihanoiPanel.setTextTmov(String.valueOf(Nmovimentos));
        mv = new Move(de,para);
        this.vectorOfMovements.addElement(mv); // register this movement

        // 'double buffering' para sincronizar e mostrar cada movimento (com parada) - sem isso, escalonador AWT mostra apenas conf. final
        this.pinta();
        }
      else { // strVerify!=null => invalid movemnt!
        String strEr1 = (topFrom>-1) ? "disc " + hastes[de].d[topFrom] : "topFrom=" + topFrom;
        String strEr2 = (topTo>-1) ? "disc " + hastes[de].d[topTo] : "topTo=" + topTo;
        System.err.println("Error: invalid movement! Ignored from=" + de + " to=" + para + ": " + strEr1 + " -> " + strEr2 + "");
        System.err.println(strVerify);
        ihanoiPanel.setTextTmsg(strVerify);
        }
    } catch (Exception e) {
      String strE;
      int tam, tamD, tamP;
      tam = hastes==null ? 0 : hastes.length;
      tamD = tam>de   ? hastes[de  ].d.length : -1;
      tamP = tam>para ? hastes[para].d.length : -1;
      strE = " - #hastes=" + tam + ", #hastes[" + de + "].d=" + tamD + ", #hastes[" + para + "].d=" + tamP;
      System.err.println("TowersPanel.moveDisco(...): error trying to register the movement " + de + "->" + para + " (" + topTo + "," + topFrom + "): " + strE + "\n: " + e);
      e.printStackTrace();
      }
    De=-1; Para=-1;
    }


  private static String haste (int i) {
    if (i==0) return "A";
    if (i==1) return "B";
    return "C";
    }


  private void retardo () {
    // Um retardo para dar tempo de visualizar movimentacao automatica
    double aux = 1;
    int j;
    for (j=0; j<TIME; j++)
        aux += Math.sin(2)+Math.cos(3); // consumir tempo: retardo
    }


  // public boolean action (Event event, Object arg)
  // Chamado de: 
  public void actionPerformed (ActionEvent event) {
    String strArg = event.getActionCommand();
    Object object = event.getSource();

    //D System.out.println("TowersPanel.actionPerformed(...): strArg=" + strArg + ": hastes.topo=(" + hastes[0].topo + "," + hastes[1].topo + "," + hastes[2].topo + "), De,Para=" + De + "," + Para);
    if (strArg.equals("A")) {
       if (De==-1) { // haste A e origem
          this.acertaOrigemDestino(0, -1, "A", " "); // De=0; ihanoiPanel.setTextFrom("A"); ihanoiPanel.setTextTo(" ");
          return;
          }
       else { // haste A e de destino
          int topTo = hastes[0].topo, topFrom = hastes[De].topo; 
          Para = 0;
          if (De==Para) { // destino igual origem => nada
             this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
             return;
             }
          if (topTo>-1 && topFrom>-1) {
             if (hastes[0].d[topTo] !=-1 && hastes[0].d[topTo] < hastes[De].d[topFrom]) {// disco em A < disco em De
                // moveMaiorSobreMenor = Lamento, tentando colocar disco maior sobre menor
                ihanoiPanel.setTextTmsg(ResourceReader.read("moveMaiorSobreMenor") + hastes[De].d[topFrom] + "->" + hastes[0].d[topTo]);
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                return;
                }
             }
          else
             if (topFrom==-1) { // Ignora: vazio -> A
             this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
             return;
             }

          this.moveDisco("A" , De, 0, topTo, topFrom); // Move disco: De -> A

          return;
          }
       } // if (strArg.equals("A"))
    else
    if (strArg.equals("B")) {
       if (De==-1) {
          this.acertaOrigemDestino(1, -1, "B", " "); // De=1; ihanoiPanel.setTextFrom("B"); ihanoiPanel.setTextTo(" "); 
          return;
          }
       else {// haste B e de destino
          int topTo = hastes[1].topo, topFrom = hastes[De].topo; 
          Para = 1;
          if (De==Para) { // destino igual origem => nada
             this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
             return;
             }
          if (topTo>-1 && topFrom>-1) {
             if (hastes[1].d[topTo]!=-1 && hastes[1].d[topTo ] < hastes[De].d[topFrom]) {//disco em B < disco em De
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                ihanoiPanel.setTextTmsg(ResourceReader.read("moveMaiorSobreMenor")); // Lamento, tentando ...
                return;
                }
             }
          else
          if (topFrom==-1) { // Ignora: vazio -> B
             this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
             return;
             }

          this.moveDisco("B" , De, 1, topTo, topFrom); // Move disco: De -> B

          if (hastes[1].topo==Ndiscos-1) { // final de movimentacao
             long paux = pot(2, Ndiscos) - 1;
             if (Nmovimentos==paux) {
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                // moveFimParabensMas = Parabens! Conseguiu mover os $N$ discos com o minimo ($N$),
                //                      mas a haste destino e a C
                String [] strVet = { "" + Ndiscos, "" + Nmovimentos }; // $N$
                ihanoiPanel.setTextTmsg(ResourceReader.readVar("moveFimParabensMas","N",strVet));
                // Nmovimentos=0; atualiza();// No applet NÃO deve ficar !
                }
             else {
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                // moveFimExcessoMas = Usou $N$ movimentos quando poderia ter usado menos!
                //                     Alem disso, haste destino deve ser C
                String [] strVet = { "" + Nmovimentos }; // $N$
                ihanoiPanel.setTextTmsg(ResourceReader.readVar("moveFimExcessoMas", "N", strVet));
                }
             ihanoiPanel.brever_setEnabled(true); // permite rever movimentos
             }
          return;
          }
       } // else if (strArg.equals("B"))
    else
    if (strArg.equals("C")) {
       if (De==-1) {
          this.acertaOrigemDestino(2, Para, "C", " "); // De=2; ihanoiPanel.setTextFrom("C"); ihanoiPanel.setTextTo(" ");
          return;
          }
       else {// haste C e de destino
          int topTo = hastes[2].topo, topFrom = hastes[De].topo;
          Para = 2;
          if (De == Para) { // destino igual origem => nada
             this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
             return;
             }
          if (topTo>-1 && topFrom>-1) {
             if (hastes[2].d[topTo]!=-1 && hastes[2].d[topTo] < hastes[De].d[topFrom]) {//disco em C < disco em De
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                ihanoiPanel.setTextTmsg(ResourceReader.read("moveMaiorSobreMenor")); // Lamento, tentando ...
                return;
                }
             }
          else if (topFrom==-1) { // Ignora: vazio -> C
                  this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                  return;
                  }

          this.moveDisco("C" , De, 2, topTo, topFrom); // Move disco: De -> C

          if (hastes[2].topo==Ndiscos-1) { // final de movimentacao
             long paux = pot(2, Ndiscos) - 1;
             if (Nmovimentos==paux) {
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                // "Parabens!! Conseguiu mover os $N$ discos com o minimo de movimentos ($N$)
                String [] strVet = { "" + Ndiscos, "" + Nmovimentos }; // $N$
                ihanoiPanel.setTextTmsg(ResourceReader.readVar("moveFimParabens","N",strVet));
                }
             else {
                this.acertaOrigemDestino(-1, -1, " ", " "); // De=-1; Para=-1; ihanoiPanel.setTextFrom(" "); ihanoiPanel.setTextTo(" ");
                String [] strVet = { "" + Nmovimentos, "" + Ndiscos };
                ihanoiPanel.setTextTmsg(ResourceReader.readVar("moveFimExcesso","N",strVet));
                }
             ihanoiPanel.brever_setEnabled(true); // permite rever movimentos
             } // if (hastes[2].topo==Ndiscos-1)
           return;
           }
       } // if (strArg.equals("C"))

    } // public void actionPerformed(ActionEvent event)


  } // public class TowersPanel extends Panel implements ActionListener
