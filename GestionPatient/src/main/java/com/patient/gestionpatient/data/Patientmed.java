package com.patient.gestionpatient.data;

public class Patientmed {
    private int refMed;
    private int idP;

    public Patientmed(int refMedicament, int idPatient) {
        this.refMed = refMedicament;
        this.idP = idPatient;
    }

    public int getRefMed() {
        return refMed;
    }

    public void setRefMed(int refMed) {
        this.refMed = refMed;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }
}
