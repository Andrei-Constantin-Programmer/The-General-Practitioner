package com.group15A.DataModel;

/**
 * Used to represent a doctor certification within the system, will be used to pass and change
 * information between the business logic and data access layer.
 * @author Milovan Gveric
 * @author Wenbo Wu
 */
public class Certification {
    private Integer doctorID;
    private Integer certID;
    private String name;
    private String field;
    private String dateObtained;

    // Constructor
    public Certification(Integer doctorID, Integer certID, String name, String field, String dateObtained) {
        this.doctorID = doctorID;
        this.certID = certID;
        this.name = name;
        this.field = field;
        this.dateObtained = dateObtained;
    }

    // All getters and setters for attributes below

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    public Integer getCertID() {
        return certID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }
}
