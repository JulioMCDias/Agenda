/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agenda.control;

import br.com.agenda.model.Caderno;
import br.com.agenda.model.Contato;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Julio
 */
public class ControleTabelaContato {
    private final DefaultTableModel TContato;
    private final JTable jTableResultadoPesquisa;
    private List<Contato> contatos;
    private Caderno caderno;

    
    public ControleTabelaContato(DefaultTableModel dtm, JTable jt){
        //contatos = new ArrayList<>();
        this.TContato = dtm;
        this.jTableResultadoPesquisa = jt;
    }   

    public List<Contato> getContatos() {
        return contatos;
    }
       
    public void iniTContato(){
        try{
        while(TContato.getRowCount()>0){
            TContato.removeRow(0);
        }
        jTableResultadoPesquisa.revalidate();
        contatos.forEach((c) -> {
            TContato.addRow(new Object[]{c.getNome(),
                c.getTelefoneFixo(),
                c.getTelefoneCelular(),
                c.getEndereco(),
                c.getDataNacimento(),
                c.getCidade(),
                c.getEmail()});
            });
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao abrir os registros");    
        }
    }
    
    public void atualizarTcontato(){
        carregarTContato(caderno);
    }
    public void carregarTContato(Caderno c) {
        caderno = c;
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        try{
            contatos = (List<Contato>)s.createQuery("FROM Contato WHERE fk_id_Caderno = "+c.getIdCaderno()).list();
            iniTContato();
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar carregar os contatos");    
        }
        s.getTransaction().commit();
    }
    
    public void carregarContatosFiltro(String campo, String valor){
        String sql="FROM Contato WHERE fk_id_Caderno = "+caderno.getIdCaderno()+" AND "+campo+" LIKE '%"+valor+"%'";
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        try{
            contatos = (List<Contato>)s.createQuery(sql).list();
            iniTContato();
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar carregar os contatos");
        } 
        s.getTransaction().commit();
    }
    
    public void inserirContatos(Contato c){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();       
        try{
            c.setCaderno(caderno);
            s.save(c);
            s.getTransaction().commit();
            carregarTContato(caderno);
            JOptionPane.showMessageDialog(null, "Contato criado na agenda:"+caderno.getNome());   
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar criar contato");   
            s.getTransaction().commit();
        }
    }
    
    
    public void atualizarContatos(Contato c){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();          
        try{
            s.update(c);
            s.getTransaction().commit();
            carregarTContato(caderno);
            JOptionPane.showMessageDialog(null, "Contato salvado");
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o contato");  
            s.getTransaction().commit();
        } 
        
    }
    
    public void excluirContatos(int p){
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();          
        try{
            s.delete(contatos.get(p));
            s.getTransaction().commit();
            carregarTContato(caderno);
            JOptionPane.showMessageDialog(null, "Contato excluido na agenda:"+caderno.getNome());    
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar excluir contato"); 
            s.getTransaction().commit();
        } 
        
    }
}
