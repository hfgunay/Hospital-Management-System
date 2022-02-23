import java.util.List;

public class AdmissionDaoImp implements PatientDao{             // AdmissionDaoImp class for CreateAdmission and AddExamination.

    ReadWrite readWrite = new ReadWrite();

    @Override
    public void AddPatient(String patientID, String name, String surname, String phoneNumber, String address, List<Patient> PatientsList) {

    }

    @Override
    public void RemovePatient(String patientID, List<Patient> PatientsList,List<Admission> AdmissionList,List<String> exOperationList) {

    }



    @Override
    public void CreateAdmission(String admissionID, String patientID,List<Admission> AdmissionList,List<String> exOperationList,List<String> OperationLength ) {


        Admission admission = new Admission(admissionID, patientID, 0);

        AdmissionList.add(admission);
        exOperationList.add(patientID);
        OperationLength.add(patientID);





        readWrite.writeadmission(AdmissionList, exOperationList);


    }

    @Override
    public void AddExamination(String admissionID, String examinationType, String operations, List<Admission> AdmissionList,List<String> exOperationList,List<String> OperationLength) {


        for (Admission a: AdmissionList) {

            if(a.getAdmissionID().equalsIgnoreCase(admissionID)) {


               int i = exOperationList.indexOf(a.getPatientID());
               int j = OperationLength.indexOf(a.getPatientID());

               int indiceex = (i + 1 + (a.getEach()*2));
               int indiceop = (i + 2 + (a.getEach()*2));

               exOperationList.add(indiceex, examinationType);

               exOperationList.add(indiceop, operations);

               String[] operationsarray = operations.split(" ");
               String length = String.valueOf(operationsarray.length);
               OperationLength.add(j+1, length);



                a.setEach(a.getEach()+1);

            }
        }

        readWrite.writeadmission(AdmissionList, exOperationList);

    }


}
