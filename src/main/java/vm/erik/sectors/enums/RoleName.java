package vm.erik.sectors.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");


    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
