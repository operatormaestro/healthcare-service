package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    PatientInfo patientInfo = new PatientInfo("Иван",
            "Иванов",
            LocalDate.of(2000, 1, 1),
            new HealthInfo(new BigDecimal("36.1"),
                    new BloodPressure(120, 80)));
    PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
    SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
    String message = "Warning, patient with id: null, need help";

    @Test
    public void checkBloodPressureTest() {

        //arrange

        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patientInfo);
        Mockito.when(patientInfoRepository.getById("2")).thenReturn(patientInfo);
        Mockito.when(patientInfoRepository.getById("3")).thenReturn(patientInfo);

        //act

        medicalService.checkBloodPressure("1", new BloodPressure(160, 100));
        medicalService.checkBloodPressure("2", new BloodPressure(120, 80));
        medicalService.checkBloodPressure("3", new BloodPressure(90, 40));

        //assert

        Mockito.verify(sendAlertService, Mockito.times(2)).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }

    @Test
    public void checkTemperatureTest() {

        //arrange

        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patientInfo);
        Mockito.when(patientInfoRepository.getById("2")).thenReturn(patientInfo);
        Mockito.when(patientInfoRepository.getById("3")).thenReturn(patientInfo);

        //act

        medicalService.checkTemperature("1", new BigDecimal("39.1"));
        medicalService.checkTemperature("2", new BigDecimal("37.1"));
        medicalService.checkTemperature("3", new BigDecimal("34.1"));

        //assert

        Mockito.verify(sendAlertService, Mockito.times(2)).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }
}
