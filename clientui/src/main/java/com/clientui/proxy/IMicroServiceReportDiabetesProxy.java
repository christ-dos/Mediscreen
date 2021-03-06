package com.clientui.proxy;

import com.clientui.models.DiabetesAssessmentClientUi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

/**
 * An Interface that manage requests send to microservice-report-diabetes
 *
 * @author Christine Duarte
 */
@FeignClient(name = "microservice-report-diabetes", url = "localhost:8083")
//@FeignClient(name = "microservice-report-diabetes", url = "microservice-report-diabetes-app:8083")
public interface IMicroServiceReportDiabetesProxy {

    @GetMapping(value = "/assess/{id}")
    DiabetesAssessmentClientUi getDiabetesAssessmentByPatientId(@Valid @PathVariable("id") int id);

}
