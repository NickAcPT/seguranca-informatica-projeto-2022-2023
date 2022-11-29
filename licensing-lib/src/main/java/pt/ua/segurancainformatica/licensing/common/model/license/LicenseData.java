package pt.ua.segurancainformatica.licensing.common.model.license;

import java.time.Instant;

public record LicenseData(
        Instant startDate,
        Instant endDate
) {
}
