package be.ucll.se.groep26backend.role.model;


public enum RoleType {
    OWNER(1), RENTER(2), ADMIN(3), ACCOUNTANT(4);

    private final int id;

    RoleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        //return "ROLE_" + this.name();
        return this.name();
    }
}