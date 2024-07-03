package it.polito.emergency;

import it.polito.emergency.EmergencyApp.*;

public class Patient {

    private String first;
    private String last;
    private String birthdate;
    private String code;
    private String reason;
    private String admittanceDate;
    private PatientStatus status;

    

    public Patient(String code, String first, String last, String birth, String reason, String admittanceDate) {
		this.first = first;
		this.last = last;
		this.birthdate = birth;
		this.code = code;
		this.reason = reason;
		this.admittanceDate = admittanceDate;
        status = PatientStatus.ADMITTED;
	}

	public String getFiscalCode() {
        return code;
    }

    public String getName() {
        return first;
    }

    public String getSurname() {
        return last;
    }

    public String getDateOfBirth() {
        return birthdate;
    }

    public String getReason() {
        return reason;
    }

    public String getDateTimeAccepted() {
        return admittanceDate;
    }

    public PatientStatus getStatus() {
        return status;
    }
}
