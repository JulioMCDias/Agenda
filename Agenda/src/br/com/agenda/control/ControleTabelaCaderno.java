/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.control;

import util.HibernateUtil;
import br.com.agenda.model.Caderno;
import br.com.agenda.view.CriarAgendaView;
import br.com.agenda.view.EscolherAgendaView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Julio
 */
public class ControleTabelaCaderno {
    private final DefaultTableModel TCaderno;
    private final JTable jTableAgendas;
    private final ControleAbasContato controlAbaCon;
    private List<Caderno> agendas;
    
    public ControleTabelaCaderno(DefaultTableModel dtm, JTable jt, ControleAbasContato cac){
        //agendas = new ArrayList<>();
        
        this.TCaderno = dtm;
        this.jTableAgendas = jt;
        this.controlAbaCon = cac;
    }

    public List<Caderno> getAgendas() {
        return agendas;
    }

    
    //-----------------atualiza Caderno-------------
    public void iniTCaderno(){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        try{
            agendas = (List<Caderno>)s.createQuery("FROM Caderno").list();
            while(TCaderno.getRowCount()>0)
                TCaderno.removeRow(0);
            jTableAgendas.revalidate();
            for(Caderno c:agendas)
                TCaderno.addRow(new Object[]{c.getNome()});
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar buscar as agendas");    
        }
        s.getTransaction().commit();
        controlAbaCon.bloquearLiberarCampos(false);
    }
    public void inserirCadernos(){
        CriarAgendaView criarAgenda = new CriarAgendaView();
        criarAgenda.setVisible(true);
        String cad = criarAgenda.getNome();
        criarAgenda.dispose();
        if((cad != null) && (!cad.equals(""))){
            Session s = HibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            try{
                s.save(new Caderno(cad));
                s.getTransaction().commit();
                iniTCaderno();
                JOptionPane.showMessageDialog(null, "Agenda criada");
                
            }catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao tentar criar a agenda");
                s.getTransaction().commit();
            }
        }else if((cad != null) && (cad.equals(""))){
            JOptionPane.showMessageDialog(null, "nome invalido");
        }
    }
    
    public void excluirCadernos(){
        EscolherAgendaView apagarAgenda = new EscolherAgendaView(agendas);
        apagarAgenda.setVisible(true);
        Caderno cad = apagarAgenda.getCaderno();
        apagarAgenda.dispose();
        if(cad != null){
            Session s = HibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();            
            try{
                s.delete(cad);
                s.getTransaction().commit();
                iniTCaderno();
                JOptionPane.showMessageDialog(null, "Agenda excluida");
            }catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao tentar excluir a agenda");  
                s.getTransaction().commit();
            }
        }       
    }
    public void atualizarCaderno(){
        EscolherAgendaView atualizarAgenda = new EscolherAgendaView(agendas);
        atualizarAgenda.setVisible(true);
        Caderno cadAtu = atualizarAgenda.getCaderno();
        atualizarAgenda.dispose();
        if(cadAtu !=null){
            CriarAgendaView nomeAgenda = new CriarAgendaView();
            nomeAgenda.setVisible(true);
            String cadNome = nomeAgenda.getNome();
            nomeAgenda.dispose();
            if(cadNome != null){
                Session s = HibernateUtil.getSessionFactory().getCurrentSession();
                s.beginTransaction();                
                try{
                    cadAtu.setNome(cadNome);
                    s.update(cadAtu);
                    s.getTransaction().commit();
                    iniTCaderno();
                    JOptionPane.showMessageDialog(null, "Agenda alterada");
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao tentar alterar a agenda");
                    s.getTransaction().commit();                   
                }            
            }
        } 
    }
    
}
