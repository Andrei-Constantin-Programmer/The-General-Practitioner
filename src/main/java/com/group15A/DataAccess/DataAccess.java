package com.group15A.DataAccess;

import com.group15A.CustomExceptions.*;
import com.group15A.DataModel.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to connect to the database and query the database using stored procedures.
 *
 * @author Andrei Constantin
 */
public class DataAccess implements IDataAccess
{
    private Connection connection;

    /**
     * Constructor for the DataAccess class.
     * It sets up the connection to the database
     * @throws DatabaseException if there was a problem connecting to the database
     */
    public DataAccess() throws DatabaseException
    {
        setupConnection();
    }

    /**
     * Get the patient with the given email and password
     * @param email The patient's email
     * @param password The patient's password
     * @return The patient
     * @throws PatientNotFoundException if the user was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient getPatient(String email, String password) throws PatientNotFoundException, DatabaseException
    {
        try {
            String query = "CALL find_patient(?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            Patient patient = getPatientFromDB(result);

            return patient;
        } catch (PatientNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get patient from the database");
        }
    }

    /**
     * Get the patient from the given result set
     * @param result The result set from the database
     * @return The patient
     * @throws PatientNotFoundException if the patient was not found
     */
    private Patient getPatientFromDB(ResultSet result) throws PatientNotFoundException
    {
        Patient patient;
        try {
            result.next();
            patient = new Patient(
                    result.getInt("id_patient"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getDate("date_of_birth"),
                    result.getString("gender"),
                    result.getString("telephone_number")
            );
        } catch(Exception ex)
        {
            throw new PatientNotFoundException();
        }
        return patient;
    }

    /**
     * Get the patient with the given id
     * @param patientID the patient's id
     * @return The patient
     * @throws PatientNotFoundException if the user was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient getPatient(int patientID) throws PatientNotFoundException, DatabaseException
    {
        try {
            String query = "CALL get_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patientID);

            ResultSet result = statement.executeQuery();
            Patient patient = getPatientFromDB(result);

            return patient;
        } catch (PatientNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get patient from the database");
        }
    }

    /**
     * Registers a new patient
     * @param patient The new patient
     * @param doctor The doctor assigned to the patient
     * @return The corresponding patient from the database
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient registerPatient(Patient patient, Doctor doctor) throws EmailInUseException, DatabaseException
    {
        try {
            String query = "CALL insert_patient(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setString(1, patient.getEmail());
            statement.setString(2, patient.getPassHash());
            statement.setString(3, patient.getFirstName());
            statement.setString(4, patient.getMiddleName());
            statement.setString(5, patient.getLastName());
            statement.setDate(6, new Date(patient.getDob().getTime()));
            statement.setString(7, patient.getGender());
            statement.setString(8, patient.getPhoneNo());
            statement.setInt(9, doctor.getDoctorID());

            statement.executeQuery();

            return getPatient(patient.getEmail(), patient.getPassHash());
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new EmailInUseException("Email already in use");
        } catch (Exception ex)
        {
            System.err.println(ex);
            throw new DatabaseException("Could not register patient in the database");
        }
    }

    /**
     * Update the given patient with the new information provided in the object
     * @param patient The modified patient
     * @return The corresponding patient from the database
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient updatePatient(Patient patient) throws CustomException
    {
        updatePatientFull(patient, getDoctor(patient));

        return getPatient(patient.getEmail(), patient.getPassHash());
    }

    /**
     * Get the corresponding doctor for the given patient
     * @param patient The patient
     * @return The patient's doctor
     * @throws DoctorNotFoundException if the doctor was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    public Doctor getDoctor(Patient patient) throws DoctorNotFoundException, DatabaseException
    {
        try {
            String query = "CALL find_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());

            ResultSet result = statement.executeQuery();
            Doctor doctor = getDoctorFromDB(result);

            return doctor;
        } catch (DoctorNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get doctor from the database");
        }
    }

    /**
     * Get the doctor with the given id
     * @param doctorID The id of the doctor
     * @return The doctor
     * @throws DoctorNotFoundException if the doctor was not found
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Doctor getDoctor(int doctorID) throws DoctorNotFoundException, DatabaseException {
        try {
            String query = "CALL get_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, doctorID);
            ResultSet result = statement.executeQuery();
            Doctor doctor = getDoctorFromDB(result);

            return doctor;
        } catch (DoctorNotFoundException ex)
        {
            throw ex;
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not get the doctor from the database");
        }
    }

    /**
     * Get the doctor from the given result set
     * @param result The result set from the database
     * @return The doctor
     * @throws DoctorNotFoundException if the doctor was not found
     */
    private Doctor getDoctorFromDB(ResultSet result) throws DoctorNotFoundException
    {
        Doctor doctor;
        try {
            result.next();
            doctor = new Doctor(
                    result.getInt("id_doctor"),
                    result.getString("email"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getDate("date_of_birth"),
                    result.getString("gender"),
                    result.getString("telephone_number")
            );
        } catch(Exception ex)
        {
            throw new DoctorNotFoundException();
        }

        return doctor;
    }

    /**
     * Update the given patient with the new information, including a new doctor
     * @param patient The modified patient
     * @param doctor The new doctor
     * @throws DatabaseException if there was a problem querying the database
     */
    private void updatePatientFull(Patient patient, Doctor doctor) throws DatabaseException
    {
        try {
            String query = "CALL update_patient(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patient.getPatientID());
            statement.setString(2, patient.getEmail());
            statement.setString(3, patient.getPassHash());
            statement.setString(4, patient.getFirstName());
            statement.setString(5, patient.getMiddleName());
            statement.setString(6, patient.getLastName());
            statement.setDate(7, new Date(patient.getDob().getTime()));
            statement.setString(8, patient.getGender());
            statement.setString(9, patient.getPhoneNo());
            statement.setInt(10, doctor.getDoctorID());

            statement.executeQuery();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not update the patient");
        }
    }

    /**
     * Change the doctor of the given patient
     * @param patient The patient
     * @param doctor The new doctor
     * @return The corresponding patient from the database
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public Patient changeDoctor(Patient patient, Doctor doctor) throws DatabaseException
    {
        try {
            updatePatientFull(patient, doctor);
            return getPatient(patient.getPatientID());
        }catch(CustomException ex)
        {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Get the full list of doctors from the database
     * @return The list of doctors
     * @throws DatabaseException if there was a problem querying the database
     */
    public List<Doctor> getDoctors() throws DatabaseException
    {
        try {
            String query = "CALL get_doctors();";
            PreparedStatement statement = connection.prepareCall(query);
            ResultSet result = statement.executeQuery();
            var doctors = new ArrayList<Doctor>();
            while (result.next()) {
                doctors.add(new Doctor(
                        result.getInt("id_doctor"),
                        result.getString("email"),
                        result.getString("first_name"),
                        result.getString("middle_name"),
                        result.getString("last_name"),
                        result.getDate("date_of_birth"),
                        result.getString("gender"),
                        result.getString("telephone_number")
                ));
            }

            return doctors;
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get doctors from the database");
        }
    }

    /**
     * Get the certifications of the specified doctor
     * @param doctor The doctor
     * @return The certifications
     * @throws DatabaseException if there was a problem querying the database
     */
    @Override
    public List<Certification> getCertifications(Doctor doctor) throws DatabaseException
    {
        try {
            String query = "CALL get_certifications_doctor(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, doctor.getDoctorID());
            ResultSet result = statement.executeQuery();
            var certifications = new ArrayList<Certification>();
            while (result.next()) {
                certifications.add(new Certification(
                        result.getInt("id_doctor"),
                        result.getInt("id_cert"),
                        result.getString("name"),
                        result.getString("field"),
                        result.getDate("dateObtained")
                ));
            }

            return certifications;
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not get certifications from the database");
        }
    }

    /**
     * Delete the patient with the given id
     * @param patientID The patient id
     * @throws DatabaseException if there was a problem querying the database
     */
    public void deletePatient(int patientID) throws DatabaseException
    {
        try{
            String query = "CALL delete_patient(?);";
            PreparedStatement statement = connection.prepareCall(query);
            statement.setInt(1, patientID);
            statement.executeQuery();
        } catch (Exception ex)
        {
            throw new DatabaseException("Could not delete the patient from the database");
        }
    }

    /**
     * Set up the connection to the database
     * @throws DatabaseException if the connection could not be established
     */
    private void setupConnection() throws DatabaseException
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/thegeneralpractitioner?user=root&password=root");
        }catch (Exception ex)
        {
            throw new DatabaseException("Could not connect to the database");
        }
    }
}
