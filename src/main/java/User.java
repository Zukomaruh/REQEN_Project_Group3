public class User {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private UserRole role; // OWNER, CUSTOMER
    private boolean active;

    public boolean validateCredentials(String inputPassword);
    public boolean isOwner();
    public boolean isCustomer();
    public void updateProfile(String email, String username);
}
