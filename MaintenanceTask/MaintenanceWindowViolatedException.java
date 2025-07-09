package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenance;

public class MaintenanceWindowViolatedException extends Exception {
    public MaintenanceWindowViolatedException() {
        super("Maintenance window violated");
    }
}
