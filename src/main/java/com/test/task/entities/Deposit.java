package com.test.task.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "deposits")
@NoArgsConstructor
@Data
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @NotNull
    @Column(name = "date_fld")
    private Date date;

    @NotNull
    @Column(name = "rate_fld")
    private Double rate;

    @NotNull
    @Column(name = "term_fld")
    private int term;

    @ManyToOne
    @JoinTable(name = "deposits_clients",
            joinColumns = @JoinColumn(name = "deposit_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Client client;

    @ManyToOne
    @JoinTable(name = "deposits_banks",
            joinColumns = @JoinColumn(name = "deposit_id"),
            inverseJoinColumns = @JoinColumn(name = "bank_id"))
    private Bank bank;
}
