package de.hbrs.ia.model;

import org.bson.Document;

public class SalesMan {
    private String firstname;
    private String lastname;
    private Integer sid;

    public SalesMan(String firstname, String lastname, Integer sid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.sid = sid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getId() {
        return sid;
    }

    public void setId(Integer sid) {
        this.sid = sid;
    }

    public Document toDocument() {
        org.bson.Document document = new Document();
        document.append("firstname" , this.firstname );
        document.append("lastname" , this.lastname );
        document.append("sid" , this.sid);
        return document;
    }
}
