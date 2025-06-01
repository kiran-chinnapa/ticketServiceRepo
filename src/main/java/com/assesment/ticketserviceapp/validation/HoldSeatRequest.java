package com.assesment.ticketserviceapp.validation;// HoldSeatRequest.java
import jakarta.validation.constraints.*;

public class HoldSeatRequest {

    @Min(value = 1, message = "At least 1 seat must be requested")
    private int numSeats;

    @Min(value = 1, message = "minLevel must be between 1 and 4")
    @Max(value = 4, message = "minLevel must be between 1 and 4")
    private Integer minLevel;

    @Min(value = 1, message = "maxLevel must be between 1 and 4")
    @Max(value = 4, message = "maxLevel must be between 1 and 4")
    private Integer maxLevel;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    // Getters and Setters
    public int getNumSeats() { return numSeats; }
    public void setNumSeats(int numSeats) { this.numSeats = numSeats; }

    public Integer getMinLevel() { return minLevel; }
    public void setMinLevel(Integer minLevel) { this.minLevel = minLevel; }

    public Integer getMaxLevel() { return maxLevel; }
    public void setMaxLevel(Integer maxLevel) { this.maxLevel = maxLevel; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}
