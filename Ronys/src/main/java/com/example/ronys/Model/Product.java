package com.example.ronys.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "PRODUCT_ID_SEQ", allocationSize = 1, initialValue = 101)
    private Long id;
    @Column(nullable = false)
    private int buyingPrice;
    @Column(nullable = false)
    private int sellingPrice;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String rakNumber;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String remarks;
    private String companyArticle;
    private String section;



    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRakNumber() {
        return rakNumber;
    }

    public void setRakNumber(String rakNumber) {
        this.rakNumber = rakNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getCompanyArticle() {
        return companyArticle;
    }

    public void setCompanyArticle(String companyArticle) {
        this.companyArticle = companyArticle;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
