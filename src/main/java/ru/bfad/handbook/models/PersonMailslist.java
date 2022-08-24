package ru.bfad.handbook.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "handbook_schema", name = "person_maillist")
@AllArgsConstructor
public class PersonMailslist {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maillist_id")
    private Integer maillistId;


    @Column(name = "person_id")
    private Integer personId;

    public PersonMailslist() {
    }

    public PersonMailslist(Integer maillistId, Integer personId) {
        this.maillistId = maillistId;
        this.personId = personId;
    }
}
