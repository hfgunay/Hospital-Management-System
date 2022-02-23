import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ReadWrite {                                             // ReadWrite class for file reading and file writing.

    ArrayList<Patient> PatientsList = new ArrayList<>();

    ArrayList<Admission> AdmissionList = new ArrayList<>();

    ArrayList<String> exOperationList = new ArrayList<>();

    ArrayList<String> OperationLength = new ArrayList<>();

    public ArrayList<Patient> getPatientsList() {
        return PatientsList;
    }

    public void setPatientsList(ArrayList<Patient> patientsList) {
        PatientsList = patientsList;
    }

    public ArrayList<Admission> getAdmissionList() {
        return AdmissionList;
    }

    public void setAdmissionList(ArrayList<Admission> admissionList) {
        AdmissionList = admissionList;
    }

    public ArrayList<String> getExOperationList() {
        return exOperationList;
    }

    public void setExOperationList(ArrayList<String> exOperationList) {
        this.exOperationList = exOperationList;
    }

    public void readpatient(String patientfile) {

        File file = new File(patientfile);



        try {

            Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name());

            while (sc.hasNextLine()) {

                String patient = sc.nextLine();

                String patientarray[] = patient.split("\t");

                String namearray[] = patientarray[1].split(" ");


                Patient patient1 = new Patient(patientarray[0], namearray[0], namearray[1], patientarray[2], patientarray[3]);


                PatientsList.add(patient1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writepatient(List<Patient> PatientsList) {

        Collections.sort(PatientsList, Patient.PIDComparator);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("patient.txt",false))) {

            for (Patient p: PatientsList) {

                String writing = p.getPatientID() + "\t" + p.getPatientName() + " "
                        + p.getPatientSurname() + "\t"
                        + p.getPhoneNumber() + "\t"
                        + p.getAddress();

                writer.write(writing);
                writer.newLine();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readadmission(String admissionfile) {

        File file = new File(admissionfile);

        ArrayList<String> linearrays = new ArrayList<>();

        try {

            Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name());

            while (sc.hasNextLine()) {

                String  line = sc.nextLine();

                String[] line1 = line.split("\\s+");

                int l1 = line1.length;

                if (l1 < 3) {

                    if (line1[0].equals("Inpatient") || line1[0].equals("Outpatient")) {
                        OperationLength.add(String.valueOf(l1-1));

                    }
                    else {
                        OperationLength.add(line1[1]);

                    }



                }
                else if (l1 > 2) {


                    OperationLength.add(String.valueOf(l1-1));

                }





                String[] linearray = line.split("\t");

                int linelength = linearray.length;

                int i;

                for (i = 0; i < linelength; i++) {

                    linearrays.add(linearray[i]);
                }





            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }



        int i;
        for (i = linearrays.size()-1; i > 0; i--) {

            int L = linearrays.size()-1;

            for (Patient p: PatientsList) {


                if(linearrays.get(i).equalsIgnoreCase(p.getPatientID())) {



                    String admissionID = linearrays.get(i-1);
                    String patientID = linearrays.get(i);

                    exOperationList.add(patientID);



                    int each = (L-i)/2;

                    int a = i;
                    while(L > i) {




                        String examinationType = linearrays.get(a+1);
                        String operations = linearrays.get(a+2);

                        exOperationList.add(examinationType);
                        exOperationList.add(operations);





                        L -= 2;
                        a += 2;
                    }

                    Admission admission = new Admission(admissionID,patientID,each);

                    AdmissionList.add(admission);

                    int undeleted = (linearrays.size() -1) - ((each*2)+2);


                    for (int x = linearrays.size() -1 ; x > undeleted ; x--) {

                        linearrays.remove(x);



                    }



                    if (i > 1) {
                        i -= 1;
                    }

                    break;


                }

            }
        }
/*
        for (int j = 0; j <exOperationList.size(); j++) {
            System.out.println(exOperationList.get(j));
        }

*/
    }
    public static void writeadmission(List<Admission> AdmissionList, List<String> exOperationList) {

        Collections.sort(AdmissionList, Admission.AIDComparator);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admission.txt",false))) {  // admission.txt

            for (Admission a: AdmissionList) {


                for (int i =0; i < exOperationList.size() ; i++) {

                    if(exOperationList.get(i).equalsIgnoreCase(a.getPatientID())) {

                        String writing = a.getAdmissionID() + "\t" + a.getPatientID();

                        writer.write(writing);
                        writer.newLine();
                        String writing1;



                        if (a.getEach() > 0) {

                            int j = 0;
                            while (j < a.getEach()) {

                                writing1 = exOperationList.get(i+1) + "\t" + exOperationList.get(i+2);
                                writer.write(writing1);
                                writer.newLine();
                                j += 1;
                                i += 2;
                            }
                        }
                        break;
                    }

                }




            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeoutput(String inputfile) {

        PatientDaoImp patientDaoImp = new PatientDaoImp();
        AdmissionDaoImp admissionDaoImp = new AdmissionDaoImp();

        readpatient("patient.txt");

        readadmission("admission.txt");

        File file = new File(inputfile);

        try {

            Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name());

            while (sc.hasNextLine()) {

                String  lin = sc.nextLine();

                String[] line = lin.split(" ");



                if (line[0].equals("AddPatient")) {

                    String address = line[5] + " " + line[6] + " " + line[7] + " " + line[8];

                    patientDaoImp.AddPatient(line[1],line[2],line[3],line[4], address, PatientsList);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt",true))) {

                        String writing = "Patient " + line[1] + " " + line[2] + " added";
                        writer.write(writing);
                        writer.newLine();
                    }


                }

                else if (line[0].equals("RemovePatient")) {




                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt",true))) {

                        for (Patient p: PatientsList) {

                            if (p.getPatientID().equals(line[1])) {

                                String name = p.getPatientName();
                                System.out.println(p.getPatientName());


                                String writing = "Patient " + line[1] + " " + name + " removed";
                                writer.write(writing);
                                writer.newLine();

                                break;
                            }
                        }


                    }
                    patientDaoImp.RemovePatient(line[1], PatientsList, AdmissionList, exOperationList);






                }

                else if (line[0].equals("CreateAdmission")) {

                    admissionDaoImp.CreateAdmission(line[1], line[2], AdmissionList, exOperationList, OperationLength);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                        String writing = "Admission " + line[1] + " created";
                        writer.write(writing);
                        writer.newLine();
                    }

                }

                else if (line[0].equals("AddExamination")) {


                    if (line.length == 4){

                        String operations = line[3];
                        admissionDaoImp.AddExamination(line[1], line[2], operations, AdmissionList, exOperationList, OperationLength);





                    }

                    else if (line.length == 5) {

                        String operations = line[3] + " " + line[4];
                        admissionDaoImp.AddExamination(line[1], line[2], operations, AdmissionList, exOperationList, OperationLength);




                    }

                    else if (line.length == 6) {

                        String operations = line[3] + " " + line[4] + " " + line[5];
                        admissionDaoImp.AddExamination(line[1], line[2], operations, AdmissionList, exOperationList, OperationLength);



                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                        String writing = line[2] + " examination added to admission " + line[1];
                        writer.write(writing);
                        writer.newLine();
                    }
                }

                else if (line[0].equals("TotalCost")) {

                    for (Admission a: AdmissionList) {

                        if (a.getAdmissionID().equals(line[1])) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                String writing = "Total cost for admission " + a.getAdmissionID();
                                writer.write(writing);
                                writer.newLine();
                            }

                            for (int o=0; o < OperationLength.size() ; o++) {

                                for (int e = 0; e < exOperationList.size(); e++) {

                                    if(OperationLength.get(o).equals(a.getPatientID()) && exOperationList.get(e).equals(a.getPatientID())) {
                                            int k = 0;
                                            int i =0;
                                            int totalcost = 0;
                                             while(i< a.getEach()) {
                                                 int cost = 0;



                                                 if (exOperationList.get(e+1+k).equals("Inpatient")) {


                                                     if (Integer.valueOf(OperationLength.get(o+1+i)) ==1) {
                                                         if(exOperationList.get(e+2+k).equals("imaging")) {
                                                             IExamination examination = new imagingOp(new Inpatient());
                                                             cost += examination.getCost();

                                                         }
                                                         else if(exOperationList.get(e+2+k).equals("tests")) {
                                                             IExamination examination = new testsOp(new Inpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperationList.get(e+2+k).equals("measurements")) {
                                                             IExamination examination = new measurementsOp(new Inpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperationList.get(e+2+k).equals("doctorvisit")) {
                                                             IExamination examination = new doctorVisitOp(new Inpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }

                                                         k += 2;
                                                     }

                                                     else if (Integer.valueOf(OperationLength.get(o+1+i)) == 2) {
                                                         String[] exOperation = exOperationList.get(e+2+k).split(" ");

                                                         if (exOperation[0].equals("imaging") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new imagingOp(new testsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("imaging") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }

                                                         else if (exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new testsOp(new imagingOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new doctorVisitOp(new Inpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }

                                                         k += 2;
                                                     }
                                                     else if (Integer.valueOf(OperationLength.get(o+1+i)) == 3) {


                                                         String[] exOperation = exOperationList.get(e+2+k).split(" ");

                                                         if (exOperation[0].equals("imaging") && exOperation[1].equals("tests") && exOperation[2].equals("measurements")){
                                                             IExamination examination = new imagingOp(new testsOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();

                                                         }

                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("tests") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new testsOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("measurements") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("measurements") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("tests") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("tests") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new imagingOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new imagingOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("imaging") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("imaging") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("imaging") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new imagingOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("imaging") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new testsOp(new imagingOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("measurements") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("measurements") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new imagingOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new testsOp(new doctorVisitOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new doctorVisitOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new imagingOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new imagingOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new testsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new measurementsOp(new Inpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }


                                                         k += 2;
                                                     }
                                                 }
                                                 else if (exOperationList.get(e+1+k).equals("Outpatient")) {


                                                     if (Integer.valueOf(OperationLength.get(o+1+i)) ==1) {
                                                         if(exOperationList.get(e+2+k).equals("imaging")) {
                                                             IExamination examination = new imagingOp(new Outpatient());
                                                             cost += examination.getCost();

                                                         }
                                                         else if(exOperationList.get(e+2+k).equals("tests")) {
                                                             IExamination examination = new testsOp(new Outpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperationList.get(e+2+k).equals("measurements")) {
                                                             IExamination examination = new measurementsOp(new Outpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperationList.get(e+2+k).equals("doctorvisit")) {
                                                             IExamination examination = new doctorVisitOp(new Outpatient());
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }

                                                         k += 2;
                                                     }

                                                     else if (Integer.valueOf(OperationLength.get(o+1+i)) == 2) {
                                                         String[] exOperation = exOperationList.get(e+2+k).split(" ");

                                                         if (exOperation[0].equals("imaging") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new imagingOp(new testsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("imaging") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }

                                                         else if (exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("imaging")) {
                                                             IExamination examination = new testsOp(new imagingOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("measurements")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         else if (exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new doctorVisitOp(new Outpatient()));
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }

                                                         k += 2;
                                                     }
                                                     else if (Integer.valueOf(OperationLength.get(o+1+i)) == 3) {


                                                         String[] exOperation = exOperationList.get(e+2+k).split(" ");

                                                         if (exOperation[0].equals("imaging") && exOperation[1].equals("tests") && exOperation[2].equals("measurements")){
                                                             IExamination examination = new imagingOp(new testsOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();

                                                         }

                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("tests") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new testsOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("measurements") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("measurements") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new imagingOp(new measurementsOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("imaging") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new imagingOp(new doctorVisitOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("tests") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("tests") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new imagingOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new imagingOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new doctorVisitOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("imaging") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("measurements") && exOperation[1].equals("imaging") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new measurementsOp(new imagingOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("imaging") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new imagingOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("imaging") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new testsOp(new imagingOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("measurements") && exOperation[2].equals("doctorvisit")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("measurements") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new testsOp(new measurementsOp(new imagingOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new testsOp(new doctorVisitOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("tests") && exOperation[1].equals("doctorvisit") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new measurementsOp(new testsOp(new doctorVisitOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new imagingOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("tests") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new testsOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements") && exOperation[2].equals("imaging")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new imagingOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("measurements") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new measurementsOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging") && exOperation[2].equals("tests")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new testsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         else if(exOperation[0].equals("doctorvisit") && exOperation[1].equals("imaging") && exOperation[2].equals("measurements")) {
                                                             IExamination examination = new doctorVisitOp(new imagingOp(new measurementsOp(new Outpatient())));
                                                             cost += examination.getCost();
                                                         }
                                                         try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                                             String writing = "\t" +exOperationList.get(e+1+k) + " " + exOperationList.get(e+2+k) + " " + cost + "$";
                                                             writer.write(writing);
                                                             writer.newLine();
                                                         }


                                                         k += 2;
                                                     }

                                                 }

                                                 i += 1;



                                                 totalcost += cost;

                                             }


                                        try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                                            String totalcosthesap = "\t" + "Total: " + totalcost + "$";
                                            writer.write(totalcosthesap);
                                            writer.newLine();
                                        }

                                        }
                                    }
                                }



                        }
                    }

                }



                else if (line[0].equals("ListPatients")) {
                    Collections.sort(PatientsList, Patient.PnameComparator);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter( "output.txt",true))) {

                        String writing1 = "Patient List:";
                        writer.write(writing1);
                        writer.newLine();

                        for (Patient patient: PatientsList) {

                            String writing2 = patient.getPatientID() +"\t"+patient.getPatientName()+" "+patient.getPatientSurname()+"\t"+patient.getPhoneNumber()+"\t"+patient.getAddress();
                            writer.write(writing2);
                            writer.newLine();

                        }
                    }

                }
            }


        }




        catch (Exception e) {
            e.printStackTrace();
        }
    }

}


