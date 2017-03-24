package giovannilenguito.co.uk.parceldelivery.model;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Customer extends User implements Serializable {
    private int customerId;
    private Address addressId;

    public Customer(String email, String username, String password, String fullName, Long contactNumber, Address addressId) {
        super(email, username, password, fullName, contactNumber);
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }
}
