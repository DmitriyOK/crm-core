package com.cafe.crm.models.Menu;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Sasha ins on 17.04.2017.
 */
public class Snacks extends Product {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "specification")
    private String specification;

    @Column(name = "cost")
    private long cost;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id", nullable = false)
    private  Set<Snacks> snacks;

    public Snacks() {
    }

    public Snacks(String name, String specification, long cost) {
        this.name = name;
        this.specification = specification;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public Set<Snacks> getSnacks() {
        return snacks;
    }

    public void setSnacks(Set<Snacks> snacks) {
        this.snacks = snacks;
    }
}
