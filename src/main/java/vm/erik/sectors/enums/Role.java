package vm.erik.sectors.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");


    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
