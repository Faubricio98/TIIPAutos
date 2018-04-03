/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author faubricioch
 * 
 */
public class Cars {
    private String name;
    private int year;
    private float mileage;
    private boolean american;
    private int serie;

    public Cars() {
        this.name="";
        this.year=0000;
        this.mileage=0;
        this.american=false;
        this.serie=0;
    }

    public Cars(String name, int year, float mileage, boolean american, int serie) {
        this.name = name;
        this.year = year;
        this.mileage = mileage;
        this.american = american;
        this.serie = serie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }

    public boolean isAmerican() {
        return american;
    }

    public void setAmerican(boolean american) {
        this.american = american;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "Cars{" + "name=" + name + ", year=" + year + ", mileage=" + mileage + ", american=" + american + ", serie=" + serie + '}';
    }
    
    public int sizeInBytes(){
        return getName().length()*2 + 4 + 4 + 1 + 4;
    }
}
