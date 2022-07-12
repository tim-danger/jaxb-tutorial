package com.javaee.jaxb;

import com.javaee.entities.Employee;
import com.javaee.entities.Employees;
import com.javaee.jaxb.utils.JAXBUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

@Path("/")
public class EmployeeResource {

    @GET
    @Path("marshal")
    @Produces(MediaType.APPLICATION_XML)
    public String marshal(@QueryParam("firstname") String firstname,
                              @QueryParam("lastname") String lastname,
                              @QueryParam("salary") String salaryAsString,
                              @QueryParam("ssn") String socialSecurityNumber,
                              @QueryParam("hireDate") String hireDate) {

        Employee employee = new Employee();
        employee.setFirstName(firstname);
        employee.setLastName(lastname);
        employee.setSsn(socialSecurityNumber);
        employee.setHireDate(getHireDate(hireDate));
        try {
            Double salary = Double.parseDouble(salaryAsString);
            employee.setSalary(salary);
        } catch(NumberFormatException nfe) {
            employee.setSalary(-500.0);
        }

        return new JAXBUtils().marshalToString(employee);
    }

    @POST
    @Path("unmarshal")
    @Produces(MediaType.TEXT_PLAIN)
    public String unmarshal(String xml) {
        return printEmployees(new JAXBUtils().unmarshalFromString(xml));
    }

    private String printEmployees(Employees employees) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        for(Employee employee : employees.getEmployee()) {
            result.append("Employee Nr.: " + ++counter + " ------------- \n");
            result.append("Firstname: " + employee.getFirstName() + "\n");
            result.append("Lastname: " + employee.getLastName() + "\n");
            result.append("Hired at: " + employee.getHireDate() + "\n");
            result.append("Earns: " + employee.getSalary() + " $" + "\n\r");
        }
        return result.toString();
    }

    private XMLGregorianCalendar getHireDate(String hireDate) {
        if(hireDate != null && !hireDate.isEmpty()) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(hireDate);
            } catch (ParseException e) {
                // do nothing
            }

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);

            try {
                XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                return xmlGregCal;
            } catch (DatatypeConfigurationException e) {
                // do nothing
            }
        }
        return null;
    }

}
