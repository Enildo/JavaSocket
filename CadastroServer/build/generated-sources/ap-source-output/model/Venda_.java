package model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.PessoaFisica;
import model.Produto;
import model.Usuario;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-06-10T20:44:47", comments="EclipseLink-2.7.9.v20210604-rNA")
@StaticMetamodel(Venda.class)
public class Venda_ { 

    public static volatile SingularAttribute<Venda, BigDecimal> precoUnitario;
    public static volatile SingularAttribute<Venda, Date> dataVenda;
    public static volatile SingularAttribute<Venda, Produto> idProduto;
    public static volatile SingularAttribute<Venda, PessoaFisica> idCliente;
    public static volatile SingularAttribute<Venda, Usuario> idUsuario;
    public static volatile SingularAttribute<Venda, Integer> quantidade;
    public static volatile SingularAttribute<Venda, Integer> idVenda;

}