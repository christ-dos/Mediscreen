package com.mediscreen.microservicepatient.model;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * An enum class that manage the gender of patient
 * F = female
 * M = Male
 *
 * @author Christine Duarte
 */
public enum Gender {
    @JsonAlias("sex") F,
    @JsonAlias("sex") M;
}
