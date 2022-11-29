package pt.ua.segurancainformatica.licensing.common.model.request;

import org.jetbrains.annotations.NotNull;
import pt.ua.segurancainformatica.licensing.common.model.ApplicationInformation;
import pt.ua.segurancainformatica.licensing.common.model.ComputerInformation;
import pt.ua.segurancainformatica.licensing.common.model.UserData;

public record LicenseRequest(
        @NotNull UserData user,
        @NotNull ApplicationInformation application,
        @NotNull ComputerInformation computer
) {
    public LicenseRequest(UserData user, ApplicationInformation application) {
        this(user, application, new ComputerInformation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseRequest that)) return false;

        if (!user.equals(that.user)) return false;
        if (!application.equals(that.application)) return false;
        return computer.equals(that.computer);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + application.hashCode();
        result = 31 * result + computer.hashCode();
        return result;
    }
}
