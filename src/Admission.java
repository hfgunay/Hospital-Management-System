import java.util.ArrayList;
import java.util.Comparator;

public class Admission {    // Admission class for keeping admission informations.

private String admissionID;

private String patientID;

private int each;






    public Admission(String admissionID, String patientID, int each) {
        this.admissionID = admissionID;
        this.patientID = patientID;
        this.each = each;


    }

    public String getAdmissionID() {
        return admissionID;
    }

    public void setAdmissionID(String admissionID) {
        this.admissionID = admissionID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public int getEach() {
        return each;
    }

    public void setEach(int each) {
        this.each = each;
    }




    public static Comparator<Admission> getAIDComparator() {
        return AIDComparator;
    }

    public static void setAIDComparator(Comparator<Admission> AIDComparator) {        // To sorting admission list.
        Admission.AIDComparator = AIDComparator;
    }

    public static Comparator<Admission> AIDComparator = new Comparator<Admission>() {
        @Override
        public int compare(Admission a1, Admission a2) {

            int AdmissionID1 = Integer.valueOf(a1.getAdmissionID());
            int AdmissionID2 = Integer.valueOf(a2.getAdmissionID());

            return AdmissionID1-AdmissionID2;
        }
    };
}
