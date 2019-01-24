package com.fdiba.webeng.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Interest {
    @Id
    @GeneratedValue
    private Integer interestid;

    @Size(min = 1)
    @Column(name = "interestname", nullable = false, unique = true)
    private String interestname;

    //default constructor
    public Interest() {
    }

    public void setInterestname(String interestname) {
        this.interestname = interestname;
    }

    public Integer getInterestid() {
        return interestid;
    }

    public void setInterestid(Integer interestid) {
        this.interestid = interestid;
    }

    public String getInterestname() {
        return interestname;
    }
}
