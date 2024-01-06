package com.code.mintyn.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bin;

    private String scheme;

    private String type;

    private String bank;

    private int count;

    public Card(String bin, String scheme, String type, String bank, int count) {
        this.bin = bin;
        this.scheme = scheme;
        this.type = type;
        this.bank = bank;
        this.count = count;
    }
}
