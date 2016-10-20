package grails.gorm.validation

import org.grails.datastore.gorm.validation.constraints.registry.DefaultValidatorRegistry
import org.grails.datastore.mapping.keyvalue.mapping.config.KeyValueMappingContext
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.validation.ValidationErrors
import org.grails.datastore.mapping.validation.ValidatorRegistry
import org.springframework.validation.Validator
import spock.lang.Specification

import javax.persistence.Entity

/**
 * Created by graemerocher on 09/06/16.
 */
class ValidatorRegistrySpec extends Specification {

    void "test validator registry"() {
        given:"A validator registry"
        MappingContext mappingContext = new KeyValueMappingContext("test")
        def entity = mappingContext.addPersistentEntity(Person)
        ValidatorRegistry registry = new DefaultValidatorRegistry(mappingContext)

        when:"A validator is created"
        Validator validator = registry.getValidator(entity)
        def person = new Person(age: -1)
        def errors = new ValidationErrors(person, Person.simpleName)

        validator.validate(person, errors)

        then:"The validator is correct"
        errors.hasErrors()
        errors.allErrors.size() == 2

    }

}

@Entity
class Mammal {
    Integer legCount
    static constraints = {
        legCount nullable: true
    }
}
@Entity
class Person extends Mammal {
    Long id
    Long version
    String town
    Integer age
    String country

    static constraints = {
        town nullable: false
        age validator: { val -> val > 0 }
        country nullable: true
    }
}


