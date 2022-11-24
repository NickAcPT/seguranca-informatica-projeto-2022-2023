package pt.ua.segurancainformatica.licensing.common;

import java.net.SocketException;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws SocketException {
        System.out.println("LicensingCommon.getNetworkInterfaceEntries() = " + Arrays.toString(LicensingCommon.getNetworkInterfaceEntries()));
        System.out.println("LicensingCommon.getEnvironmentVariableEntries() = " + Arrays.toString(LicensingCommon.getEnvironmentVariableEntries()));
    }
}
