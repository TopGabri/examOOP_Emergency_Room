package it.polito.emergency;

public class Professional {


    private String id;
    private String specialization;
    private String first;
    private String last;
    private String period;
    private String start;
    private String end;


    public Professional(String id, String first, String last, String specialization, String period){
        this.id = id;
        this.first = first;
        this.last = last;
        this.specialization = specialization;
        this.period = period;
        this.start = startEndDuty(period)[0];
        this.end = startEndDuty(period)[1];
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return first;
    }

    public String getSurname() {
        return last;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPeriod() {
        return period;
    }

    public String getWorkingHours() {
        return null;
    }

    public String[] startEndDuty(String period){
        return period.split(" to ");
    }

    public boolean isAvailable(String period){
        String [] startEnd = period.split(" to ");
        String start = startEnd[0];
        String end = startEnd[1];

        if (this.start.compareTo(start) <= 0 && this.end.compareTo(end) >= 0)
            return true;

        return false;       
    }

    public boolean isAvaliableForPatient(String date){

        if (this.start.compareTo(date) <= 0 && this.end.compareTo(date) >= 0)
            return true;

        return false;
    }
}
