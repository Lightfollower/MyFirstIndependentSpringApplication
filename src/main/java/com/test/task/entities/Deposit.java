package com.test.task.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "deposits")
@NoArgsConstructor
@Data
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @Column(name = "date_fld")
    private Date date;

    @Column(name = "rate_fld")
    private Double rate;

    @Column(name = "term_fld")
    private int term;

    @ManyToOne
    @JoinTable(name = "deposits_clients",
            joinColumns = @JoinColumn(name = "deposit_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
//    @JsonBackReference
    private Client client;

    @ManyToOne
    @JoinTable(name = "deposits_banks",
            joinColumns = @JoinColumn(name = "deposit_id"),
            inverseJoinColumns = @JoinColumn(name = "bank_id"))
//    @JsonBackReference
    private Bank bank;
}
