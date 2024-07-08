package com.example.equinosappapi.models;

public enum PrediccionEnum {
    INTERESADO, SERENO, DISGUSTADO;

    public static PrediccionEnum fromString(String str) {
        if (str != null) {
            for (PrediccionEnum prediccion : PrediccionEnum.values()) {
                if (str.equalsIgnoreCase(prediccion.name())) {
                    return prediccion;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + PrediccionEnum.class.getCanonicalName() + "." + str);
    }
}
