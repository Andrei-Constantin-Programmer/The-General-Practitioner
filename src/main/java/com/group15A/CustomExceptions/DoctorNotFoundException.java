package com.group15A.CustomExceptions;

public class DoctorNotFoundException extends Exception
{
    public DoctorNotFoundException()
    {
        super("The doctor with the given details was not found.");
    }
}
