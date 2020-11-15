package com.test.task.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "banks")
@NoArgsConstructor
@Data
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long id;

    @NotNull
    @Column(name = "name_fld")
    private String name;

    @NotNull
    @Column
    private String BIC;

    @OneToMany
    @JoinTable(name = "deposits_banks",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name = "deposit_id"))
    private List<Deposit> deposits;
}
