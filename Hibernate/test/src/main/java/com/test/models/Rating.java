package com.test.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long grouId;

    @Column(name = "avg_rating")
    private double avgRating;

    @Column(name = "votes")
    private int votes;

    public Rating(){}


}
