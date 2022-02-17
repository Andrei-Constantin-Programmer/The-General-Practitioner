package com.group15A.DataAccess;

import com.group15A.DataModel.*;

import java.util.List;

/**
 * An interface to the Data Access Layer
 *
 * @author Andrei
 */
public interface IDataAccess
{
    Patient getPatient(String email, String password);

    Patient registerPatient(Patient patient);

    Patient updatePatient(Patient patient);

    List<Doctor> getDoctors();

    List<Certification> getCertifications(Doctor doctor);
}
