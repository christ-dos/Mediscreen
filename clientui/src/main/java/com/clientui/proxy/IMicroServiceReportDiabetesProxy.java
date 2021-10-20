package com.clientui.proxy;

import com.clientui.models.DiabetesAssessmentClientUi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "microservice-report-diabetes", url = "localhost:8083")
public interface IMicroServiceReportDiabetesProxy {

    @GetMapping(value = "/assess/{id}")
    DiabetesAssessmentClientUi getDiabetesAssessmentByPatientId(@PathVariable("id") int id);

    @GetMapping(value = "/assess/familyName/{lastName}")
    DiabetesAssessmentClientUi getDiabetesAssessmentByFamilyName(@PathVariable("lastName") String lastName);


}
