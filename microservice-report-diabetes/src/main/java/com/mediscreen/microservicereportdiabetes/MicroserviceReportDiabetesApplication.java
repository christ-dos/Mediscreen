package com.mediscreen.microservicereportdiabetes;

import com.mediscreen.microservicereportdiabetes.model.NotesPatientReport;
import com.mediscreen.microservicereportdiabetes.model.PatientReport;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServiceHistoryPatientReportProxy;
import com.mediscreen.microservicereportdiabetes.proxy.IMicroServicePatientReportProxy;
import com.mediscreen.microservicereportdiabetes.service.ReportDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.mediscreen.microservicereportdiabetes")
@EnableSwagger2
public class MicroserviceReportDiabetesApplication implements CommandLineRunner {
	@Autowired
	private ReportDiabetesService reportDiabetesService;

	@Autowired
	private IMicroServicePatientReportProxy microServicePatientReportProxy;

	@Autowired
	private IMicroServiceHistoryPatientReportProxy microServiceHistoryPatientReportProxy;

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceReportDiabetesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Iterable<NotesPatientReport> list = microServiceHistoryPatientReportProxy.getListNotesByPatient(3);
		list.forEach(x->System.out.println(x));

		int count = reportDiabetesService.searchTriggerWordInNotesPatient(4);
		System.out.println("resultat: " + count);

	}
}
