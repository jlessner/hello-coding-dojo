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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class BuilderStructureTest {
  private static ArchCondition<JavaClass> havePublicDefaultConstructor = new ArchCondition<JavaClass>("have public default constructor") {

    @Override
    public void check(JavaClass item, ConditionEvents events) {
      boolean publicDefaultConstructorExists = item
        .getConstructors()
        .stream()
        .filter(ctor -> ctor.getParameters().isEmpty())
        .filter(ctor -> ctor.getModifiers().contains(JavaModifier.PUBLIC))
        .findAny()
        .isPresent();

      if (!publicDefaultConstructorExists) {
        String message = String.format("Class %s does not have a public default constructor", item.getName());
        events.add(SimpleConditionEvent.violated(item, message));
      }
    }
  };

  private static ArchCondition<JavaClass> havePublicPersistMethod = new ArchCondition<JavaClass>("have method 'persist'") {
    @Override
    public void check(JavaClass item, ConditionEvents events) {
      boolean hasPersistMethod = item
        .getMethods()
        .stream()
        .filter(method -> method.getName().equals("persist"))
        .filter(method -> method.getModifiers().contains(JavaModifier.PUBLIC))
        .findAny()
        .isPresent();

      if (!hasPersistMethod) {
        String message = String.format("Class %s does not have a public method named 'persist'", item.getName());
        events.add(SimpleConditionEvent.violated(item, message));
      }
    }
  };

  private static ArchCondition<JavaClass> haveDefaultsClass = new ArchCondition<JavaClass>("have defaults class") {
    @Override
    public void check(JavaClass item, ConditionEvents events) {
      String defaultsClassName = item.getSimpleName() + "Defaults";
      if (!item.getPackage().containsClassWithSimpleName(defaultsClassName)) {
        String message = String.format("Class %s does not have an associated %s class", item.getName(), defaultsClassName);
        events.add(SimpleConditionEvent.violated(item, message));
      }
    }
  };

  private static GivenClassesConjunction builderClasses = classes()
    .that()
    .haveSimpleNameEndingWith("Builder");

  private static JavaClasses allClasses;

  @BeforeAll
  static void readClassesFromBuilderPackages() {
    allClasses = new ClassFileImporter().importPaths("target/classes");
  }

  @Test
  void testBuildersWellPlacedInPackages() {
    builderClasses
      .should()
      .resideInAnyPackage("builder", "reference.builder")
      .check(allClasses);
  }

  @Test
  void testDefaultConstructor() {
    builderClasses.should(havePublicDefaultConstructor).check(allClasses);
  }

  @Test
  void testPublicPersistMethod() {
    builderClasses.should(havePublicPersistMethod).check(allClasses);
  }

  @Test
  void testBuilderStructure() {
    builderClasses.should(haveDefaultsClass).check(allClasses);
  }

}
