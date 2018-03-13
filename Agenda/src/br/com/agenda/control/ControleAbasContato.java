/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.control;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Julio
 */

public class ControleAbasContato {
    private final JPanel jPanelPesquisarContato;
    private final JPanel jPanelNovoContato;
    public ControleAbasContato(JPanel p,JPanel n){
        jPanelPesquisarContato = p;
        jPanelNovoContato = n;
    }
    
   //================== bloquearLiberarCampos ==========================
    public void bloquearLiberarCampos(boolean acao){
    Component[] componentes = jPanelPesquisarContato.getComponents(); // altere para o nome da variavel do seu painel
    JScrollPane jsp;
    for(int i= 0;i<2;i++){
        for (Component componente : componentes) {
            componente.setEnabled(acao);
        }
        componentes = jPanelNovoContato.getComponents();
    }
} 
}
