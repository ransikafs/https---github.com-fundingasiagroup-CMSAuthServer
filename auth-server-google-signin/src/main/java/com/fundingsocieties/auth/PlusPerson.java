package com.fundingsocieties.auth;

import java.util.ArrayList;
import java.util.List;

public class PlusPerson {

    PlusName name;
    List<PlusEmail> emails = new ArrayList<>();

    public List<PlusEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<PlusEmail> emails) {
        this.emails = emails;
    }

    public PlusName getName() {
        return name;
    }

    public void setName(PlusName name) {
        this.name = name;
    }
}
