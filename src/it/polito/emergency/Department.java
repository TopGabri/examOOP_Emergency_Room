package it.polito.emergency;
import java.util.*;

public class Department {

    private String name;
    private int capacity;
    private List<Patient> patients;


	public Department(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
	}

    public boolean hasAvailableBeds (){

        if (capacity > 0)
            return true;
        return false;
    }

    public void hospitalize(Patient patient){
        if (hasAvailableBeds()){
            patients.add(patient);
            capacity--;
        }
    }

    
}
