package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Compra;
import model.Pessoa;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-06-10T20:44:47", comments="EclipseLink-2.7.9.v20210604-rNA")
@StaticMetamodel(PessoaJuridica.class)
public class PessoaJuridica_ { 

    public static volatile SingularAttribute<PessoaJuridica, Integer> idPessoa;
    public static volatile SingularAttribute<PessoaJuridica, Pessoa> pessoa;
    public static volatile CollectionAttribute<PessoaJuridica, Compra> compraCollection;
    public static volatile SingularAttribute<PessoaJuridica, String> cnpj;

}