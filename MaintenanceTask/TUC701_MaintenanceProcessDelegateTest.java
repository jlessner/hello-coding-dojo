package com.nextlevel.fastlane.myBusinessSupplier.aepmako.maintenance;

import com.nextlevel.platform.date.DateTool;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.pvm.runtime.ExecutionImpl;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TUC701_MaintenanceProcessDelegateTest {

    @Test
    public void testMaintenanceWindowViolated() {
        TUC701_MaintenanceProcessDelegate testSubject = executeDelegate();
        testSubject.assertLogContains("Maintenance window violated");
    }

    private TUC701_MaintenanceProcessDelegate executeDelegate() {
        final TUC701_MaintenanceProcessDelegate testSubject = new TUC701_MaintenanceProcessDelegate();
        final DelegateExecution execution = new ExecutionImpl();
        try {
            testSubject.execute(execution);
        } catch (Exception e) {
            fail("delegate must not throw an exception, but did: " + e.getMessage());
        }
        return testSubject;
    }
}
