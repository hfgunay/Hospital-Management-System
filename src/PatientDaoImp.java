import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientDaoImp implements PatientDao {              // PatientDaoImp class for AddPatient and RemovePatient.

    ReadWrite readWrite = new ReadWrite();

    @Override
    public void AddPatient(String patientID, String name, String surname, String phoneNumber, String address,List<Patient> PatientsList) {


        Patient patient = new Patient(patientID, name, surname , phoneNumber, address);

        PatientsList.add(patient);
        readWrite.writepatient(PatientsList);



    }

    @Override
    public void RemovePatient(String patientID,List<Patient> PatientsList, List<Admission> AdmissionList,List<String> exOperationList) {

            for (Patient patient: PatientsList) {

                if(patient.getPatientID().equals(patientID)) {

                    PatientsList.remove(patient);


                    break;
                }

            }

            for (Admission admission: AdmissionList) {

                if(admission.getPatientID().equals(patientID)) {

                    AdmissionList.remove(admission);

                    break;
                }
            }
            readWrite.writepatient(PatientsList);
            readWrite.writeadmission(AdmissionList,exOperationList);
    }



    @Override
    public void CreateAdmission(String admissionID, String patientID, List<Admission> AdmissionList, List<String> exOperationList, List<String> OperationLength) {

    }

    @Override
    public void AddExamination(String admissionID, String examinationType, String operations, List<Admission> AdmissionList,List<String> exOperationList,List<String> OperationLength) {

    }

}
