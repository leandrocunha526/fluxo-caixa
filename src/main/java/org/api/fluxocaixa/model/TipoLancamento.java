package org.api.fluxocaixa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoLancamento {
    CREDITO("Crédito"),
    DEBITO("Débito");

    private final String label;

    TipoLancamento(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TipoLancamento fromValue(String value) {
        for (TipoLancamento type : values()) {
            if (type.label.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo inválido: " + value + ". NOTE: Aceita `Crédito` ou `Débito`. ");
    }
}
