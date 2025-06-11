/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import cadastroserver.exceptions.NonexistentEntityException;
import cadastroserver.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Venda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Compra;
import model.Produto;

/**
 *
 * @author enild
 */
public class ProdutoJpaController implements Serializable {

    public ProdutoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) throws PreexistingEntityException, Exception {
        if (produto.getVendaCollection() == null) {
            produto.setVendaCollection(new ArrayList<Venda>());
        }
        if (produto.getCompraCollection() == null) {
            produto.setCompraCollection(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Venda> attachedVendaCollection = new ArrayList<Venda>();
            for (Venda vendaCollectionVendaToAttach : produto.getVendaCollection()) {
                vendaCollectionVendaToAttach = em.getReference(vendaCollectionVendaToAttach.getClass(), vendaCollectionVendaToAttach.getIdVenda());
                attachedVendaCollection.add(vendaCollectionVendaToAttach);
            }
            produto.setVendaCollection(attachedVendaCollection);
            Collection<Compra> attachedCompraCollection = new ArrayList<Compra>();
            for (Compra compraCollectionCompraToAttach : produto.getCompraCollection()) {
                compraCollectionCompraToAttach = em.getReference(compraCollectionCompraToAttach.getClass(), compraCollectionCompraToAttach.getIdCompra());
                attachedCompraCollection.add(compraCollectionCompraToAttach);
            }
            produto.setCompraCollection(attachedCompraCollection);
            em.persist(produto);
            for (Venda vendaCollectionVenda : produto.getVendaCollection()) {
                Produto oldIdProdutoOfVendaCollectionVenda = vendaCollectionVenda.getIdProduto();
                vendaCollectionVenda.setIdProduto(produto);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
                if (oldIdProdutoOfVendaCollectionVenda != null) {
                    oldIdProdutoOfVendaCollectionVenda.getVendaCollection().remove(vendaCollectionVenda);
                    oldIdProdutoOfVendaCollectionVenda = em.merge(oldIdProdutoOfVendaCollectionVenda);
                }
            }
            for (Compra compraCollectionCompra : produto.getCompraCollection()) {
                Produto oldIdProdutoOfCompraCollectionCompra = compraCollectionCompra.getIdProduto();
                compraCollectionCompra.setIdProduto(produto);
                compraCollectionCompra = em.merge(compraCollectionCompra);
                if (oldIdProdutoOfCompraCollectionCompra != null) {
                    oldIdProdutoOfCompraCollectionCompra.getCompraCollection().remove(compraCollectionCompra);
                    oldIdProdutoOfCompraCollectionCompra = em.merge(oldIdProdutoOfCompraCollectionCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduto(produto.getIdProduto()) != null) {
                throw new PreexistingEntityException("Produto " + produto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto = em.find(Produto.class, produto.getIdProduto());
            Collection<Venda> vendaCollectionOld = persistentProduto.getVendaCollection();
            Collection<Venda> vendaCollectionNew = produto.getVendaCollection();
            Collection<Compra> compraCollectionOld = persistentProduto.getCompraCollection();
            Collection<Compra> compraCollectionNew = produto.getCompraCollection();
            Collection<Venda> attachedVendaCollectionNew = new ArrayList<Venda>();
            for (Venda vendaCollectionNewVendaToAttach : vendaCollectionNew) {
                vendaCollectionNewVendaToAttach = em.getReference(vendaCollectionNewVendaToAttach.getClass(), vendaCollectionNewVendaToAttach.getIdVenda());
                attachedVendaCollectionNew.add(vendaCollectionNewVendaToAttach);
            }
            vendaCollectionNew = attachedVendaCollectionNew;
            produto.setVendaCollection(vendaCollectionNew);
            Collection<Compra> attachedCompraCollectionNew = new ArrayList<Compra>();
            for (Compra compraCollectionNewCompraToAttach : compraCollectionNew) {
                compraCollectionNewCompraToAttach = em.getReference(compraCollectionNewCompraToAttach.getClass(), compraCollectionNewCompraToAttach.getIdCompra());
                attachedCompraCollectionNew.add(compraCollectionNewCompraToAttach);
            }
            compraCollectionNew = attachedCompraCollectionNew;
            produto.setCompraCollection(compraCollectionNew);
            produto = em.merge(produto);
            for (Venda vendaCollectionOldVenda : vendaCollectionOld) {
                if (!vendaCollectionNew.contains(vendaCollectionOldVenda)) {
                    vendaCollectionOldVenda.setIdProduto(null);
                    vendaCollectionOldVenda = em.merge(vendaCollectionOldVenda);
                }
            }
            for (Venda vendaCollectionNewVenda : vendaCollectionNew) {
                if (!vendaCollectionOld.contains(vendaCollectionNewVenda)) {
                    Produto oldIdProdutoOfVendaCollectionNewVenda = vendaCollectionNewVenda.getIdProduto();
                    vendaCollectionNewVenda.setIdProduto(produto);
                    vendaCollectionNewVenda = em.merge(vendaCollectionNewVenda);
                    if (oldIdProdutoOfVendaCollectionNewVenda != null && !oldIdProdutoOfVendaCollectionNewVenda.equals(produto)) {
                        oldIdProdutoOfVendaCollectionNewVenda.getVendaCollection().remove(vendaCollectionNewVenda);
                        oldIdProdutoOfVendaCollectionNewVenda = em.merge(oldIdProdutoOfVendaCollectionNewVenda);
                    }
                }
            }
            for (Compra compraCollectionOldCompra : compraCollectionOld) {
                if (!compraCollectionNew.contains(compraCollectionOldCompra)) {
                    compraCollectionOldCompra.setIdProduto(null);
                    compraCollectionOldCompra = em.merge(compraCollectionOldCompra);
                }
            }
            for (Compra compraCollectionNewCompra : compraCollectionNew) {
                if (!compraCollectionOld.contains(compraCollectionNewCompra)) {
                    Produto oldIdProdutoOfCompraCollectionNewCompra = compraCollectionNewCompra.getIdProduto();
                    compraCollectionNewCompra.setIdProduto(produto);
                    compraCollectionNewCompra = em.merge(compraCollectionNewCompra);
                    if (oldIdProdutoOfCompraCollectionNewCompra != null && !oldIdProdutoOfCompraCollectionNewCompra.equals(produto)) {
                        oldIdProdutoOfCompraCollectionNewCompra.getCompraCollection().remove(compraCollectionNewCompra);
                        oldIdProdutoOfCompraCollectionNewCompra = em.merge(oldIdProdutoOfCompraCollectionNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produto.getIdProduto();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getIdProduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            Collection<Venda> vendaCollection = produto.getVendaCollection();
            for (Venda vendaCollectionVenda : vendaCollection) {
                vendaCollectionVenda.setIdProduto(null);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
            }
            Collection<Compra> compraCollection = produto.getCompraCollection();
            for (Compra compraCollectionCompra : compraCollection) {
                compraCollectionCompra.setIdProduto(null);
                compraCollectionCompra = em.merge(compraCollectionCompra);
            }
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
