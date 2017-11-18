
/*
 * iHanoi : Problema das Torres de Hano'i na Internet
 * iMatica: http://www.matematica.br
 * LInE : "L"aboratory of "I"nformatics i"n" "E"ducation : http://line.ime.usp ; http://www.usp.br/line
 * 
 * @author Leo^nidas de Oliveira Branda~o
 * @description About iHanoi
 * @see ihanoi/iHanoi.java
 * @version
 *  2017/10/01 : new line (last with "LInE - http://line.ime.usp.br")
 *  2016/08/01 : new layout
 *  2008/05    : internationalization (messages in a file)
 */

package ihanoi;

import java.awt.Button;
import java.awt.Container;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogSobre extends Dialog implements ActionListener {

  // Width and height: see ihanoi/iHanoi: LARG_SOBRE=590, ALT_SOBRE=390

  static final int DimL = iHanoi.SOBRE_LNH + 4, DimD = 40;
  private TextArea Texto;

  private void montaJanela (String msg) {
    this.setLayout(new java.awt.BorderLayout()); //layout
    this.setSize(iHanoi.LARG_SOBRE, iHanoi.ALT_SOBRE);
    this.setBackground(iHanoi.corH);

    Panel Pmsg = new Panel();
    Pmsg.setLayout(new java.awt.BorderLayout()); //layout
    Pmsg.setBackground(iHanoi.corH);

    Texto = new TextArea(msg, iHanoi.SOBRE_LNH, iHanoi.SOBRE_COL);
    Texto.setEditable(false);
    Texto.setBackground(iHanoi.corF);
    Texto.setForeground(Color.white);
    Pmsg.add(Texto);

    Panel Pok = new Panel();
    Pok.setLayout(new java.awt.FlowLayout()); //layout
    Button Bfim = addButton(Pok, this, "OK");
    Bfim.setForeground(Color.white);

    this.setFont(iHanoi.FONTE_SOBRE); //
    this.add(Pmsg, java.awt.BorderLayout.CENTER);
    this.add(Pok, java.awt.BorderLayout.SOUTH);
    }

  // Construtor
  public DialogSobre (Frame parent, String msg) {
    super(parent, ResourceReader.read("torres"), true);         
    montaJanela(msg);
    addWindowListener(new WindowAdapter() { public void
       windowClosing(WindowEvent e) {
         // System.out.println("Fim");
         setVisible(false); }
       } ); }

  public static Button addButton (Container p, ActionListener contAction, String name) {
    Button b = new Button(name);
    b.setSize(DimL, DimD);
    b.addActionListener(contAction);
    p.add(b);
    return b;
    }

  public void actionPerformed (ActionEvent evt) {
    String arg = evt.getActionCommand();
    //System.out.println("action: "+arg);
    if (arg.equals("OK")) { //  } // Button Bfim
       this.setVisible(false);
       }
    else if (arg.equals("Cancel"))
       this.setVisible(false);
    }

  public void showDialog () {
    this.show();
    }

  }