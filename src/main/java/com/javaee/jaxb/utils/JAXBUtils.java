package com.javaee.jaxb.utils;

import com.javaee.entities.Employee;
import com.javaee.entities.Employees;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class JAXBUtils {

    public String marshalToString(Employee employee) {

        try {
            JAXBContext context = JAXBContext.newInstance(Employees.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // setting the container
            Employees employees = new Employees();
            employees.getEmployee().add(employee);
            StringWriter sw = new StringWriter();
            m.marshal(employees, sw);
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employees unmarshalFromString(String employees) {
        try {
            JAXBContext context = JAXBContext.newInstance(Employees.class);
            Unmarshaller m = context.createUnmarshaller();
            return (Employees) m.unmarshal(new StringReader(employees));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
