package br.com.caelum.stella.nfe.builder.cofins.impl;

import java.math.BigDecimal;

import br.com.caelum.stella.nfe.ObjectCreator;
import br.com.caelum.stella.nfe.builder.cofins.COFINSTributadoPelaAliquota;
import br.com.caelum.stella.nfe.builder.cofins.enums.SituacaoTributaria;
import br.com.caelum.stella.nfe.modelo.COFINSAliq;

public class COFINSTributadoImpl implements COFINSTributadoPelaAliquota, ObjectCreator {

    private final COFINSBuilderDelegate<COFINSAliq> delegate;

    public COFINSTributadoImpl(final SituacaoTributaria situacaoTributaria) {
        delegate = new COFINSBuilderDelegate<COFINSAliq>(COFINSAliq.class, situacaoTributaria);
    }

    public COFINSTributadoPelaAliquota withAliquotaEmPercentual(final BigDecimal aliquotaEmPercentual) {
        delegate.withAliquotaEmPercentual(aliquotaEmPercentual);
        return this;
    }

    public COFINSTributadoPelaAliquota withValor(final BigDecimal valor) {
        delegate.withValor(valor);
        return this;
    }

    public COFINSTributadoPelaAliquota withValorBaseCalculo(final BigDecimal valorBaseCalculo) {
        delegate.withValorBaseCalculo(valorBaseCalculo);
        return this;
    }

    public COFINSAliq getInstance() {
        return delegate.getReference();
    }

}