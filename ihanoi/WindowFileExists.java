/*
 * iHanoi - Interactive/Internet Tangram: http://www.matematica.br/ihanoi
 * 
 * Free interactive solutions to teach and learn
 * 
 * iMath Project: http://www.matematica.br
 * LInE           http://line.ime.usp.br
 *
 * @author  Leo^nidas de Oliveira Branda~o
 * 
 * @description Utilities to manage files (from hard disc and from Web)
 * 
 * @see     ihanoi/util/ProcessReadWrite.java
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

import java.awt.*;
import java.awt.event.*;


public class WindowFileExists extends WarningDialog implements KeyListener {

  private iHanoiPanel ihanoiPanel;

  protected Button botaoCancela;
  private boolean eh_cancela = false;
  public boolean eh_cancela () { return eh_cancela; }

  private static String nome;
  private int ihn_txt_html; // 0 -> IHN; 1 -> TXT; 2 -> HTML; 3 -> PS; 4 -> GIF

  // Para definir nome do arquivo a ser gravado
  // Chamado em: IGeom.processaMenu(Object obj, String s)
  public static void setNome (String nomeNovo) { nome = nomeNovo; }

  public WindowFileExists (iHanoi ihanoi, String comentario, int ihn_txt_html) {
    super(ihanoi, comentario, ResourceReader.read("wfeFileExists"), true); // File already exists
    this.ihanoiPanel = ihanoi.ihanoiPanel;

    if (nome=="" || nome==null || nome.length()==0)
      nome = ihanoi.getLastFileRecorded(); // iHanoiPanel: getLastFileRecorded(): String strLastFileRecorded

    setTitle(ResourceReader.read("wfeFileName") + " " + nome); // name of file
    ok.requestFocus(); 

    botaoCancela = new Button(ResourceReader.read("wfeCancel")); // "Cancel"
    botaoCancela.addKeyListener(this);
    botaoCancela.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        String string = e.getActionCommand();
        if (string.equals(ResourceReader.read("wfeCancel"))) { // "Cancel"
           botaoCancela();
           }
        //D else
        //D if (string.equals(ResourceReader.read("wfeCancel"))) { // "Cancel"
        //D    botaoOk();
        //D    }
        }
      });
    super.firstPanel.add(botaoCancela); // painel base em WarningDialog
    setVisibleFront();
    }

  // @see ProcessReadWrite.java: processWriteFile(iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame, String strContent)
  protected void botaoOk () {
    int resp = -2;

    // 0 -> IHN; 1 -> TXT; 2 -> HTML; 3 -> PS; 4 -> GIF
    switch (ihn_txt_html) {
      case 0: // it is IHN (iHanoi format), use 'ProcessReadWrite.processWriteFile(iHanoi ihanoi, iHanoiPanel ihanoiPanel, Frame frame, String strContent)'
              break;
      case 1: System.out.println("WindowFileExists.botaoOk(): FALTA implementar TXT"); // TXT
              // resp = { 1 => sucesso; -1 => erro }
              String [] param = { nome };
              String str = "";
              if (resp==1)
                 str = "WindowFileExists.botaoOk(): FALTA implementar 1";
              else
                 str = "WindowFileExists.botaoOk(): FALTA implementar 2";
              ihanoiPanel.setTextTmsg(str);
              System.out.println(str);
              break;
      //case 2: ihanoi.buildApplet(nome);
      //        break;
      // o PrintJob already has its own test for "duplicated file" => from PJ do not reach here!
      //case 4: ihanoiPanel.setTextTmsg(ResourceReader.read("msgMenuExportaGIF")+" "+nome+ResourceReader.read("msgMenuExportaGIF_")); //
      //        ihanoi.buildGif(nome);
      //        break;
      }

    dispose();
    }

  protected void botaoCancela () {
    setTitle(ResourceReader.read("wfeCancel")); // "cancel"
    eh_cancela = true;
    if (ihn_txt_html == 1) {
       //System.out.println("WindowFileExists: cancel");
       }
    dispose();
    return;
    }
    
  public void keyTyped (KeyEvent keyevent) {
    int keyCode = keyevent.getKeyCode();
    Button botao_ativo = (Button) keyevent.getSource();
    if ( keyCode == KeyEvent.VK_ENTER) {
       //System.out.println("KeyTyped: "+ paramString());
       if ( botao_ativo==botaoCancela ) botaoCancela();
       }
    }
    
  public void keyPressed (KeyEvent keyevent) {
    int keyCode = keyevent.getKeyCode();
    Button botao_ativo = (Button) keyevent.getSource();
    if ( keyCode == KeyEvent.VK_ENTER) {
       //if ( keyevent.isActionKey() ) { }
       //if ( botaoCancela.isFocusTraversable() ) { }
       if ( botao_ativo==botaoCancela ) {
          //System.out.println("KeyPressed: cancela.isFocusTraversable()="+ botaoCancela.isFocusTraversable());
          botaoCancela();
          }
       else 
       if ( botao_ativo==ok ) {
          botaoOk();
          }
    //else
    //if ( keyCode == KeyEvent.VK_LEFT) eh_cancela = !eh_cancela;
    //else
    //if ( keyCode == KeyEvent.VK_RIGHT) eh_cancela = !eh_cancela;
    //else
    //if ( keyCode == KeyEvent.VK_UP) eh_cancela = !eh_cancela;
    //else
    //if ( keyCode == KeyEvent.VK_DOWN) eh_cancela = !eh_cancela;
    }
  }

}



