package be.ucll.se.groep26backend.rents.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;

public class CheckOut {
    @Min(0)
    private int returnMillage;

    @Min(0)
    private int returnFuelQuantity;

    private LocalDate endDate; 

    

    public CheckOut() {
    }

    public CheckOut(int returnMillage, int returnFuelQuantity, LocalDate endDate) {
        this.returnMillage = returnMillage;
        this.returnFuelQuantity = returnFuelQuantity;
        this.endDate = endDate;
    }

    public int getReturnMillage() {
        return returnMillage;
    }

    public void setReturnMillage(int returnMillage) {
        this.returnMillage = returnMillage;
    }

    public int getReturnFuelQuantity() {
        return returnFuelQuantity;
    }

    public void setReturnFuelQuantity(int returnFuelQuantity) {
        this.returnFuelQuantity = returnFuelQuantity;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    
}
