package com.example.lenovo.citycenter.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 02/01/2017.
 */

public class Item implements Serializable{

   private String id,name,description ,photo1,phone1,phone2,phone3,phone4,phone5,categoryName,subcategoryName,city,region,site,categoryID,pdf_url;
   private float rate;
    double lon,lat;


    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = "https://docs.google.com/gview?url=https://sodicadmin.azurewebsites.net/PDF/"+pdf_url+"&embedded=true";
    }

    ArrayList<String> phones=new ArrayList<>();

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
        if(!phone1.matches("null"))
            phones.add(phone1);
    }


    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
         this.phone2 = phone2;
        if(!phone2.matches("null"))
            phones.add(phone2);
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
        if(!phone3.matches("null"))
          phones.add(phone3);

    }

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
        if(!phone4.matches("null"))
          phones.add(phone4);

    }

    public String getPhone5() {
        return phone5;
    }

    public void setPhone5(String phone5) {

        this.phone5 = phone5;
        if(!phone5.matches("null"))
            phones.add(phone5);

    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {

        this.rate = Math.round(rate);
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    private boolean like;

    public Item() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        photo1=photo1.replaceAll(" ","%20");
        this.photo1 = photo1;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
