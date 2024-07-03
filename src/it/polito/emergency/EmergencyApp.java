package it.polito.emergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import static java.util.stream.Collectors.*;

public class EmergencyApp {

    public enum PatientStatus {
        ADMITTED,
        DISCHARGED,
        HOSPITALIZED
    }

    private Map<String, Professional> professionals = new HashMap<>();
    private Map<String, Department> departments = new HashMap<>();
    private Map<String, Patient> patients = new HashMap<>();
    private Map<String, Report> reports = new HashMap<>();
    private String reportId = "1";
    
    /**
     * Add a professional working in the emergency room
     * 
     * @param id
     * @param name
     * @param surname
     * @param specialization
     * @param period
     * @param workingHours
     */
    public void addProfessional(String id, String name, String surname, String specialization, String period) {
        professionals.put(id, new Professional(id, name, surname, specialization, period));
    }

    /**
     * Retrieves a professional utilizing the ID.
     *
     * @param id The id of the professional.
     * @return A Professional.
     * @throws EmergencyException If no professional is found.
     */    
    public Professional getProfessionalById(String id) throws EmergencyException {
        
        if (!professionals.containsKey(id))
            throw new EmergencyException();

        return professionals.get(id);
    }

    /**
     * Retrieves the list of professional IDs by their specialization.
     *
     * @param specialization The specialization to search for among the professionals.
     * @return A list of professional IDs who match the given specialization.
     * @throws EmergencyException If no professionals are found with the specified specialization.
     */    
    public List<String> getProfessionals(String specialization) throws EmergencyException {
        
        List<String> ps = professionals.values().stream()
        .filter(p -> p.getSpecialization().equals(specialization))
        .map(Professional::getId)
        .collect(toList());

        if (ps.isEmpty())
            throw new EmergencyException();

        return ps;
    }

    /**
     * Retrieves the list of professional IDs who are specialized and available during a given period.
     *
     * @param specialization The specialization to search for among the professionals.
     * @param period The period during which the professional should be available, formatted as "YYYY-MM-DD to YYYY-MM-DD".
     * @return A list of professional IDs who match the given specialization and are available during the period.
     * @throws EmergencyException If no professionals are found with the specified specialization and period.
     */    
    public List<String> getProfessionalsInService(String specialization, String period) throws EmergencyException {
        
        List<String> ps = professionals.values().stream()
        .filter(p -> p.isAvailable(period))
        .filter(p -> p.getSpecialization().equals(specialization))
        .map(Professional::getId)
        .collect(toList());

        if (ps.isEmpty())
            throw new EmergencyException();

        return ps;
    }

    /**
     * Adds a new department to the emergency system if it does not already exist.
     *
     * @param name The name of the department.
     * @param maxPatients The maximum number of patients that the department can handle.
     * @throws EmergencyException If the department already exists.
     */
    public void addDepartment(String name, int maxPatients) {
        
        departments.put(name, new Department(name, maxPatients));
    }

    /**
     * Retrieves a list of all department names in the emergency system.
     *
     * @return A list containing the names of all registered departments.
     * @throws EmergencyException If no departments are found.
     */
    public List<String> getDepartments() throws EmergencyException {
        
        if (departments.isEmpty())
            throw new EmergencyException();

        return departments.keySet().stream().collect(toList());
    
    }

    /**
     * Reads professional data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a professional's ID, name, surname, specialization, period of availability, and working hours.
     * The expected format of each line is: matricola, nome, cognome, specializzazione, period, orari_lavoro
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of professionals successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */
    public int readFromFileProfessionals(Reader reader) throws IOException {
        
        if (reader == null)
            throw new IOException();

        try {
        
            int n = 0;

            char[] characters = new char[1000000];
            reader.read(characters);

            String file = new String(characters);

            String [] lines = file.split("\n");

            for (int i = 1; i < lines.length; i++){
                String [] p = lines[i].strip().split(",");
                addProfessional(p[0], p[1], p[2], p[3], p[4]);
                n++;
            }

            return n;
        } catch(IOException e){
            throw new IOException();
        }
    
        
    }

    /**
     * Reads department data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a department's name and the maximum number of patients it can accommodate.
     * The expected format of each line is: nome_reparto, num_max
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of departments successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */    
    public int readFromFileDepartments(Reader reader) throws IOException {
        if (reader == null)
            throw new IOException();

        try {
            int n = 0;

            char[] characters = new char[1000000];
            reader.read(characters);

            String file = new String(characters);

            String [] lines = file.split("\n");

            for (int i = 1; i < lines.length; i++){
                String [] p = lines[i].strip().split(",");
                addDepartment(p[0], Integer.parseInt(p[1]));
                n++;
            }

            return n;

        } catch(IOException e){
            throw new IOException();
        }
    
    }

    /**
     * Registers a new patient in the emergency system if they do not exist.
     * 
     * @param fiscalCode The fiscal code of the patient, used as a unique identifier.
     * @param name The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The birth date of the patient.
     * @param reason The reason for the patient's visit.
     * @param dateTimeAccepted The date and time the patient was accepted into the emergency system.
     */
    public Patient addPatient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        
        if (patients.containsKey(fiscalCode))
            return patients.get(fiscalCode);

        patients.put(fiscalCode, new Patient(fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted));

