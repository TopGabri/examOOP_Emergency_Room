package it.polito.emergency;

public class Report {

    String id;
    String professionalId; 
    String fiscalCode; 
    String date;
    String description;

    public Report(String id, String professionalId, String fiscalCode, String date, String description) {
		this.id = id;
		this.professionalId = professionalId;
		this.fiscalCode = fiscalCode;
		this.date = date;
		this.description = description;
	}

	public String getId() {
        return id;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getDate() {
        return date;
    }


    public String getDescription() {
        return description;
    }
}
