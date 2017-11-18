/**
 * iGeom :: Interactive/Internet Geometry
 * iMa'tica Project - LEM (Laboratory of Math Teaching/Learning)
 * http://www.matematica.br
 *
 * @author Leo^nidas de Oliveira Branda~o
 * @description Window to block execution, waiting for some action
 * @see igeom/script/ComentariosDialogo.java: estende 'WarningDialog'
 *
 * @see igeom/ig/JanelaArqExistente.java - estende 'WarningDialog.java'
 * @see igeom/ig/JanelaConfig.java - estende 'WarningDialog.java'
 * 
 **/

package ihanoi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class WarningDialog extends Dialog implements KeyListener, FocusListener {   

  private static final int ALTURA = 11; // utilizada para fazer altura da janela proporcional ao número de msgs

  private static final int larg = 550, largAdicional = 195,
                           alt  = 120; // 

  private int altComplConf = 0, // if comes from extension 'JanelaConfig.java' => use more lines
              lagComplConf = 0;

  protected Button ok;

  protected Button
     buttonYes, buttonNo, buttonCancel;

  private int action = 1; // used to cancel iGeom closing action: 1 => usual close; -1 => cancel iGeom closing action
  public int getAction () { return action; } // used to cancel iGeom closing action

  // Main Panel inside WarningDialog
  Panel firstPanel = new Panel(new FlowLayout());

  String strMax = ""; // maior string

  public void focusOK () {
    ok.requestFocus();	
    } 

  // Tambem chamado de 'JanelaArqExistente'; igeom/script/ComentariosDialogo.java
  public  void setVisibleFront () {
    this.setVisible(true);
    this.toFront();
    }
   

  // Chamado via: gravao de arquivo HTML
  public WarningDialog (iHanoi ihanoi, String comentario, String nome, boolean ehHTML) {
    super(ihanoi.getFrame(), ResourceReader.read("dialAttention") + ": " + nome, true);
    String [] strC = new String[1];
    strC[0] = comentario;
    montaWarning(strC);
    addWindowListener(new WindowAdapter() { // para poder fechar a janela com clique sobre o X
            public void windowClosing(WindowEvent evt) { fecha(); }
            } );
    focusOK();
    }


  // ihanoi/ihanoi.java
  public WarningDialog (iHanoi ihanoi, String comentario, String nome) {
    super(ihanoi.getFrame(), ResourceReader.read("dialAttention") + ": " + nome, false); //D  false
    String [] strC = new String[1];
    strC[0] = comentario;
    montaWarning(strC);

    addWindowListener(new WindowAdapter() { // para poder fechar a janela com clique sobre o X
       public void windowClosing(WindowEvent evt) { fecha(); }
       } );
    focusOK();
    setVisibleFront();
    }

  // Chamado em: ihanoi/util/Exercicio.esconda(); ihanoi/script/ComentariosDialogo.ComentariosDialogo(Interpretador inter, String comment)
  public WarningDialog (String [] comentarios) {
    super(new Frame(), ResourceReader.read("dialAttention"), true);
    montaWarning(comentarios);
    setVisibleFront();
    }

  // ihanoi/util/Exercicio: ... public void Corretor(AreaDeDesenho area_desenho, Construcao crt, Lista listaAluno, String codigoScr) 
  // ihanoi/nucleo/TrataMouse.java
  public WarningDialog (String titulo, String comentario) {
    super(new Frame(), ResourceReader.read("dialAttention") + ": " + titulo, true);
    String [] strC = new String[1];
    strC[0] = comentario;
    montaWarning(strC);
    // Em 'JFrame frame' esta definido 'frame.setAlwaysOnTop(true);'
    setVisibleFront(); 
    }

  // ihanoi/util/Exercicio: 
  public WarningDialog (String titulo, String [] comentarios) {
    super(new Frame(), ResourceReader.read("dialAttention") + ": " + titulo, true);
    montaWarning(comentarios);
    setVisibleFront();
    }
   
  // Chamado em: Exercicio.esconda(); ihanoi.ig.JanelaArqExistente(); ihanoi/ig/JanelaAdicionarScript.java; ihanoi/ig/JanelaRemoverScript.java; ihanoi/ig/PainelComm.java; ADD
  public WarningDialog (String comentario) {
    super(new Frame(), ResourceReader.read("dialAttention"), true);
    String [] strC = new String[1];
    strC[0] = comentario;
    montaWarning(strC);
    setVisibleFront();
    }

  // Chamado em: ihanoi/script/ComentariosDialogo.java: construtor
  public WarningDialog (String comentario, boolean b1, boolean b12) {
    super(new Frame(), ResourceReader.read("dialAttention"), true);
    String [] strC = new String[1];
    strC[0] = comentario;
    montaWarning(strC);
    }

  // Chamado em: ihanoi/ig/JanelaConfig.java: construtor;
  public WarningDialog (String comentario, boolean bool) {
    super(new Frame(), ResourceReader.read("dialAttention"), true);
    String [] strC = new String[1];
    strC[0] = comentario;
    altComplConf = 400;
    lagComplConf = 300;
    addBotaoOK();
    this.setLayout(new BorderLayout());
    firstPanel.add(ok,"East");
    setSize(lagComplConf, altComplConf);
    focusOK();
    }


  // @see: ihanoi/ihanoi: fechaihanoi() when there exists a tab with construction not saved
  //       You do not save the construction under tab i. If you close ihanoi this construction will be lost
  //       Are you sure that ihanoi must be closed?
  // @see: ihanoi/ig/AreaDeDesenho: keyPressed(KeyEvent): any DEL with script registering => remove will cancel the script (user must choose)
  public WarningDialog (iHanoi ihanoi, String [] strMsgs) {
    super(ihanoi.getFrame(), ResourceReader.read("dialAttention"), true); //ATTENTION: must be MODAL !!!!
    setBackground(iHanoi.fundo_azul_claro);
    setForeground(Color.white);

    buttonYes = new Button(ResourceReader.read("buttonYes"));
    buttonNo = new Button(ResourceReader.read("buttonNo"));

    buttonYes.addKeyListener(this); // user agree that all construction not saved are going to be lost
    buttonYes.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { botaoOk(); } });

    buttonNo.addKeyListener(this); // user 
    buttonNo.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { action = -1; botaoOk(); } });

    buttonNo.addFocusListener(this);

    // Build options' buttons
    firstPanel.add(buttonNo); // ("West",buttonNo);
    firstPanel.add(buttonYes); // ("East",buttonYes);

    Button [] buttons = { buttonYes, buttonNo };
    putMessages(strMsgs); // complete the panel
    buttonNo.requestFocus();

    // put the inferior panel: with buttons
    this.add("South", firstPanel); // this.add(firstPanel,BorderLayout.SOUTH);//

    setVisibleFront();

    } // WarningDialog(iHanoi ihanoi, String [] strMsgs)


  private void addBotaoOK () {
    setBackground(iHanoi.fundo_azul_claro); // fundo_menu_primario 
    setForeground(Color.white); 

    ok = new Button("Ok");
    ok.addFocusListener(this);
    ok.addKeyListener(this);
    ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) { botaoOk(); }
        });
    }


  private void putMessages (String [] comentarios) {
    //RR 1
    if (comentarios==null)
       comentarios = new String[] { "<" + ResourceReader.read("msgVazio") + ">" };
    int numL = comentarios.length; 
    this.setLayout(new BorderLayout());

    //RR 2
    Panel secondPanel = new Panel(new GridLayout(numL,1));
    Label [] vet_lab = new Label[numL];
    int max = 0;
    for (int i=0; i<numL; i++) {
        vet_lab[i] = new Label(comentarios[i]);
        vet_lab[i].setFont(iHanoi.ftPlain10);
	if (comentarios[i].length() > max) { max = comentarios[i].length(); strMax = comentarios[i]; }
        secondPanel.add(vet_lab[i],i);
        }
    this.add("Center", secondPanel);

    //RR 3
    this.setFont(iHanoi.ftPlain10);

    FontMetrics fm = this.getFontMetrics(this.getFont());
    int ma = fm.getMaxAscent(), md = fm.getMaxDescent(), h = fm.getAscent() - fm.getDescent(), leading = fm.getLeading();
    int lfm = fm.stringWidth(strMax);

    lfm = fm.stringWidth(strMax) + largAdicional; //
    this.setSize(lfm, alt + (ALTURA*numL));

    } // void putMessages(String [] comentarios)
   

  private void montaWarning (String [] comentarios) {
    addWindowListener(new WindowAdapter() { // para poder fechar a janela com clique sobre o X
            public void windowClosing(WindowEvent evt) { fecha(); }
            } );
    //RR 1
    putMessages(comentarios);

    addBotaoOK();
    firstPanel.add(ok);

    //RR 2

    this.add("South", firstPanel);

    //RR 3

    focusOK();

    }

  protected void botaoOk () {
    setTitle("OK");
    // System.out.print("WarningDialog: OK");
    // removeNotify(); setVisible(false);
    // dispose(); <- é lento para responder no Java 1.4 BlackDown
    // System.out.println(" fim");
    fecha();
    }

  protected void fecha () {
    this.setVisible(false); dispose(); // <- é lento para responder no Java 1.4 BlackDown
    }
   

  // Process keyboard command (ENTER)
  public synchronized void keyPressed (KeyEvent keyevent) {// é extented em JanelaArqExistente
    int keyCode = keyevent.getKeyCode();
    if (keyCode == KeyEvent.VK_ENTER) {
       Button botao_ativo = (Button) keyevent.getSource();
       if ( botao_ativo == ok ) botaoOk();
       else if ( botao_ativo == buttonYes ) botaoOk();
       else if ( botao_ativo == buttonNo ) { action = -1; botaoOk(); }
       }
    }

  public void keyTyped (KeyEvent keyevent) { // is extented in JanelaArqExistente
    int keyCode = keyevent.getKeyCode();
    if ( keyCode == KeyEvent.VK_ENTER) {
       Button botao_ativo = (Button) keyevent.getSource();
       if ( botao_ativo == ok ) botaoOk();
       }     
    }
   
  public final void keyReleased (KeyEvent keyevent) {
    }
   
  public void focusGained (FocusEvent focusevent) {
    }

  public void focusLost (FocusEvent focusevent) {
    }

  }
