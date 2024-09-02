package com.example.equinosappapi.models;

public enum PredictionEnum {
    INTERESADO, SERENO, DISGUSTADO;

    public static PredictionEnum fromString(String str) {
        if (str != null) {
            for (PredictionEnum prediction : PredictionEnum.values()) {
                if (str.equalsIgnoreCase(prediction.name())) {
                    return prediction;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + PredictionEnum.class.getCanonicalName() + "." + str);
    }
}