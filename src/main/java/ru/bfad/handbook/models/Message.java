package ru.bfad.handbook.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "handbook_schema", name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private From from;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "message_to")
    private String to;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Message(From from, String subject, String text, User user, String to) {
        this.from = from;
        this.subject = subject;
        this.text = text;
        this.user = user;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from=" + from +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", to='" + to + '\'' +
                '}';
    }
}
