package archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reference.builder.ReferenceEntityBuilderI;

import static com.tngtech.archunit.lang.conditions.ArchConditions.have;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class BuilderStructureTest {
  private static JavaClasses allClasses;

  @BeforeAll
  static void readAllClasses() {
    allClasses = new ClassFileImporter().importPaths("target/classes", "target/test-classes");
    System.out.println(allClasses.size());
  }

  private static final GivenClassesConjunction builderClasses = classes()
    .that()
    .haveSimpleNameEndingWith("Builder");

  @Test
  void testBuildersWellPlacedInPackages() {
    builderClasses
      .should()
      .resideInAnyPackage("..builder..")
      .check(allClasses);
  }

  @Test
  void testBuildersHavePersistMethod_ViaInterface() {
    builderClasses
      .should()
      .implement(ReferenceEntityBuilderI.class)
      .check(allClasses);
  }

  @Test
  void testBuildersHavePersistMethod_ViaMethodCheck() {
    DescribedPredicate<JavaClass> publicPersistMethod = DescribedPredicate.describe("public method persist()",
      javaClass ->  javaClass.getMethods()
        .stream()
        .filter(method -> method.getName().equals("persist"))
        .filter(method -> method.getModifiers().contains(JavaModifier.PUBLIC))
        .findAny()
        .isPresent());

    builderClasses
      .should(have(publicPersistMethod))
      .check(allClasses);
  }

}
