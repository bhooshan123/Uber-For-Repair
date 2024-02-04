public class User {
    public String name;
    public String mobile;
    public String email;
    public String vehicleType;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String mobile, String email, String vehicleType) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.vehicleType = vehicleType;
    }
}