        return patients.get(fiscalCode);
    }

    /**
     * Retrieves a patient or patients based on a fiscal code or surname.
     *
     * @param identifier Either the fiscal code or the surname of the patient(s).
     * @return A single patient if a fiscal code is provided, or a list of patients if a surname is provided.
     *         Returns an empty collection if no match is found.
     */    
    public List<Patient> getPatient(String identifier) throws EmergencyException {
        
        List<Patient> l = patients.values().stream()
        .filter(p -> p.getFiscalCode().equals(identifier) || p.getSurname().equals(identifier))
        .collect(toList());

        if (l.isEmpty())
            throw new EmergencyException();

        return l;
    }

    /**
     * Retrieves the fiscal codes of patients accepted on a specific date, 
     * sorted by acceptance time in descending order.
     *
     * @param date The date of acceptance to filter the patients by, expected in the format "yyyy-MM-dd".
     * @return A list of patient fiscal codes who were accepted on the given date, sorted from the most recent.
     *         Returns an empty list if no patients were accepted on that date.
     */
    public List<String> getPatientsByDate(String date) {
        
        return patients.values().stream()
        .filter(p-> p.getDateTimeAccepted().equals(date))
        .sorted(Comparator.comparing(Patient::getSurname).thenComparing(Patient::getName))
        .map(Patient::getFiscalCode)
        .collect(toList());
    }

    /**
     * Assigns a patient to a professional based on the required specialization and checks availability during the request period.
     *
     * @param fiscalCode The fiscal code of the patient.
     * @param specialization The required specialization of the professional.
     * @return The ID of the assigned professional.
     * @throws EmergencyException If the patient does not exist, if no professionals with the required specialization are found, or if none are available during the period of the request.
     */
    public String assignPatientToProfessional(String fiscalCode, String specialization) throws EmergencyException {
        
        if (!patients.containsKey(fiscalCode))
            throw new EmergencyException();

        Optional <String> pr = professionals.values().stream()
        .filter(p -> p.getSpecialization().equals(specialization))
        .filter(p -> p.isAvaliableForPatient(patients.get(fiscalCode).getDateTimeAccepted()))
        .sorted(Comparator.comparing(Professional::getId))
        .map(Professional::getId)
        .findFirst();

        if (pr.isPresent()){
            return pr.get();

        }
            

        throw new EmergencyException();

    }

    public Report saveReport(String professionalId, String fiscalCode, String date, String description) throws EmergencyException {
        
        if (!professionals.containsKey(professionalId))
            throw new EmergencyException();

        reports.put(reportId, new Report(reportId, professionalId, fiscalCode, date, description));

        String tmp = reportId;
        int x = Integer.parseInt(reportId);
        x++;
        reportId = Integer.toString(x);

        return reports.get(tmp);
    }

    /**
     * Either discharges a patient or hospitalizes them depending on the availability of space in the requested department.
     * 
     * @param fiscalCode The fiscal code of the patient to be discharged or hospitalized.
     * @param departmentName The name of the department to which the patient might be admitted.
     * @throws EmergencyException If the patient does not exist or if the department does not exist.
     */
    public void dischargeOrHospitalize(String fiscalCode, String departmentName) throws EmergencyException {
        
        if (!patients.containsKey(fiscalCode) || !departments.containsKey(departmentName))
            throw new EmergencyException();

        Patient p = patients.get(fiscalCode);

        Department d = departments.get(departmentName);

        if (d.hasAvailableBeds()){
            d.hospitalize(p);
            p.hospitalize();
        } else {
            p.discharge();
        }
    }

    /**
     * Checks if a patient is currently hospitalized in any department.
     *
     * @param fiscalCode The fiscal code of the patient to verify.
     * @return 0 if the patient is currently hospitalized, -1 if not hospitalized or discharged.
     * @throws EmergencyException If no patient is found with the given fiscal code.
     */
    public int verifyPatient(String fiscalCode) throws EmergencyException{
        
        if (!patients.containsKey(fiscalCode))
            throw new EmergencyException();

        PatientStatus s = patients.get(fiscalCode).getStatus();

        if (s.equals(PatientStatus.DISCHARGED))
            return 0;
        else if (s.equals(PatientStatus.HOSPITALIZED))
            return 1;
        else 
            return -1;
    }

    /**
     * Returns the number of patients currently being managed in the emergency room.
     *
     * @return The total number of patients in the system.
     */    
    public int getNumberOfPatients() {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients admitted on a specified date.
     *
     * @param dateString The date of interest provided as a String (format "yyyy-MM-dd").
     * @return The count of patients admitted on that date.
     */
    public int getNumberOfPatientsByDate(String date) {
        //TODO: to be implemented
        return -1;
    }

    public int getNumberOfPatientsHospitalizedByDepartment(String departmentName) throws EmergencyException {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients who have been discharged from the emergency system.
     *
     * @return The count of discharged patients.
     */
    public int getNumberOfPatientsDischarged() {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of discharged patients who were treated by professionals of a specific specialization.
     *
     * @param specialization The specialization of the professionals to filter by.
     * @return The count of discharged patients treated by professionals of the given specialization.
     */
    public int getNumberOfPatientsAssignedToProfessionalDischarged(String specialization) {
        //TODO: to be implemented
        return -1;
    }
}
