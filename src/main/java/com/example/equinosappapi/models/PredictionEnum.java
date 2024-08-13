package com.example.equinosappapi.models;

public enum PredictionEnum {
    INTERESADO, SERENO, DISGUSTADO;

    public static PredictionEnum fromString(String str) {
        if (str != null) {
            for (PredictionEnum predition : PredictionEnum.values()) {
                if (str.equalsIgnoreCase(predition.name())) {
                    return predition;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + PredictionEnum.class.getCanonicalName() + "." + str);
    }
}
