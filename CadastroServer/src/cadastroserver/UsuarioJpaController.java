package controller;

import cadastroserver.exceptions.NonexistentEntityException;
import cadastroserver.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Venda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Compra;
import model.Usuario;

public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getVendaCollection() == null) {
            usuario.setVendaCollection(new ArrayList<Venda>());
        }
        if (usuario.getCompraCollection() == null) {
            usuario.setCompraCollection(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Venda> attachedVendaCollection = new ArrayList<Venda>();
            for (Venda vendaCollectionVendaToAttach : usuario.getVendaCollection()) {
                vendaCollectionVendaToAttach = em.getReference(vendaCollectionVendaToAttach.getClass(), vendaCollectionVendaToAttach.getIdVenda());
                attachedVendaCollection.add(vendaCollectionVendaToAttach);
            }
            usuario.setVendaCollection(attachedVendaCollection);
            Collection<Compra> attachedCompraCollection = new ArrayList<Compra>();
            for (Compra compraCollectionCompraToAttach : usuario.getCompraCollection()) {
                compraCollectionCompraToAttach = em.getReference(compraCollectionCompraToAttach.getClass(), compraCollectionCompraToAttach.getIdCompra());
                attachedCompraCollection.add(compraCollectionCompraToAttach);
            }
            usuario.setCompraCollection(attachedCompraCollection);
            em.persist(usuario);
            for (Venda vendaCollectionVenda : usuario.getVendaCollection()) {
                Usuario oldIdUsuarioOfVendaCollectionVenda = vendaCollectionVenda.getIdUsuario();
                vendaCollectionVenda.setIdUsuario(usuario);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
                if (oldIdUsuarioOfVendaCollectionVenda != null) {
                    oldIdUsuarioOfVendaCollectionVenda.getVendaCollection().remove(vendaCollectionVenda);
                    oldIdUsuarioOfVendaCollectionVenda = em.merge(oldIdUsuarioOfVendaCollectionVenda);
                }
            }
            for (Compra compraCollectionCompra : usuario.getCompraCollection()) {
                Usuario oldIdUsuarioOfCompraCollectionCompra = compraCollectionCompra.getIdUsuario();
                compraCollectionCompra.setIdUsuario(usuario);
                compraCollectionCompra = em.merge(compraCollectionCompra);
                if (oldIdUsuarioOfCompraCollectionCompra != null) {
                    oldIdUsuarioOfCompraCollectionCompra.getCompraCollection().remove(compraCollectionCompra);
                    oldIdUsuarioOfCompraCollectionCompra = em.merge(oldIdUsuarioOfCompraCollectionCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Collection<Venda> vendaCollectionOld = persistentUsuario.getVendaCollection();
            Collection<Venda> vendaCollectionNew = usuario.getVendaCollection();
            Collection<Compra> compraCollectionOld = persistentUsuario.getCompraCollection();
            Collection<Compra> compraCollectionNew = usuario.getCompraCollection();
            Collection<Venda> attachedVendaCollectionNew = new ArrayList<Venda>();
            for (Venda vendaCollectionNewVendaToAttach : vendaCollectionNew) {
                vendaCollectionNewVendaToAttach = em.getReference(vendaCollectionNewVendaToAttach.getClass(), vendaCollectionNewVendaToAttach.getIdVenda());
                attachedVendaCollectionNew.add(vendaCollectionNewVendaToAttach);
            }
            vendaCollectionNew = attachedVendaCollectionNew;
            usuario.setVendaCollection(vendaCollectionNew);
            Collection<Compra> attachedCompraCollectionNew = new ArrayList<Compra>();
            for (Compra compraCollectionNewCompraToAttach : compraCollectionNew) {
                compraCollectionNewCompraToAttach = em.getReference(compraCollectionNewCompraToAttach.getClass(), compraCollectionNewCompraToAttach.getIdCompra());
                attachedCompraCollectionNew.add(compraCollectionNewCompraToAttach);
            }
            compraCollectionNew = attachedCompraCollectionNew;
            usuario.setCompraCollection(compraCollectionNew);
            usuario = em.merge(usuario);
            for (Venda vendaCollectionOldVenda : vendaCollectionOld) {
                if (!vendaCollectionNew.contains(vendaCollectionOldVenda)) {
                    vendaCollectionOldVenda.setIdUsuario(null);
                    vendaCollectionOldVenda = em.merge(vendaCollectionOldVenda);
                }
            }
            for (Venda vendaCollectionNewVenda : vendaCollectionNew) {
                if (!vendaCollectionOld.contains(vendaCollectionNewVenda)) {
                    Usuario oldIdUsuarioOfVendaCollectionNewVenda = vendaCollectionNewVenda.getIdUsuario();
                    vendaCollectionNewVenda.setIdUsuario(usuario);
                    vendaCollectionNewVenda = em.merge(vendaCollectionNewVenda);
                    if (oldIdUsuarioOfVendaCollectionNewVenda != null && !oldIdUsuarioOfVendaCollectionNewVenda.equals(usuario)) {
                        oldIdUsuarioOfVendaCollectionNewVenda.getVendaCollection().remove(vendaCollectionNewVenda);
                        oldIdUsuarioOfVendaCollectionNewVenda = em.merge(oldIdUsuarioOfVendaCollectionNewVenda);
                    }
                }
            }
            for (Compra compraCollectionOldCompra : compraCollectionOld) {
                if (!compraCollectionNew.contains(compraCollectionOldCompra)) {
                    compraCollectionOldCompra.setIdUsuario(null);
                    compraCollectionOldCompra = em.merge(compraCollectionOldCompra);
                }
            }
            for (Compra compraCollectionNewCompra : compraCollectionNew) {
                if (!compraCollectionOld.contains(compraCollectionNewCompra)) {
                    Usuario oldIdUsuarioOfCompraCollectionNewCompra = compraCollectionNewCompra.getIdUsuario();
                    compraCollectionNewCompra.setIdUsuario(usuario);
                    compraCollectionNewCompra = em.merge(compraCollectionNewCompra);
                    if (oldIdUsuarioOfCompraCollectionNewCompra != null && !oldIdUsuarioOfCompraCollectionNewCompra.equals(usuario)) {
                        oldIdUsuarioOfCompraCollectionNewCompra.getCompraCollection().remove(compraCollectionNewCompra);
                        oldIdUsuarioOfCompraCollectionNewCompra = em.merge(oldIdUsuarioOfCompraCollectionNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Venda> vendaCollection = usuario.getVendaCollection();
            for (Venda vendaCollectionVenda : vendaCollection) {
                vendaCollectionVenda.setIdUsuario(null);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
            }
            Collection<Compra> compraCollection = usuario.getCompraCollection();
            for (Compra compraCollectionCompra : compraCollection) {
                compraCollectionCompra.setIdUsuario(null);
                compraCollectionCompra = em.merge(compraCollectionCompra);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class);
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
