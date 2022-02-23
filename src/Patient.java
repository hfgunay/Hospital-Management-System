import java.util.Comparator;

public class Patient {               // Patient class for keeping patient informations.


    private String patientID;

    private String patientName;

    private String patientSurname;

    private String phoneNumber;

    private String address;

    public Patient(String patientID, String patientName, String patientSurname, String phoneNumber, String address) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Comparator<Patient> PnameComparator = new Comparator<Patient>() {   // To sorting patient list.
        @Override
        public int compare(Patient p1, Patient p2) {

            String PatientName1 = p1.getPatientName().toUpperCase();
            String PatientName2 = p2.getPatientName().toUpperCase();

            return PatientName1.compareTo(PatientName2);
        }
    };

    public static Comparator<Patient> PIDComparator = new Comparator<Patient>() {
        @Override
        public int compare(Patient p1, Patient p2) {

            int patientID1 = Integer.valueOf(p1.getPatientID());
            int patientID2 = Integer.valueOf(p2.getPatientID());

            return patientID1-patientID2;
        }
    };
}
