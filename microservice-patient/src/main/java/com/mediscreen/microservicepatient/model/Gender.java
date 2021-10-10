package com.mediscreen.microservicepatient.model;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * An enum class that manage the gender of patient
 * F = female
 * M = Male
 */
public enum Gender {
    @JsonAlias("sex") F,
    @JsonAlias("sex") M;
}
