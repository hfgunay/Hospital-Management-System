import java.util.List;

public interface PatientDao {    // Interface for DAO pattern.

    public void AddPatient(String patientID, String name, String surname, String phoneNumber, String address,List<Patient> PatientsList);

    public void RemovePatient(String patientID,List<Patient> PatientsList,List<Admission> AdmissionList,List<String> exOperationList);



    public void CreateAdmission(String admissionID, String patientID , List<Admission> AdmissionList ,List<String> exOperationList, List<String> OperationLength );

    public void AddExamination(String admissionID, String examinationType, String operations , List<Admission> AdmissionList,List<String> exOperationList, List<String> OperationLength );


}
