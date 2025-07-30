package archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BuilderStructureTest {
  private static JavaClasses allClasses;

  @BeforeAll
  static void readAllClasses() {
    allClasses = new ClassFileImporter().importPaths("target/classes");
  }

  @Test
  void testBuildersWellPlacedInPackages() {
    System.out.println(allClasses.size());
  }

}
