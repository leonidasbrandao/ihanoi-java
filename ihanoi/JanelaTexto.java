
/**
 * iHanoi - http://www.matematica.br/ihanoi
 * LInE - http://www.ime.usp.br/line ; http://www.usp.br/line
 * Tools to teach and learn
 * 
 * @author Leônidas de Oliveira Brandão
 * @see ihanoi/TowersPanel.java: 'reverTodosOsMovimentos()' creates and calls this Frame class
 * 
 * @description This window (java.awt.Frame) is to presents the sequence of movements achieved by the learner.
 *
 **/

package ihanoi;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class JanelaTexto extends Frame implements KeyListener, FocusListener {

  private static int NUMBEROFLINES = 30; // number of lines in the text area (for iHanoi movements)

  private static int altComp = 20;  // para altura do TextField com endereço "http://www.matematica.br"
  private static int largura, altura;

  public  static final int alturaMin = 40,   // minimal height to the logo at top of this frame
                           altura_topo = 40; // height of Painel_topo

  private static final Color fundoTopo = Color.white;

  Panel painel_princ,  painel_fundo;//
  Painel_topo painel_topo;
  TextArea Texto; //TextField Texto; //
  Label msgTitulo;
  String strMsgAdicional = null;

  private Dialog Dfim;
  Button _botaoOk;

  Color //
        fundo_menu_primario   = iHanoi.corFP, // ihanoi/iHanoi.java: corFP=new Color(40, 90, 200) // cor p/ fundo barra menu primária
        corF                  = Color.white,
        fundo_menu_secundario = iHanoi.corH; // ihanoi/iHanoi.java: corH=new Color(60, 90, 150)

  public JanelaTexto (Component comp, int largura, int altura, String msgTexto) {
    super(ResourceReader.read("ihanoi"));
    this.largura = largura;
    this.altura  = altura;
    addWindowListener( new WindowAdapter() { // substitui o "deprecated" WINDOW_DESTROY
      public void windowClosing(WindowEvent e) { fecha(); }
      });
    montaJanela(msgTexto);
    }


  // fecha janela
  public void fecha () {
    this.setVisible(false); // evita lentidão ao fechar janela em Java 4
    dispose();
    }


  // igeom/util/EnviaTexto!leiaDadosWeb(String,String): para alterar texto na janela
  public void setTexto (String strT) {
    Texto.setText(strT);
    }

  // Monta a janela: Msg guarda o código a ser exibido
  private void montaJanela (String Msg) {     
    // System.out.println("[igeom/ig/JanelaTexto] montatexto("+Msg+")");
    // try { String str=null; System.out.println(str.charAt(3)); }
    // catch (Exception e) { e.printStackTrace(); }

    painel_topo = new Painel_topo(); //
    painel_princ= new Panel();
    painel_fundo= new Panel();

    setBackground(fundoTopo); // Color.white

    Texto = new TextArea(Msg, NUMBEROFLINES, 48); // TextArea(Msg, 40, 48); // para font 10
    Texto.setEditable(false); // Texto.setBackground(corF);
    //                         1  2   :  1 é núm. de linha e 2 núm. de colunas
    //Texto.setEnabled(true); // para tentar fazer c/ q/ no Windows o fundo continue escuro...

    // Problemas c/ cor de fundo no Window - comentando o "setForeground", a cor de fundo vai errada, mas ao menos as letras fica legíveis...
    Texto.setBackground(fundo_menu_secundario);//Texto.setBackground( fundo_menu_primario ); 
    Texto.setForeground(fundoTopo);
    Texto.setFont(iHanoi.ftPlain8);   //

    setForeground(fundo_menu_secundario);      //setForeground( fundo_menu_secundario );

    _botaoOk = new Button("OK"); _botaoOk.setSize(10,20); 
    _botaoOk.setFont(iHanoi.FONTE1);
    _botaoOk.setBackground(fundoTopo);
    _botaoOk.addFocusListener(this); // <----
    _botaoOk.addKeyListener(this); // <----
    _botaoOk.addActionListener( new ActionListener() {
       public void actionPerformed(ActionEvent e) { botaoOk(); }
       });

    painel_fundo.add(_botaoOk);

    painel_princ.setLayout(new BorderLayout());
    painel_princ.add(Texto, BorderLayout.CENTER);         //  [ Movements achieved... ]
    painel_princ.add(painel_fundo, BorderLayout.SOUTH);   //  [          [OK]         ]

    this.setLayout(new BorderLayout());
    this.add(painel_topo, BorderLayout.NORTH); // CENTER
    this.add(painel_princ, BorderLayout.SOUTH);

    this.setSize(largura, altura+altComp); // vem do ihanoi.iHanoi: largMov, altMov

    }

  protected void botaoOk () {
    fecha();
    }

  public synchronized void keyPressed (KeyEvent keyevent) {// é extented em JanelaArqExistente
    int keyCode = keyevent.getKeyCode();
    if ( keyCode == KeyEvent.VK_ENTER) {
       Button botao_ativo = (Button) keyevent.getSource();
       if ( botao_ativo == _botaoOk ) { botaoOk(); }
       }
    }   

  public void keyTyped (KeyEvent keyevent) { } // é extented em JanelaArqExistente

  public final void keyReleased(KeyEvent keyevent) {
    }

  public void focusGained (FocusEvent focusevent) {
    }

  public void focusLost (FocusEvent focusevent) {
    }


  // Painel do topo da janela, com o 
  public class Painel_topo extends Panel {
    String Msg1 = ResourceReader.read("ihanoi"), //
           Msg2 = "http://www.matematica.br/";
    Panel localTopPanel;
    Panel painelMsg;
    Label labelVersao = new Label(ResourceReader.read("movVersao") + ": " + iHanoi.VERSION);
    TextField Ttexto2 = new TextField(Msg2, NUMBEROFLINES);

    Painel_topo () {
      Label Ltexto1 = new Label(Msg1);
      Ltexto1.setFont(iHanoi.FONTE1); // new Font("Helvetica", Font.BOLD, 10)

      localTopPanel = new Panel(); //
      localTopPanel.setLayout(new BorderLayout());
      localTopPanel.add(BorderLayout.WEST, Ltexto1);

      setLayout(new BorderLayout());

      setForeground(fundo_menu_secundario);
      Ttexto2.setEditable(false);
      Ttexto2.setFont(iHanoi.ftPlain10); // new Font("Helvetica", Font.BOLD, 8)

      painelMsg = new Panel();
      painelMsg.setLayout(new BorderLayout());
      Label labelMsgAdicional = null;
      labelVersao.setFont(iHanoi.ftPlain11); // 
      painelMsg.add("North",labelVersao); // South

      if (strMsgAdicional!=null) {
         labelMsgAdicional = new Label(strMsgAdicional);
         labelMsgAdicional.setFont(iHanoi.ftPlain10);
         painelMsg.add("Center",labelMsgAdicional);
         }

      setLayout(new BorderLayout());

      this.add("North", localTopPanel);
      this.add("Center", painelMsg); // labelVersao
      this.add("South", Ttexto2);
      }

    }

  }
